package com.wanmi.sbc.goods;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.api.provider.goods.GoodsAuditProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.response.goods.GoodsViewByIdResponse;
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
    public BaseResponse<MicroServicePage<GoodsSyncVO>> list(@RequestBody GoodsAuditQueryRequest request) {
        return goodsAuditProvider.list(request);
    }

    @ApiOperation(value = "提交同盾审核")
    @PostMapping(value = "/image/audit")
    public BaseResponse imageAudit(@RequestBody GoodsAuditQueryRequest request){
        goodsAuditProvider.auditGoods(request);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "广告法人工审核通过（批量和单个一个接口）")
    @PostMapping(value = "/manual/approve")
    public BaseResponse adManualApprove(@RequestBody GoodsAuditQueryRequest request){
        return goodsAuditProvider.approveAdManual(request);
    }

    @ApiOperation(value = "广告法人工审核拒绝（批量和单个一个接口）")
    @PostMapping(value = "/manual/reject")
    public BaseResponse adManualReject(@RequestBody GoodsAuditQueryRequest request){
        return goodsAuditProvider.rejectAdManual(request);
    }

    @ApiOperation(value = "上架审核通过（批量和单个一个接口）")
    @PostMapping(value = "/launch/approve")
    public BaseResponse launchApprove(@RequestBody GoodsAuditQueryRequest request){
        return goodsAuditProvider.launchApprove(request);
    }

    @ApiOperation(value = "上架审核拒绝（批量和单个一个接口）")
    @PostMapping(value = "/launch/reject")
    public BaseResponse launchReject(@RequestBody GoodsAuditQueryRequest request){
        return goodsAuditProvider.launchReject(request);
    }

    @ApiOperation(value = "发布商品（批量和单个一个接口）")
    @PostMapping(value = "/publish")
    public BaseResponse publishGoods(@RequestBody GoodsAuditQueryRequest request){
        return goodsAuditProvider.publish(request);
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/detail")
    public BaseResponse<GoodsViewByIdResponse> detail(@RequestParam("goodsNo")String goodsNo){
        return goodsAuditProvider.detail(goodsNo);
    }



}