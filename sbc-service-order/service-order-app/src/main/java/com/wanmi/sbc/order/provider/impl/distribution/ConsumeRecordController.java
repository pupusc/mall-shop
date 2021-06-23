package com.wanmi.sbc.order.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.distribution.ConsumeRecordProvider;
import com.wanmi.sbc.order.api.request.distribution.ConsumeRecordAddRequest;
import com.wanmi.sbc.order.api.request.distribution.ConsumeRecordModifyRequest;
import com.wanmi.sbc.order.api.response.distribution.ConsumeRecordAddResponse;
import com.wanmi.sbc.order.api.response.distribution.ConsumeRecordModifyResponse;
import com.wanmi.sbc.order.bean.vo.ConsumeRecordVo;
import com.wanmi.sbc.order.distribution.model.root.ConsumeRecord;
import com.wanmi.sbc.order.distribution.service.ConsumeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Description: 消费记录控制器
 * @Autho qiaokang
 * @Date：2019-03-05 19:15:25
 */
@Validated
@RestController
public class ConsumeRecordController implements ConsumeRecordProvider {

    /**
     * 注入消费记录service
     */
    @Autowired
    private ConsumeRecordService consumeRecordService;

    /**
     * 新增消费记录
     * @param request
     * @return
     */
    @Override
    public BaseResponse<ConsumeRecordAddResponse> addConsumeRecord(@RequestBody @Valid ConsumeRecordAddRequest request) {
        ConsumeRecord insertRecord = new ConsumeRecord();
        KsBeanUtil.copyProperties(request, insertRecord);
        ConsumeRecordVo consumeRecordVo = consumeRecordService.addConsumeRecord(insertRecord);

        return BaseResponse.success(new ConsumeRecordAddResponse(consumeRecordVo));
    }

    /**
     * 更新消费记录
     * @param request
     * @return
     */
    @Override
    public BaseResponse<ConsumeRecordModifyResponse> modifyConsumeRecord(@RequestBody @Valid ConsumeRecordModifyRequest request) {
        ConsumeRecord consumeRecord = new ConsumeRecord();
        consumeRecord.setOrderId(request.getOrderId());
        consumeRecord.setValidConsumeSum(request.getValidConsumeSum());
        consumeRecord.setFlowState(request.getFlowState());
        int count = consumeRecordService.modifyConsumeRecord(consumeRecord);

        return BaseResponse.success(new ConsumeRecordModifyResponse(count));
    }

}
