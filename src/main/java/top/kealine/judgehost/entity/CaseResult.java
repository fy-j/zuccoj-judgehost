package top.kealine.judgehost.entity;

import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.constant.JudgeResult;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CaseResult {
    private long solutionId;
    private int testcaseId;
    private int result;
    private int memoryUsed;
    private int timeUsed;
    private String remark;

    public CaseResult() {}
    public CaseResult(long solutionId, int testcaseId, int result, int memoryUsed, int timeUsed, String remark) {
        this.solutionId = solutionId;
        this.result = result;
        this.memoryUsed = memoryUsed;
        this.timeUsed = timeUsed;
        this.remark = remark;
        this.testcaseId = testcaseId;
    }

    public static CaseResult getCaseResult(long solutionId, int testcaseId){
        CaseResult result = new CaseResult();
        result.setSolutionId(solutionId);
        result.setTestcaseId(testcaseId);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(Config.JUDGEHOST_TEST_DIR + "judge/result.txt")));
            String line = br.readLine();
            result.setResult(Integer.parseInt(line));
            line = br.readLine();
            result.setTimeUsed(Integer.parseInt(line));
            line = br.readLine();
            result.setMemoryUsed(Integer.parseInt(line));
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            result.setRemark(sb.toString());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static CaseResult getSystemErrorInstance(long solutionId, int testcaseId) {
        return new CaseResult(
                solutionId,
                testcaseId,
                JudgeResult.SYSTEM_ERROR,
                0,
                0,
                "SYSTEM_ERROR"
        );
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

    public int getTestcaseId() {
        return testcaseId;
    }

    public void setTestcaseId(int testcaseId) {
        this.testcaseId = testcaseId;
    }
}
