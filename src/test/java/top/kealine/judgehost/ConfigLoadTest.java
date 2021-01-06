package top.kealine.judgehost;

import org.junit.Test;
import top.kealine.judgehost.util.ConfigUtil;

public class ConfigLoadTest {
    @Test
    public void testConfig() {
        assert ConfigUtil.get("judgehost.username").equals("ArcherLuo");
        assert ConfigUtil.get("judgehost.password").equals("judgehostArcherLuo");
    }
}
