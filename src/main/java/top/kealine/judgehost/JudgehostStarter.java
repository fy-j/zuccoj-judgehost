package top.kealine.judgehost;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import top.kealine.judgehost.util.ConfigUtil;
import top.kealine.judgehost.worker.HeartbeatPublisher;
import top.kealine.judgehost.worker.JudgeTaskListener;

public class JudgehostStarter {
    private static final Log logger = LogFactory.getLog(JudgehostStarter.class);
    /*
        Judgehost-Starter
     */
    public static void main(String[] args) {
        logger.info(String.format("Judgehost %s is running...", ConfigUtil.get("judgehost.username")));
        if (!JudgehostInitializer.init()) {
            logger.error("Judgehost initialize failed! Program exit.");
            return;
        }
        new Thread(new HeartbeatPublisher(), "HeartbeatPublisher").start();
        logger.info("Thread <HeartbeatPublisher> start successfully.");

        new Thread(new JudgeTaskListener(), "JudgeTaskListener").start();
        logger.info("Thread <JudgeTaskListener> start successfully.");
    }
}
