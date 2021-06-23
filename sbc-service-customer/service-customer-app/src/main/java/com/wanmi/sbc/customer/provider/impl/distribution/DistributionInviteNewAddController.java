package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewProvider;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewUpdateResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionInviteNewAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionInviteNewRecordBatchUpdateResponse;
import com.wanmi.sbc.customer.bean.dto.DistributionInviteNewRecordDTO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewRecordVO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewVo;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.distribution.service.CustomerDistributionInviteNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 邀新记录新增控制层
 * @Autho qiaokang
 * @Date：2019-03-05 09:37:24
 */
@RestController
public class DistributionInviteNewAddController implements DistributionInviteNewProvider {

    /**
     * 注入分销员邀新服务层
     */
    @Autowired
    private CustomerDistributionInviteNewService customerDistributionInviteNewService;

    /**
     * 新增邀新记录
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewAddResponse> addDistributionInviteNewRecord(@RequestBody @Valid DistributionInviteNewAddRequest request) {
        InviteNewRecord inviteNewRecord = new InviteNewRecord();
        KsBeanUtil.copyProperties(request, inviteNewRecord);
        DistributionInviteNewVo inviteNewVo = customerDistributionInviteNewService.addInviteNewRecord(inviteNewRecord);
        return BaseResponse.success(new DistributionInviteNewAddResponse(inviteNewVo));
    }

    /**
     * 更新邀新记录
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewVo> updateAndFlash(@RequestBody @Valid DistributionInviteNewUpdateRequest request) {
        DistributionInviteNewVo vo = customerDistributionInviteNewService.update(request);
        return BaseResponse.success(vo);
    }

    @Override
    public BaseResponse<DistributionInviteNewRecordBatchUpdateResponse> batchUpdate(@RequestBody @Valid DistributionInviteNewRecordBatchUpdateRequest request) {
        List<DistributionInviteNewRecordDTO> list = request.getList();
        List<InviteNewRecord> inviteNewRecordList = KsBeanUtil.convert(list, InviteNewRecord.class);
        List<InviteNewRecord> recordList = customerDistributionInviteNewService.batchUpdate(inviteNewRecordList);
        List<DistributionInviteNewRecordVO> voList = KsBeanUtil.convert(recordList, DistributionInviteNewRecordVO.class);
        return BaseResponse.success(new DistributionInviteNewRecordBatchUpdateResponse(voList));
    }

    /**
     * 更新邀新记录-补发
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewVo> updateBySupplyAgain(@RequestBody @Valid DistributionInviteNewSupplyAgainRequest request) {
        DistributionInviteNewVo vo = customerDistributionInviteNewService.updateBySupplyAgain(request);
        return BaseResponse.success(vo);
    }

    /**
     * 新增邀新记录,发放奖励奖金、优惠券(注册邀新触发)
     * @param request
     * @return
     */
    @Override
    public BaseResponse addRegister(@RequestBody @Valid DistributionInviteNewAddRegisterRequest request) {
        customerDistributionInviteNewService.addDistributionInviteNewRecord(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 更新邀新记录，发放奖励奖金、优惠券（定时任务触发）
     * @param modifyRegisterRequest
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewUpdateResponse> modify(@RequestBody @Valid DistributionInviteNewModifyRegisterRequest modifyRegisterRequest) {
        DistributionInviteNewUpdateResponse response = customerDistributionInviteNewService.modifyDistributionInviteNewRecord(modifyRegisterRequest);
        return BaseResponse.success(response);
    }
}
