package com.wanmi.sbc.goods.api.response.flashsalegoods;

import com.wanmi.sbc.common.util.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019-07-23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IsFlashSaleResponse implements Serializable {

    private static final long serialVersionUID = 3778688262118884915L;

    /**
     * 活动时间：13:00
     */
    @ApiModelProperty(value = "活动时间：13:00")
    private String activityTime;

    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsId;

    /**
     * 是否有未结束活动关联商品
     */
    @ApiModelProperty(value = "是否有未结束活动关联商品")
    private Boolean isFlashSale;

    /**
     * 从数据库实体转换为返回前端的用户信息
     * (字段顺序不可变)
     */
    public IsFlashSaleResponse convertFromNativeSQLResult(Object result) {
        Object[] results = StringUtil.cast(result, Object[].class);
        if (results == null || results.length < 1) {
            return this;
        }

        this.setActivityTime(StringUtil.cast(results, 0, String.class));
        this.setGoodsId(StringUtil.cast(results, 1, String.class));
        return this;
    }
}
