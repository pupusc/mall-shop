package com.wanmi.sbc.order.bean.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.account.bean.dto.OfflineAccountDTO;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收款单
 * Created by zhangjin on 2017/3/20.
 */
@Data
@ApiModel
public class ReceivableDTO implements Serializable {

    @ApiModelProperty(value = "收款单标识")
    private String receivableId;

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号")
    private String receivableNo;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 线上账户
     */
    @ApiModelProperty(value = "线上账户")
    private Long onlineAccountId;

    /**
     * 线下账户
     */
    @ApiModelProperty(value = "线下账户")
    private Long offlineAccountId;

    /**
     * 收款账号
     */
    @ApiModelProperty(value = "收款账号")
    private String receivableAccount;

    /**
     * 评论
     */
    @ApiModelProperty(value = "评论")
    private String comment;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标识")
    private DeleteFlag delFlag;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delTime;

    @ApiModelProperty(value = "账务支付单")
    @JsonBackReference
    private PayOrderDTO payOrder;

    /**
     * 支付单外键
     */
    @ApiModelProperty(value = "支付单外键")
    private String payOrderId;

    /**
     * 收款在线渠道
     */
    @ApiModelProperty(value = "收款在线渠道")
    private String payChannel;

    @ApiModelProperty(value = "支付渠道id")
    private Long payChannelId;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String encloses;

    /**
     * 离线账户
     */
    @ApiModelProperty(value = "离线账户")
    private OfflineAccountDTO offlineAccount;
}

