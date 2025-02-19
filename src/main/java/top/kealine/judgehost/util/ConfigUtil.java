package top.kealine.judgehost.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.JudgehostStarter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class ConfigUtil {

    private static final Log logger = LogFactory.getLog(ConfigUtil.class);
    private final static String FILE = "judgehost.properties";
    private final static Properties PROPERTIES = new Properties();

    static{
        try {
            logger.info(ClassLoader.getSystemResourceAsStream(FILE));
            PROPERTIES.load(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(FILE)), StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toEvnName(String key) {
        return key.toUpperCase().replaceAll("\\.", "_");
    }

    public static String getFromEvn(String key) {
        return System.getenv(toEvnName(key));
    }

    public static String get(String key){
        return get(key, null);
    }

    public static String get(String key,String defaultValue){
        String value = getFromEvn(key);
        return value == null ? PROPERTIES.getProperty(key, defaultValue) : value;
    }
}
