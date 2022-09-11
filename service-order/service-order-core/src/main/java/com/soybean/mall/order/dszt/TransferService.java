package com.soybean.mall.order.dszt;

import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.order.trade.model.root.Trade;
import org.springframework.stereotype.Service;

/**
 * Description: 转换服务
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/9/10 2:21 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class TransferService {


    /**
     * Trade 转化成 CreateOrderReq
     * @param trade
     * @return
     */
    public CreateOrderReq trade2CreateOrderReq(Trade trade) {
        CreateOrderReq createOrderReq = new CreateOrderReq();

        return createOrderReq;
    }


}
