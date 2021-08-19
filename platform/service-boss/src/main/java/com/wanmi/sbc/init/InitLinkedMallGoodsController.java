package com.wanmi.sbc.init;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class InitLinkedMallGoodsController {
    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @GetMapping("/init/linkedmall/goods")
    public BaseResponse initLinkedMallGoods() {
        ThirdPlatformConfigResponse response =
                thirdPlatformConfigQueryProvider.get(new ThirdPlatformConfigByTypeRequest(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue())).getContext();
        if (response != null && ThirdPlatformType.LINKED_MALL.equals(response.getThirdPlatformType()) && Integer.valueOf(1).equals(response.getStatus())) {
            goodsProvider.initLinkedMallGoods();
            return BaseResponse.SUCCESSFUL();
        } else {
            return BaseResponse.success("配置未启用");
        }
    }

    /**
     * 删除linkedmall商品，linkedmall删除接口回调失败，手动删除
     *
     * @return
     */
    @GetMapping("/del/linkedmall/goods")
    public BaseResponse<String> delLinkedMallGoods() {
        ThirdPlatformConfigResponse response =
                thirdPlatformConfigQueryProvider.get(new ThirdPlatformConfigByTypeRequest(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue())).getContext();
        if (response != null && ThirdPlatformType.LINKED_MALL.equals(response.getThirdPlatformType()) && Integer.valueOf(1).equals(response.getStatus())) {
            List<String> esGoodsInfoIds = goodsProvider.delLinkedMallGoods().getContext();
            if (esGoodsInfoIds != null && esGoodsInfoIds.size() > 0) {
                esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().skuIds(esGoodsInfoIds).build());
            }
            return BaseResponse.SUCCESSFUL();
        } else {
            return BaseResponse.success("配置未启用");
        }
    }

}
