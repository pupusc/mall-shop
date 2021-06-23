package com.wanmi.sbc.marketing.api.request.distributionrecord;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * <p>DistributionRecord修改参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@ApiModel
public class DistributionRecordModifyRequest{
	private static final long serialVersionUID = 1L;

	/**
	 * 更新分销记录列表
	 */
	List<DistributionRecordUpdateInfo> updateInfos;

}