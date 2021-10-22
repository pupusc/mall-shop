package com.wanmi.sbc.goods;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsAuditProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.bean.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @menu 商城审核
 */
@Api(tags = "GoodsAuditController", description = "商品审核服务 Api")
@RestController
@RequestMapping("/goods/audit")
public class GoodsAuditController {

    @Autowired
    private GoodsAuditProvider goodsAuditProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @ApiOperation(value = "审核类目")
    @RequestMapping(value = "/cate/list", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateSyncVO>> listGoodsCate() {

        return goodsQueryProvider.listGoodsCateSync();
    }

    @ApiOperation(value = "查询商品审核列表")
    @PostMapping(value = "/list")
    public BaseResponse<Page<GoodsSyncVO>> list(@RequestBody GoodsAuditQueryRequest request) {
        return goodsAuditProvider.list(request);
    }

}