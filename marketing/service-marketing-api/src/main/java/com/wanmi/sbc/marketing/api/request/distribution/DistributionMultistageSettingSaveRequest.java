package com.wanmi.sbc.marketing.api.request.distribution;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.bean.dto.DistributorLevelDTO;
import com.wanmi.sbc.marketing.bean.enums.CommissionUnhookType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:41 2019/6/13
 * @Description:
 */
@ApiModel
@Data
public class DistributionMultistageSettingSaveRequest extends BaseRequest {

    private static final long serialVersionUID = -1345542882010090718L;

    /**
     * 佣金提成脱钩
     */
    @ApiModelProperty("佣金提成脱钩")
    @NotNull
    private CommissionUnhookType commissionUnhookType;

    /**
     * 分销员等级规则
     */
    @ApiModelProperty("分销员等级规则")
    @NotNull
    private String distributorLevelDesc;

    /**
     * 分销员等级列表
     */
    @ApiModelProperty("分销员等级列表")
    @NotEmpty
    private List<DistributorLevelDTO> distributorLevels;


    public void checkParam() { }
}