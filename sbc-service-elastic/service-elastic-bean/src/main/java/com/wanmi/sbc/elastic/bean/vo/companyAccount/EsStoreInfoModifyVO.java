package com.wanmi.sbc.elastic.bean.vo.companyAccount;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ES商品实体类
 * 以SKU维度
 * Created by dyt on 2017/4/21.
 */
@Data
@ApiModel
public class EsStoreInfoModifyVO implements Serializable {

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
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
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private Integer companyType;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private Integer auditState;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    private String auditReason;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态")
    private Integer accountState;

    /**
     * 账号禁用原因
     */
    @ApiModelProperty(value = "账号禁用原因")
    private String accountDisableReason;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态")
    private Integer storeState;

    /**
     * 账号关闭原因
     */
    @ApiModelProperty(value = "账号关闭原因")
    private String storeClosedReason;

    /**
     * 商家类型0品牌商城，1商家
     */
    @ApiModelProperty(value = "商家类型0品牌商城，1商家")
    private Integer storeType;

    /**
     * 是否是主账号
     */
    @ApiModelProperty(value = "店铺名称")
    private String isMasterAccount;

    /**
     * 店铺删除状态
     */
    @ApiModelProperty(value = "店铺删除状态")
    private DeleteFlag storeDelFlag;

    /**
     * 公司删除状态
     */
    @ApiModelProperty(value = "公司删除状态")
    private DeleteFlag companyInfoDelFlag;

    /**
     * 员工删除状态
     */
    @ApiModelProperty(value = "员工删除状态")
    private DeleteFlag employeeDelFlag;

    /**
     * 是否确认打款 (-1:全部,0:否,1:是)
     */
    @ApiModelProperty(value = "是否确认打款(-1:全部,0:否,1:是)")
    private Integer remitAffirm;


    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "入驻时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;


    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "合同有效期开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractStartDate;


    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "合同有效期截至时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime contractEndDate;

}
