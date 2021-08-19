package com.wanmi.sbc.customer.api.response.level;

import com.wanmi.sbc.customer.bean.vo.CustomerBaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class CustomerLevelListByCustomerIdsResponse implements Serializable {

    private static final long serialVersionUID = -5023862202125296544L;

    /**
     * 客户等级信息集合
     */
    @ApiModelProperty(value = "客户等级信息集合")
    private List<CustomerBaseVO> customerLevelVOList;

}
