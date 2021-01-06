package top.kealine.judgehost.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class JudgeTask {
    private long solutionId;
    private String code;
    private List<Integer> testcaseList;
    private int problemId;
    private int timeLimit;
    private int memoryLimit;
    private int lang;

    public JudgeTask(String json) {
        JSONObject data = JSON.parseObject(json);
        this.solutionId = data.getLong("solutionId");
        this.code = data.getString("code");
        this.testcaseList = data.getJSONArray("testcaseList").toJavaList(Integer.class);
        this.problemId = data.getInteger("problemId");
        this.timeLimit = data.getInteger("timeLimit");
        this.memoryLimit = data.getInteger("memoryLimit");
        this.lang = data.getInteger("lang");
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

    public List<Integer> getTestcaseList() {
        return testcaseList;
    }

    public void setTestcaseList(List<Integer> testcaseList) {
        this.testcaseList = testcaseList;
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

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }
}
