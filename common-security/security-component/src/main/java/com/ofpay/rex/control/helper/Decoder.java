package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public abstract interface Decoder
{
    public abstract Object decode(Object paramObject)
            throws DecoderException;
}
