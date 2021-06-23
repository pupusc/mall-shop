package com.wanmi.sbc.account.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商家收款账户entity
 * Created by sunkun on 2017/11/30.
 */
@ApiModel
@Data
public class CompanyAccountVO implements Serializable {

    private static final long serialVersionUID = -4030991142326108742L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "商家收款账户id")
    private Long accountId;


    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息id")
    private Long companyInfoId;

    /**
     * 是否收到平台首次打款 0、否 1、是
     */
    @ApiModelProperty(value = "是否收到平台首次打款")
    private DefaultFlag isReceived = DefaultFlag.NO;

    /**
     * 是否主账号 0、否 1、是
     */
    @ApiModelProperty(value = "是否主账号")
    private DefaultFlag isDefaultAccount = DefaultFlag.NO;

    /**
     * 账户名称
     */
    @ApiModelProperty(value = "账户名称")
    private String accountName;

    /**
     * 开户银行
     */
    @ApiModelProperty(value = "开户银行")
    private String bankName;

    /**
     * 支行
     */
    @ApiModelProperty(value = "支行")
    private String bankBranch;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    private String bankNo;

    @ApiModelProperty(value = "银行账号编码")
    private String bankCode;

    /**
     * 账号状态 0: 启用 1:禁用'
     */
    @ApiModelProperty(value = "账号状态", dataType = "com.wanmi.sbc.account.bean.enums.AccountStatus")
    private Integer bankStatus;

    /**
     * 第三方店铺id
     */
    @ApiModelProperty(value = "第三方店铺id")
    private String thirdId;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime update_time;

    /**
     * 删除时间
     */
    @ApiModelProperty(value = "删除时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime deleteTime;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private Integer deleteFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 打款金额
     */
    @ApiModelProperty(value = "打款金额")
    private BigDecimal remitPrice;
}
