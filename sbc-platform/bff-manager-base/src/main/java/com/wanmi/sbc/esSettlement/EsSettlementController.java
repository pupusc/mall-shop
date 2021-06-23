package com.wanmi.sbc.esSettlement;

import com.wanmi.sbc.account.api.provider.finance.record.SettlementQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.SettlementPageRequest;
import com.wanmi.sbc.account.api.response.finance.record.SettlementListResponse;
import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.response.store.EsStoreInfoResponse;
import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementProvider;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementListRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsInitStoreInfoRequest;
import com.wanmi.sbc.elastic.bean.dto.settlement.EsSettlementDTO;
import com.wanmi.sbc.goods.bean.vo.MarketingLabelVO;
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
@Api(tags = "EsSettlementController", description = "es店铺初始化 API")
@RestController("EsSettlementController")
@RequestMapping("/esSettlement")
public class EsSettlementController {

    private static final Logger logger = LoggerFactory.getLogger(EsSettlementController.class);



    @Autowired
    private EsSettlementProvider esSettlementProvider;

    @Autowired
    private SettlementQueryProvider settlementQueryProvider;

    /**
     * 初始化结算单
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "初始化结算单")
    @RequestMapping(value = "initSettlement", method = RequestMethod.POST)
    public BaseResponse<SettlementListResponse> page(@RequestBody @Valid SettlementPageRequest
                                                                         pageRequest) {
        pageRequest.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
        int pageNum = pageRequest.getPageNum();
        while (true){
            logger.info(String.format("初始化结算单第[%s]页 ", pageNum));
            pageRequest.setPageNum(pageNum);
            BaseResponse<SettlementListResponse> response =  settlementQueryProvider.initEsPage(pageRequest);
            if(Objects.nonNull(response.getContext())){
                List<SettlementViewVO> lists = response.getContext().getLists();
                if(CollectionUtils.isNotEmpty(lists)){
                    esSettlementProvider.initSettlementList(EsSettlementListRequest.builder().lists(
                            lists.stream().map(settlementViewVo -> {
                                EsSettlementDTO esSettlementDTO = KsBeanUtil.convert(settlementViewVo, EsSettlementDTO.class);
                                return esSettlementDTO;
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
     * 查看初始化结算单
     *
     * @param pageRequest
     * @return BaseResponse
     */
    @ApiOperation(value = "查看初始化结算单")
    @RequestMapping(value = "querySettlement", method = RequestMethod.POST)
    public BaseResponse<SettlementListResponse> querySettlement(@RequestBody @Valid SettlementPageRequest pageRequest) {
        pageRequest.setPageSize(pageRequest.getPageSize() != null ? pageRequest.getPageSize() : 2000);
            BaseResponse<SettlementListResponse> response =  settlementQueryProvider.initEsPage(pageRequest);
            if(Objects.nonNull(response.getContext())){
                List<SettlementViewVO> lists = response.getContext().getLists();
                if(CollectionUtils.isNotEmpty(lists)){
                    esSettlementProvider.initSettlementList(EsSettlementListRequest.builder().lists(
                            lists.stream().map(settlementViewVo -> {
                                EsSettlementDTO esSettlementDTO = KsBeanUtil.convert(settlementViewVo, EsSettlementDTO.class);
                                return esSettlementDTO;
                            }).collect(Collectors.toList())
                    ).build());
                }
        }
        return BaseResponse.success(response.getContext());
    }
}