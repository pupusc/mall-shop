package com.wanmi.ares.report.flow.model.reponse;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName FlowDataResponse
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/21 18:45
 **/
@Data
@Builder
public class FlowDataResponse {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 流量统计汇总时间
     */
    private Date date;

    /**
     * 访问人数
     */
    private Long uv;

    /**
     * 访问量
     */
    private Long pv;

    /**
     * 商品访问人数
     */
    private Long goodsUv;

    /**
     * 商品访问量
     */
    private Long goodsPv;

    /**
     * 店铺id
     */
    private String companyId;

    /**
     * 统计月份
     */
    private String month;

    /**
     * 统计类型，0：最近七天；1：最近30天；2：按月统计
     */
    private Byte type;

    /**
     * 创建时间
     */
    private Date creatTime;

}
