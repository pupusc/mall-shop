package com.wanmi.sbc.topic.response;

import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import com.wanmi.sbc.goods.bean.dto.TagsDto;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class GoodsOrBookResponse implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * SkuId
     */
    private String skuId;

    /**
     * SkuNo
     */
    private String skuNo;

    /**
     * SpuId
     */
    private String SpuId;


    /**
     * 排序数
     */
    private Integer sorting;
    /**
     * 图片地址
     */
    private String imageUrl;


    /**
     *  市场价
     */
    private BigDecimal marketPrice;


    /**
     * 最新计算的会员价
     * 为空，以市场价为准
     */
    private BigDecimal salePrice;

    /**
     * 商品名
     */
    private String goodsName;


    /**
     * 促销标签
     */
    private List<MarketingLabelVO> marketingLabels = new ArrayList<>();

    private TagsDto tagsDto;
}

