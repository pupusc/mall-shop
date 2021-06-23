package com.wanmi.ares.report.goods.dao;

import org.apache.ibatis.annotations.Param;

public interface GoodsMapper {

    int delSevenDayReport();

    /**
     * 清理商品品牌近30日报表
     * @return 影响行数
     */
    int delThirtyDayReport();

    /**
     * 清理日报表
     * @param date 时间
     */
    void delDayReport(@Param("date") String date);

    /**
     * 清理月报表
     * @param date 时间
     */
    void delMonthReport(@Param("date") Integer date);

    /**
     * 保存日报数据
     * @param date
     * @param beginTime
     * @param endTime
     */
    void saveDayReportBySelect(@Param("statDate") String date, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 保存月报数据
     * @param date
     * @param beginTime
     * @param endTime
     */
    void saveMonthReportBySelect(@Param("statDate") int date, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 保存近七天数据
     * @param beginTime
     * @param endTime
     */
    void saveSevenDayReportBySelect(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 保存近30天数据
     * @param beginTime
     * @param endTime
     */
    void saveThirtyDayReportBySelect(@Param("beginTime") String beginTime, @Param("endTime") String endTime);

}
