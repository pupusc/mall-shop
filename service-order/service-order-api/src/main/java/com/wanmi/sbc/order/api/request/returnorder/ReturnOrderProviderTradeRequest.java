package com.wanmi.sbc.order.api.request.returnorder;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/2/18 9:21 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ReturnOrderProviderTradeRequest implements Serializable {

    @NotBlank
    private String tid;
}
