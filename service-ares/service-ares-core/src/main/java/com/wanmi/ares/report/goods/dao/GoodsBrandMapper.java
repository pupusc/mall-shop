package com.wanmi.ares.report.goods.dao;


import com.wanmi.ares.report.goods.model.criteria.GoodsQueryCriteria;
import com.wanmi.ares.report.goods.model.root.GoodsBrandReport;
import com.wanmi.ares.report.goods.model.root.GoodsReport;
import com.wanmi.ares.request.GoodsBrandQueryRequest;
import com.wanmi.ares.source.model.root.GoodsBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品品牌报表Mapper类
 * Created by dyt on 2017/11/12.
 */
@Mapper
public interface GoodsBrandMapper extends GoodsMapper {

    /**
     * 公共查询功能
     * @return
     */
    List<GoodsReport> queryGoodsBrandReport(GoodsQueryCriteria criteria);

    /**
     * 公共统计功能
     * @return
     */
    Long countGoodsBrandReport(GoodsQueryCriteria criteria);


    /**
     * 清理商品品牌近7日报表
     * @return 影响行数
     */
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


    List<GoodsBrand> queryByIds(GoodsBrandQueryRequest request);

    List<GoodsBrandReport> queryGoodsBrandReportByExport(GoodsQueryCriteria criteria);
}
