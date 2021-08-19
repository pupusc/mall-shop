package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCustomerRelaListByConditionResponse implements Serializable {

    private static final long serialVersionUID = -8518243246871984707L;

    @ApiModelProperty(value = "店铺-会员")
    private List<StoreCustomerRelaVO> relaVOList;

}
