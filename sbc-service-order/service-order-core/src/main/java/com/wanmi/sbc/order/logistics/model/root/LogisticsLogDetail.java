package com.wanmi.sbc.order.logistics.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 物流记录明细
 * Created by dyt on 2020/4/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsLogDetail implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * 内容上海分拨中心/装件入车扫
     */
    private String context;

    /**
     * 时间，原始格式
     */
    private String time;

    /**
     * 本数据元对应的签收状态。
     */
    private String status;

    /**
     * 本数据元对应的行政区域的编码
     */
    private String areaCode;

    /**
     * 本数据元对应的行政区域的名称
     */
    private String areaName;
}
