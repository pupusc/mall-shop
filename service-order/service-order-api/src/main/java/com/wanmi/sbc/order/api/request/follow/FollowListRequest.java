package com.wanmi.sbc.order.api.request.follow;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class FollowListRequest extends BaseQueryRequest {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private List<Long> followIds;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    /**
     * 批量SKU编号
     */
    @ApiModelProperty(value = "批量SKU编号")
    private List<String> goodsInfoIds;

    /**
     * 收藏标识
     */
    @ApiModelProperty(value = "收藏标识",dataType = "com.wanmi.sbc.order.bean.enums.FollowFlag")
    private Integer followFlag;

    /**
     * 会员编号
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * 客户等级
     */
    @ApiModelProperty(value = "客户等级id")
    private Long customerLevelId;

    /**
     * 客户等级折扣
     */
    @ApiModelProperty(value = "客户等级折扣")
    private BigDecimal customerLevelDiscount;

    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;


}
