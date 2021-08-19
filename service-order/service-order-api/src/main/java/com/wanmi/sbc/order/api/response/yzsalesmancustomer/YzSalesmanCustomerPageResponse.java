package com.wanmi.sbc.order.api.response.yzsalesmancustomer;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.bean.vo.YzSalesmanCustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系分页结果</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 有赞销售员客户关系分页结果
     */
    @ApiModelProperty(value = "有赞销售员客户关系分页结果")
    private MicroServicePage<YzSalesmanCustomerVO> yzSalesmanCustomerVOPage;
}
