package com.wanmi.sbc.order.api.request.returnorder;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/4/25 2:49 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class RefundRejectRequest implements Serializable {

    private String rid;

    private String returnReanson;

    /**
     * 强制拒绝 1 表示强制拒绝
     */
    private Integer forceReject;
}
