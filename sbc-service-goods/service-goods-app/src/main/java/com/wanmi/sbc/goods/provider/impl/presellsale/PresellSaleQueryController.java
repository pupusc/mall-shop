package com.wanmi.sbc.goods.provider.impl.presellsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSaleByIdRequest;
import com.wanmi.sbc.goods.api.request.presellsale.PresellSalePageRequest;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSalePageResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import com.wanmi.sbc.goods.bean.enums.PresellSaleStatus;
import com.wanmi.sbc.goods.bean.vo.PresellSaleVO;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;
import com.wanmi.sbc.goods.presellsale.request.PresellSaleQueryRequest;
import com.wanmi.sbc.goods.presellsale.response.PresellSaleQueryResponse;
import com.wanmi.sbc.goods.presellsale.response.TotalNum;
import com.wanmi.sbc.goods.presellsale.service.PresellSaleService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class PresellSaleQueryController implements PresellSaleQueryProvider {

    @Autowired
    private PresellSaleService presellSaleService;

    /**
     * @param request 包含预售活动id查询请求结构 {@link PresellSaleByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<PresellSaleResponse> presellSaleById(@Valid PresellSaleByIdRequest request) {
        PresellSaleResponse response = presellSaleService.findPresellSaleById(request.getPresellSaleId());
        return BaseResponse.success(response);

    }

    @Override
    public BaseResponse page( PresellSalePageRequest request) {
        PresellSaleQueryRequest presellSaleQueryRequest = KsBeanUtil.convert(request, PresellSaleQueryRequest.class);
        PresellSaleQueryResponse page = presellSaleService.page(presellSaleQueryRequest);
        Page<PresellSale> presellSalesPage = page.getPresellSalesPage();
        Map<String, TotalNum> countNumList = page.getCountNumList();
        MicroServicePage<PresellSaleVO> microServicePage = new MicroServicePage<>();
        if(Objects.nonNull(presellSalesPage) && CollectionUtils.isNotEmpty(presellSalesPage.getContent())) {
            microServicePage = KsBeanUtil.convertPage(presellSalesPage,PresellSaleVO.class);

            for (PresellSaleVO presellSaleVO : microServicePage) {

                Integer suspended = presellSaleVO.getSuspended();
                if(!suspended.equals(Integer.valueOf(1))){
                    //判断活动状态
                    LocalDateTime startTime = presellSaleVO.getStartTime();
                    LocalDateTime endTime = presellSaleVO.getEndTime();
                    LocalDateTime now = LocalDateTime.now();
                    if(startTime.isAfter(now)){
                        presellSaleVO.setPresellSaleStatus(Integer.valueOf(3));
                    }
                    if(endTime.isBefore(now)){
                        presellSaleVO.setPresellSaleStatus(Integer.valueOf(4));
                    }
                    else{
                        presellSaleVO.setPresellSaleStatus(Integer.valueOf(1));

                    }
                }else{
                    presellSaleVO.setPresellSaleStatus(Integer.valueOf(2));
                }

                TotalNum totalNum = countNumList.get(presellSaleVO.getId());
                if(totalNum!=null){
                    presellSaleVO.setHandselTotalNum(totalNum.getHandselTotalNum());
                    presellSaleVO.setFinalPaymentNum(totalNum.getFinalPaymentNum());
                    presellSaleVO.setFullPaymentNum(totalNum.getFullPaymentNum());
                }
            }
        }
        List<PresellSaleVO> collect =new ArrayList<>();
        if(request.getQueryTab().equals(PresellSaleStatus.S_NS)){
           collect= microServicePage.stream().filter(i->!i.getPresellSaleStatus().equals(Integer.valueOf(4))).collect(Collectors.toList());
            microServicePage.setContent(collect);

        }

        return BaseResponse.success(microServicePage);
    }


}
