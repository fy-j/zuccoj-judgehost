package top.kealine.judgehost.entity;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.constant.JudgeResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SolutionResult {
    private long solutionId;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private String remark;
    private List<Integer> passTestcase;

    public SolutionResult() {}
    public SolutionResult(long solutionId, int result, int memoryUsed, int timeUsed, String remark, List<Integer> passTestcase) {
        this.solutionId = solutionId;
        this.result = result;
        this.memoryUsed = memoryUsed;
        this.timeUsed = timeUsed;
        this.remark = remark.trim().replaceAll(Config.JUDGEHOST_TEST_DIR, "");
        this.passTestcase = passTestcase == null ? ImmutableList.of() : passTestcase;
    }

    public static SolutionResult of(List<CaseResult> results) {
        if (results == null || results.isEmpty()) {
            return null;
        }
        CaseResult finalResult = null;
        int maxMemoryUsed = -1;
        int maxTimeUsed = -1;
        List<Integer> passTestcase = new LinkedList<>();
        for(CaseResult result: results) {
            maxMemoryUsed = Math.max(maxMemoryUsed, result.getMemoryUsed());
            maxTimeUsed = Math.max(maxTimeUsed, result.getTimeUsed());
            if (result.getResult() == JudgeResult.AC) {
                passTestcase.add(result.getTestcaseId());
            } else if (finalResult == null){
                finalResult = result;
            }
        }
        return new SolutionResult(
                results.get(0).getSolutionId(),
                finalResult == null ? JudgeResult.AC :finalResult.getResult(),
                maxMemoryUsed,
                maxTimeUsed,
                finalResult == null ? "" :finalResult.getRemark(),
                passTestcase
        );
    }

    public static SolutionResult getCompileErrorInstance(long solutionId, CompileResult compileResult) {
        assert compileResult.isCE();
        return new SolutionResult(
                solutionId,
                JudgeResult.COMPILE_ERROR,
                0,
                0,
                compileResult.getCompilerOutput(),
                null
        );
    }

    public static SolutionResult getSystemErrorInstance(long solutionId) {
        return getSystemErrorInstance(solutionId, "System Error");
    }

    public static SolutionResult getSystemErrorInstance(long solutionId, String msg) {
        return new SolutionResult(
                solutionId,
                JudgeResult.SYSTEM_ERROR,
                0,
                0,
                msg,
                null
        );
    }

    public Map<String, Object> toMap() {
        return ImmutableMap.<String, Object>builder()
                .put("solutionId", solutionId)
                .put("result", result)
                .put("memoryUsed", memoryUsed)
                .put("timeUsed", timeUsed)
                .put("remark", remark)
                .put("passTestcase", JSON.toJSON(passTestcase))
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
