package com.wanmi.sbc.util;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author chenzhen
 */
public class DitaUtil {

    /**
     * 得到yyyy-MM-dd HH:mm:ss<br>
     * 例如：return 2007-05:03 25:03:19<br>
     */
    public static String getCurrentAllDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 小写的hh取得12小时，大写的HH取的是24小时
        Date date = new Date();
        return df.format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss 天数+-
     * @return
     */
    public static String dayDiff(String datetime,int days){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try
        {
            date = format.parse(datetime);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        if (date==null) return "";
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH,days);
        date=cal.getTime();
        //System.out.println("3 days after(or before) is "+format.format(date));
        cal=null;
        return format.format(date);
    }

    public static boolean isBlank(String strIn) {
        if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotBlank(String strIn) {
        if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static List<String> randomList(String userIds, String proied) {
        String[] ids = userIds.split(",", -1);
        int len = ids.length;
        List<String> users = new ArrayList<>();
        double v = len * Double.valueOf(proied);
        if(v<1){
            Integer range = RandomUtil.getRandomRange(0, len - 1);
            users.add(ids[range]);
        }else{
            //四舍五入
            int round = (int) Math.round(v);
            List<Integer> number = RandomUtil.getNumber(0, len, round);
            for (Integer num:number) {
                users.add(ids[num]);
            }
        }
        return users;
    }

    public static boolean isPhoneEndWith(String customerAccount, String proied) {
       if(StringUtils.isBlank(proied)||StringUtils.isBlank(customerAccount)){
           return false;
       }
        for (String endStr:proied.split(",",-1)) {
             if(customerAccount.endsWith(endStr)){
                 return true;
             }
        }
        return false;
    }
}