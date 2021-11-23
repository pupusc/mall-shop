package com.wanmi.sbc.blacklist;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListProviderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 新增黑名单
     * @param goodsBlackListProviderRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse add(@Validated @RequestBody GoodsBlackListProviderRequest goodsBlackListProviderRequest) {
        return BaseResponse.success(goodsBlackListProvider.add(goodsBlackListProviderRequest));
    }

    /**
     * 删除黑名单
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseResponse delete(@PathVariable("id") Integer id) {
        return BaseResponse.success(goodsBlackListProvider.delete(id));
    }



}
