package top.kealine.judgehost.worker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.JudgehostInitializer;
import top.kealine.judgehost.entity.SolutionResult;
import top.kealine.judgehost.util.HttpUtil;

import java.util.Objects;

public class ResultSubmitter {
    public static final Log logger = LogFactory.getLog(ResultSubmitter.class);

    public static boolean submitSolutionResult(SolutionResult result) {
        int tryTimes = 10;
        while (tryTimes-- > 0) {
            logger.info(String.format("Begin to submit result, rest try times = %s", tryTimes));
            Response response = HttpUtil.post("/judge", result.toMap());
            if (response == null || !response.isSuccessful() || response.code() != 200) {
                logger.error("Submit failed in HTTP.");
                continue;
            }
            try {
                JSONObject data = JSON.parseObject(Objects.requireNonNull(response.body()).string());
                int code = data.getInteger("code");
                if (code == 200) {
                    logger.info("Submit successfully.");
                    return true;
                } else if (code == 700) {
                    logger.error("Got code 700, try to re-login.");
                    try {
                        JudgehostInitializer.login();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Login Failed.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Submit failed.");
            }
        }
        return false;
    }
}
