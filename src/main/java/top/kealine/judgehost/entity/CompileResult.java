package top.kealine.judgehost.entity;

import top.kealine.judgehost.config.Config;

import java.io.*;

public class CompileResult {
    private int status;
    private String compilerOutput;

    public CompileResult(){}

    public static CompileResult getCompileResult(){
        CompileResult result = new CompileResult();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(Config.JUDGEHOST_TEST_DIR + "result.txt")));
            String line = br.readLine();
            result.setStatus(Integer.parseInt(line));
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            result.setCompilerOutput(sb.toString().trim());
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

    public boolean isCE() {
        return (status !=1 );
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCompilerOutput() {
        return compilerOutput;
    }

    public void setCompilerOutput(String compilerOutput) {
        this.compilerOutput = compilerOutput;
    }
}
