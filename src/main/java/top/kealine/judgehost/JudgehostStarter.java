package top.kealine.judgehost;

import top.kealine.judgehost.worker.HeartbeatPublisher;
import top.kealine.judgehost.worker.JudgeTaskListener;

public class JudgehostStarter {
    /*
        Judgehost-Starter
     */
    public static void main(String[] args) {
        if (!JudgehostInitializer.init()) {
            System.out.println("[JudgehostStarter.main] Initialize Failed");
            return;
        }
        new Thread(new HeartbeatPublisher()).start();
        System.out.println("[JudgehostStarter.main] Thread <HeartbeatPublisher> is running...");

        new Thread(new JudgeTaskListener()).start();
        System.out.println("[JudgehostStarter.main] Thread <JudgeTaskListener> is running...");
    }
}
