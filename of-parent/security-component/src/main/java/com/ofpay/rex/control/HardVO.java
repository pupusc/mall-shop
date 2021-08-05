package com.ofpay.rex.control;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;

/**
 * Created by chengyong on 14/12/12.
 */
public class HardVO implements Serializable {

    // (bios,cpuId,diskId) (mainboard,mac)

    private static final long serialVersionUID = 4751649544138950507L;

    /****
     * mac地址
     */
    private String mac;

    public String getCpuId() {
        return cpuId;
    }

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
    }

    /****
     * cpuid
     */
    private String cpuId;

    /****
     * 硬盘id
     */
    private String diskId;

    /****
     * biosid
     */
    private String biosId;

    /****
     * 主板id
     */
    private String mainboard;

    /*****
     * 机器指纹
     */
    private String SerialNumber;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }

    public String getBiosId() {
        return biosId;
    }

    public void setBiosId(String biosId) {
        this.biosId = biosId;
    }

    public String getMainboard() {
        return mainboard;
    }

    public void setMainboard(String mainboard) {
        this.mainboard = mainboard;
    }

    public String getSerialNumber() {
        return DigestUtils.md5Hex((this.getBiosId() + this.getCpuId() + this.getDiskId()).getBytes());
    }


}
