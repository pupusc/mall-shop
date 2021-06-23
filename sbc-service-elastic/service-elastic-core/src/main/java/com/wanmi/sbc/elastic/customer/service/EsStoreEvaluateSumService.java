package com.wanmi.sbc.elastic.customer.service;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluatesum.StoreEvaluateSumQueryProvider;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumPageRequest;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import com.wanmi.sbc.elastic.api.request.customer.EsStoreEvaluateSumPageRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsStoreEvaluateSumPageResponse;
import com.wanmi.sbc.elastic.customer.model.root.StoreEvaluateSum;
import com.wanmi.sbc.elastic.customer.repository.EsStoreEvaluateSumRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author: HouShuai
 * @date: 2020/12/3 18:59
 * @description: 商家评价
 */
@Slf4j
@Service
public class EsStoreEvaluateSumService {

    @Autowired
    private EsStoreEvaluateSumRepository storeEvaluateSumRepository;

    @Autowired
    private StoreEvaluateSumQueryProvider storeEvaluateSumQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public BaseResponse<EsStoreEvaluateSumPageResponse> page(EsStoreEvaluateSumPageRequest queryRequest) {

        NativeSearchQuery searchQuery = queryRequest.esCriteria();

        Page<StoreEvaluateSum> page = storeEvaluateSumRepository.search(searchQuery);
        Page<StoreEvaluateSumVO> newPage = page.map(entity -> {
            StoreEvaluateSumVO response = new StoreEvaluateSumVO();
            BeanUtils.copyProperties(entity, response);
            return response;
        });
        MicroServicePage<StoreEvaluateSumVO> microPage = new MicroServicePage<>(newPage, queryRequest.getPageable());
        EsStoreEvaluateSumPageResponse finalRes = new EsStoreEvaluateSumPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }


    /**
     * 初始化商家评价
     * @param request
     */
    public void init(EsStoreEvaluateSumPageRequest request) {
        //手动删除索引时，重新设置mapping
        createIndexAddMapping();
        Boolean flg = Boolean.TRUE;
        int pageNum = request.getPageNum();
        int pageSize = 2000;
        StoreEvaluateSumPageRequest queryRequest = KsBeanUtil.convert(request, StoreEvaluateSumPageRequest.class);
        try {
            while (flg) {
                queryRequest.setPageNum(pageNum);
                queryRequest.setPageSize(pageSize);
                List<StoreEvaluateSumVO> storeEvaluateSumVOList = storeEvaluateSumQueryProvider.page(queryRequest).getContext().getStoreEvaluateSumVOPage().getContent();
                if (CollectionUtils.isEmpty(storeEvaluateSumVOList)) {
                    flg = Boolean.FALSE;
                    log.info("==========ES初始化商家评价列表，结束pageNum:{}==============", pageNum);
                } else {
                    List<StoreEvaluateSum> storeEvaluateSumList = KsBeanUtil.convert(storeEvaluateSumVOList, StoreEvaluateSum.class);
                    storeEvaluateSumRepository.saveAll(storeEvaluateSumList);
                    log.info("==========ES初始化商家评价列表成功，当前pageNum:{}==============", pageNum);
                    pageNum++;
                }
            }
        } catch (Exception e) {
            log.info("==========ES初始化商家评价列表异常，异常pageNum:{}==============", pageNum);
            throw new SbcRuntimeException("K-120011", new Object[]{pageNum});
        }
    }

    /**
     * 创建索引以及mapping
     */
    private void createIndexAddMapping() {
        //手动删除索引时，重新设置mapping
        if (!elasticsearchTemplate.indexExists(StoreEvaluateSum.class)) {
            elasticsearchTemplate.createIndex(StoreEvaluateSum.class);
            elasticsearchTemplate.putMapping(StoreEvaluateSum.class);
        }
    }
}
