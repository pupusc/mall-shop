package com.wanmi.sbc.esCustomerFunds;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsPageRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsListResponse;
import com.wanmi.sbc.account.bean.vo.CustomerFundsForEsVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.customerFunds.EsCustomerFundsProvider;
import com.wanmi.sbc.elastic.api.request.customerFunds.EsCustomerFundsListRequest;
import com.wanmi.sbc.elastic.bean.dto.customerFunds.EsCustomerFundsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author yangzhen
 * @Description //es结算账号初始化
 * @Date 19:51 2020/12/9
 * @Param
 * @return
 **/
@Api(tags = "EsCustomerFundsController", description = "es会员资金 API")
@RestController("EsCustomerFundsController")
@RequestMapping("/esCustomerFunds")
public class EsCustomerFundsController {

    private static final Logger logger = LoggerFactory.getLogger(EsCustomerFundsController.class);




    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private EsCustomerFundsProvider esCustomerFundsProvider;

    /**
     * 初始化会员资金
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "初始化会员资金")
    @RequestMapping(value = "initCustomerFunds", method = RequestMethod.POST)
    public BaseResponse<CustomerFundsListResponse> page(@RequestBody @Valid CustomerFundsPageRequest
                                                                         pageRequest) {
        pageRequest.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
        int pageNum = pageRequest.getPageNum();
        while (true){
            logger.info(String.format("初始化会员资金第[%s]页 ", pageNum));
            pageRequest.setPageNum(pageNum);
            BaseResponse<CustomerFundsListResponse> response =  customerFundsQueryProvider.initEsPage(pageRequest);
            if(Objects.nonNull(response.getContext())){
                List<CustomerFundsForEsVO> lists = response.getContext().getLists();
                if(CollectionUtils.isNotEmpty(lists)){
                    esCustomerFundsProvider.initCustomerFundsList(EsCustomerFundsListRequest.builder().esCustomerFundsDTOS(
                            lists.stream().map(customerFundsVO -> {
                                EsCustomerFundsDTO esCustomerFundsDTO = KsBeanUtil.convert(customerFundsVO, EsCustomerFundsDTO.class);
                                return esCustomerFundsDTO;
                            }).collect(Collectors.toList())
                    ).build());
                    pageNum = pageNum + 1;
                }else{
                    break;
                }
            }else{
                break;
            }
        }

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 查看初始化会员资金
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "查看初始化会员资金")
    @RequestMapping(value = "queryCustomerFunds", method = RequestMethod.POST)
    public BaseResponse<CustomerFundsListResponse> queryCustomerFunds(@RequestBody @Valid CustomerFundsPageRequest pageRequest) {
        pageRequest.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
        BaseResponse<CustomerFundsListResponse> response = customerFundsQueryProvider.initEsPage(pageRequest);
        if (Objects.nonNull(response.getContext())) {
            List<CustomerFundsForEsVO> lists = response.getContext().getLists();
            if (CollectionUtils.isNotEmpty(lists)) {
                esCustomerFundsProvider.initCustomerFundsList(EsCustomerFundsListRequest.builder().esCustomerFundsDTOS(
                        lists.stream().map(customerFundsVO -> {
                            EsCustomerFundsDTO esCustomerFundsDTO = KsBeanUtil.convert(customerFundsVO, EsCustomerFundsDTO.class);
                            return esCustomerFundsDTO;
                        }).collect(Collectors.toList())
                ).build());
            }

        }
        return BaseResponse.success(response.getContext());
    }
}