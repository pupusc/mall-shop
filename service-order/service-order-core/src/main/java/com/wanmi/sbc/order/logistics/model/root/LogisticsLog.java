package com.wanmi.sbc.order.logistics.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流记录
 * Created by dyt on 2020/4/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsLog implements Serializable {

    private static final long serialVersionUID = -6414564526969459575L;

    /**
     * uuid
     */
    @Id
    private String id;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 快递单号
     */
    private String logisticNo;

    /**
     * 购买人编号
     */
    private String customerId;

    /**
     * 是否结束
     */
    private Boolean endFlag;

    /**
     * 监控状态:polling:监控中，shutdown:结束，abort:中止，updateall：重新推送。
     * status=shutdown快递单为已签收时
     * status= abort message为“3天查询无记录”或“60天无变化时”
     * 对于status=abort需要增加额外的处理逻辑
     */
    private String status;

    /**
     * 快递单当前状态，包括0在途，1揽收，2疑难，3签收，4退签，5派件，6退回等7个状态
     */
    private String state;

    /**
     * 监控状态相关消息，如:3天查询无记录，60天无变化
     */
    private String message;

    /**
     * 快递公司编码是否出过错
     */
    private String autoCheck;

    /**
     * 本地物流公司标准编码
     */
    private String comOld;

    /**
     * 快递纠正新编码
     */
    private String comNew;

    /**
     * 是否签收标记
     */
    private String isCheck;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 出发地城市
     */
    private String from;

    /**
     * 目的地城市
     */
    private String to;

    /**
     * 商品图片
     */
    private String goodsImg;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 回调更新时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 订阅申请状态
     */
    private Boolean successFlag;

    /**
     * 签收时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime checkTime;

    /**
     * 本地发货单号
     */
    private String deliverId;

    /**
     * 物流记录
     */
    private List<LogisticsLogDetail> logisticsLogDetails;
}
