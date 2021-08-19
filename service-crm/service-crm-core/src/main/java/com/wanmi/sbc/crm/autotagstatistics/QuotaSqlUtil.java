package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.TagParamColumn;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuotaSqlUtil {

    public static String TagName(String name, StatisticsDimensionInfo info, String column){
        String tagName = "";
        if (TagParamColumn.DATE.equals(info.getParamResult().getParamName())){
            tagName = "case " + column + " when 0 then '周一' when 1 then '周二' when 2 then '周三' when 3 then " +
                    "'周四' when 4 then '周五' when 5 then '周六' when 6 then '周日' end ";
        } else if (TagParamColumn.TIME.equals(info.getParamResult().getParamName())){
            tagName = "case when ('00:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column +
                    ",'HH:mm') <'01:00') then '00:00-01:00' " +
                    "when ('01:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'02:00') then '01:00-02:00'" +
                    "when ('02:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'03:00') then '02:00-03:00'" +
                    "when ('03:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'04:00') then '03:00-04:00'" +
                    "when ('04:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'05:00') then '04:00-05:00'" +
                    "when ('05:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'06:00') then '05:00-06:00'" +
                    "when ('06:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'07:00') then '06:00-07:00'" +
                    "when ('07:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'08:00') then '07:00-08:00'" +
                    "when ('08:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'09:00') then '08:00-09:00'" +
                    "when ('09:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'10:00') then '09:00-10:00'" +
                    "when ('10:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'11:00') then '10:00-11:00'" +
                    "when ('11:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'12:00') then '11:00-12:00'" +
                    "when ('12:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'13:00') then '12:00-13:00'" +
                    "when ('13:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'14:00') then '13:00-14:00'" +
                    "when ('14:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'15:00') then '14:00-15:00'" +
                    "when ('15:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'16:00') then '15:00-16:00'" +
                    "when ('16:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'17:00') then '16:00-17:00'" +
                    "when ('17:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'18:00') then '17:00-18:00'" +
                    "when ('18:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'19:00') then '18:00-19:00'" +
                    "when ('19:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'20:00') then '19:00-20:00'" +
                    "when ('20:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'21:00') then '20:00-21:00'" +
                    "when ('21:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'22:00') then '21:00-22:00'" +
                    "when ('22:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'23:00') then '22:00-23:00'" +
                    "when ('23:00' <= date_format(" + column + ",'HH:mm') and date_format(" + column + ",'HH" +
                    ":mm') <'24:00') then '23:00-00:00'" +
                    " else '' end";
        } else if (TagParamColumn.TERMINAL_SOURCE.equals(info.getParamResult().getParamName())){
            tagName = "case " + column + " when 1 then 'H5' when 2 then 'PC' when 3 then 'APP' when 4 then " +
                    "'小程序' else '' end ";
        } else {
            tagName = "trim(nvl(" + column + ",''))";
        }
        return " concat('" + name + "','：',concat_ws('，', collect_set(" + tagName+ "))) as tagName ";
    }

    public static String sql(String bigDataSql){
        // 大数据weekday替换
        Pattern pattern = Pattern.compile("WEEKDAY\\(.*?\\)");
        Matcher matcher = pattern.matcher(bigDataSql);
        while (matcher.find()){
            String weekDay = matcher.group().trim();
            String subStr = weekDay.substring(8, weekDay.length()-1);
            bigDataSql = bigDataSql.replace(weekDay, "pmod(datediff("+subStr+",'2018-01-01'),7)");
        }
        bigDataSql = bigDataSql.replaceAll("NOW\\(\\)", "date_format\\(CURRENT_TIMESTAMP,'yyyy-MM-dd HH:mm:ss'\\)");
        bigDataSql = bigDataSql.replaceAll("%Y-%m-%d", "yyyy-MM-dd");
        bigDataSql = bigDataSql.replaceAll("interval", "").replaceAll("day\\)", ")");
        return bigDataSql;
    }
}
