package top.kealine.judgehost.entity;

import com.google.common.collect.ImmutableMap;
import top.kealine.judgehost.constant.JudgeResult;

import java.util.List;
import java.util.Map;

public class SolutionResult {
    private long solutionId;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private String remark;

    public SolutionResult() {}
    public SolutionResult(long solutionId, int result, int memoryUsed, int timeUsed, String remark) {
        this.solutionId = solutionId;
        this.result = result;
        this.memoryUsed = memoryUsed;
        this.timeUsed = timeUsed;
        this.remark = remark;
    }

    public static SolutionResult of(List<CaseResult> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }
        int finalResult = JudgeResult.AC;
        int maxMemoryUsed = -1;
        int maxTimeUsed = -1;
        for(CaseResult result: results) {
            maxMemoryUsed = Math.max(maxMemoryUsed, result.getMemoryUsed());
            maxTimeUsed = Math.max(maxTimeUsed, result.getTimeUsed());
            if (result.getResult() != JudgeResult.AC) {
                finalResult = result.getResult();
                break;
            }
        }
        return new SolutionResult(
                results.get(0).getSolutionId(),
                finalResult,
                maxMemoryUsed,
                maxTimeUsed,
                results.get(0).getRemark()
        );
    }

    public static SolutionResult getCompileErrorInstance(long solutionId, CompileResult compileResult) {
        assert compileResult.isCE();
        return new SolutionResult(
                solutionId,
                JudgeResult.COMPILE_ERROR,
                0,
                0,
                compileResult.getCompilerOutput()
        );
    }

    public static SolutionResult getSystemErrorInstance(long solutionId) {
        return new SolutionResult(
                solutionId,
                JudgeResult.SYSTEM_ERROR,
                0,
                0,
                "System Error"
        );
    }

    public Map<String, Object> toMap() {
        return ImmutableMap.<String, Object>builder()
                .put("solutionId", solutionId)
                .put("result", result)
                .put("memoryUsed", memoryUsed)
                .put("timeUsed", timeUsed)
                .put("remark", remark)
                .build();
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(int memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
