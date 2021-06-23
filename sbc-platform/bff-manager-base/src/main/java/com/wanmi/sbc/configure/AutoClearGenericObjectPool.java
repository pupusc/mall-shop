package com.wanmi.sbc.configure;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class AutoClearGenericObjectPool<T> extends GenericObjectPool<T> {

    public AutoClearGenericObjectPool(PooledObjectFactory<T> factory) {
        super(factory);
    }

    public AutoClearGenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }

    @Override
    public void returnObject(T obj) {
        super.returnObject(obj);
        //空闲数>=激活数时，清理掉空闲连接
        //保证 有5个守护
        if ((getNumIdle() + getNumActive() >5) && getNumIdle() >= getNumActive()) {
            clear();
        }
    }

}

