package com.wanmi.sbc.marketing.api.request.grouponcenter;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>H5-拼团活动首页列表查询请求参数</p>
 *
 * @author chenli
 * @date 2019-05-21 14:02:38
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponCenterListRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 7705983488453217344L;
    /**
     * 拼团分类ID
     */
    @ApiModelProperty(value = "拼团分类ID")
    private String grouponCateId;

    /**
     * spu商品名称
     */
    @ApiModelProperty(value = "spu商品名称")
    private String goodsName;

    /**
     * 是否精选
     */
    @ApiModelProperty(value = "是否精选")
    private Boolean sticky = Boolean.FALSE;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;

}