package com.wanmi.sbc.elastic.goods.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsBrandPageResponse;
import com.wanmi.sbc.elastic.goods.model.root.EsGoodsBrand;
import com.wanmi.sbc.elastic.goods.repository.EsGoodsBrandRepository;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/10 14:44
 * @description <p> </p>
 */
@Service
public class EsGoodsBrandQueryService {

    @Autowired
    private EsGoodsBrandRepository esGoodsBrandRepository;

    public BaseResponse<EsGoodsBrandPageResponse> page(EsGoodsBrandPageRequest request) {
        NativeSearchQuery searchQuery = request.esCriteria();
        Page<EsGoodsBrand> esGoodsBrandPage = esGoodsBrandRepository.search(searchQuery);
        Page<GoodsBrandVO> newPage = esGoodsBrandPage.map(entity -> {
            GoodsBrandVO goodsBrandVO = new GoodsBrandVO();
            BeanUtils.copyProperties(entity, goodsBrandVO);
            return goodsBrandVO;
        });

        MicroServicePage<GoodsBrandVO> microServicePage = new MicroServicePage<>(newPage, request.getPageable());
        EsGoodsBrandPageResponse response = EsGoodsBrandPageResponse.builder().goodsBrandPage(microServicePage).build();
        return BaseResponse.success(response);
    }

}
