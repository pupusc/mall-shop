package com.wanmi.ares.report.goods.model.criteria;

import lombok.Data;

import java.util.List;

/**
 * Es 商品查询条件
 *
 * @author liangck
 * @version 1.0
 * @since 16/6/28 17:29
 */
@Data
public class GoodsQueryCriteria {

    /**
     * 表名
     */
    private String table;

    /**
     * 商户编号
     */
    private String companyId;

    /**
     * 时间
     */
    private String date;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 排序列名
     */
    private String sortCol;

    /**
     * 排序类型
     */
    private String sortType;

    /**
     * 批量ids
     */
    private List<String> ids;

    /**
     * 是否叶子节点
     * 仅分类模块使用
     */
    private Integer isLeaf;

    /**
     * 分页起始值
     */
    private Long number;

    /**
     * 分页大小
     */
    private Long size;

    /**
     * 开始时间
     */
    private String begDate;

    /**
     * 结束时间
     */
    private String endDate;
}
