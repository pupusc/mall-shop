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
public enum ThirdInvokePublishStatusEnum {

//    1：初始状态，2：推送完成 3、推送失败
    INIT(1, "初始状态"),
    SUCCESS(2, "推送成功"),
    FAIL(3, "推送失败"),
    CANCEL(4, "推送取消");

    private Integer code;
    private String message;
}
