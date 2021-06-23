package com.wanmi.ares.utils;


import java.time.LocalDate;

/**
 * 商品的工具类
 * Created by zhangjin on 2017/4/28.
 */
public class GoodsDateUtil {

    /**
     * 根据类型返回时间范围
     * @param type 时间类型
     * @return 时间格式化
     */
    public static String getDay(int type){
//        if(type == Constants.GoodsGenerateType.LATEST7DAYS || type == Constants.GoodsGenerateType.LATEST30DAYS){
//            return null;
//        }

        LocalDate time = LocalDate.now();
        switch (type){
            case Constants.GoodsGenerateType.YESTERDAY:
                time = time.minusDays(1);
                break;
            case Constants.GoodsGenerateType.LATEST7DAYS:
                time = time.minusDays(7);
                break;
            case Constants.GoodsGenerateType.LATEST30DAYS:
                time = time.minusDays(30);
                break;
            case Constants.GoodsGenerateType.MONTH://上一个月
                time = time.minusMonths(1);
                time = LocalDate.of(time.getYear(), time.getMonth(), 1);
                return DateUtil.format(time,DateUtil.FMT_MONTH_1);
        }
        return DateUtil.format(time,DateUtil.FMT_DATE_1);
    }

    /**
     * 根据类型返回时间范围
     * @param type 时间类型
     * @return {开始时间,结束时间}
     */
    public static LocalDate[] getTimes(int type){
        LocalDate begTime = LocalDate.now();
        LocalDate endTime = LocalDate.now();
        switch (type){
            case Constants.GoodsGenerateType.YESTERDAY://昨日
                begTime = begTime.minusDays(1);
                endTime = endTime.minusDays(1);
                break;
            case Constants.GoodsGenerateType.LATEST7DAYS://近7天
                begTime = begTime.minusDays(7);
                endTime = endTime.minusDays(1);
                break;
            case Constants.GoodsGenerateType.LATEST30DAYS://近30天
                begTime = begTime.minusDays(30);
                endTime = endTime.minusDays(1);
                break;
            case Constants.GoodsGenerateType.MONTH://上一个月
                begTime = begTime.minusMonths(1);
                begTime = LocalDate.of(begTime.getYear(), begTime.getMonth(), 1);
                endTime = LocalDate.of(begTime.getYear(), endTime.getMonth(), 1).minusDays(1);//上个月最后一天
                break;
        }

        return new LocalDate[]{begTime, endTime};
    }

}
