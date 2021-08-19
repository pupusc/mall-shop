package com.wanmi.sbc.order.api.response.yzsalesmancustomer;

import com.wanmi.sbc.order.bean.vo.YzSalesmanCustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系修改结果</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的有赞销售员客户关系信息
     */
    @ApiModelProperty(value = "已修改的有赞销售员客户关系信息")
    private YzSalesmanCustomerVO yzSalesmanCustomerVO;
}
