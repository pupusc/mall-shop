package com.wanmi.sbc.order.provider.impl.thirdplatformtrade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.thirdplatformtrade.ThirdPlatformTradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import com.wanmi.sbc.order.bean.vo.ThirdPlatformTradeVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.order.thirdplatformtrade.model.root.ThirdPlatformTrade;
import com.wanmi.sbc.order.thirdplatformtrade.service.LinkedMallTradeService;
import com.wanmi.sbc.order.trade.model.entity.value.Buyer;
import com.wanmi.sbc.order.thirdplatformtrade.request.ThirdPlatformTradeQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 第三方平台订单处理
 * @Autho daiyitian
 * @Date：2020-03-27 09:17
 */
@Validated
@RestController
public class ThirdPlatformTradeQueryController implements ThirdPlatformTradeQueryProvider {

    @Autowired
    private LinkedMallTradeService thirdPlatformTradeService;

    @Override
    public BaseResponse<ThirdTradeListByTradeIdsResponse> listByTradeIds(@RequestBody @Valid ThirdPlatformTradeListByTradeIdsRequest request) {
        List<ThirdPlatformTrade> tradeList = thirdPlatformTradeService.listByTradeIds(request.getTradeIds());
        if (tradeList.isEmpty()) {
            return BaseResponse.success(ThirdTradeListByTradeIdsResponse.builder().tradeList(Collections.emptyList()).build());
        }
        List<ThirdPlatformTradeVO> tradeVOList = tradeList.stream().map(i -> KsBeanUtil.convert(i, ThirdPlatformTradeVO.class)).collect(Collectors.toList());
        return BaseResponse.success(ThirdTradeListByTradeIdsResponse.builder().tradeList(tradeVOList).build());
    }

    /**
     * 分页查询第三方平台订单
     *
     * @param tradePageCriteriaRequest 带参分页参数
     * @return
     */
    @Override
    public BaseResponse<TradePageCriteriaResponse> pageCriteria(@RequestBody @Valid ThirdPlatformTradePageCriteriaRequest tradePageCriteriaRequest) {
        ThirdPlatformTradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradePageCriteriaRequest.getTradePageDTO(), ThirdPlatformTradeQueryRequest.class);
        Criteria criteria;
        if (tradePageCriteriaRequest.isReturn()) {
            criteria = tradeQueryRequest.getCanReturnCriteria();
        } else {
            criteria = tradeQueryRequest.getWhereCriteria();
        }
        Page<ThirdPlatformTrade> page = thirdPlatformTradeService.page(criteria, tradeQueryRequest);
        MicroServicePage<TradeVO> tradeVOS = KsBeanUtil.convertPage(page, TradeVO.class);
        return BaseResponse.success(TradePageCriteriaResponse.builder().tradePage(tradeVOS).build());
    }


    /**
     * 根据主订单号查询第三方订单
     *
     * @param tradeListByOrderCodeRequest 主交易单id {@link TradeListByParentIdRequest}
     * @return
     */
    @Override
    public BaseResponse<ThirdTradeListByTradeIdsResponse> listByTradeId(@Valid TradeListByOrderCodeRequest tradeListByOrderCodeRequest) {
        List<ThirdPlatformTrade> tradeList =
                thirdPlatformTradeService.findListByTradeId(tradeListByOrderCodeRequest.getTradeId());
        if (tradeList.isEmpty()) {
            return BaseResponse.success(ThirdTradeListByTradeIdsResponse.builder().tradeList(Collections.emptyList()).build());
        }
        // 主订单号对应的子订单的买家信息应该是相同的
        ThirdPlatformTrade trade = tradeList.get(0);

        final Buyer buyer = trade.getBuyer();
        //统一设置账号加密后的买家信息
        List<ThirdPlatformTradeVO> tradeVOList = tradeList.stream().map(i -> {
            i.setBuyer(buyer);
            return KsBeanUtil.convert(i, ThirdPlatformTradeVO.class);
        }).collect(Collectors.toList());
        return BaseResponse.success(ThirdTradeListByTradeIdsResponse.builder().tradeList(tradeVOList).build());
    }

    /**
     * 按条件统计数量
     * @param tradeCountCriteriaRequest 带参分页参数 {@link TradeCountCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<ProviderTradeCountCriteriaResponse> countCriteria(@RequestBody @Valid ThirdPlatformTradeCountCriteriaRequest tradeCountCriteriaRequest) {
        ThirdPlatformTradeQueryRequest tradeQueryRequest = KsBeanUtil.convert(
                tradeCountCriteriaRequest.getTradePageDTO(), ThirdPlatformTradeQueryRequest.class);
        long count = thirdPlatformTradeService.countNum(tradeQueryRequest.getWhereCriteria(), tradeQueryRequest);
        return BaseResponse.success(ProviderTradeCountCriteriaResponse.builder().count(count).build());
    }
}
