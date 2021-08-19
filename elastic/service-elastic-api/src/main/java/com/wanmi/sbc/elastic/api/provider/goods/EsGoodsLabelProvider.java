package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateNameRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateSortRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsLabelUpdateVisibleRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGoodsLabelProvider")
public interface EsGoodsLabelProvider {

    @PostMapping("/elastic/${application.elastic.version}/goods/label/update/name")
    BaseResponse updateLabelName(@RequestBody @Valid EsGoodsLabelUpdateNameRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/label/update/visible")
    BaseResponse updateLabelVisible(@RequestBody @Valid EsGoodsLabelUpdateVisibleRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/label/sort/visible")
    BaseResponse updateGoodsLabelSort(@RequestBody @Valid EsGoodsLabelUpdateSortRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/label/delete/by/ids")
    BaseResponse deleteSomeLabel(@RequestBody @Valid EsGoodsLabelDeleteByIdsRequest request);

}
