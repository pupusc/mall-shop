package com.wanmi.sbc.goods.api.provider.distributionmatter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.distributionmatter.DistributionGoodsMatterPageRequest;
import com.wanmi.sbc.goods.api.request.distributionmatter.QueryByIdListRequest;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionByIdsResponse;
import com.wanmi.sbc.goods.api.response.distributionmatter.DistributionGoodsMatterPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.goods.name}", contextId = "DistributionGoodsMatterQueryProvider")
public interface DistributionGoodsMatterQueryProvider {

    /**
     * 分页商品素材
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/page")
    BaseResponse<DistributionGoodsMatterPageResponse> page(@RequestBody @Valid DistributionGoodsMatterPageRequest distributionGoodsMatterPageRequest);

    /**
     * 查询商品素材
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/by-ids")
    BaseResponse<DistributionByIdsResponse> queryByIds(@RequestBody @Valid QueryByIdListRequest idListRequest);

    /**
     * 分页查询商品素材
     *
     * @param distributionGoodsMatterPageNewRequest
     * @return
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/page/new")
    BaseResponse<DistributionGoodsMatterPageResponse> pageNew(@RequestBody DistributionGoodsMatterPageRequest distributionGoodsMatterPageNewRequest);

    /**
     * 分页查询商品素材
     *
     * @param distributionGoodsMatterPageNewRequest
     * @return
     */
    @PostMapping("/distribution-goods-matter/${application.goods.version}/page/new-for-boss")
    BaseResponse<DistributionGoodsMatterPageResponse> pageNewForBoss(@RequestBody DistributionGoodsMatterPageRequest distributionGoodsMatterPageNewRequest);

}
