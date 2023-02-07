package com.wanmi.sbc.topic.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewBookPointResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

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
     * spuNo
     */
    private String spuNo;

    /**
     * 排序数
     */
    private Integer sortNum;

    /**
     * 标签值
     */
    private String skuTag;


    /**
     *  数量
     */
    private Integer num;
}