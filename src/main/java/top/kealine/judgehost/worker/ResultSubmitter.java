package top.kealine.judgehost.worker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import top.kealine.judgehost.JudgehostInitializer;
import top.kealine.judgehost.entity.SolutionResult;
import top.kealine.judgehost.util.HttpUtil;

import java.util.Objects;

public class ResultSubmitter {
    public static boolean submitSolutionResult(SolutionResult result) {
        int tryTimes = 10;
        while (tryTimes-- > 0) {
            Response response = HttpUtil.post("/judge", result.toMap());
            if (response == null || !response.isSuccessful() || response.code() != 200) {
                continue;
            }
            try {
                JSONObject data = JSON.parseObject(Objects.requireNonNull(response.body()).string());
                int code = data.getInteger("code");
                if (code == 200) {
                    return true;
                } else if (code == 700) {
                    System.out.println("[ResultSubmitter] Submit Result got 700, try to re-login.");
                    try {
                        JudgehostInitializer.login();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
