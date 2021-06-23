package com.wanmi.sbc.order.api.request.follow;

import com.wanmi.sbc.order.bean.enums.FollowFlag;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowAdd;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowDelete;
import com.wanmi.sbc.order.api.request.follow.validGroups.FollowFilter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class InvalidGoodsDeleteRequest implements Serializable {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private List<Long> followIds;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotBlank(groups = { FollowAdd.class})
    private String goodsInfoId;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    @NotEmpty(groups = {FollowDelete.class, FollowFilter.class})
    private List<String> goodsInfoIds;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Long goodsNum;

    /**
     * 收藏标识
     */
    @ApiModelProperty(value = "收藏标识")
    @Enumerated
    private FollowFlag followFlag;
}
