package com.wanmi.sbc.elastic.api.request.goods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ES商品SKU请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsGoodsInfoRequest {

    /**
     * 批量商品SkuID
     */
    @ApiModelProperty(value = "批量商品SkuID")
    private List<String> skuIds;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺ID
     */
    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    /**
     * spuId
     */
    @ApiModelProperty(value = "spuId")
    private String goodsId;

    /**
     * spuIds
     */
    @ApiModelProperty(value = "spuIds")
    private List<String> goodsIds;

    /**
     * 品牌ids
     */
    @ApiModelProperty(value = "品牌ids")
    private List<Long> brandIds;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态", notes = "0：待审核 1：已审核 2：审核失败 3：禁售中")
    private CheckStatus auditStatus;


    /**
     * 创建开始时间
     */
    @ApiModelProperty(value = "创建开始时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;

    /**
     * 创建结束时间
     */
    @ApiModelProperty(value = "创建结束时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

    /**
     * 初始化开始页码
     */
    @ApiModelProperty(value = "初始化开始页码")
    private Integer pageIndex;

    /**
     * 初始化开始页码
     */
    @ApiModelProperty(value = "初始化每批数量")
    private Integer pageSize;

    /**
     * 如果有范围进行初始化索引，无需删索引
     *
     * @return true:clear index false:no
     */
    public boolean isClearEsIndex() {
        if (CollectionUtils.isNotEmpty(skuIds)
                || companyInfoId != null || storeId != null
                || StringUtils.isNotBlank(goodsId) || CollectionUtils.isNotEmpty(goodsIds) || CollectionUtils.isNotEmpty(brandIds)
                || createTimeBegin != null || createTimeEnd != null
                || pageIndex != null) {
            return false;
        }
        return true;
    }
}
