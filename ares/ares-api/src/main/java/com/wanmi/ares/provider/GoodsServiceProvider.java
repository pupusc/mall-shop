package com.wanmi.ares.provider;

import com.wanmi.ares.request.goods.GoodsReportRequest;
import com.wanmi.ares.view.goods.GoodsReportPageView;
import com.wanmi.ares.view.goods.GoodsTotalView;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author liguang3096
 * @Description do something here2
 * @date 2019-05-16 11:34
 */
@FeignClient(name = "${application.ares.name}", contextId = "GoodsServiceProvider")
public interface GoodsServiceProvider {
    @PostMapping("/ares/${application.ares.version}/goods/queryGoodsTotal")
    GoodsTotalView queryGoodsTotal(@RequestBody @Valid GoodsReportRequest request);

    @PostMapping("/ares/${application.ares.version}/goods/querySkuReport")
    GoodsReportPageView querySkuReport(@RequestBody @Valid GoodsReportRequest request);

    @PostMapping("/ares/${application.ares.version}/goods/queryCateReport")
    GoodsReportPageView queryCateReport(@RequestBody @Valid GoodsReportRequest request);

    @PostMapping("/ares/${application.ares.version}/goods/queryBrandReport")
    GoodsReportPageView queryBrandReport(@RequestBody @Valid GoodsReportRequest request);

    @PostMapping("/ares/${application.ares.version}/goods/queryStoreCateReport")
    GoodsReportPageView queryStoreCateReport(@RequestBody @Valid GoodsReportRequest request);

}
