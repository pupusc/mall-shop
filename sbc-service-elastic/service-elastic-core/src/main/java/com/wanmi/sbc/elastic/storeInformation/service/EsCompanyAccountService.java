package com.wanmi.sbc.elastic.storeInformation.service;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyPageRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.StoreInfoQueryPageRequest;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyInfoResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsListStoreByNameForAutoCompleteResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsSearchInfoResponse;
import com.wanmi.sbc.elastic.bean.vo.companyAccount.EsCompanyAccountVO;
import com.wanmi.sbc.elastic.bean.vo.storeInformation.EsCompanyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsMapper;
import org.springframework.stereotype.Service;

/**
 * @Author yangzhen
 * @Description //商家结算账户查询
 * @Date 9:44 2020/12/8
 * @Param
 * @return
 **/
@Slf4j
@Service
public class EsCompanyAccountService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ResultsMapper resultsMapper;

    /**
     * 分页查找商家结算账号
     *
     * @param queryRequest
     * @return
     */
    public EsCompanyAccountResponse companyAccountPage(EsCompanyAccountQueryRequest queryRequest) {
        EsCompanyAccountResponse response = new EsCompanyAccountResponse();
        EsSearchInfoResponse<EsCompanyAccountVO> page = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchInfoResponse.build(searchResponse, resultsMapper, EsCompanyAccountVO.class));
        response.setEsCompanyAccountPage(new MicroServicePage<>(page.getData(),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), page.getTotal()));
        return response;


    }

    /**
     * 分页查找商家结算账号
     *
     * @param queryRequest
     * @return
     */
    public EsCompanyInfoResponse companyInfoPage(EsCompanyPageRequest queryRequest) {
        EsCompanyInfoResponse response = new EsCompanyInfoResponse();
        EsSearchInfoResponse<EsCompanyInfoVO> page = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchInfoResponse.build(searchResponse, resultsMapper, EsCompanyInfoVO.class));
        response.setEsCompanyAccountPage(new MicroServicePage<>(page.getData(),
                PageRequest.of(queryRequest.getPageNum(), queryRequest.getPageSize()), page.getTotal()));
        return response;

    }


    /**
     * 根据店铺名称或者类型自动填充店铺下拉选项，默认5条
     */
    public EsListStoreByNameForAutoCompleteResponse queryStoreByNameAndStoreTypeForAutoComplete(StoreInfoQueryPageRequest queryRequest){
        queryRequest.setPageSize(5);
        EsListStoreByNameForAutoCompleteResponse response = new EsListStoreByNameForAutoCompleteResponse();
        EsSearchInfoResponse<StoreVO> page = elasticsearchTemplate.query(queryRequest.getSearchCriteria(),
                searchResponse -> EsSearchInfoResponse.build(searchResponse, resultsMapper, StoreVO.class));
        response.setStoreVOList(page.getData());
        return response;
    }


}
