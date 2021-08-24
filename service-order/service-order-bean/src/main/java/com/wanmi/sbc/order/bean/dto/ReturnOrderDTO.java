package com.wanmi.sbc.order.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.enums.PayType;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.account.bean.enums.RefundStatus;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.bean.enums.ReturnReason;
import com.wanmi.sbc.order.bean.enums.ReturnType;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import com.wanmi.sbc.order.bean.vo.TradeDistributeItemVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 退货单
 * Created by jinwei on 19/4/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderDTO implements Serializable {

    private static final long serialVersionUID = -8964710078593724385L;

    /**
     * 退单号
     */
    @ApiModelProperty(value = "退单号")
    private String id;

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号", required = true)
    @NotBlank
    private String tid;
    /**
     * 樊登用户id
     */
    @ApiModelProperty(value = "樊登用户id")
    private String fanDengUserNo;

    @ApiModelProperty(value = "子订单编号")
    private String ptid;

    @ApiModelProperty(value = "ThirdPlatformTrade的Id")
    private String thirdPlatformTradeId;

    @ApiModelProperty(value = "供应商ID")
    private String providerId;

    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    @ApiModelProperty(value = "供应商编码")
    private String providerCode;

    @ApiModelProperty(value = "providerCompanyInfoId")
    private Long providerCompanyInfoId;

    /**
     * linkedmall退货原因id
     */
    @ApiModelProperty(value = "linkedmall退货原因id")
    private Long thirdReasonId;

    /**
     * linkedmall退货原因内容
     */
    @ApiModelProperty(value = "linkedmall退货原因内容")
    private String thirdReasonTips;

    /**
     * linkedmall侧 商家同意退单的留言，一般包含实际退货地址
     */
    @ApiModelProperty(value = "linkedmall侧 商家同意退单的留言，一般包含实际退货地址")
    private String thirdSellerAgreeMsg;

    /**
     * 尾款退单号
     */
    @ApiModelProperty(value = "尾款退单号")
    private String businessTailId;

    /**
     * 客户信息 买家信息
     */
    @ApiModelProperty(value = "客户信息 买家信息")
    private BuyerDTO buyer;

    /**
     * 客户账户信息
     */
    @ApiModelProperty(value = "客户账户信息")
    private ReturnCustomerAccountDTO customerAccount;

    /**
     * 商家信息
     */
    @ApiModelProperty(value = "商家信息")
    private CompanyDTO company;

    /**
     * 退货原因
     */
    @ApiModelProperty(value = "退货原因")
    private ReturnReason returnReason;

    /**
     * 退货说明
     */
    @ApiModelProperty(value = "退货说明")
    private String description;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private ReturnWay returnWay;

    /**
     * 退单附件
     */
    @ApiModelProperty(value = "退单附件")
    private List<String> images;

    /**
     * 退货商品信息
     */
    @ApiModelProperty(value = "退货商品信息")
    private List<ReturnItemDTO> returnItems;

    /**
     * 退单赠品信息
     */
    @ApiModelProperty(value = "退单赠品信息")
    private List<ReturnItemDTO> returnGifts = new ArrayList<>();

    /**
     * 退货总金额
     */
    @ApiModelProperty(value = "退货总金额")
    private ReturnPriceDTO returnPrice;

    /**
     * 退积分信息
     */
    @ApiModelProperty(value = "退积分信息")
    private ReturnPointsDTO returnPoints;

    /**
     * 退知豆信息
     */
    @ApiModelProperty(value = "退知豆信息")
    private ReturnKnowledgeDTO returnKnowledgeDTO;

    /**
     * 收货人信息
     */
    @ApiModelProperty(value = "收货人信息")
    private ConsigneeDTO consignee;

    /**
     * 退货物流信息
     */
    @ApiModelProperty(value = "退货物流信息")
    private ReturnLogisticsDTO returnLogistics;

    /**
     * 退货单状态
     */
    @ApiModelProperty(value = "退货单状态")
    private ReturnFlowState returnFlowState;

    /**
     * 退货日志记录
     */
    @ApiModelProperty(value = "退货日志记录")
    private List<ReturnEventLogDTO> returnEventLogs = new ArrayList<>();

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String rejectReason;

    /**
     * 支付方式枚举
     */
    @ApiModelProperty(value = "支付方式枚举")
    private PayType payType;

    /**
     * 支付渠道枚举
     */
    @ApiModelProperty(value = "支付渠道枚举")
    private PayWay payWay;

    /**
     * 退单类型
     */
    @ApiModelProperty(value = "退单类型")
    private ReturnType returnType;

    /**
     * 退单来源
     */
    @ApiModelProperty(value = "退单来源")
    private Platform platform;

    /**
     * 退款单状态
     */
    @ApiModelProperty(value = "退款单状态")
    private RefundStatus refundStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 退单完成时间
     */
    @ApiModelProperty(value = "退单完成时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finishTime;

    /**
     * 是否被结算
     */
    @ApiModelProperty(value = "是否被结算", dataType = "com.wanmi.sbc.common.enums.BoolFlag")
    private Boolean hasBeanSettled;

    /**
     * 分销渠道类型
     */
    @ApiModelProperty(value = "分销渠道类型")
    private ChannelType channelType;

    /**
     * 退款失败原因
     */
    private String refundFailedReason;


    /**
     * 基础分销设置-小店名称
     */
    @ApiModelProperty(value = "小店名称")
    private String shopName;


    /**
     * 邀请人分销员id
     */
    @ApiModelProperty(value = "邀请人分销员id")
    private String distributorId;


    /**
     * 邀请人会员id
     */
    @ApiModelProperty(value = "邀请人会员id")
    private String inviteeId;

    /**
     * 分销员名称
     */
    @ApiModelProperty(value = "分销员名称")
    private String distributorName;

    /**
     * 订单所属第三方平台类型
     */
    private ThirdPlatformType thirdPlatformType;

    /**
     * 订单所属第三方平台的订单id
     */
    private String thirdPlatformOrderId;

    /**
     * 第三方内部卖家名称
     */
    private String thirdSellerName;

    /**
     * 第三方内部卖家编号
     */
    @ApiModelProperty(value = "第三方内部卖家编号")
    private String thirdSellerId;

    /**
     * 第三方平台支付失败状态  true:失败 false:成功
     */
    @ApiModelProperty(value = "第三方平台支付失败状态  true:失败 false:成功")
    private Boolean thirdPlatformPayErrorFlag;

    /**
     * 外部商家订单号
     * linkedMall -> 淘宝订单号
     */
    @ApiModelProperty(value = "外部商家订单号")
    private String outOrderId;
    /**
     * 终端来源
     */
    @ApiModelProperty(value = "终端来源")
    private TerminalSource terminalSource;

    /**
     * 分销单品列表
     */
    @ApiModelProperty(value = "分销单品列表")
    private List<TradeDistributeItemVO> distributeItems = new ArrayList<>();


    /**
     * 退货赠品
     */
    @ApiModelProperty(value = "退货赠品")
    private Boolean returnGift;

    /**
     * 退货地址
     */
    @ApiModelProperty(value = "退货地址")
    private ReturnAddressDTO returnAddress;
}
