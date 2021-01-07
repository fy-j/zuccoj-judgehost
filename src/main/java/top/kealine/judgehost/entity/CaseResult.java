package top.kealine.judgehost.entity;

public class CaseResult {
    private long solutionId;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private String remark;

    public CaseResult() {}
    public CaseResult(long solutionId, int result, int memoryUsed, int timeUsed, String remark) {
        this.solutionId = solutionId;
        this.result = result;
        this.memoryUsed = memoryUsed;
        this.timeUsed = timeUsed;
        this.remark = remark;
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
