package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelBatchSaveRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributorLevelDeleteRequest;
import com.wanmi.sbc.customer.api.request.distribution.NormalDistributorLevelNameUpdateRequest;
import com.wanmi.sbc.customer.api.response.distribution.DistributorLevelInitResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午4:33 2019/6/13
 * @Description: 分销等级provider
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributorLevelProvider")
public interface DistributorLevelProvider {

    /**
     * 批量保存分销员等级
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributor-level/batch-save")
    BaseResponse batchSave(@RequestBody @Valid DistributorLevelBatchSaveRequest request);

    /**
     * 删除分销员等级
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributor-level/delete")
    BaseResponse delete(@RequestBody @Valid DistributorLevelDeleteRequest request);

    /**
     * 修改普通分销员等级名称
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributor-level/update-normal-name")
    BaseResponse updateNormalDistributorLevelName(@RequestBody @Valid NormalDistributorLevelNameUpdateRequest request);

    /**
     *  初始化默认分销员等级
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributor-level/init")
    BaseResponse<DistributorLevelInitResponse> initDistributorLevel();
}
