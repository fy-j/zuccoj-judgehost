package top.kealine.judgehost.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import top.kealine.judgehost.util.ConfigUtil;

public class HeartbeatPublisher implements Runnable{
    private static final Log logger = LogFactory.getLog(HeartbeatPublisher.class);

    private static final String JUDGEHOST_HEARTBEAT_KEY;
    private static final Jedis redis;

    static {
        JUDGEHOST_HEARTBEAT_KEY = "ZUCCOJ::JUDGEHOST::HEARTBEAT::" + ConfigUtil.get("judgehost.username");
        redis = new Jedis(ConfigUtil.get("redis.host"), Integer.parseInt(ConfigUtil.get("redis.port")));
        redis.auth(ConfigUtil.get("redis.password"));
        redis.select( Integer.parseInt(ConfigUtil.get("redis.database")));
    }

    private static void sendHeartbeat() {
        redis.set(JUDGEHOST_HEARTBEAT_KEY, Long.toString(System.currentTimeMillis()));
//        redis.expire(JUDGEHOST_HEARTBEAT_KEY, 10);
    }

    public static void begin() {
        while (true) {
            logger.info("HeartbeatPublisher is running...");
            try {
                while(true) {
                    sendHeartbeat();
                    Thread.sleep(10 * 1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Sleep to dead... try again...");
            }
        }
    }

    @Override
    public void run() {
        begin();
    }
}
