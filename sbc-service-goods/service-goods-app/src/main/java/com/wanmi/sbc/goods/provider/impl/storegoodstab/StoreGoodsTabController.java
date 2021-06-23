package com.wanmi.sbc.goods.provider.impl.storegoodstab;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.storegoodstab.StoreGoodsTabProvider;
import com.wanmi.sbc.goods.api.request.storegoodstab.*;
import com.wanmi.sbc.goods.api.response.storegoodstab.StoreGoodsTabAddResponse;
import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import com.wanmi.sbc.goods.storegoodstab.service.StoreGoodsTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:55 2018/12/13
 * @Description:
 */
@RestController
@Validated
public class StoreGoodsTabController implements StoreGoodsTabProvider {

    @Autowired
    private StoreGoodsTabService storeGoodsTabService;

    /**
     * 新增店铺商品模板
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<StoreGoodsTabAddResponse> add(@RequestBody @Valid StoreGoodsTabAddRequest request) {
        StoreGoodsTabVO storeGoodsTabVO = storeGoodsTabService.add(request);
        return BaseResponse.success(new StoreGoodsTabAddResponse(storeGoodsTabVO));
    }


    /**
     * 修改店铺商品模板
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid StoreGoodsTabModifyRequest request) {
        storeGoodsTabService.edit(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除店铺商品模板
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse delete(@RequestBody @Valid StoreGoodsTabDeleteRequest request) {
        storeGoodsTabService.delete(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 修改排序
     * @param request
     */
    @Override
    public BaseResponse editSort(@RequestBody @Valid StoreGoodsTabEditSortRequest request) {
        storeGoodsTabService.editSort(KsBeanUtil.convertList(request.getTabList(), StoreGoodsTab.class));
        return BaseResponse.SUCCESSFUL();
    }
}
