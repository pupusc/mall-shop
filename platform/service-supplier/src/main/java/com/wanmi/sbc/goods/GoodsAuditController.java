package com.wanmi.sbc.goods;

import com.wanmi.sbc.goods.api.request.goods.GoodsSyncPageRequest;
import org.springframework.data.domain.Page;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.bean.vo.GoodsCateSyncVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSyncVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "GoodsAuditContrller", description = "商品审核服务 API")
@RestController
public class GoodsAuditController {
    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @ApiOperation(value = "审核类目")
    @RequestMapping(value = "/goods/audit/cate/list", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateSyncVO>> listGoodsCate() {
        return goodsQueryProvider.listGoodsCateSync();
    }

    @ApiOperation(value = "列表")
    @RequestMapping(value = "/goods/audit/list", method = RequestMethod.POST)
    public BaseResponse<Page<GoodsSyncVO>> listGoods(@RequestBody GoodsSyncPageRequest request) {
        return null;
    }
}
