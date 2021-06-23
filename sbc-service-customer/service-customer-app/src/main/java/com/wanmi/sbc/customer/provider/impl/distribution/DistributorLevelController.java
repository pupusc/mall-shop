package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributorLevelProvider;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelBatchSaveRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelDeleteRequest;
import com.wanmi.sbc.customer.api.request.distribution.NormalDistributorLevelNameUpdateRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelInitResponse;
import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributorLevel;
import com.wanmi.sbc.customer.distribution.service.DistributorLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:29 2019/6/13
 * @Description: 分销等级controller
 */
@RestController
public class DistributorLevelController implements DistributorLevelProvider {

    @Autowired
    private DistributorLevelService distributorLevelService;

    @Override
    public BaseResponse batchSave(@RequestBody @Valid DistributorLevelBatchSaveRequest request) {
        List<DistributorLevel> levels = KsBeanUtil.convert(request.getDistributorLevelList(), DistributorLevel.class);
        distributorLevelService.batchSave(levels);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse delete(@RequestBody @Valid DistributorLevelDeleteRequest request) {
        distributorLevelService.delete(request.getDistributorLevelId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateNormalDistributorLevelName(@RequestBody @Valid NormalDistributorLevelNameUpdateRequest request) {
        distributorLevelService.updateNormalDistributorLevelName(request.getDistributorLevelName());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<DistributorLevelInitResponse> initDistributorLevel() {
        DistributorLevel level = distributorLevelService.initDistributorLevel();
        DistributorLevelInitResponse response = new DistributorLevelInitResponse();
        response.getDistributorLevels().add(KsBeanUtil.convert(level, DistributorLevelVO.class));
        return BaseResponse.success(response);
    }
}
