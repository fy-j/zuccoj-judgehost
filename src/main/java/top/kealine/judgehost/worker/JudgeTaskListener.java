package top.kealine.judgehost.worker;

import redis.clients.jedis.Jedis;
import top.kealine.judgehost.entity.JudgeTask;
import top.kealine.judgehost.util.ConfigUtil;

import java.util.List;

public class JudgeTaskListener implements Runnable{
    private static final String JUDGE_TASK_QUEUE_KEY = "ZUCCOJ::JUDGE::TASK::QUEUE";
    private static final Jedis redis;

    static {
        redis = new Jedis(ConfigUtil.get("redis.host"), Integer.parseInt(ConfigUtil.get("redis.port")));
        redis.auth(ConfigUtil.get("redis.password"));
        redis.select( Integer.parseInt(ConfigUtil.get("redis.database")));
    }

    private String listen() {
        while (true) {
            List<String> tasks = redis.brpop(0, JUDGE_TASK_QUEUE_KEY);
            if (tasks != null && tasks.size()>=2) {
                return tasks.get(1);
            }
        }
    }

    public void begin() {
        System.out.println("[JudgeTaskListener.begin] Begin to listen task");
        while (true) {
            MainJudger.runJudgeTask(new JudgeTask(listen()));
        }
    }

    @Override
    public void run() {
        begin();
    }
}
