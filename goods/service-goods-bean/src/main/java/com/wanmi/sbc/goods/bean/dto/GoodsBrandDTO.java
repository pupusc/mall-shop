package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商品品牌DTO
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsBrandDTO implements Serializable {

    private static final long serialVersionUID = -7625427662011211087L;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    private Long brandId;

    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    /**
     * 拼音
     */
    @ApiModelProperty(value = "拼音")
    private String pinYin;

    /**
     * 简拼
     */
    @ApiModelProperty(value = "简拼")
    private String sPinYin;

    /**
     * 店铺id(平台默认为0)
     */
    @ApiModelProperty(value = "店铺id(平台默认为0)")
    private Long storeId = 0L;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 删除标志
     */
    @ApiModelProperty(value = "删除标志", notes = "0: 否, 1: 是")
    private DeleteFlag delFlag;

    /**
     * 品牌别名
     */
    @ApiModelProperty(value = "品牌别名")
    private String nickName;

    /**
     * 品牌logo
     */
    @ApiModelProperty(value = "品牌logo")
    private String logo;
}
