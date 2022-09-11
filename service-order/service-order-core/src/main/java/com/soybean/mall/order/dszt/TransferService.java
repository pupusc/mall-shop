package com.soybean.mall.order.dszt;
import com.wanmi.sbc.erp.api.constant.DeviceTypeEnum;
import com.wanmi.sbc.erp.api.req.CreateOrderReq.BuyAddressReq;
import com.google.common.collect.Lists;
import java.util.Date;
import java.time.LocalDateTime;
import com.google.common.collect.Maps;

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
    public CreateOrderReq trade2CreateOrderReq(Trade trade,) {
        CreateOrderReq createOrderReq = new CreateOrderReq();
        createOrderReq.setPlatformCode(trade.getId());
        createOrderReq.setOrderSource("XX_MALL");
        createOrderReq.setUserId(0L);
        createOrderReq.setBuyerMemo(trade.getBuyerRemark());
        createOrderReq.setDeviceType(DeviceTypeEnum.WEB.getType());
        createOrderReq.setSaleChannelId(0); //TODO
        createOrderReq.setPostFee(0);
        createOrderReq.setPayTimeOut(new Date());
        createOrderReq.setBuyGoodsBOS(Lists.newArrayList());
        createOrderReq.setBuyAddressBO(new BuyAddressReq());
        createOrderReq.setShopId("");
        createOrderReq.setSellerMemo("");
        createOrderReq.setBookModel(0);
        createOrderReq.setBookTime(LocalDateTime.now());
        createOrderReq.setOrderSnapshot(Maps.newHashMap());


        return createOrderReq;
    }


}
