package com.wanmi.sbc.goods.provider.impl.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.cate.BossGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.BossGoodsCateCheckSignChildRequest;
import com.wanmi.sbc.goods.api.request.cate.BossGoodsCateCheckSignGoodsRequest;
import com.wanmi.sbc.goods.cate.service.S2bGoodsCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-07 18:47
 */
@Validated
@RestController
public class BossGoodsCateQueryController implements BossGoodsCateQueryProvider {

    @Autowired
    private S2bGoodsCateService s2bGoodsCateService;

    /**
     * @param request 验证是否有子类(包含签约分类) {@link BossGoodsCateCheckSignChildRequest}
     * @return
     */
    @Override
    public BaseResponse<Integer> checkSignChild(@RequestBody @Valid BossGoodsCateCheckSignChildRequest request) {
        return BaseResponse.success(s2bGoodsCateService.checkSignChild(request.getCateId()));
    }

    /**
     * @param request 验证签约分类下是否有商品 {@link BossGoodsCateCheckSignGoodsRequest}
     * @return
     */
    @Override
    public BaseResponse<Integer> checkSignGoods(@RequestBody @Valid BossGoodsCateCheckSignGoodsRequest request) {
        return BaseResponse.success(s2bGoodsCateService.checkSignGoods(request.getCateId()));
    }
}
