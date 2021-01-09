package top.kealine.judgehost.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import top.kealine.judgehost.entity.JudgeTask;
import top.kealine.judgehost.util.ConfigUtil;

import java.util.List;

public class JudgeTaskListener implements Runnable{
    private static final Log logger = LogFactory.getLog(JudgeTaskListener.class);
    private static final String JUDGE_TASK_QUEUE_KEY = "ZUCCOJ::JUDGE::TASK::QUEUE";
    private static final Jedis redis;

    static {
        redis = new Jedis(ConfigUtil.get("redis.host"), Integer.parseInt(ConfigUtil.get("redis.port")));
        redis.auth(ConfigUtil.get("redis.password"));
        redis.select( Integer.parseInt(ConfigUtil.get("redis.database")));
    }

    private String listen() {
        while (true) {
            logger.info("Listening for task...");
            try {
                List<String> tasks = redis.brpop(0, JUDGE_TASK_QUEUE_KEY);
                if (tasks != null && tasks.size()>=2) {
                    return tasks.get(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Redis BRPOP command error... try again...");
            }
        }
    }

    public void begin() {
        logger.info("JudgeTaskListener is running...");
        while (true) {
            try {
                Thread judger = new Thread(new MainJudger(new JudgeTask(listen())), "MainJudger");
                judger.start();
                logger.info("Thread <MainJudger> start successfully, waiting for it exit.");
                judger.join();
                logger.info("Thread <MainJudger> exit.");
            } catch (Exception e) {
                logger.error("MainJudger error.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        begin();
    }
}
