package com.wanmi.sbc.goods.bean.vo;

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
 * 商品品牌实体类
 * Created by dyt on 2017/4/11.
 */
@ApiModel
@Data
public class GoodsListBrandVO implements Serializable {

    private static final long serialVersionUID = -3369219732567679167L;

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

}
