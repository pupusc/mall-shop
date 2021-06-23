package com.wanmi.sbc.elastic.bean.vo.storeInformation;

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

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: yangzhen
 * @Date: Created In 10:02 2020/12/17
 * @Description: 模糊匹配返回店铺信息
 */
@Data
@ApiModel
public class EsStoreInfoVO implements Serializable {

    private static final long serialVersionUID = -5015819682991590128L;

    /**
     * 商家主键
     */
    @ApiModelProperty(value = "商家主键")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

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
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

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
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型(0、供应商 1、商家)")
    private StoreType storeType;

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
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 账号关闭原因
     */
    @ApiModelProperty(value = "账号关闭原因")
    private String storeClosedReason;

}
