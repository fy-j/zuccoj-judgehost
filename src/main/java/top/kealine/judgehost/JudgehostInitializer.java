package top.kealine.judgehost;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import okhttp3.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.testcase.TestcaseLocalTemp;
import top.kealine.judgehost.util.ConfigUtil;
import top.kealine.judgehost.util.FileUtil;
import top.kealine.judgehost.util.HttpUtil;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class JudgehostInitializer {
    public static final Log logger = LogFactory.getLog(JudgehostInitializer.class);

    public static void loadConfig() {
        logger.info("Loading config...");

        // load from properties
        Config.SEVER_HOST = ConfigUtil.get("server.host");
        Config.JUDGEHOST_USERNAME = ConfigUtil.get("judgehost.username");
        Config.JUDGEHOST_PASSWORD = ConfigUtil.get("judgehost.password");
        Config.JUDGEHOST_ROOT = ConfigUtil.get("judgehost.root");
        Config.JUDGEHOST_TESTCASE_LIMIT = Integer.valueOf(ConfigUtil.get("judgehost.testcase.limit"));

        // calc config
        Config.JUDGEHOST_CORE_DIR = Config.JUDGEHOST_ROOT + "core/";
        Config.JUDGEHOST_TESTCASE_DIR = Config.JUDGEHOST_ROOT + "testcase/";
        Config.JUDGEHOST_TEST_DIR = Config.JUDGEHOST_ROOT + "test/";
    }

    public static void loadLocalTestcase() throws Exception {
        logger.info("Loading local testcase...");

        TestcaseLocalTemp.clear();
        File localDir = new File(Config.JUDGEHOST_TESTCASE_DIR);
        if (localDir.isDirectory()) {
            File[] files = localDir.listFiles();
            if (files != null) {
                Arrays.stream(files).forEach( f -> {
                    TestcaseLocalTemp.setTestcase(f.getName(), FileUtil.md5Hex(f));
                });
            }
        } else {
            logger.error("JUDGEHOST_TESTCASE_DIR is not a directory!");
            throw new Exception("[Initializer.loadLocalTestcase] JUDGEHOST_TESTCASE_DIR is not a directory");
        }
    }

    public static void login() throws Exception {
        logger.info("Begin to Login...");
        Response response = HttpUtil.post("/login", ImmutableMap.of(
                "username", Config.JUDGEHOST_USERNAME,
                "password", Config.JUDGEHOST_PASSWORD
        ), false);
        if (response!=null && response.isSuccessful() && response.code() == 200) {
            try {
                JSONObject data = JSON.parseObject(Objects.requireNonNull(response.body()).string());
                if (data.getInteger("code") == 200) {
                    Config.JUDGEHOST_TOKEN = data.getString("data");
                    logger.info("Login successfully");
                } else {
                    logger.error(String.format("Server return code=%s", data.getInteger("code")));
                    throw new Exception(String.format("[Initializer.login] Server return code=%s", data.getInteger("code")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("[Initializer.login] HTTP ERROR / PASSWORD ERROR");
        }
    }

    public static boolean init() {
        try {
            loadConfig();
            loadLocalTestcase();
            login();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
