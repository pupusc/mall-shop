package com.wanmi.sbc.elastic.provider.impl.customer;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.elastic.api.provider.customer.EsCustomerDetailQueryProvider;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageRequest;
import com.wanmi.sbc.elastic.api.request.customer.EsCustomerDetailPageTwoRequest;
import com.wanmi.sbc.elastic.api.response.customer.EsCustomerDetailPageResponse;
import com.wanmi.sbc.elastic.bean.constant.customer.CustomerConstant;
import com.wanmi.sbc.elastic.bean.vo.customer.EsCustomerDetailVO;
import com.wanmi.sbc.elastic.customer.mapper.EsCustomerDetailMapper;
import com.wanmi.sbc.elastic.customer.model.root.EsCustomerDetail;
import com.wanmi.sbc.elastic.customer.service.EsCustomerDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@Validated
public class EsCustomerDetailQueryController implements EsCustomerDetailQueryProvider {

    @Autowired
    private EsCustomerDetailService esCustomerDetailService;

    @Autowired
    private EsCustomerDetailMapper esCustomerDetailMapper;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Override
    public BaseResponse<EsCustomerDetailPageResponse> page(@Valid EsCustomerDetailPageRequest request) {
        PaidCardCustomerRelQueryRequest req = new PaidCardCustomerRelQueryRequest();
        req.setDelFlag(DeleteFlag.NO);
        //构建es侧搜索会员信息的查询条件
/*        if(CustomerConstant.COMMON_CUSTOMER_TYPE.equals(request.getPaidCardKeyword())){
            // 说明是查询非会员
            request.setSearchType(CustomerConstant.CUSTOMER_SEARCH_TYPE_EXCLUDE);
        }else if(!CustomerConstant.ALL_CUSTOMER_TYPE.equals(request.getPaidCardKeyword())){
            //说明要根据付费卡ID进行过滤
            request.setSearchType(CustomerConstant.CUSTOMER_SEARCH_TYPE_INCLUDE);
            req.setPaidCardId(request.getPaidCardKeyword());
        }*/

        // 只要不是查询全部会员就都要去加载付费会员卡信息
/*        if(Objects.nonNull(request.getPaidCardKeyword()) &&
                !CustomerConstant.ALL_CUSTOMER_TYPE.equals(request.getPaidCardKeyword())){
            BaseResponse<List<PaidCardCustomerRelVO>> response =
                    paidCardCustomerRelQueryProvider.getRelInfo(req);
            List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = response.getContext();
            if(CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)){
                List<String> customerIdList =
                        paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getCustomerId).distinct().collect(Collectors.toList());
                request.setCustomerIdList(customerIdList);
            }
        }*/


        Page<EsCustomerDetail> page = esCustomerDetailService.page(request);
        List<EsCustomerDetail> esCustomerDetailList = page.getContent();
        if (CollectionUtils.isEmpty(esCustomerDetailList)){
           return BaseResponse.success(new EsCustomerDetailPageResponse(Lists.newArrayList(),page.getTotalElements(),request.getPageNum()));
        }

        // 查询是否是付费会员
        List<String> customerIdList = esCustomerDetailList.stream().map(EsCustomerDetail::getCustomerId).distinct().collect(Collectors.toList());
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList
                = this.paidCardCustomerRelQueryProvider.list(PaidCardCustomerRelListRequest.builder()
                .customerIdList(customerIdList)
                .delFlag(DeleteFlag.NO)
                .build())
                .getContext().getPaidCardCustomerRelVOList();
        List<String> paidCustomerIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getCustomerId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(paidCustomerIdList)){
            esCustomerDetailList.stream().filter(esCustomerDetail->paidCustomerIdList.contains(esCustomerDetail.getCustomerId())).forEach(esCustomerDetail->{
                // 设置为是付费会员
                esCustomerDetail.setIsPaidCardCustomer(1);
            });
        }


        List<EsCustomerDetailVO> esCustomerDetailVOList = esCustomerDetailMapper.customerDetailToEsCustomerDetailVO(esCustomerDetailList);
        esCustomerDetailService.wrapperEsCustomerDetailVO(esCustomerDetailVOList,request.getCompanyInfoId());
        esCustomerDetailService.fillArea(esCustomerDetailVOList);
        return BaseResponse.success(new EsCustomerDetailPageResponse(esCustomerDetailVOList,page.getTotalElements(),request.getPageNum()));
    }

    @Override
    public BaseResponse<EsCustomerDetailPageResponse> pageForEnterpriseCustomer(@RequestBody @Valid EsCustomerDetailPageTwoRequest request) {
        Page<EsCustomerDetail> page = esCustomerDetailService.pageForEnterpriseCustomer(request);
        List<EsCustomerDetail> esCustomerDetailList = page.getContent();
        if (CollectionUtils.isEmpty(esCustomerDetailList)){
            return BaseResponse.success(new EsCustomerDetailPageResponse(Lists.newArrayList(),page.getTotalElements(),request.getPageNum()));
        }
        List<EsCustomerDetailVO>  esCustomerDetailVOList = esCustomerDetailService.wrapperEsCustomerDetailVO(esCustomerDetailList,request);
        if (request.getFillArea()) {
            esCustomerDetailService.fillArea(esCustomerDetailVOList);
        }
        return BaseResponse.success(new EsCustomerDetailPageResponse(esCustomerDetailVOList,page.getTotalElements(),request.getPageNum()));
    }
}
