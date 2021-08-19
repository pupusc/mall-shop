package com.wanmi.sbc.marketing.api.provider.halfpricesecondpiece;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.halfpricesecondpiece.HalfPriceSecondPieceAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销第二件业务</p>
 * author: weiwenhao
 * Date: 2020-05-22
 */
@FeignClient(value = "${application.marketing.name}", contextId = "HalfPriceSecondPieceProvider")
public interface HalfPriceSecondPieceProvider {

    /**
     * 新增第二件半价活动
     * @param request 第二件半价活动 {@link HalfPriceSecondPieceAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/half_price_second_piece/add")
    BaseResponse addHalfPriceSecondPiece(@RequestBody @Valid HalfPriceSecondPieceAddRequest request);

    /**
     * 修改第二件半价活动
     * @param request 修改第二件半价活动 {@link HalfPriceSecondPieceAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/half_price_second_piece/modify")
    BaseResponse modifyHalfPriceSecondPiece(@RequestBody @Valid HalfPriceSecondPieceAddRequest request);

}
