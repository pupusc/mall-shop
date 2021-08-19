package com.wanmi.sbc.elastic.goods.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandPageRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsBrandSaveRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsBrandAddResponse;
import com.wanmi.sbc.elastic.goods.model.root.EsGoodsBrand;
import com.wanmi.sbc.elastic.goods.repository.EsGoodsBrandRepository;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandPageRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/10 15:15
 * @description <p> </p>
 */
@Slf4j
@Service
public class EsGoodsBrandService {

    @Autowired
    private EsGoodsBrandRepository esGoodsBrandRepository;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    public BaseResponse<EsGoodsBrandAddResponse> addGoodsBrandList(EsGoodsBrandSaveRequest request) {
        List<GoodsBrandVO> goodsBrandVOList = request.getGoodsBrandVOList();

        if (CollectionUtils.isEmpty(goodsBrandVOList)) {
            return BaseResponse.success(new EsGoodsBrandAddResponse(Collections.emptyList()));
        }

        List<EsGoodsBrand> goodsBrandList = KsBeanUtil.convertList(goodsBrandVOList, EsGoodsBrand.class);

        Iterable<EsGoodsBrand> esGoodsBrands = esGoodsBrandRepository.saveAll(goodsBrandList);

        List<EsGoodsBrand> goodsBrandArrayList = Lists.newArrayList(esGoodsBrands);

        List<GoodsBrandVO> goodsBrands = KsBeanUtil.convertList(goodsBrandArrayList, GoodsBrandVO.class);

        return BaseResponse.success(new EsGoodsBrandAddResponse(goodsBrands));
    }


    public void init(EsGoodsBrandPageRequest request) {

        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        Integer pageSize = 2000;

        GoodsBrandPageRequest queryRequest = KsBeanUtil.convert(request, GoodsBrandPageRequest.class);
        try {
            while (flg) {
                queryRequest.putSort("createTime", SortType.DESC.toValue());
                queryRequest.setPageNum(pageNum);
                queryRequest.setPageSize(pageSize);
                List<GoodsBrandVO> goodsBrandVOList = goodsBrandQueryProvider.page(queryRequest).getContext()
                        .getGoodsBrandPage().getContent();
                if (CollectionUtils.isEmpty(goodsBrandVOList)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化品牌列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<EsGoodsBrand> newInfos = KsBeanUtil.convert(goodsBrandVOList, EsGoodsBrand.class);
                    esGoodsBrandRepository.saveAll(newInfos);
                    log.info("==========ES初始化品牌列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化品牌列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }
    }
}
