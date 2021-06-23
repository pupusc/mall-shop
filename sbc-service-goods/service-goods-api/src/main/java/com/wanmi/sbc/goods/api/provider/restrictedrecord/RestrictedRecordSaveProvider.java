package com.wanmi.sbc.goods.api.provider.restrictedrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.restrictedrecord.*;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordAddResponse;
import com.wanmi.sbc.goods.api.response.restrictedrecord.RestrictedRecordModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;

/**
 * <p>限售保存服务Provider</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@FeignClient(value = "${application.goods.name}", contextId = "RestrictedRecordSaveProvider.class")
public interface RestrictedRecordSaveProvider {

	/**
	 * 新增限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordAddRequest 限售新增参数结构 {@link RestrictedRecordAddRequest}
	 * @return 新增的限售信息 {@link RestrictedRecordAddResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/add")
	BaseResponse<RestrictedRecordAddResponse> add(@RequestBody @Valid RestrictedRecordAddRequest restrictedRecordAddRequest);

	/**
	 * 修改限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordModifyRequest 限售修改参数结构 {@link RestrictedRecordModifyRequest}
	 * @return 修改的限售信息 {@link RestrictedRecordModifyResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/modify")
	BaseResponse<RestrictedRecordModifyResponse> modify(@RequestBody @Valid RestrictedRecordModifyRequest restrictedRecordModifyRequest);

	/**
	 * 单个删除限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordDelByIdRequest 单个删除参数结构 {@link RestrictedRecordDelByIdRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/delete-by-id")
	BaseResponse deleteById(@RequestBody @Valid RestrictedRecordDelByIdRequest restrictedRecordDelByIdRequest);

	/**
	 * 批量删除限售API
	 *
	 * @author 限售记录
	 * @param restrictedRecordDelByIdListRequest 批量删除参数结构 {@link RestrictedRecordDelByIdListRequest}
	 * @return 删除结果 {@link BaseResponse}
	 */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/delete-by-id-list")
	BaseResponse deleteByIdList(@RequestBody @Valid RestrictedRecordDelByIdListRequest restrictedRecordDelByIdListRequest);

    /**
     * 批量新增/叠加限售记录
     * @param request
     * @return
     */
	@PostMapping("/goods/${application.goods.version}/restrictedrecord/batch-add")
	BaseResponse batchAdd(@RequestBody @Valid RestrictedRecordBatchAddRequest request);


    /**
     * 批量新增/更新限售记录
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/restrictedrecord/reduce-restricted-record")
    BaseResponse reduceRestrictedRecord(@RequestBody @Valid RestrictedRecordBatchAddRequest request);

}

