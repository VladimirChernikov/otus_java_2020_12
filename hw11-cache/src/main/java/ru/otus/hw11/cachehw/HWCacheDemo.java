package ru.otus.hw11.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sergey
 * created on 14.12.18.
 */
public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        HwListener<String, Integer> listener2 = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {} listener2", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put(new String("1"), Integer.valueOf(1));
        cache.put(new String("2"), Integer.valueOf(1));
        cache.addListener(listener2);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");
        logger.info("getValue:{}", cache.get("1"));
        // logger.info("getValue:{}", cache.get("2"));
        // cache.removeListener(listener);
        //
        listener2 = null;
        
        System.gc();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        logger.info("getValue after gc: {}", cache.get("1"));
        logger.info("getValue after gc: {}", cache.get("2"));
        
    }
}
