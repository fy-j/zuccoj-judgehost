package top.kealine.judgehost.testcase;

import top.kealine.judgehost.config.Config;
import top.kealine.judgehost.util.FileUtil;

import java.util.AbstractMap;
import java.util.Map;

public class TestcaseLocalTemp {
    private static final LRUCache<String, String> localTemp;

    static {
        localTemp = new LRUCache<>(Config.JUDGEHOST_TESTCASE_LIMIT);
    }

    public static void clear() {
        localTemp.clear();
    }

    public static void setTestcase(String filename, String md5) {
        localTemp.put(filename, md5);
        Map.Entry<String, String> removed = localTemp.getRemoved();
        if (removed != null) {
            FileUtil.delete(filename);
        }
    }

    public static Map.Entry<String,String> getTestcase(int testcaseId, boolean isInput) {
        String filename = testcaseId + (isInput?".in":".ans");
        String md5 = localTemp.getOrDefault(filename, null);
        if (md5 == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<String, String>(filename, md5);
    }

}
