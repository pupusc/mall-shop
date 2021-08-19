package com.wanmi.sbc.order.trade.request;


import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 提交订单公用方法的参数类型
 * (定义成一个类,是为了后面方便扩展字段)
 *
 * @author bail
 * @date 2018/5/5.13:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeWrapperListRequest {

   private TradeCommitRequest tradeCommitRequest;

   private List<TradeItemGroup> tradeItemGroups;

   private Map<Long, CommonLevelVO> storeLevelMap;

   private List<StoreVO> storeVOList;
}
