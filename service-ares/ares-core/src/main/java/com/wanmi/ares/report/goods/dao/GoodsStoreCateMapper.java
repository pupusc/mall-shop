package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.GoodsCateReport;
import com.wanmi.ares.report.goods.model.root.GoodsReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface GoodsStoreCateMapper extends GoodsMapper{

    /**
     * 公共查询功能
     * @return
     */
    List<GoodsReport> queryGoodsCateReport(GoodsQueryCriteria criteria);

    /**
     * 公共统计功能
     * @return
     */
    Long countGoodsCateReport(GoodsQueryCriteria criteria);


    /**
     * 清理商品分类近7日报表
     * @return 影响行数
     */
    int delSevenDayReport();

    /**
     * 清理商品分类近30日报表
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


    List<GoodsReport> queryGoodsCateReportByCateId(GoodsQueryCriteria criteria);

    List<Long> queryStoreCateChild(@Param("cateId") Integer cateId);

    /**
     * 导出
     * @param criteria
     * @return
     */
    List<GoodsCateReport> queryGoodsStoreCateReportByExport(GoodsQueryCriteria criteria);
}
