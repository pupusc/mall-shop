package com.ofpay.rex.control;

import java.io.Serializable;

/**
 * Created by chengyong on 14/12/12.
 */
public class SafeControlVO implements Serializable {

    private static final long serialVersionUID = 4751649544138950507L;


    private String password;
    private String mac;
    private String cupid;



    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCupid() {
        return cupid;
    }

    public void setCupid(String cupid) {
        this.cupid = cupid;
    }


}
