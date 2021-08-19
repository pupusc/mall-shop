package com.wanmi.sbc.order.api.response.yzsalesmancustomer;

import com.wanmi.sbc.order.bean.vo.YzSalesmanCustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>有赞销售员客户关系列表结果</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 有赞销售员客户关系列表结果
     */
    @ApiModelProperty(value = "有赞销售员客户关系列表结果")
    private List<YzSalesmanCustomerVO> yzSalesmanCustomerVOList;
}
