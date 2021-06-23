package com.wanmi.sbc.elastic.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandDeleteByIdsRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsBrandUpdateRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsCateDeleteRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsCateUpdateNameRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: songhanlin
 * @Date: Created In 18:19 2020/11/30
 * @Description: TODO
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCateBrandProvider")
public interface EsCateBrandProvider {

    @PostMapping("/elastic/${application.elastic.version}/goods/cate/update")
    BaseResponse updateToEs(@RequestBody @Valid EsCateUpdateNameRequest request);


    @PostMapping("/elastic/${application.elastic.version}/goods/cate/delete")
    BaseResponse deleteCateFromEs(@RequestBody @Valid EsCateDeleteRequest request);

    @PostMapping("/elastic/${application.elastic.version}/goods/brand/update")
    BaseResponse updateBrandFromEs(@RequestBody @Valid EsBrandUpdateRequest request);

}
