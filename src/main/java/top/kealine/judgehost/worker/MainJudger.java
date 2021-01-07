package top.kealine.judgehost.worker;

import top.kealine.judgehost.constant.JudgeResult;
import top.kealine.judgehost.entity.CaseResult;
import top.kealine.judgehost.entity.CaseTask;
import top.kealine.judgehost.entity.JudgeTask;
import top.kealine.judgehost.entity.SolutionResult;

import java.util.List;
import java.util.stream.Collectors;

public class MainJudger {
    private static void prepareEnv() {

    }

    private static CaseResult runCaseTask(CaseTask task) {
        prepareEnv();
        return new CaseResult(
                task.getSolutionId(),
                JudgeResult.AC,
                1024000,
                674,
                ""
        );
    }

    public static void runJudgeTask(JudgeTask judgeTask) {
        System.out.println("[MainJudger] new task solutionId=" + judgeTask.getSolutionId());
        List<CaseTask> tasks = judgeTask.toCaseTask();
        List<CaseResult> results = tasks.stream().map(MainJudger::runCaseTask).collect(Collectors.toList());
        SolutionResult result = SolutionResult.of(results);
        if (!ResultSubmitter.submitSolutionResult(result)) {
            System.out.println("[MainJudger] result submit failed.");
        }
    }
}
