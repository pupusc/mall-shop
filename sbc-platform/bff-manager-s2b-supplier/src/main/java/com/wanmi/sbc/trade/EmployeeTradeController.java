package com.wanmi.sbc.trade;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreInfoByIdRequest;
import com.wanmi.sbc.customer.api.response.store.StoreInfoResponse;
import com.wanmi.sbc.customer.bean.dto.CompanyInfoDTO;
import com.wanmi.sbc.customer.bean.dto.StoreInfoDTO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeAddBatchRequest;
import com.wanmi.sbc.order.api.request.trade.TradeWrapperBackendCommitRequest;
import com.wanmi.sbc.order.bean.dto.TradeAddDTO;
import com.wanmi.sbc.order.bean.dto.TradeCreateDTO;
import com.wanmi.sbc.order.bean.vo.TradeCommitResultVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * <p>业务员助手订单controller</p>
 * Created by of628-wenzhi on 2018-04-13-下午2:51.
 */
@Api(tags = "EmployeeTradeController", description = "业务员助手订单服务API")
@RequestMapping("/trade/manager")
@RestController
@Validated
public class EmployeeTradeController {

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Resource
    private StoreQueryProvider storeQueryProvider;

    @Resource
    private TradeProvider tradeProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    /**
     *
     * 描述：    创建订单
     * 场景：    用于业务员助手后台下单
     * @param:  请求参数 {@link TradeCreateDTO}
     * @return: 返回结果 {@link TradeCommitResultVO}
     *
     */
    @ApiOperation(value = "创建订单")
    @RequestMapping(value = "/create", method = RequestMethod.PUT)
    @MultiSubmit
    @GlobalTransactional
    public BaseResponse<TradeCommitResultVO> create(@Valid @RequestBody TradeCreateDTO tradeCreateRequest) {
        Operator operator = commonUtil.getOperator();
        CompanyInfoVO companyInfo = companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(commonUtil.getCompanyInfoId()).build()
        ).getContext();
        Long storeId = commonUtil.getStoreId();
        StoreInfoResponse storeInfoResponse = storeQueryProvider.getStoreInfoById(new StoreInfoByIdRequest(storeId)).getContext();

        //1.校验与包装订单信息-与商家代客下单公用
        TradeVO trade = tradeQueryProvider.wrapperBackendCommit(TradeWrapperBackendCommitRequest.builder().operator(operator)
                .companyInfo(KsBeanUtil.convert(companyInfo, CompanyInfoDTO.class)).storeInfo(KsBeanUtil.convert(storeInfoResponse, StoreInfoDTO.class))
                .tradeCreate(tradeCreateRequest).build()).getContext().getTradeVO();
        //2.订单入库(转换成list,传入批量创建订单的service方法,同一套逻辑,能够回滚)
        TradeAddBatchRequest tradeAddBatchRequest = TradeAddBatchRequest.builder()
                .tradeDTOList(KsBeanUtil.convert(Collections.singletonList(trade), TradeAddDTO.class))
                .operator(operator)
                .build();
        List<TradeCommitResultVO> result = tradeProvider.addBatch(tradeAddBatchRequest).getContext().getTradeCommitResultVOS();
        return BaseResponse.success(result.get(0));
    }
}
