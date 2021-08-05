package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public abstract interface BinaryEncoder
        extends Encoder
{
    public abstract byte[] encode(byte[] paramArrayOfByte)
            throws EncoderException;
}
