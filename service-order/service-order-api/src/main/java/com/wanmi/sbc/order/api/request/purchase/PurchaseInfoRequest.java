package com.wanmi.sbc.order.api.request.purchase;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class PurchaseInfoRequest implements Serializable {

    /**
     * 单品ids
     */
    private List<String> goodsInfoIds;

    /**
     * 登录用户
     */
    private CustomerVO customer;

    /**
     * 邀请人
     */
    private String inviteeId;

    /**
     * 区的区域码
     */
    private Long areaId;

}
