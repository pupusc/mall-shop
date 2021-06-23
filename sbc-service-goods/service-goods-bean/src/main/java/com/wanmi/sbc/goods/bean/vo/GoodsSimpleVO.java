package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.annotation.CanEmpty;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @discription 商品简易信息，存储redis中
 * @author yangzhen
 * @date 2020/9/8 18:54
 * @param
 * @return
 */
@ApiModel
@Data
public class GoodsSimpleVO implements Serializable {

    private static final long serialVersionUID = 2757888812286445293L;


    /**
     * 商品编号，采用UUID
     */
    @ApiModelProperty(value = "商品编号，采用UUID")
    private String goodsId;

    /**
     * 分类编号
     */
    @ApiModelProperty(value = "分类编号")
    private Long cateId;

    /**
     * 品牌编号
     */
    @ApiModelProperty(value = "品牌编号")
    @CanEmpty
    private Long brandId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品副标题
     */
    @ApiModelProperty(value = "商品副标题")
    private String goodsSubtitle;

    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    private String goodsNo;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    @CanEmpty
    private String goodsImg;

}
