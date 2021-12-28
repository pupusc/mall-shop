package com.wanmi.sbc.order.returnorder.service;

import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderAddProviderRequest;
import com.wanmi.sbc.order.bean.enums.ReturnWay;
import org.springframework.stereotype.Service;

/**
 * Description: 退单
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/12/23 7:53 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class ReturnOrderNewService {


//    public void createReturnOrder(ReturnOrderAddProviderRequest returnOrderAddProviderRequest) {
//
//
//        long count = returnOrder.getReturnItems().stream()
//                .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType())
//                        || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
//        long giftsCount = returnOrder.getReturnGifts().stream()
//                .filter(t -> GoodsType.VIRTUAL_COUPON.equals(t.getGoodsType())
//                        || GoodsType.VIRTUAL_GOODS.equals(t.getGoodsType())).count();
//        if (count + giftsCount == returnOrder.getReturnItems().size() + returnOrder.getReturnGifts().size()) {
//            // 虚拟商品 只能直接退款
//            returnOrder.setReturnWay(ReturnWay.OTHER);
//        }
//    }
}
