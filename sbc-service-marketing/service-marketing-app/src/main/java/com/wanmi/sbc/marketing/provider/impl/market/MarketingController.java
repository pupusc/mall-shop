package com.wanmi.sbc.marketing.provider.impl.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.market.MarketingProvider;
import com.wanmi.sbc.marketing.api.request.market.*;
import com.wanmi.sbc.marketing.api.response.market.MarketingAddResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingDeleteResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingPauseResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingStartResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-21 14:27
 */
@Validated
@RestController
public class MarketingController implements MarketingProvider {

    @Autowired
    private MarketingService marketingService;

    /**
     * @param addRequest 新增参数 {@link MarketingAddRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingAddResponse> add(@RequestBody @Valid MarketingAddRequest addRequest) {

        Marketing marketing = marketingService.addMarketing(KsBeanUtil.convert(addRequest, MarketingSaveRequest.class));
        MarketingAddResponse response = MarketingAddResponse.builder().marketingVO(KsBeanUtil.convert(marketing, MarketingVO.class)).build();

        return BaseResponse.success(response);
    }

    /**
     * @param modifyRequest 新增参数 {@link MarketingModifyRequest}
     * @return
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid MarketingModifyRequest modifyRequest) {

        marketingService.modifyMarketing(KsBeanUtil.convert(modifyRequest, MarketingSaveRequest.class));

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param deleteByIdRequest 营销ID {@link MarketingDeleteByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingDeleteResponse> deleteById(@RequestBody @Valid MarketingDeleteByIdRequest deleteByIdRequest) {

        MarketingDeleteResponse response = MarketingDeleteResponse.builder()
                .resultNum( marketingService.deleteMarketingById(deleteByIdRequest.getMarketingId())).build();

        return BaseResponse.success(response);
    }

    /**
     * @param pauseByIdRequest 营销ID {@link MarketingPauseByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingPauseResponse> pauseById(@RequestBody @Valid MarketingPauseByIdRequest pauseByIdRequest) {

        MarketingPauseResponse response = MarketingPauseResponse.builder()
                .resultNum( marketingService.pauseMarketingById(pauseByIdRequest.getMarketingId())).build();
        return BaseResponse.success(response);
    }

    /**
     * @param startByIdRequest 营销ID {@link MarketingStartByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<MarketingStartResponse> startById(@RequestBody @Valid MarketingStartByIdRequest startByIdRequest) {

        MarketingStartResponse response = MarketingStartResponse.builder()
                .resultNum(marketingService.startMarketingById(startByIdRequest.getMarketingId())).build();

        return BaseResponse.success(response);
    }
}
