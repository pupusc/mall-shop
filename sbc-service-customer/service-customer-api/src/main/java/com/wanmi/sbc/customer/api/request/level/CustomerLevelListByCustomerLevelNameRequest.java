package com.wanmi.sbc.customer.api.request.level;

import com.wanmi.sbc.customer.bean.dto.MarketingCustomerLevelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 客户等级查询请求参数
 * Created by CHENLI on 2017/4/13.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLevelListByCustomerLevelNameRequest implements Serializable {

    private static final long serialVersionUID = -5023862202125296544L;

    /**
     * 营销会员等级对象
     */
    @ApiModelProperty(value = "营销会员等级对象")
    @NotNull
    private List<MarketingCustomerLevelDTO> customerLevelDTOList;

}
