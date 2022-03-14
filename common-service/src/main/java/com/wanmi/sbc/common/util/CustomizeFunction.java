package com.wanmi.sbc.common.util;

/**
 * @author: Geek Wang
 * @createDate: 2019/6/27 14:04
 * @version: 1.0
 */
@FunctionalInterface
public interface CustomizeFunction<T,R> {

	R handler(T a, T b);
}

