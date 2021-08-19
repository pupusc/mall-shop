package com.wanmi.sbc.goods.api.request.customergoodsevaluatepraise;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员商品评价点赞关联表通用查询请求参数</p>
 *
 * @author lvzhenwei
 * @date 2019-05-07 14:25:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerGoodsEvaluatePraiseQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 批量查询-主键List
     */
    @ApiModelProperty(value = "批量查询-主键List")
    private List<String> idList;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 商品评价id
     */
    @ApiModelProperty(value = "商品评价id")
    private String goodsEvaluateId;

    /**
     * 搜索条件:创建时间开始
     */
    @ApiModelProperty(value = "搜索条件:创建时间开始")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeBegin;
    /**
     * 搜索条件:创建时间截止
     */
    @ApiModelProperty(value = "搜索条件:创建时间截止")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTimeEnd;

}