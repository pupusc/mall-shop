package com.wanmi.sbc.marketing.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.distribution.*;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * <p>分销设置保存服务Provider</p>
 * @author gaomuwei
 * @date 2019-02-19 10:08:02
 */
@FeignClient(value = "${application.marketing.name}", contextId = "DistributionSettingProvider")
public interface DistributionSettingProvider {

	/**
	 * 保存社交分销开关状态
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/open-flag")
	BaseResponse saveOpenFlag(@RequestBody @Valid DistributionOpenFlagSaveRequest request);

	/**
	 * 保存基础设置API
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/save-basic")
	BaseResponse saveBasic(@RequestBody @Valid DistributionBasicSettingSaveRequest request);

	/**
	 * 保存分销员招募设置API
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/save-recruit")
	BaseResponse saveRecruit(@RequestBody @Valid DistributionRecruitSettingSaveRequest request);

	/**
	 * 保存奖励模式设置API
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/save-reward")
	BaseResponse saveReward(@RequestBody @Valid DistributionRewardSettingSaveRequest request);

	/**
	 * 保存多级分销设置API
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/setting/save-multistage")
	BaseResponse saveMultistage(@RequestBody @Valid DistributionMultistageSettingSaveRequest request);

	/**
	 * 保存店铺分销设置API
	 *
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/distribution/store-setting/save")
	BaseResponse saveStoreSetting(@RequestBody @Valid DistributionStoreSettingSaveRequest request);

}

