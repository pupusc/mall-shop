package com.wanmi.sbc.goods.provider.impl.SuspensionV2;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;

import com.wanmi.sbc.goods.SuspensionV2.model.Suspension;
import com.wanmi.sbc.goods.SuspensionV2.service.SuspensionService;
import com.wanmi.sbc.goods.api.provider.SuspensionV2.SuspensionProvider;
import com.wanmi.sbc.goods.api.request.SuspensionV2.SuspensionByTypeRequest;
import com.wanmi.sbc.goods.api.response.SuspensionV2.SuspensionByTypeResponse;
import com.wanmi.sbc.goods.bean.dto.SuspensionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>悬浮窗查询服务接口实现</p>
 * @author lws
 * @date 2023-02-4
 */
@RestController
@Validated
public class SuspensionQueryController implements SuspensionProvider {

    @Autowired
    private SuspensionService suspensionService;

    @Override
    public BaseResponse<SuspensionByTypeResponse> getByType(@RequestBody @Valid SuspensionByTypeRequest suspensionByTypeRequest) {

        List<SuspensionDTO> listDto=new ArrayList<>();
        List<Suspension> suspensionByType = suspensionService.getSuspensionByType(suspensionByTypeRequest.getType());
        for(Suspension suspension : suspensionByType) {
            SuspensionDTO suspensionDTOTemp=new SuspensionDTO();
            KsBeanUtil.copyPropertiesThird(suspension, suspensionDTOTemp);
            listDto.add(suspensionDTOTemp);
        }
        SuspensionByTypeResponse suspensionByTypeResponse =new SuspensionByTypeResponse(listDto);
        return BaseResponse.success(suspensionByTypeResponse);
    }
}
