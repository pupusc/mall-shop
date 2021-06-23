package com.wanmi.sbc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统工具类
 * Created by d on 2017/4/28.
 */
@Component
public class OsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsUtil.class);

    @Value("${manager.mode}")
    private String managerMode;

    public boolean isS2b() {
        return "s2b".equalsIgnoreCase(managerMode);
    }

    public boolean isB2b() {
        return !isS2b();
    }


    /**
     * 当前环境是否是window
     *
     * @return true:是,false:否
     */
    public boolean isWindow() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * 单命令方法
     *
     * @param cmd
     * @return
     */
    public String callCmd(String[] cmd) {
        StringBuilder result = new StringBuilder();
        String line;
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            is = new InputStreamReader(proc.getInputStream());
            br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            LOGGER.error("单命令Cmd运行异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("单命令Cmd运行关闭写入异常", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("单命令Cmd运行关闭写入异常", e);
                }
            }
        }
        return result.toString();
    }

    /**
     * 双命令方法
     *
     * @param cmd     第一个命令
     * @param another 第二个命令
     * @return 第二个命令的执行结果
     */
    public String callCmd(String[] cmd, String[] another) {
        StringBuilder result = new StringBuilder();
        String line = "";
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            int i = proc.waitFor();//已经执行完第一个命令，准备执行第二个命令
            if (i == 0) {//0标识执行成功 其他为异常
                proc = rt.exec(another);
                is = new InputStreamReader(proc.getInputStream());
                br = new BufferedReader(is);
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
        } catch (Exception e) {
            LOGGER.error("双命令Cmd运行异常", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    LOGGER.error("双命令Cmd运行关闭异常", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("双命令Cmd运行关闭异常", e);
                }
            }
        }
        return result.toString();
    }

    /**
     * @param ip           目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    public String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; //如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }

    /**
     * 获取mac地址（window环境）
     *
     * @param ip 目标ip
     * @return Mac Address
     */
    public String getMacInWindows(final String ip) {
        String[] cmd = {"cmd", "/c", "ping ".concat(ip)};
        String[] another = {"cmd", "/c", "arp -a"};
        String cmdResult = callCmd(cmd, another);
        return filterMacAddress(ip, cmdResult, "-");
    }

    /**
     * 获取mac地址（linux环境）
     *
     * @param ip 目标ip
     * @return  Mac Address
     */
    public String getMacInLinux(final String ip) {
        String[] cmd = {"/bin/sh", "-c", "ping ".concat(ip).concat(" -c 2 && arp -a")};
        String cmdResult = callCmd(cmd);
        return filterMacAddress(ip, cmdResult, ":");
    }

    /**
     * 获取MAC地址
     *
     * @param ip 目标ip
     * @return 返回MAC地址
     */
    public String getMacAddress(String ip) {
        if (isWindow()) {
            return getMacInWindows(ip).trim();
        }
        return getMacInLinux(ip).trim();
    }
}
