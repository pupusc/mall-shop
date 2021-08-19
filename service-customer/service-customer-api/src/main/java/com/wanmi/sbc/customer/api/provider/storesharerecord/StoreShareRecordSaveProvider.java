package com.wanmi.sbc.customer.api.provider.storesharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordAddRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordDelByIdListRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordDelByIdRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordModifyRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordAddResponse;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商城分享保存服务Provider</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreShareRecordSaveProvider")
public interface StoreShareRecordSaveProvider {

    /**
     * 新增商城分享API
     *
     * @param storeShareRecordAddRequest 商城分享新增参数结构 {@link StoreShareRecordAddRequest}
     * @return 新增的商城分享信息 {@link StoreShareRecordAddResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/add")
    BaseResponse<StoreShareRecordAddResponse> add(@RequestBody @Valid StoreShareRecordAddRequest storeShareRecordAddRequest);

    /**
     * 修改商城分享API
     *
     * @param storeShareRecordModifyRequest 商城分享修改参数结构 {@link StoreShareRecordModifyRequest}
     * @return 修改的商城分享信息 {@link StoreShareRecordModifyResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/modify")
    BaseResponse<StoreShareRecordModifyResponse> modify(@RequestBody @Valid StoreShareRecordModifyRequest storeShareRecordModifyRequest);

    /**
     * 单个删除商城分享API
     *
     * @param storeShareRecordDelByIdRequest 单个删除参数结构 {@link StoreShareRecordDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid StoreShareRecordDelByIdRequest storeShareRecordDelByIdRequest);

    /**
     * 批量删除商城分享API
     *
     * @param storeShareRecordDelByIdListRequest 批量删除参数结构 {@link StoreShareRecordDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid StoreShareRecordDelByIdListRequest storeShareRecordDelByIdListRequest);

}

