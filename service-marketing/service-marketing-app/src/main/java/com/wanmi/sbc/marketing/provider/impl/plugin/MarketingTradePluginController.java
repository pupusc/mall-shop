package com.wanmi.sbc.marketing.provider.impl.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingTradePluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingTradeBatchWrapperRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingTradeBatchWrapperResponse;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingWrapperVO;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.plugin.ITradeCommitPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>订单营销插件服务操作接口</p>
 * author: daiyitian
 * Date: 2018-11-15
 */
@Validated
@RestController
public class MarketingTradePluginController implements MarketingTradePluginProvider {

    @Autowired
    private List<ITradeCommitPlugin> tradeCommitPlugins;

    /**
     * 订单营销批量处理
     * @param request 包含多个营销处理结构 {@link MarketingTradeBatchWrapperRequest}
     * @return 处理结果 {@link MarketingTradeBatchWrapperResponse}
     */
    @Override
    public BaseResponse<MarketingTradeBatchWrapperResponse> batchWrapper(@RequestBody @Valid
                                                                                 MarketingTradeBatchWrapperRequest request){
        List<TradeMarketingWrapperVO> wraperVOList = new ArrayList<>();
        request.getWraperDTOList()
                .stream().map(dto -> KsBeanUtil.convert(dto, TradeMarketingPluginRequest.class))
                .forEach(dto -> {
                    tradeCommitPlugins.forEach(plugin -> {
                        TradeMarketingResponse response = plugin.wraperMarketingFullInfo(dto);
                        if (response != null) {
                            wraperVOList.add(KsBeanUtil.convert(response, TradeMarketingWrapperVO.class));
                        }
                    });
                });
        return BaseResponse.success(MarketingTradeBatchWrapperResponse.builder().wraperVOList(wraperVOList).build());
    }

}
