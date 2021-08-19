package com.wanmi.sbc.goods.api.response.brand;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品品牌查询结果
 * Created by sunkun on 2017/11/13.
 */
@ApiModel
@Data
public class GoodsBrandResponse {

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
    @ApiModelProperty(value = "店铺id", notes = "平台默认为0")
    private Long storeId = 0L;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
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

    /**
     * 商家名称(多个以','分割)
     */
    @ApiModelProperty(value = "商家名称", notes = "多个以','分割")
    private String supplierNames;

}
