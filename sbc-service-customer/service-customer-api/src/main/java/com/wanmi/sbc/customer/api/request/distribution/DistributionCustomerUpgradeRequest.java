package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:05 2019/3/12
 * @Description:
 */
@ApiModel
@Data
public class DistributionCustomerUpgradeRequest {

    /**
     * 待升级的用户id
     */
    @ApiModelProperty("待升级的用户id")
    @NotNull
    private String customerId;

    /**
     * 分销员等级设置信息
     */
    @ApiModelProperty("分销员等级设置信息")
    private List<DistributorLevelVO> distributorLevelVOList;

    /**
     * 基础邀新奖励限制
     */
    @ApiModelProperty("基础邀新奖励限制")
    private DefaultFlag baseLimitType;

    /**
     * 邀新人数
     */
    @ApiModelProperty("邀新人数")
    private Integer inviteNum = NumberUtils.INTEGER_ZERO;

    /**
     * 销售额(元)
     */
    @ApiModelProperty(value = "销售额(元) ")
    private BigDecimal sales = BigDecimal.ZERO;

    /**
     * 分销佣金(元)
     */
    @ApiModelProperty(value = "分销佣金(元) ")
    private BigDecimal commission = BigDecimal.ZERO;

}
