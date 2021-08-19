package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreCustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * <Description> <br>
 *
 * @author hejiawen<br>
 * @version 1.0<br>
 * @taskId <br>
 * @createTime 2018-09-11 17:14 <br>
 * @see com.wanmi.sbc.customer.api.response.store <br>
 * @since V1.0<br>
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerResponse implements Serializable {

    private static final long serialVersionUID = 1354761764680241876L;

    @ApiModelProperty(value = "店铺-会员")
    List<StoreCustomerVO> storeCustomerVOList;
}
