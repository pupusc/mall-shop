package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.GoodsReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface GoodsCateReportMapper {

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
     * 批量保存商品分类今日报表
     * @param goodsCateReports 商品分类对象
     * @return 影响行数
     */
    int saveDayReport(List<GoodsReport> goodsCateReports);

    /**
     * 批量保存商品分类昨日报表
     * @param goodsCateReports 商品分类对象
     * @return 影响行数
     */
    int saveYesterdayDayReport(List<GoodsReport> goodsCateReports);

    /**
     * 批量保存商品分类近7日报表
     * @param goodsCateReports 商品分类对象
     * @return 影响行数
     */
    int saveSevenDayReport(List<GoodsReport> goodsCateReports);

    /**
     * 批量保存商品分类近30日报表
     * @param goodsCateReports 商品分类对象
     * @return 影响行数
     */
    int saveThirtyDayReport(List<GoodsReport> goodsCateReports);

    /**
     * 批量保存商品分类月报表
     * @param goodsCateReports 商品分类对象
     * @return 影响行数
     */
    int saveMonthReport(List<GoodsReport> goodsCateReports);

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
}
