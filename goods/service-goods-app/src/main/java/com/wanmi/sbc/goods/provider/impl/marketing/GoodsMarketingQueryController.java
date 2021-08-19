package com.wanmi.sbc.goods.provider.impl.marketing;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.marketing.GoodsMarketingQueryProvider;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingListByCustomerIdRequest;
import com.wanmi.sbc.goods.api.response.marketing.GoodsMarketingListByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import com.wanmi.sbc.goods.marketing.model.data.GoodsMarketing;
import com.wanmi.sbc.goods.marketing.service.GoodsMarketingService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description: 商品营销查询控制器
 * @Date: 2018-11-07 13:46
 */
@Validated
@RestController
public class GoodsMarketingQueryController implements GoodsMarketingQueryProvider {

    @Autowired
    private GoodsMarketingService goodsMarketingService;

    
    @Override
    public BaseResponse<GoodsMarketingListByCustomerIdResponse> listByCustomerId(@RequestBody @Valid GoodsMarketingListByCustomerIdRequest request) {

        List<GoodsMarketing> goodsMarketingList = goodsMarketingService.queryGoodsMarketingList(request.getCustomerId());

        if(CollectionUtils.isEmpty(goodsMarketingList)){
            return BaseResponse.success(GoodsMarketingListByCustomerIdResponse.builder().build());
        }

        List<GoodsMarketingVO> goodsMarketingVOList = KsBeanUtil.convertList(goodsMarketingList, GoodsMarketingVO.class);

        return BaseResponse.success(GoodsMarketingListByCustomerIdResponse.builder()
                .goodsMarketings(goodsMarketingVOList).build());

    }
}
