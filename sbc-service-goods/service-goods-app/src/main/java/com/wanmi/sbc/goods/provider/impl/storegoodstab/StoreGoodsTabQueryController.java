package com.wanmi.sbc.goods.provider.impl.storegoodstab;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.storegoodstab.StoreGoodsTabQueryProvider;
import com.wanmi.sbc.goods.api.request.storegoodstab.StoreGoodsTabListByStoreIdRequest;
import com.wanmi.sbc.goods.api.response.storegoodstab.StoreGoodsTabListByStoreIdResponse;
import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import com.wanmi.sbc.goods.storegoodstab.service.StoreGoodsTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:42 2018/12/21
 * @Description:
 */
@RestController
@Validated
public class StoreGoodsTabQueryController implements StoreGoodsTabQueryProvider {

    @Autowired
    private StoreGoodsTabService storeGoodsTabService;

    /**
     * 根据店铺id查询商品模板列表
     *
     * @param request {@link StoreGoodsTabListByStoreIdRequest}
     * @return 商品模板列表 {@link StoreGoodsTabListByStoreIdResponse}
     */
    @Override
    public BaseResponse<StoreGoodsTabListByStoreIdResponse> listByStoreId(
            @RequestBody @Valid StoreGoodsTabListByStoreIdRequest request) {
        List<StoreGoodsTabVO> storeGoodsTabs = storeGoodsTabService.query(request.getStoreId());
        if (CollectionUtils.isEmpty(storeGoodsTabs)) {
            return BaseResponse.success(new StoreGoodsTabListByStoreIdResponse(Collections.EMPTY_LIST));
        }
        return BaseResponse.success(new StoreGoodsTabListByStoreIdResponse(storeGoodsTabs));
    }

}
