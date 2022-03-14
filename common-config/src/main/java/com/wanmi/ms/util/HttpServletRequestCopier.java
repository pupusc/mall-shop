package com.wanmi.ms.util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpServletRequestCopier
 * Created by aqlu on 16/5/4.
 */
public class HttpServletRequestCopier extends HttpServletRequestWrapper {

    /**
     * output stream
     */
    private ServletInputStream inputStream;

    /**
     * reader
     */
    private BufferedReader reader;

    /**
     * copy inputstream
     */
    private ServletInputStreamCopier copier;


    public HttpServletRequestCopier(HttpServletRequest request) {
        super(request);

    }

    /**
     * @see javax.servlet.ServletRequestWrapper#getInputStream()
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.reader != null) {
            throw new IllegalStateException(
                    "getReader() has already been called on this request.。。。");
        }
        if (this.inputStream == null) {
            this.inputStream = getRequest().getInputStream();
            this.copier = new ServletInputStreamCopier(this.inputStream);
        }
        return this.copier;
    }

    public BufferedReader getReader() throws IOException {
        if (inputStream != null) {
            throw new IllegalStateException("getInputStream() has already been called on this request.");
        }

        if (reader == null) {
            copier = new ServletInputStreamCopier(getRequest().getInputStream());
            reader = new BufferedReader(new InputStreamReader(copier));
        }

        return reader;
    }

    /**
     * get copy
     * @return copy
     */
    public byte[] getCopy() {
        if (this.copier != null) {
            return this.copier.getCopy();
        }
        return new byte[0];
    }

}
