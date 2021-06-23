package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 待审核品牌
 * Created by sunkun on 2017/11/1.
 */
@ApiModel
@Data
public class CheckBrandVO implements Serializable {

    private static final long serialVersionUID = 8612803288881315599L;

    /**
     * 待审核品牌分类
     */
    @ApiModelProperty(value = "待审核品牌分类")
    private Long checkBrandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String name;

    /**
     * 品牌昵称
     */
    @ApiModelProperty(value = "品牌名称")
    private String nickName;

    /**
     * 品牌logo
     */
    @ApiModelProperty(value = "品牌logo")
    private String logo;

    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 审核状态(0:未审核,1:通过,2:驳回)
     */
    @ApiModelProperty(value = "审核状态", dataType = "com.wanmi.sbc.goods.bean.enums.GoodsBrandCheckStatus")
    private Integer status = 0;

}
