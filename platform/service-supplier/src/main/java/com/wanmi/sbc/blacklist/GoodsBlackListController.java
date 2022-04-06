package com.wanmi.sbc.blacklist;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListCacheProviderRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListCreateOrUpdateRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListProviderRequest;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListSearchRequest;
import com.wanmi.sbc.goods.api.request.blacklist.response.GoodsBlackListData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/11/23 7:47 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/goods-blacklist")
@RestController
public class GoodsBlackListController {

    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;

    /**
     * @description 添加黑名单
     * @param goodsBlackListProviderRequest
     * @menu 黑名单
     * @status done
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody GoodsBlackListProviderRequest goodsBlackListProviderRequest) {
        return BaseResponse.success(goodsBlackListProvider.add(goodsBlackListProviderRequest));
    }

    /**
     * @description 删除黑名单
     * @param id
     * @menu 黑名单
     * @status done
     */
    @GetMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id) {
        return BaseResponse.success(goodsBlackListProvider.delete(id));
    }

    /**
     * @description 更新黑名单
     * @param goodsBlackListCreateOrUpdateRequest
     * @menu 黑名单
     * @status done
     */
    @PostMapping("/update")
    public BaseResponse update(@RequestBody GoodsBlackListCreateOrUpdateRequest goodsBlackListCreateOrUpdateRequest){
        goodsBlackListProvider.update(goodsBlackListCreateOrUpdateRequest);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @description 查询黑名单
     * @param goodsBlackListCacheProviderRequest
     * @menu 黑名单
     * @status done
     */
    @PostMapping("/list")
    public BaseResponse<List<GoodsBlackListData>> list(@RequestBody GoodsBlackListCacheProviderRequest goodsBlackListCacheProviderRequest){
        return goodsBlackListProvider.list(goodsBlackListCacheProviderRequest);
    }

}
