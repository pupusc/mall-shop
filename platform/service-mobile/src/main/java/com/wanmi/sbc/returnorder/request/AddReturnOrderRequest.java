package com.wanmi.sbc.returnorder.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Description: 退款
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/23 1:18 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class AddReturnOrderRequest {

    /**
     * 订单id
     */
    @NotBlank
    private String orderId;
}
