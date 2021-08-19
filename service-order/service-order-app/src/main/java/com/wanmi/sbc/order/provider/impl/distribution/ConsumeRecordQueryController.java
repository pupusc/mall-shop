package com.wanmi.sbc.order.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.QueryByIdListRequest;
import com.wanmi.sbc.common.base.QueryByIdRequest;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailPageRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerDetailPageResponse;
import com.wanmi.sbc.order.api.provider.distribution.ConsumeRecordQueryProvider;
import com.wanmi.sbc.order.api.request.distribution.PageByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.distribution.CountConsumeResponse;
import com.wanmi.sbc.order.bean.vo.CountCustomerConsumeVO;
import com.wanmi.sbc.order.distribution.service.ConsumeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
public class ConsumeRecordQueryController implements ConsumeRecordQueryProvider {

    @Autowired
    private ConsumeRecordService consumeRecordService;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    /**
     * 统计分销员的顾客
     *
     * @param request
     */
    @Override
    public BaseResponse<Integer> countByDistributionCustomerId(@RequestBody @Valid QueryByIdRequest request) {
        int num = consumeRecordService.countByDistributionCustomerId(request.getId());
        return BaseResponse.success(num);
    }

    /**
     * 统计累计的有效消费金额，订单数，
     */
    @Override
    public BaseResponse<CountConsumeResponse> countValidConsume(@RequestBody @Valid QueryByIdListRequest request) {
        List<CountCustomerConsumeVO> voList = consumeRecordService.countValidConsume(request.getIdList());
        //查询客户的注册时间
        this.queryRegisterTime(voList);
        CountConsumeResponse response = new CountConsumeResponse();
        response.setCountCustomerConsumeList(voList);

        return BaseResponse.success(response);
    }

    /**
     * 统计累计的消费金额，订单数，
     */
    @Override
    public BaseResponse<CountConsumeResponse> countConsume(@RequestBody @Valid QueryByIdListRequest request) {
        List<CountCustomerConsumeVO> voList = consumeRecordService.countConsume(request.getIdList());
        //查询客户的注册时间
        this.queryRegisterTime(voList);
        CountConsumeResponse response = new CountConsumeResponse();
        response.setCountCustomerConsumeList(voList);

        return BaseResponse.success(response);
    }

    /**
     * 分页，以客户id分组
     *
     * @param request
     */
    @Override
    public BaseResponse<MicroServicePage> pageByCustomerId(@RequestBody @Valid PageByCustomerIdRequest request) {
        MicroServicePage<CountCustomerConsumeVO> response = consumeRecordService.pageByCustomerId(request);
        return BaseResponse.success(response);
    }

    private List<CountCustomerConsumeVO> queryRegisterTime(List<CountCustomerConsumeVO> voList) {
        List<String> customerIds = voList.stream().map(item->item.getCustomerId()).collect(Collectors.toList());
        CustomerDetailPageRequest request = new CustomerDetailPageRequest();
        request.setCustomerIds(customerIds);
        CustomerDetailPageResponse response =customerQueryProvider.page(request).getContext();
        if(response.getTotal() == 0 ){
            return voList;
        }
        voList.forEach(vo->{
            String customerId = vo.getCustomerId();
            response.getDetailResponseList().forEach(item->{
                if(customerId.equals(item.getCustomerId())){
                    vo.setRegisterTime(item.getCreateTime());
                }
            });
        });
        return voList;
    }

}
