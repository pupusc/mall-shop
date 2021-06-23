package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.trade.TradeItemQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeItemByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.trade.TradeItemByCustomerIdResponse;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.trade.model.root.TradeItemGroup;
import com.wanmi.sbc.order.trade.service.TradeItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单商品服务查询接口</p>
 * @Author: daiyitian
 * @Description: 退单服务查询接口
 * @Date: 2018-12-03 15:40
 */
@Validated
@RestController
public class TradeItemQueryController implements TradeItemQueryProvider {

    @Autowired
    private TradeItemService tradeItemService;

    /**
     * 根据客户id查询已确认订单商品快照
     *
     * @param request 根据客户id查询已确认订单商品快照请求结构 {@link TradeItemByCustomerIdRequest}
     * @return 订单商品快照列表 {@link TradeItemByCustomerIdResponse}
     */
    @Override
    public BaseResponse<TradeItemByCustomerIdResponse> listByTerminalToken(@RequestBody @Valid TradeItemByCustomerIdRequest
                                                                         request) {
        List<TradeItemGroup> groupList = tradeItemService.find(request.getTerminalToken());
        List<TradeItemGroupVO> groupVOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupList)) {
            groupVOList = KsBeanUtil.convert(groupList, TradeItemGroupVO.class);
        }
        return BaseResponse.success(TradeItemByCustomerIdResponse.builder().tradeItemGroupList(groupVOList).build());
    }
}
