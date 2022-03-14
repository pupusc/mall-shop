package com.wanmi.ms.log;

import java.util.HashMap;
import java.util.Map;

/**
 * Dubbo接口日志摆渡器
 * Created by aqlu on 14-3-26.
 */
public final class IntfLogThreadFerry {
    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>();

    public static final String UUID = "uuid";

    public static final String SESSIONID = "sessionId";

    /**
     * 注意：在设置UUID时，
     * @param uuid uuid
     */
    public static void putUUID(String uuid) {
        getThreadContext().put(UUID, uuid);
    }

    public static void putSessionId(String sessionId) {
        getThreadContext().put(SESSIONID, sessionId);
    }

    public static void removeUUID() {
        getThreadContext().remove(UUID);
    }

    public static void removeSessionId() {
        getThreadContext().remove(SESSIONID);
    }

    public static String getUUID() {
        return getThreadContext().get(UUID);
    }

    public static String getSessionId() {
        return getThreadContext().get(SESSIONID);
    }

    public static void clean() {
        threadLocal.remove();
    }

    public static Map<String, String> getThreadContext() {
        Map<String, String> map = threadLocal.get();

        if (map == null) {
            HashMap<String, String> newMap = new HashMap<String, String>();
            threadLocal.set(newMap);

            map = threadLocal.get();
        }
        return map;
    }
}
