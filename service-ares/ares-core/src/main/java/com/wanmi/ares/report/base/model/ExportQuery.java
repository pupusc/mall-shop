package com.wanmi.ares.report.base.model;

import com.wanmi.ares.export.model.entity.ExportDataEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>报表导出请求参数</p>
 * Created by of628-wenzhi on 2017-11-01-下午5:27.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExportQuery {

    /**
     * 开始日期,yyyy-MM-dd
     */
    private String dateFrom;

    /**
     * 结束日期,yyyy-MM-dd
     */
    private String dateTo;

    /**
     * 商户id
     */
    private String companyId;

    /**
     * 数据开始index
     */
    private int beginIndex;

    /**
     * 抓取条数
     */
    private int size = 5000;

    /**
     * 从传入对象 转换成 生成报表excel的查询实体
     *
     * @param entity
     */
    public ExportQuery convertFromRequest(ExportDataEntity entity) {
        this.dateFrom = entity.getBeginDate();
        this.dateTo = entity.getEndDate();
        this.companyId = Long.toString(entity.getCompanyInfoId());
        return this;
    }

    public String getCompanyId() {
        return companyId == null ? "0" : companyId;
    }
}
