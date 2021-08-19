package com.wanmi.sbc.goods.bean.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分销员商品表
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:02
 * @version: 1.0
 */
@Data
public class DistributorGoodsInfoModifySequenceDTO implements Serializable {


    private String id;

    /**
     * 分销员对应的会员ID
     */
    private String customerId;

    /**
     * 分销商品SKU编号
     */
    private String goodsInfoId;


    /**
     * 分销商品SPU编号
     */
    private String goodsId;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 是否删除,0：否，1：是
     */
    private Integer status;


    /**
     * 分销商品顺序
     */
    private Integer sequence;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
}
