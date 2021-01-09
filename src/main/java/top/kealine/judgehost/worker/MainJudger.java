package top.kealine.judgehost.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.constant.JudgeResult;
import top.kealine.judgehost.constant.SupportedLanguage;
import top.kealine.judgehost.entity.*;
import top.kealine.judgehost.testcase.TestcasePreparer;
import top.kealine.judgehost.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainJudger implements Runnable {
    private static final Log logger = LogFactory.getLog(MainJudger.class);

    private final JudgeTask judgeTask;
    public MainJudger(JudgeTask judgeTask) {
        this.judgeTask = judgeTask;
    }

    private static void clearOldFiles(File testRoot) {
        logger.info(String.format("Clean old files in %s", testRoot.getPath()));
        if (!testRoot.exists()) {
            logger.info("Dir is not exists, make it!");
            testRoot.mkdir();
        }
        File[] oldFiles = testRoot.listFiles();
        if (oldFiles != null && oldFiles.length >0 ) {
            for (File oldFile: oldFiles) {
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
        }
        logger.info("Cleaning work done.");
    }

    private static void copyCompiler(File target) throws IOException {
        logger.info(String.format("Copy Compiler to %s", target.getPath()));

        File compiler = new File(Config.JUDGEHOST_CORE_DIR + "Compiler");
        Files.copy(compiler.toPath(), target.toPath());
    }

    private static void copyCore(File target) throws IOException {
        logger.info(String.format("Copy Core to %s", target.getPath()));

        File core = new File(Config.JUDGEHOST_CORE_DIR + "Core");
        Files.copy(core.toPath(), target.toPath());
    }

    private static void prepareEnv(int lang) throws IOException {
        logger.info("Begin to prepare env.");
        File judgeDir = new File(Config.JUDGEHOST_TEST_DIR + "judge");
        clearOldFiles(judgeDir);
        if (judgeDir.exists()) {
            judgeDir.delete();
        }
        judgeDir.mkdir();
        copyCore(new File(Config.JUDGEHOST_TEST_DIR + "judge/Core"));
        String userProgramName;
        switch (lang) {
            case SupportedLanguage.C:
            case SupportedLanguage.CPP: userProgramName = "Main"; break;
            case SupportedLanguage.JAVA: userProgramName = "Main.class"; break;
            default: userProgramName = "!ERROR!";
        }
        File userProgram = new File(Config.JUDGEHOST_TEST_DIR + userProgramName);
        File userProgramTarget = new File(Config.JUDGEHOST_TEST_DIR + "judge/" + userProgramName);
        Files.copy(userProgram.toPath(), userProgramTarget.toPath());
    }

    private static CaseResult judge(CaseTask task) {
        try {
            logger.info("Begin to run Core...");
            ProcessBuilder pb = new ProcessBuilder(
                    Config.JUDGEHOST_TEST_DIR + "judge/Core",
                    "-l", Integer.toString(task.getLang()),
                    "-t", Integer.toString(task.getTimeLimit()),
                    "-m", Integer.toString(task.getMemoryLimit()),
                    "-d", Config.JUDGEHOST_TEST_DIR + "judge"
            );
            Process pro = pb.start();
            int exitVal = pro.waitFor();
            logger.info(String.format("Core exited, exit code = %s", exitVal));
            return CaseResult.getCaseResult(task.getSolutionId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Core throw an Exception");
            return null;
        }
    }

    private static CaseResult runCaseTask(CaseTask task) {
        try {
            prepareEnv(task.getLang());
            TestcasePreparer.prepareTestcase(task.getTestcaseId(), Config.JUDGEHOST_TEST_DIR + "judge/");
            return judge(task);
        } catch (Exception e) {
            e.printStackTrace();
            return CaseResult.getSystemErrorInstance(task.getSolutionId());
        }
    }

    private static void releaseUserCode(String code, int lang) {
        logger.info("Release user's code");

        FileUtil.save(Config.JUDGEHOST_TEST_DIR + "Main" + SupportedLanguage.getLangSuffix(lang), code);
    }

    public static CompileResult compileUserCode(File testRoot, String code, int lang) {
        logger.info("Begin to compile user's code...");

        clearOldFiles(testRoot);
        releaseUserCode(code, lang);
        try {
            copyCompiler(new File(Config.JUDGEHOST_TEST_DIR + "Compiler"));

            logger.info("Begin to compile...");

            ProcessBuilder pb = new ProcessBuilder(
                    Config.JUDGEHOST_TEST_DIR + "Compiler",
                    "-c", Config.JUDGEHOST_TEST_DIR + "Main" + SupportedLanguage.getLangSuffix(lang),
                    "-d", Config.JUDGEHOST_TEST_DIR
            );
            Process pro = pb.start();
            int exitVal = pro.waitFor();

            logger.info(String.format("Compile done, exit code = %s", exitVal));
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Compiler unknown error.");
            return null;
        }
        return CompileResult.getCompileResult();
    }


    public static void runJudgeTask(JudgeTask judgeTask) {
        logger.info(String.format("New task! solutionId = %s", judgeTask.getSolutionId()));

        File testRoot = new File(Config.JUDGEHOST_TEST_DIR);

        CompileResult compileResult = compileUserCode(testRoot, judgeTask.getCode(), judgeTask.getLang());
        SolutionResult finalResult;
        if (compileResult == null) {
            logger.info("Result = System Error.");
            finalResult = SolutionResult.getSystemErrorInstance(judgeTask.getSolutionId());
        }else if (compileResult.isCE()) {
            logger.info("Result = Compile Error.");
            finalResult = SolutionResult.getCompileErrorInstance(judgeTask.getSolutionId(), compileResult);
        } else {
            logger.info("Compile Successful, begin to judge.");

            // Judge
            List<CaseTask> tasks = judgeTask.toCaseTask();
            List<CaseResult> results = tasks
                    .stream()
                    .map(MainJudger::runCaseTask)
                    .collect(Collectors.toList());
            finalResult = SolutionResult.of(results);

            logger.info(String.format("Judge done, solutionId = %s, final result is %s", finalResult.getSolutionId(), finalResult.getResult()));
        }
        if (ResultSubmitter.submitSolutionResult(finalResult)) {
            logger.info("SolutionResult submitted.");
        } else {
            logger.error("SolutionResult submit failed.");
        }
    }

    @Override
    public void run() {
        runJudgeTask(this.judgeTask);
    }
}
