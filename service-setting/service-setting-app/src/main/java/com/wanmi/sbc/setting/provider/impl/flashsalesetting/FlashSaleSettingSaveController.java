package com.wanmi.sbc.setting.provider.impl.flashsalesetting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.setting.api.provider.flashsalesetting.FlashSaleSettingSaveProvider;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingListModifyRequest;
import com.wanmi.sbc.setting.api.request.flashsalesetting.FlashSaleSettingQueryRequest;
import com.wanmi.sbc.setting.api.response.flashsalesetting.FlashSaleSettingCancelListResponse;
import com.wanmi.sbc.setting.bean.vo.FlashSaleSettingVO;
import com.wanmi.sbc.setting.flashsalesetting.model.root.FlashSaleSetting;
import com.wanmi.sbc.setting.flashsalesetting.service.FlashSaleSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>秒杀设置保存服务接口实现</p>
 *
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@RestController
@Validated
public class FlashSaleSettingSaveController implements FlashSaleSettingSaveProvider {

    @Autowired
    private FlashSaleSettingService flashSaleSettingService;

    @Override
    public BaseResponse<FlashSaleSettingCancelListResponse> modifyList(@RequestBody @Valid FlashSaleSettingListModifyRequest
    flashSaleSettingListModifyRequest) {
        FlashSaleSettingQueryRequest queryReq = new FlashSaleSettingQueryRequest();
        queryReq.setDelFlag(DeleteFlag.NO);
        List<FlashSaleSetting> flashSaleSettingList = flashSaleSettingService.list(queryReq);
        //被取消的场次
        List<String> cancelList = new ArrayList<>();
        flashSaleSettingList.forEach(flashSaleSetting -> {
            FlashSaleSettingVO flashSaleSettingVO = flashSaleSettingListModifyRequest.getFlashSaleSettingVOS().stream().filter(s -> s
                    .getId().equals(flashSaleSetting.getId())).findFirst().orElse(null);
            if(EnableStatus.ENABLE.equals(flashSaleSetting.getStatus()) && EnableStatus.DISABLE.equals(flashSaleSettingVO
                    .getStatus())){
                cancelList.add(flashSaleSetting.getTime());
            }
            flashSaleSetting.setStatus(flashSaleSettingVO.getStatus());
            flashSaleSetting.setUpdatePerson(flashSaleSettingVO.getUpdatePerson());
            flashSaleSetting.setUpdateTime(flashSaleSettingVO.getUpdateTime());
        });

        flashSaleSettingService.modifyList(flashSaleSettingList);

        return BaseResponse.success(new FlashSaleSettingCancelListResponse(cancelList));
    }

}

