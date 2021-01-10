package top.kealine.judgehost.entity;

public class CaseTask {
    private long solutionId;
    private int testcaseId;
    private String code;
    private int problemId;
    private int timeLimit;
    private int memoryLimit;
    private int lang;
    private boolean isSpj;

    public CaseTask(long solutionId, int testcaseId, String code, int problemId, int timeLimit, int memoryLimit, int lang, boolean isSpj) {
        this.solutionId = solutionId;
        this.testcaseId = testcaseId;
        this.code = code;
        this.problemId = problemId;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.lang = lang;
        this.isSpj = isSpj;
    }

    public long getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(long solutionId) {
        this.solutionId = solutionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(int testcaseId) {
        this.testcaseId = testcaseId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public int getLang() {
        return lang;
    }

    public void setLang(int lang) {
        this.lang = lang;
    }

    public boolean isSpj() {
        return isSpj;
    }

    public void setSpj(boolean spj) {
        isSpj = spj;
    }
}
