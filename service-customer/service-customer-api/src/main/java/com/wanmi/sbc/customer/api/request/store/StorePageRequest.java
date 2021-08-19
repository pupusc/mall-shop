package com.wanmi.sbc.customer.api.request.store;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorePageRequest extends BaseQueryRequest {

    private static final long serialVersionUID = 7503466614532874310L;

    /**
     * 查询店铺ID
     */
    @ApiModelProperty(value = "查询店铺ID")
    private Long storeId;

    /**
     * 批量查询店铺ID
     */
    @ApiModelProperty(value = "批量查询店铺ID")
    private List<Long> storeIds;

    /**
     * 大于或等于签约开始时间
     */
    @ApiModelProperty(value = "大于或等于签约开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime gteContractStartDate;

    /**
     * 小于或等于签约结束时间
     */
    @ApiModelProperty(value = "小于或等于签约结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime lteContractEndDate;

    /**
     * 批量省
     */
    @ApiModelProperty(value = "批量省")
    private List<Long> provinceIds;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private List<Long> cityIds;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private List<Long> areaIds;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private BoolFlag companyType;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态")
    private CheckState auditState;

    /**
     * 店铺状态 0、开启 1、关店
     */
    @ApiModelProperty(value = "店铺状态")
    private StoreState storeState;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标记", dataType = "com.wanmi.sbc.common.enums.DeleteFlag")
    private DeleteFlag delFlag;

    /**
     * 关键字范围（店铺名称、商家名称）
     */
    @ApiModelProperty(value = "关键字范围")
    private String keywords;

    /**
     * 批量全部区域IDs
     */
    @ApiModelProperty(value = "批量全部区域IDs")
    private List<Long> allAreaIds;

    /**
     * 商铺名称
     */
    @ApiModelProperty(value = "商铺名称")
    private String storeName;

    /**
     * 商家类型
     */
    @ApiModelProperty(value = "商家类型")
    private StoreType storeType;


}
