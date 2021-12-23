package com.fangdeng.server.dto;

import com.fangdeng.server.enums.DeliveryStatus;
import com.fangdeng.server.vo.DeliveryItemVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryInfoDTO implements Serializable {
    private static final long serialVersionUID = -4962136385329266486L;
    /**
     * 发货单编号
     */
    @ApiModelProperty(value = "发货单编号")
    private String code;

    /**
     * 订单商品发货状态(
     * 0:未发货
     * 1:已发货
     * 2:部分发货)
     */
    @ApiModelProperty(value = "订单商品发货状态")
    private Integer deliveryStatus;

    /**
     * 发货时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deliverTime;


    /**
     * 发货单商品集合
     */
    @ApiModelProperty(value = "发货单商品集合")
    private List<GoodsItemDTO> goodsList;


    /**
     * 快递公司代码
     */
    @ApiModelProperty(value = "快递公司代码")
    private String expressCode;

    /**
     * 快递公司名称
     */
    @ApiModelProperty(value = "快递公司名称")
    private String expressName;

    /**
     * 快递单号
     */
    @ApiModelProperty(value = "快递单号")
    private String expressNo;

    /**
     * 子订单号
     */
    @ApiModelProperty(value = "子订单号")
    private String oid;

    /**
     * 平台单号
     */
    @ApiModelProperty(value = "平台单号")
    private String platformCode;
}
