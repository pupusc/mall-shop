package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
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
 * @createTime 2018-09-13 14:31 <br>
 * @see com.wanmi.sbc.customer.api.response.store <br>
 * @since V1.0<br>
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCustomerRelaListResponse implements Serializable {

    private static final long serialVersionUID = -4784983695203121318L;

    @ApiModelProperty(value = "店铺-会员")
    private List<StoreCustomerRelaVO> storeCustomerRelaVOS;
}
