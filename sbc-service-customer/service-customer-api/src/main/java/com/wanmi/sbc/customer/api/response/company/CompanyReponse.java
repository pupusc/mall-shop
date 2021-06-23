package com.wanmi.sbc.customer.api.response.company;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 平台端商家列表
 * Created by sunkun on 2017/11/6.
 */
@ApiModel
@Data
public class CompanyReponse {

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 店铺Id
     */
    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;

    /**
     * 商家账号
     */
    @ApiModelProperty(value = "商家账号")
    private String accountName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private BoolFlag companyType;

    /**
     * 签约开始日期
     */
    @ApiModelProperty(value = "签约开始日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;

    /**
     * 签约结束日期
     */
    @ApiModelProperty(value = "签约结束日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

    /**
     * 申请入驻时间
     */
    @ApiModelProperty(value = "申请入驻时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState auditState;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    private String auditReason;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态")
    private AccountState accountState;

    /**
     * 账号禁用原因
     */
    @ApiModelProperty(value = "账号禁用原因")
    private String accountDisableReason;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 账号关闭原因
     */
    @ApiModelProperty(value = "账号关闭原因")
    private String storeClosedReason;

    /**
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型0品牌商城，1商家")
    private StoreType storeType;

}
