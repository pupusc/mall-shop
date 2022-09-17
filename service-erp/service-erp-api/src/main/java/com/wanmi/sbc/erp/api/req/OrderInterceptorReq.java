package com.wanmi.sbc.erp.api.req;

import lombok.Data;

import java.io.Serializable;


/**
 * Description: 拦截商品
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/8/26 3:17 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderInterceptorReq implements Serializable {

    /**
     * 子发货订单id
     */
    private Long devItemId;

    /**
     * 子订单id
     */
    private Long orderItemId;


}
