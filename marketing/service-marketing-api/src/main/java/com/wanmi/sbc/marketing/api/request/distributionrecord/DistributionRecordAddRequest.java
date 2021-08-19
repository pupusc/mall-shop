package com.wanmi.sbc.marketing.api.request.distributionrecord;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * <p>DistributionRecord新增参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@ApiModel
public class DistributionRecordAddRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 分销记录保存列表
	 */
	List<DistributionRecordAddInfo> distributionRecordAddInfos;

}