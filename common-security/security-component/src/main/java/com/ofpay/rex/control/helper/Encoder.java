package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public abstract interface Encoder
{
    public abstract Object encode(Object paramObject)
            throws EncoderException;
}
