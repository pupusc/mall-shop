package com.ofpay.rex.control.helper;

/**
 * Created by Miner on 2016/3/7.
 */
public class EncoderException
        extends Exception
{
    private static final long serialVersionUID = 1L;

    public EncoderException() {}

    public EncoderException(String message)
    {
        super(message);
    }

    public EncoderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public EncoderException(Throwable cause)
    {
        super(cause);
    }
}
