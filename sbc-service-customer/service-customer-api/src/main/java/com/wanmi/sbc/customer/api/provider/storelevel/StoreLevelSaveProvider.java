package com.wanmi.sbc.customer.api.provider.storelevel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storelevel.*;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelAddResponse;
import com.wanmi.sbc.customer.api.response.storelevel.StoreLevelModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商户客户等级表保存服务Provider</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreLevelSaveProvider")
public interface StoreLevelSaveProvider {

    /**
     * 新增商户客户等级表API
     *
     * @param storeLevelAddRequest 商户客户等级表新增参数结构 {@link StoreLevelAddRequest}
     * @return 新增的商户客户等级表信息 {@link StoreLevelAddResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/add")
    BaseResponse<StoreLevelAddResponse> add(@RequestBody @Valid StoreLevelAddRequest storeLevelAddRequest);

    /**
     * 修改商户客户等级表API
     *
     * @param storeLevelModifyRequest 商户客户等级表修改参数结构 {@link StoreLevelModifyRequest}
     * @return 修改的商户客户等级表信息 {@link StoreLevelModifyResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/modify")
    BaseResponse<StoreLevelModifyResponse> modify(@RequestBody @Valid StoreLevelModifyRequest storeLevelModifyRequest);

    /**
     * 批量修改店铺等级表API
     *
     * @param storeLevelListModifyRequest 商户客户等级表修改参数结构 {@link StoreLevelListModifyRequest}
     * @return 修改的商户客户等级表信息 {@link StoreLevelListModifyRequest}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/modify-list")
    BaseResponse modifyList(@RequestBody @Valid StoreLevelListModifyRequest storeLevelListModifyRequest);

    /**
     * 单个删除商户客户等级表API
     *
     * @param storeLevelDelByIdRequest 单个删除参数结构 {@link StoreLevelDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid StoreLevelDelByIdRequest storeLevelDelByIdRequest);

    /**
     * 初始化店铺等级
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/init")
    BaseResponse initStoreLevel(@RequestBody @Valid StoreLevelInitRequest request);
}

