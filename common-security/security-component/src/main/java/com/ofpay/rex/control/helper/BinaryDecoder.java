package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public abstract interface BinaryDecoder
        extends Decoder
{
    public abstract byte[] decode(byte[] paramArrayOfByte)
            throws DecoderException;
}
