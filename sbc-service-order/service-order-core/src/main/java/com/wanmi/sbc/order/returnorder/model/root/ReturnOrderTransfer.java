package com.wanmi.sbc.order.returnorder.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.returnorder.model.entity.ReturnItem;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPoints;
import com.wanmi.sbc.order.returnorder.model.value.ReturnPrice;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退货单
 * Created by jinwei on 19/4/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "returnOrderTransfer")
public class ReturnOrderTransfer implements Serializable {

    /**
     * 退单号
     */
    @Id
    private String id;

    /**
     * 订单编号
     */
    private String tid;

    /**
     * 客户信息 买家信息
     */
    private Buyer buyer;

    /**
     * 支付方式
     */
    private ReturnWay returnWay;

    /**
     * 退单附件
     */
    private List<String> images;

    /**
     * 退货商品信息
     */
    private List<ReturnItem> returnItems;

    /**
     * 退单赠品信息
     */
    private List<ReturnItem> returnGifts;

    /**
     * 退货总金额
     */
    private ReturnPrice returnPrice;

    /**
     * 退积分信息
     */
    private ReturnPoints returnPoints;

    /**
     * 收货人信息
     */
    private Consignee consignee;

    /**
     * 退货单状态
     */
    private ReturnFlowState returnFlowState;

    /**
     * 支付方式枚举
     */
    private PayType payType;

    /**
     * 退单类型
     */
    private ReturnType returnType;

    /**
     * 退单来源
     */
    private Platform platform;


    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


}
