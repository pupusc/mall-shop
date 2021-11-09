package com.wanmi.sbc.marketing.provider.impl.points;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.api.provider.points.PointsExchangeProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingPageRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangeAcitvityAddRequest;
import com.wanmi.sbc.marketing.api.request.points.PointsExchangePageRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.vo.PointsExchangeActivityVO;
import com.wanmi.sbc.marketing.points.service.PointsExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Validated
public class PointsExchangeController implements PointsExchangeProvider {

    @Autowired
    private PointsExchangeService pointsExchangeService;

    @Override
    public BaseResponse add(@Valid PointsExchangeAcitvityAddRequest request) {
        pointsExchangeService.add(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<MicroServicePage<PointsExchangeActivityVO>> page(@RequestBody PointsExchangePageRequest request) {
        return BaseResponse.success(pointsExchangeService.list(request));
    }

    @Override
    public BaseResponse<PointsExchangeActivityVO> detail(Integer id) {
        return BaseResponse.success(pointsExchangeService.detail(id));
    }

    @Override
    public BaseResponse modify(@Valid PointsExchangeAcitvityAddRequest request) {
        return null;
    }

    @Override
    public BaseResponse pause(Integer id) {
        return null;
    }
}
