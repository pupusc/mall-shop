package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运费模板
 * Created by sunkun on 2018/5/2.
 */
@ApiModel
@Data
public class FreightTemplateVO implements Serializable {

    private static final long serialVersionUID = 7236002394207629461L;

    /**
     * 运费模板id
     */
    @ApiModelProperty(value = "运费模板id")
    private Long freightTempId;

    /**
     * 运费模板名称
     */
    @ApiModelProperty(value = "运费模板名称")
    private String freightTempName;

    /**
     * 运送方式(1:快递配送) {@link DeliverWay}
     */
    @ApiModelProperty(value = "运送方式", notes = "0: 默认方式, 1: 快递配送")
    private DeliverWay deliverWay;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 默认标识
     */
    @ApiModelProperty(value = "默认标识", notes = "0: 否, 1: 是")
    private DefaultFlag defaultFlag;

    /**
     * 删除标识
     */
    @ApiModelProperty(value = "删除标识", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

}
