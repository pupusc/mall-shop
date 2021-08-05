package com.ofpay.rex.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Created by chengyong on 17/8/10.
 */
public class BouncyCastleProviderSingleton {
    private static BouncyCastleProvider instance;

    private BouncyCastleProviderSingleton() {
    }

    public static synchronized BouncyCastleProvider getInstance() {
        if (instance == null) {
            instance = new BouncyCastleProvider();
        }
        return instance;
    }

}

