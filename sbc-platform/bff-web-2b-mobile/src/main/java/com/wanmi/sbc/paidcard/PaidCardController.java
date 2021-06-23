package com.wanmi.sbc.paidcard;

import com.wanmi.sbc.common.annotation.MultiSubmitWithToken;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.paidcard.PaidCardQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardbuyrecord.PaidCardBuyRecordQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcard.CustomerPaidCardQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcard.PaidCardBuyRequest;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardBuyResponse;
import com.wanmi.sbc.customer.api.response.paidcardcustomerrel.PaidCardCustomerRelListResponse;
import com.wanmi.sbc.customer.bean.enums.AccessType;
import com.wanmi.sbc.customer.bean.enums.EffectStatusEnum;
import com.wanmi.sbc.customer.bean.vo.PaidCardBuyRecordVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Api(tags = "PaidCardController", description = "mobile 查询缓存的html页面信息bff")
@RestController
@RequestMapping("/paidCard")
public class PaidCardController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PaidCardQueryProvider paidCardQueryProvider;

    @Autowired
    private PaidCardBuyRecordQueryProvider paidCardBuyRecordQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @ApiOperation(value = "会员开通页面数据接口")
    @PostMapping(value = "/query-card-info")
    public BaseResponse queryCardInfo(@RequestBody CustomerPaidCardQueryRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        BaseResponse response = paidCardQueryProvider.queryCardInfo(request);
        return response;
    }


    @ApiOperation(value = "C端查询购买记录")
    @PostMapping(value = "/query-buy-record")
    public BaseResponse queryBuyRecord(@RequestBody PaidCardBuyRecordListRequest request) {
        request.setCustomerId(commonUtil.getOperatorId());
        request.setSortColumn("createTime");
        List<PaidCardBuyRecordVO> paidCardBuyRecordVOList = paidCardBuyRecordQueryProvider.list(request).getContext().getPaidCardBuyRecordVOList();
        // 查询当前用户所有的付费卡实例
        List<PaidCardCustomerRelVO> paidCardCustomerRelVOList =
                this.paidCardCustomerRelQueryProvider.list(PaidCardCustomerRelListRequest
                        .builder()
                        .delFlag(DeleteFlag.NO)
                        .customerId(request.getCustomerId())
                        .build())
                        .getContext()
                        .getPaidCardCustomerRelVOList();
        List<String> effectRelIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getId).collect(Collectors.toList());
        //处理数据
        LocalDateTime now = LocalDateTime.now();
        paidCardBuyRecordVOList.forEach(paidCardBuyRecordVO->{
            String customerPaidcardId = paidCardBuyRecordVO.getCustomerPaidcardId();
            if(!effectRelIdList.contains(customerPaidcardId) ){
                paidCardBuyRecordVO.setEffectStatus(EffectStatusEnum.CLOSED);
            }else if(paidCardBuyRecordVO.getInvalidTime().isBefore(now)){
                paidCardBuyRecordVO.setEffectStatus(EffectStatusEnum.INVALID);
            }else if(paidCardBuyRecordVO.getBeginTime().isAfter(now)){
                paidCardBuyRecordVO.setEffectStatus(EffectStatusEnum.NOEFFECT);
            }else{
                PaidCardCustomerRelVO paidCardCustomerRelVO
                        = paidCardCustomerRelVOList.stream().filter(rel -> rel.getId().equals(customerPaidcardId)).findFirst().orElse(null);
                if(Objects.isNull(paidCardCustomerRelVO)){
                    paidCardBuyRecordVO.setEffectStatus(EffectStatusEnum.INVALID);
                }
            }
        });
        return BaseResponse.success(paidCardBuyRecordVOList);
    }

    @ApiOperation(value = "确认购买")
    @MultiSubmitWithToken
    @PostMapping(value = "/commit")
    public BaseResponse<PaidCardBuyResponse> commit(@RequestBody @Valid PaidCardBuyRequest req){
        req.setCustomerId(commonUtil.getOperatorId());
        req.setCustomer(commonUtil.getCustomer());
        BaseResponse<PaidCardBuyResponse> response = this.paidCardQueryProvider.commit(req);
        return response;
    }

    @ApiOperation(value = "查询用户已开通的付费卡信息")
    @GetMapping(value = "/getHave")
    public BaseResponse<PaidCardCustomerRelListResponse> getHave(){
        List<PaidCardCustomerRelVO> list = paidCardCustomerRelQueryProvider.listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
                .customerId(commonUtil.getOperatorId())
                .delFlag(DeleteFlag.NO)
                .endTimeBegin(LocalDateTime.now())
                .build()).getContext();
        if(CollectionUtils.isNotEmpty(list)){
            //按照折扣率和开始时间排序:折扣率大的在前面，折扣率相同的时间开始时间早的在前面
            Collections.sort(list,Comparator.comparing(PaidCardCustomerRelVO::getDelFlag) .thenComparing(o1->o1.getPaidCardVO().getDiscountRate()).thenComparing(PaidCardCustomerRelVO::getBeginTime));

//            list.sort(Comparator.comparing(PaidCardCustomerRelVO::getDelFlag)
//                    .thenComparing((o1, o2) -> {
//                        if(AccessType.BUY.equals(o1.getPaidCardVO().getAccessType())) {
//                            return 1;
//                        } else {
//                            return -1;
//                        }
//                    }).thenComparing(PaidCardCustomerRelVO::getBeginTime)
//            );
        }
        return BaseResponse.success(PaidCardCustomerRelListResponse.builder().paidCardCustomerRelVOList(list).build());
    }

}
