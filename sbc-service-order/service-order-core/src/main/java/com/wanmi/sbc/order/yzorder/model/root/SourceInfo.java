package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 交易来源信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceInfo implements Serializable {

    private static final long serialVersionUID = -8098632060292716805L;

    /**
     * 订单唯一识别码
     */
    private String book_key;

    /**
     * 订单标记
     */
    private String order_mark;

    /**
     * 平台
     */
    private Source source;

    /**
     * 是否来自线下订单
     */
    private Boolean is_offline_order;

    /**
     * 活动类型
     */
    private String biz_source;
}
