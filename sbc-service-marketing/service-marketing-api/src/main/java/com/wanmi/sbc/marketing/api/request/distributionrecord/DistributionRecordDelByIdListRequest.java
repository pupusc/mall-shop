package com.wanmi.sbc.marketing.api.request.distributionrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除DistributionRecord请求参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionRecordDelByIdListRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-分销记录表主键List
	 */
	@NotEmpty
	private List<String> recordIdList;
}