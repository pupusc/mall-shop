package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsInfoQueryCriteria;
import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.SkuReport;
import com.wanmi.ares.request.GoodsInfoQueryRequest;
import com.wanmi.ares.source.model.root.GoodsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Sku报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface SkuMapper extends GoodsMapper {

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
     * 清理Sku近7日报表
     * @return 影响行数
     */
    int delSevenDayReport();



    /**
     * 清理Sku近30日报表
     * @return 影响行数
     */
    int delThirtyDayReport();

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


    List<SkuReport> querySkuCustomer(GoodsQueryCriteria criteria);


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
     * 查询GoodsInfo
     * @param request
     * @return
     */
    List<GoodsInfo> queryGoodsInfo(GoodsInfoQueryRequest request);

    List<GoodsInfo> queryGoodsInfoInGoodsReport(GoodsInfoQueryCriteria criteria);

    Long queryGoodsInfoInGoodsReportCount(GoodsInfoQueryCriteria criteria);
}
