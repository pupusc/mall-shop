package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.SkuReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Sku报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface SkuReportMapper {

    /**
     * 公共查询功能
     * @return
     */
    List<SkuReport> querySkuReport(GoodsQueryCriteria criteria);

    /**
     * 公共统计功能
     * @return
     */
    Long countSkuReport(GoodsQueryCriteria criteria);

    /**
     * Sku日报表分组查询功能
     * @return
     */
    List<SkuReport> querySkuReportByGroup(GoodsQueryCriteria criteria);

    /**
     * 统计去重Sku
     * @return
     */
    Long countSkuReportByGroup(GoodsQueryCriteria criteria);


    /**
     * 批量保存Sku今日报表
     * @param skuReports Sku对象
     * @return 影响行数
     */
    void saveDayReport(List<SkuReport> skuReports);

    /**
     * 批量保存Sku近7日报表
     * @param skuReports Sku对象
     * @return 影响行数
     */
    void saveSevenDayReport(List<SkuReport> skuReports);

    /**
     * 批量保存Sku近30日报表
     * @param skuReports Sku对象
     * @return 影响行数
     */
    void saveThirtyDayReport(List<SkuReport> skuReports);

    /**
     * 批量保存Sku月报表
     * @param skuReports Sku对象
     * @return 影响行数
     */
    void saveMonthReport(List<SkuReport> skuReports);

    /**
     * 清理Sku近7日报表
     * @return 影响行数
     */
    void delSevenDayReport();

    /**
     * 清理Sku近30日报表
     * @return 影响行数
     */
    void delThirtyDayReport();

    /**
     * 清理日报表
     * @param date 时间
     */
    void delDayReport(@Param("date") String date);

    /**
     * 清理日报表
     * @param date 时间
     */
    void delDayReportByLtDate(@Param("date") String date);

    /**
     * 清理月报表
     * @param date 时间
     */
    void delMonthReport(@Param("date") String date);

    /**
     * 清理月报表
     * @param date 时间
     */
    void delMonthReportByDate(@Param("date") String date);



}
