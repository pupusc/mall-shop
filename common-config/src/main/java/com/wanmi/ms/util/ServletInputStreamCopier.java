package com.wanmi.ms.util;


import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * copy bytes to a buffer
 */
public class ServletInputStreamCopier extends ServletInputStream {

    private boolean readed;

    /**
     * output stream
     */
    private InputStream inputStream;

    /**
     * keep a copy
     */
    private ByteArrayOutputStream copy;

    /**
     * constructor
     */
    public ServletInputStreamCopier(InputStream inputStream1) {
        this.inputStream = inputStream1;
        this.copy = new ByteArrayOutputStream(1024);
    }

    /**
     * @see InputStream#read()
     */
    @Override
    public int read() throws IOException {
        int result = this.inputStream.read();
        if (result != -1) {
            this.copy.write(result);
        } else {
            this.readed = true;
        }
        return result;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int count = this.inputStream.read(b);
        if (count > 0) {
            this.copy.write(b, 0, count);
        } else {
            this.readed = true;
        }
        return count;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = this.inputStream.read(b, off, len);
        if (count > 0) {
            this.copy.write(b, off, count);
            if (off + len >= b.length) {
                this.readed = true;
            }
        } else {
            this.readed = true;
        }
        return count;
    }

    /**
     * get the copy
     *
     * @return bytes
     */
    public byte[] getCopy() {
        return this.copy.toByteArray();
    }

    public boolean isFinished() {
        return this.readed;
    }

    public boolean isReady() {
        return false;
    }

    public void setReadListener(ReadListener listener) {
    }
}