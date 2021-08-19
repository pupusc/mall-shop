package com.wanmi.sbc.elastic.api.request.standard;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
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
 * ES商品初始化请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsStandardInitRequest {

    /**
     * spuId
     */
    @ApiModelProperty(value = "商品库id")
    private String goodsId;

    /**
     * spuIds
     */
    @ApiModelProperty(value = "商品库id")
    private List<String> goodsIds;

    /**
     * 品牌ids
     */
    @ApiModelProperty(value = "品牌ids")
    private List<Long> brandIds;

    /**
     * 批量商品分类
     */
    @ApiModelProperty(value = "批量商品分类")
    private List<Long> cateIds;

    /**
     * 商品来源，0供应商，1商家,2 linkedmall
     */
    @ApiModelProperty(value = "商品来源，0供应商，1商家，2 linkedmall")
    private Integer goodsSource;

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
     * 关联的商品id
     */
    @ApiModelProperty(value = "关联的商品id")
    private List<String> relGoodsIds;

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
        if (StringUtils.isNotBlank(goodsId) || CollectionUtils.isNotEmpty(goodsIds) || CollectionUtils.isNotEmpty(brandIds)
                || CollectionUtils.isNotEmpty(cateIds)
                || CollectionUtils.isNotEmpty(relGoodsIds)
                || goodsSource != null
                || createTimeBegin != null || createTimeEnd != null
                || pageIndex != null) {
            return false;
        }
        return true;
    }
}
