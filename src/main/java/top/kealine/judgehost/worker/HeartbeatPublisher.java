package top.kealine.judgehost.worker;

import redis.clients.jedis.Jedis;
import top.kealine.judgehost.util.ConfigUtil;

public class HeartbeatPublisher implements Runnable{
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
        try {
            System.out.println("[HeartbeatPublisher.begin] Begin to heartbeat");
            while(true) {
                sendHeartbeat();
                Thread.sleep(10 * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        begin();
    }
}
