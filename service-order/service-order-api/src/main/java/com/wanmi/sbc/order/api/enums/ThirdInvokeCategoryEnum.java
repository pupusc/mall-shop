package com.wanmi.sbc.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/19 2:33 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Getter
@AllArgsConstructor
public enum ThirdInvokeCategoryEnum {

    INVOKE_ORDER(1, "订单"),
    INVOKE_RETURN_ORDER(2, "售后订单");

    private Integer code;
    private String message;
}
