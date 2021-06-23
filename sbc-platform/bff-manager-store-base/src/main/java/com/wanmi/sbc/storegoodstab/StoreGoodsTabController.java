package com.wanmi.sbc.storegoodstab;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.storegoodstab.StoreGoodsTabProvider;
import com.wanmi.sbc.goods.api.provider.storegoodstab.StoreGoodsTabQueryProvider;
import com.wanmi.sbc.goods.api.request.storegoodstab.*;
import com.wanmi.sbc.goods.api.response.storegoodstab.StoreGoodsTabAddResponse;
import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品详情模板管理Controller
 * Author: 谢猛男
 * Time: 2018/10/12 10:21
 */
@Api(tags = "StoreGoodsTabController", description = "商品详情模板管理服务API")
@RestController
@RequestMapping("/storeGoodsTab")
public class StoreGoodsTabController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreGoodsTabQueryProvider storeGoodsTabQueryProvider;

    @Autowired
    private StoreGoodsTabProvider storeGoodsTabProvider;

    /**
     * 查询商家商品详情模板List
     */
    @ApiOperation(value = "查询商家商品详情模板List")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List<StoreGoodsTabVO>> list() {
        StoreGoodsTabListByStoreIdRequest request = new StoreGoodsTabListByStoreIdRequest();
        request.setStoreId(commonUtil.getStoreId());
        return BaseResponse.success(storeGoodsTabQueryProvider.listByStoreId(request).getContext().getStoreGoodsTabs());
    }

    /**
     * 新增店铺商品详情模板
     *
     * @param saveRequest 新增详情模板
     */
    @ApiOperation(value = "新增店铺商品详情模板")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<StoreGoodsTabAddResponse> add(@RequestBody StoreGoodsTabAddRequest saveRequest) {
        if ("商品详情".equals(saveRequest.getTabName())) {
            return BaseResponse.error("商品详情模板名称已经存在");
        }
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        saveRequest.setCreatePerson(commonUtil.getOperatorId());
        saveRequest.setUpdatePerson(commonUtil.getOperatorId());
        return storeGoodsTabProvider.add(saveRequest);
    }

    /**
     * 拖拽改变排序
     *
     * @param tabSortRequest
     * @return
     */
    @ApiOperation(value = "拖拽改变排序")
    @RequestMapping(value = "/editSort", method = RequestMethod.PUT)
    public BaseResponse editSort(@RequestBody StoreGoodsTabEditSortRequest tabSortRequest) {

        return storeGoodsTabProvider.editSort(tabSortRequest);
    }

    /**
     * 编辑店铺商品详情模板
     *
     * @param saveRequest 编辑的商品详情模板
     */
    @ApiOperation(value = "编辑店铺商品详情模板")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody StoreGoodsTabModifyRequest saveRequest) {
        if ("商品详情".equals(saveRequest.getTabName())) {
            return BaseResponse.error("商品详情模板名称已经存在");
        }
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        saveRequest.setUpdatePerson(commonUtil.getOperatorId());
        return storeGoodsTabProvider.modify(saveRequest);
    }

    /**
     * 删除店铺商品详情模板
     */
    @ApiOperation(value = "删除店铺商品详情模板")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "tabId", value = "模板Id", required = true)
    @RequestMapping(value = "/{tabId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long tabId) {
        StoreGoodsTabDeleteRequest saveRequest = new StoreGoodsTabDeleteRequest();
        Long storeId = commonUtil.getStoreId();
        saveRequest.setStoreId(storeId);
        saveRequest.setTabId(tabId);

        return storeGoodsTabProvider.delete(saveRequest);
    }

}
