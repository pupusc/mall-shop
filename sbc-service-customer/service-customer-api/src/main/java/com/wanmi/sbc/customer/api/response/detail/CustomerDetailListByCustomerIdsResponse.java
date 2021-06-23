package com.wanmi.sbc.customer.api.response.detail;

import com.wanmi.sbc.customer.bean.vo.CustomerDetailBaseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据会员id集合查询会员详情基础数据集合
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailListByCustomerIdsResponse implements Serializable {

    private static final long serialVersionUID = -7395969401233896552L;

    /**
     * 会员详情基础数据集合
     */
    @ApiModelProperty(value = "会员详情基础数据集合")
    private List<CustomerDetailBaseVO> list;


}
