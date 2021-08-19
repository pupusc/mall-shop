package com.wanmi.sbc.crm.provider.impl.rfmstatistic;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.rfmstatistic.RfmScoreStatisticQueryProvider;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmCustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.crm.api.request.rfmstatistic.RfmScoreStatisticRequest;
import com.wanmi.sbc.crm.api.response.rfmgroupstatistics.RefmCustomerDetailByCustomerIdResponse;
import com.wanmi.sbc.crm.bean.vo.RfmStatisticVo;
import com.wanmi.sbc.crm.rfmstatistic.model.RfmCustomerDetail;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmCustomerDetailService;
import com.wanmi.sbc.crm.rfmstatistic.service.RfmScoreStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-16
 * \* Time: 15:13
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
public class RfmScoreStatisticController implements RfmScoreStatisticQueryProvider {

    @Autowired
    private RfmScoreStatisticService rfmScoreStatisticService;
    @Autowired
    private RfmCustomerDetailService rfmcustomerDetailService;

    @Override
    public BaseResponse list(@RequestBody RfmScoreStatisticRequest request) {
        List<RfmStatisticVo> list = this.rfmScoreStatisticService.queryScoreList(request);
        return BaseResponse.success(list);
    }

    @Override
    public BaseResponse customerInfo(@RequestBody RfmScoreStatisticRequest request){
        Map<String,Object> map = this.rfmcustomerDetailService.queryCustomerDetail(request.getCustomerId());

        return BaseResponse.success(map);
    }

    @Override
    public BaseResponse<RefmCustomerDetailByCustomerIdResponse> customerDetail(@RequestBody @Valid
                                                                                       RfmCustomerDetailByCustomerIdRequest
                                                                                       request) {
        RfmCustomerDetail detail = this.rfmcustomerDetailService.getCustomerDetailByCustomerId(request.getCustomerId());
        if(Objects.isNull(detail)){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.success(KsBeanUtil.convert(detail, RefmCustomerDetailByCustomerIdResponse.class));
    }
}
