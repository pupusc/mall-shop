package com.wanmi.sbc.marketing.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionSettingByDistributorIdRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingListByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.response.distribution.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销设置查询服务Provider</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@FeignClient(value = "${application.marketing.name}", contextId = "DistributionSettingQueryProvider")
public interface DistributionSettingQueryProvider {

	/**
	 * 查询分销设置API
	 *
	 * @return 分销设置详情 {@link DistributionSettingGetResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/query")
	BaseResponse<DistributionSettingGetResponse> getSetting();

	/**
	 * 查询分销设置中商品审核开关
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/goods-query")
	BaseResponse<Boolean> getDistributionGoodsSwitch();


	/**
	 * 查询店铺分销设置API
	 *
	 * @return 分销设置详情 {@link DistributionSettingGetResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/store-setting/get-by-store-id")
	BaseResponse<DistributionStoreSettingGetByStoreIdResponse> getStoreSettingByStoreId(
			@RequestBody @Valid DistributionStoreSettingGetByStoreIdRequest request);

	/**
	 * 查询店铺id集合查询分销设置API
	 * @param request {@link DistributionStoreSettingListByStoreIdsRequest }
	 * @return  {@link DistributionStoreSettingListByStoreIdsResponse }
	*/
	@PostMapping("/marketing/${application.marketing.version}/distribution/store-setting/list-by-store-ids")
	BaseResponse<DistributionStoreSettingListByStoreIdsResponse> listByStoreIds(@RequestBody @Valid DistributionStoreSettingListByStoreIdsRequest request);

	/**
	 * 查询分销设置API——共小C端使用
	 *
	 * @return 分销设置基本信息 {@link DistributionSettingGetResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/query-for-mobile")
	BaseResponse<DistributionSimSettingResponse> getSimSetting(@RequestBody @Valid DistributionSettingByDistributorIdRequest request);


	/**
	 * 查询分销设置API
	 *
	 * @return 分销设置基本信息 {@link DistributionSettingGetResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/find-one-setting")
	BaseResponse<DistributionSimSettingResponse> findOneSetting();

}

