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

        if (target.exists()) {
            return;
        }
        File compiler = new File(Config.JUDGEHOST_CORE_DIR + "Compiler");
        Files.copy(compiler.toPath(), target.toPath());
    }

    private static void copyCore(File target) throws IOException {
        logger.info(String.format("Copy Core to %s", target.getPath()));

        File core = new File(Config.JUDGEHOST_CORE_DIR + "Core");
        Files.copy(core.toPath(), target.toPath());
    }

    private static void prepareEnv(int lang, boolean isSpj) throws IOException {
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

        if (isSpj) {
            File spj = new File(Config.JUDGEHOST_TEST_DIR + "SpecialJudge");
            File spjTarget = new File(Config.JUDGEHOST_TEST_DIR + "judge/SpecialJudge");
            Files.copy(spj.toPath(), spjTarget.toPath());
        }
    }

    private static CaseResult judge(CaseTask task) {
        try {
            logger.info("Begin to run Core...");
            ProcessBuilder pb;
            if (task.isSpj()) {
                // -s for spj
                pb = new ProcessBuilder(
                        Config.JUDGEHOST_TEST_DIR + "judge/Core",
                        "-l", Integer.toString(task.getLang()),
                        "-t", Integer.toString(task.getTimeLimit()),
                        "-m", Integer.toString(task.getMemoryLimit()),
                        "-d", Config.JUDGEHOST_TEST_DIR + "judge",
                        "-s"
                );
            } else {
                pb = new ProcessBuilder(
                        Config.JUDGEHOST_TEST_DIR + "judge/Core",
                        "-l", Integer.toString(task.getLang()),
                        "-t", Integer.toString(task.getTimeLimit()),
                        "-m", Integer.toString(task.getMemoryLimit()),
                        "-d", Config.JUDGEHOST_TEST_DIR + "judge"
                );
            }
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
            prepareEnv(task.getLang(), task.isSpj());
            TestcasePreparer.prepareTestcase(task.getTestcaseId(), Config.JUDGEHOST_TEST_DIR + "judge/");
            return judge(task);
        } catch (Exception e) {
            e.printStackTrace();
            return CaseResult.getSystemErrorInstance(task.getSolutionId());
        }
    }

    private static void releaseCode(String code, int lang) {
        logger.info("Release code...");

        FileUtil.save(Config.JUDGEHOST_TEST_DIR + "Main" + SupportedLanguage.getLangSuffix(lang), code);
    }

    public static CompileResult compileCode(File testRoot, String code, int lang) {
        releaseCode(code, lang);
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

    public static boolean compileSpjCode(File testRoot, String code) {
        logger.info("Begin to compile spj code...");
        CompileResult compileResult = compileCode(testRoot, code, SupportedLanguage.CPP);
        if (compileResult == null || compileResult.isCE()) {
            return false;
        }
        File spj = new File(testRoot.getPath() + "/Main");
        File spjNewName = new File(testRoot.getPath() + "/SpecialJudge");
        return spj.renameTo(spjNewName);
    }

    public static CompileResult compileUserCode(File testRoot, String code, int lang) {
        logger.info("Begin to compile user's code...");
        return compileCode(testRoot, code, lang);
    }

    public static SolutionResult runJudgeTask(JudgeTask judgeTask) {
        File testRoot = new File(Config.JUDGEHOST_TEST_DIR);
        clearOldFiles(testRoot);

        //compile spj code
        if (judgeTask.isSpj()) {
            if (compileSpjCode(testRoot, judgeTask.getSpj())) {
                logger.info("Compile spj successfully.");
            } else {
                logger.error("Compile spj failed!");
                return SolutionResult.getSystemErrorInstance(judgeTask.getSolutionId(), "Compile SPJ got error...");
            }
        }

        // compile user's code
        CompileResult compileResult = compileUserCode(testRoot, judgeTask.getCode(), judgeTask.getLang());

        //SE
        if (compileResult == null) {
            logger.info("Result = System Error.");
            return SolutionResult.getSystemErrorInstance(judgeTask.getSolutionId(), "Something wrong in Compiler...");
        }

        // CE
        if (compileResult.isCE()) {
            logger.info("Result = Compile Error.");
            return SolutionResult.getCompileErrorInstance(judgeTask.getSolutionId(), compileResult);
        }

        logger.info("Compile Successfully, begin to judge.");

        // Judge
        List<CaseTask> tasks = judgeTask.toCaseTask();
        List<CaseResult> results = tasks
                .stream()
                .map(MainJudger::runCaseTask)
                .collect(Collectors.toList());
        return SolutionResult.of(results);
    }

    @Override
    public void run() {
        logger.info(String.format("New task! solutionId = %s", this.judgeTask.getSolutionId()));
        SolutionResult finalResult = runJudgeTask(this.judgeTask);
        logger.info(String.format("Judge done, solutionId = %s, final result is %s", finalResult.getSolutionId(), finalResult.getResult()));

        if (ResultSubmitter.submitSolutionResult(finalResult)) {
            logger.info("SolutionResult submitted.");
        } else {
            logger.error("SolutionResult submit failed.");
        }
    }
}
