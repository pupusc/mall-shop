package com.wanmi.sbc.elastic.bean.vo.companyAccount;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author yangzhen
 * @Description //商家结算账号
 * @Date 17:38 2020/12/9
 * @Param
 * @return
 **/
@Data
@ApiModel
public class EsCompanyAccountVO implements Serializable {

    /**
     * 商家主键
     */
    @ApiModelProperty(value = "商家主键")
    private Long companyInfoId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 申请入驻时间
     */
    @ApiModelProperty(value = "申请入驻时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;

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
     * 是否通过打款确认
     */
    @ApiModelProperty(value = "是否通过打款确认")
    private BoolFlag remitAffirm;
}
