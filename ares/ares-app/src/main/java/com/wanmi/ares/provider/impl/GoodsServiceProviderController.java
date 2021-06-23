package com.wanmi.ares.provider.impl;

import com.wanmi.ares.provider.GoodsServiceProvider;
import com.wanmi.ares.request.goods.GoodsReportRequest;
import com.wanmi.ares.thrift.GoodsServiceImpl;
import com.wanmi.ares.view.goods.GoodsReportPageView;
import com.wanmi.ares.view.goods.GoodsTotalView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here
 * @date 2019-05-16 11:54
 */
@RestController
public class GoodsServiceProviderController implements GoodsServiceProvider {

    @Autowired
    private GoodsServiceImpl goodsService;

    @Override
    public GoodsTotalView queryGoodsTotal(@RequestBody @Valid GoodsReportRequest request) {
        return goodsService.queryGoodsTotal(request);
    }

    @Override
    public GoodsReportPageView querySkuReport(@RequestBody @Valid GoodsReportRequest request)  {
        return goodsService.querySkuReport(request);
    }

    @Override
    public GoodsReportPageView queryCateReport(@RequestBody @Valid GoodsReportRequest request)  {
        return goodsService.queryCateReport(request);
    }

    @Override
    public GoodsReportPageView queryBrandReport(@RequestBody @Valid GoodsReportRequest request)  {
        return goodsService.queryBrandReport(request);
    }

    @Override
    public GoodsReportPageView queryStoreCateReport(@RequestBody @Valid GoodsReportRequest request)  {
        return goodsService.queryStoreCateReport(request);
    }
}
