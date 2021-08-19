package com.wanmi.sbc.marketing.cache.handler;

import java.util.Map;

/**
 * <p></p>
 *
 * @author: Maosheng Liu
 * @time: 2020/7/30 9:45
 */
public interface ICacheKeyHandler<T,E> {
    Map<T,E> handle(Map<T, E> container, Object... params);
}
