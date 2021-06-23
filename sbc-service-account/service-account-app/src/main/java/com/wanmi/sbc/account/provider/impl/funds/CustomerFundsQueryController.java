package com.wanmi.sbc.account.provider.impl.funds;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.*;
import com.wanmi.sbc.account.api.response.funds.*;
import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.account.bean.vo.CustomerFundsVO;
import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelListByCustomerIdsRequest;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelListByCustomerIdsResponse;
import com.wanmi.sbc.customer.bean.vo.CustomerBaseVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 会员资金查询接口
 * @author: Geek Wang
 * @createDate: 2019/2/19 14:33
 * @version: 1.0
 */
@RestController
@Validated
public class CustomerFundsQueryController implements CustomerFundsQueryProvider {

    @Autowired
    private CustomerFundsService customerFundsService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 会员资金分页查询
     * @param request 分页查询条件 {@link CustomerFundsPageRequest}
     * @return {@link BaseResponse<CustomerFundsPageResponse>}
     */
    @Override
    public BaseResponse<CustomerFundsPageResponse> page(@RequestBody @Valid CustomerFundsPageRequest request) {
        Page<CustomerFunds> page = customerFundsService.page(request);
        List<CustomerFundsVO> customerFundsVOList = getCustomerFundsVOS(page);
        setCustomerLevelName(customerFundsVOList);

        return BaseResponse.success(new CustomerFundsPageResponse(new MicroServicePage<>(customerFundsVOList, request.getPageRequest(),  page.getTotalElements())));
    }

    /**
     * 会员资金导出查询
     * @param request 导出查询条件 {@link CustomerFundsExportRequest}
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsExportResponse> export(@RequestBody @Valid CustomerFundsExportRequest request) {
        CustomerFundsPageRequest queryReq = new CustomerFundsPageRequest();
        KsBeanUtil.copyPropertiesThird(request, queryReq);

        List<CustomerFundsVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 10; i++) {
            queryReq.setPageNum(i);
            queryReq.setPageSize(100);

            Page<CustomerFunds> page = customerFundsService.page(queryReq);
            List<CustomerFundsVO> data = getCustomerFundsVOS(page);

            setCustomerLevelName(data);

            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }
        return BaseResponse.success(new CustomerFundsExportResponse(dataRecords));
    }


    /**
     * Page转换List
     * @param page
     * @return
     */
    private List<CustomerFundsVO> getCustomerFundsVOS(Page<CustomerFunds> page) {
        return page.getContent().stream().map(customerFunds -> {
            CustomerFundsVO vo = new CustomerFundsVO();
            KsBeanUtil.copyPropertiesThird(customerFunds, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据会员资金ID查询会员资金信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsByCustomerFundsIdResponse> getByCustomerFundsId(@RequestBody @Valid CustomerFundsByCustomerFundsIdRequest request){
        CustomerFunds customerFunds =  customerFundsService.findByCustomerFundsId(request.getCustomerFundsId());
        if (Objects.isNull(customerFunds)){
            return BaseResponse.success(new CustomerFundsByCustomerFundsIdResponse());
        }
        CustomerFundsByCustomerFundsIdResponse response = new CustomerFundsByCustomerFundsIdResponse();
        KsBeanUtil.copyPropertiesThird(customerFunds,response);
        return BaseResponse.success(response);
    }

    /**
     * 根据会员资金ID查询会员资金信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsByCustomerFundsIdForEsResponse> getByCustomerFundsIdForEs(@RequestBody @Valid CustomerFundsByCustomerFundsIdRequest request){
        CustomerFunds customerFunds =  customerFundsService.findByCustomerFundsId(request.getCustomerFundsId());
        if (Objects.isNull(customerFunds)){
            return BaseResponse.success(new CustomerFundsByCustomerFundsIdForEsResponse());
        }
        CustomerFundsByCustomerFundsIdForEsResponse response = new CustomerFundsByCustomerFundsIdForEsResponse();
        KsBeanUtil.copyPropertiesThird(customerFunds,response);
        return BaseResponse.success(response);
    }


    /**
     * 根据会员ID查询会员资金信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsByCustomerIdResponse> getByCustomerId(@RequestBody @Valid CustomerFundsByCustomerIdRequest request){
        CustomerFunds customerFunds =  customerFundsService.findByCustomerId(request.getCustomerId());
        if (Objects.isNull(customerFunds)){
            return BaseResponse.success(CustomerFundsByCustomerIdResponse.builder().accountBalance(BigDecimal
                    .ZERO).blockedBalance(BigDecimal.ZERO).withdrawAmount(BigDecimal.ZERO).amountReceived(BigDecimal
                    .ZERO).expenditure(0L).income(0L).amountPaid(BigDecimal.ZERO).build());

        }
        CustomerFundsByCustomerIdResponse response = KsBeanUtil.convert(customerFunds,CustomerFundsByCustomerIdResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 根据会员ID查询会员资金信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsByCustomerIdForEsResponse> getByCustomerIdForEs(@RequestBody @Valid CustomerFundsByCustomerIdRequest request){
        CustomerFunds customerFunds =  customerFundsService.findByCustomerId(request.getCustomerId());
        if (Objects.isNull(customerFunds)){
            return BaseResponse.success(null);
        }
        CustomerFundsByCustomerIdForEsResponse response = KsBeanUtil.convert(customerFunds,CustomerFundsByCustomerIdForEsResponse.class);
        return BaseResponse.success(response);
    }

    /**
     * 根据会员ID列表查询会员资金信息
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CustomerFundsListResponse> getByCustomerIdListForEs(@RequestBody @Valid CustomerFundsByCustomerIdListRequest request) {
        List<CustomerFunds> customerFundsList = customerFundsService.findListByCustomerIds(request.getCustomerIds());
        if (CollectionUtils.isEmpty(customerFundsList)) {
            return BaseResponse.success(null);
        }
        List<CustomerFundsForEsVO> list =
                customerFundsList.stream().map(customerFunds -> {
                    CustomerFundsForEsVO vo = new CustomerFundsForEsVO();
                    KsBeanUtil.copyPropertiesThird(customerFunds, vo);
                    return vo;
                }).collect(Collectors.toList());
        return BaseResponse.success(new CustomerFundsListResponse(list));
    }

    private void setCustomerLevelName(List<CustomerFundsVO> customerFundsVOList ){
        List<String> customerIdList = customerFundsVOList.stream().map(CustomerFundsVO::getCustomerId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(customerIdList)){
            BaseResponse<CustomerLevelListByCustomerIdsResponse> baseResponse = customerLevelQueryProvider.listByCustomerIds(new CustomerLevelListByCustomerIdsRequest(customerIdList));
            List<CustomerBaseVO> customerLevelVOList =  baseResponse.getContext().getCustomerLevelVOList();
            if (CollectionUtils.isNotEmpty(customerLevelVOList)){
                Map<String,String> map = customerLevelVOList.stream().filter(customerBaseVO -> StringUtils.isNotBlank(customerBaseVO.getCustomerId())).collect(Collectors.toMap(CustomerBaseVO::getCustomerId,
                        (customerBaseVO) -> StringUtils.isNotBlank(customerBaseVO.getCustomerLevelName()) ? customerBaseVO.getCustomerLevelName() : StringUtils.EMPTY));
                if(MapUtils.isNotEmpty(map)) {
                    customerFundsVOList.forEach(customerFundsVO -> {
                        String levelName = map.get(customerFundsVO.getCustomerId());
                        if(StringUtils.isNotBlank(levelName)) {
                            customerFundsVO.setCustomerLevelName(levelName);
                        }
                    });
                }
            }
        }
    }


    @Override
    public BaseResponse<CustomerFundsListResponse> initEsPage(@RequestBody @Valid CustomerFundsPageRequest request){
        return BaseResponse.success(new CustomerFundsListResponse((customerFundsService.initEsPage(request))));
    }
}
