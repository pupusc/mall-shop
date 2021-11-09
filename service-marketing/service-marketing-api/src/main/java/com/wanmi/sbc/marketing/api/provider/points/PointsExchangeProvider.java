package com.wanmi.sbc.marketing.api.provider.points;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingPageRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangeAcitvityAddRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangePageRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;



@FeignClient(value = "${application.marketing.name}", contextId = "PointsExchangeProvider")
public interface PointsExchangeProvider {

    /**
     * 新增积分兑换活动
     */
    @PostMapping("/marketing/${application.marketing.version}/point/exchange/activity/add")
    BaseResponse add(@RequestBody @Valid PointsExchangeAcitvityAddRequest request);

    /**
     * 分页查询促销活动
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/point/exchange/activity/page")
    BaseResponse<MicroServicePage<PointsExchangeActivityVO>> page(@RequestBody PointsExchangePageRequest request);

    @GetMapping("/marketing/${application.marketing.version}/point/exchange/activity/detail")
    BaseResponse<PointsExchangeActivityVO> detail(@RequestParam("id")Integer id);

    @PostMapping("/marketing/${application.marketing.version}/point/exchange/activity/modify")
    BaseResponse modify(@RequestBody @Valid PointsExchangeAcitvityAddRequest request);

    @PostMapping("/marketing/${application.marketing.version}/point/exchange/activity/pause")
    BaseResponse pause(@RequestParam("id")Integer id);

}
