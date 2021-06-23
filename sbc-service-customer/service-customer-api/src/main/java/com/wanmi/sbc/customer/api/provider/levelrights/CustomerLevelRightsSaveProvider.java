package com.wanmi.sbc.customer.api.provider.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsAddRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsDeleteRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsModifyRequest;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员等级权益表保存服务Provider</p>
 *
 * @author minchen
 * @date 2019-02-21 14:01:26
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelRightsSaveProvider")
public interface CustomerLevelRightsSaveProvider {

    /**
     * 新增会员等级权益表API
     *
     * @param customerLevelRightsAddRequest 会员等级权益表新增参数结构 {@link CustomerLevelRightsAddRequest}
     * @return 新增的会员等级权益表信息 {@link CustomerLevelRightsResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/add")
    BaseResponse<CustomerLevelRightsResponse> add(@RequestBody @Valid CustomerLevelRightsAddRequest customerLevelRightsAddRequest);

    /**
     * 修改会员等级权益表API
     *
     * @param customerLevelRightsModifyRequest 会员等级权益表修改参数结构 {@link CustomerLevelRightsModifyRequest}
     * @return 修改的会员等级权益表信息 {@link CustomerLevelRightsResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/modify")
    BaseResponse<CustomerLevelRightsResponse> modify(@RequestBody @Valid CustomerLevelRightsModifyRequest customerLevelRightsModifyRequest);

    /**
     * 单个删除会员等级权益表API
     *
     * @param customerLevelRightsDeleteRequest 单个删除参数结构 {@link CustomerLevelRightsDeleteRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid CustomerLevelRightsDeleteRequest customerLevelRightsDeleteRequest);

    /**
     * 拖拽排序
     *
     * @param queryRequest 拖拽排序参数结构 {@link CustomerLevelRightsQueryRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/edit-sort")
    BaseResponse editSort(@RequestBody @Valid CustomerLevelRightsQueryRequest queryRequest);

}

