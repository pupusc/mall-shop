package com.wanmi.sbc.common.util.auth;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @ClassName: NetworkUtil
 * @Description: 网络接口工具类,用于获取ip地址，mac地址，CPU_ID等信息
 * @Author: ZhangLingKe
 * @CreateDate: 2019/4/16 15:28
 * @Version: 1.0
 */
public class NetworkUtil {

    /**
     * 尽量获取site-local的网络接口信息
     * @return
     * @throws UnknownHostException
     * @throws SocketException
     */
    public static String getLocalMac() throws UnknownHostException, SocketException {
        return getMacByInetAddress(getLocalHostLANAddress());
    }

    public static String getMacByInetAddress(InetAddress ia) throws SocketException {
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        if (Objects.isNull(mac)) {
            return "127.0.0.1";
        }

        StringBuffer sb = new StringBuffer();
        for(int i=0; i<mac.length; i++) {
            if(i!=0) {
                sb.append("-");
            }
            int temp = mac[i]&0xff;
            String str = Integer.toHexString(temp);
            if(str.length()==1) {
                sb.append("0"+str);
            }else {
                sb.append(str);
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 获取本机ip 尽量获取site-local
     * @return
     * @throws UnknownHostException
     */
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }


}
