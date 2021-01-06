package top.kealine.judgehost;

import org.junit.Test;
import top.kealine.judgehost.testcase.LRUCache;

public class LRUCacheTest {

    @Test
    public void test() {
        LRUCache<Integer, String> lruCache = new LRUCache<>(5);
        lruCache.put(1, "1");
        lruCache.put(2, "2");
        lruCache.put(3, "3");
        lruCache.put(4, "4");
        lruCache.put(5, "5");
        assert lruCache.getRemoved() == null;
        lruCache.put(6, "6");
        assert lruCache.getRemoved().getKey().equals(1);
        assert lruCache.getOrDefault(1, null) == null;
        lruCache.get(2);
        lruCache.put(7, "7");
        assert lruCache.getRemoved().getKey().equals(3);
        assert lruCache.getOrDefault(3, null) == null;
        assert lruCache.getOrDefault(2, null).equals("2");
        assert lruCache.size() == 5;
    }
}
