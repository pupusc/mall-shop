package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeItemModifyGoodsNumResponse;
import com.wanmi.sbc.order.bean.vo.TradeItemGroupVO;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.TradeItemSnapshot;
import com.wanmi.sbc.order.trade.service.TradeItemService;
import com.wanmi.sbc.order.trade.service.TradeItemSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>订单商品服务操作接口</p>
 * @Author: daiyitian
 * @Description: 退单服务操作接口
 * @Date: 2018-12-03 15:40
 */
@Validated
@RestController
public class TradeItemController implements TradeItemProvider {

    @Autowired
    private TradeItemService tradeItemService;

    @Autowired
    private TradeItemSnapshotService tradeItemSnapshotService;

    /**
     * 保存订单商品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse snapshot(@RequestBody @Valid TradeItemSnapshotRequest request){
        tradeItemService.snapshot(request, KsBeanUtil.convert(request.getTradeItems(), TradeItem.class),
                request.getTradeMarketingList(), request.getSkuList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<TradeItemModifyGoodsNumResponse> modifyGoodsNum(@RequestBody @Valid TradeItemModifyGoodsNumRequest request){
        TradeItemSnapshot snapshot = tradeItemService.modifyGoodsNum(request);
        List<TradeItemGroupVO> groupVOList = new ArrayList<>();
        if (Objects.nonNull(snapshot)) {
            groupVOList = KsBeanUtil.convert(snapshot.getItemGroups(), TradeItemGroupVO.class);
        }
        return BaseResponse.success(TradeItemModifyGoodsNumResponse.builder().tradeItemGroupList(groupVOList).build());
    }

    /**
     * 根据customerId删除订单商品快照
     *
     * @param request 根据customerId删除订单商品快照请求结构 {@link TradeItemDeleteByCustomerIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse deleteByCustomerId(@RequestBody @Valid TradeItemDeleteByCustomerIdRequest request){
        tradeItemService.remove(request.getTerminalToken());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 保存订单赠品快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotGiftRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse snapshotGift(@Valid TradeItemSnapshotGiftRequest request) {
        tradeItemService.fullGiftSnapshot(request.getTerminalToken(), request.getTradeMarketingDTO());
        return BaseResponse.SUCCESSFUL();
    }
    /**
     * 换购商品保存订单快照
     *
     * @param request 保存订单商品快照请求结构 {@link TradeItemSnapshotGiftRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse snapshotMarkup(@Valid TradeItemSnapshotMarkupRequest request) {
        tradeItemService.markupSnapshot(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse confirmSettlement(@Valid TradeItemConfirmSettlementRequest request) {
        tradeItemService.confirmSettlement(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse snapshotCycleBuyGift(@Valid @RequestBody TradeItemSnapshotCycleBuyGiftRequest request) {
        tradeItemService.cycleBuyGiftSnapshot(request.getTerminalToken(), request.getCycleBuyInfo());
        return BaseResponse.SUCCESSFUL();
    }
}
