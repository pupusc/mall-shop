package com.wanmi.sbc.goods.api.request.restrictedrecord;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除限售请求参数</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedRecordDelByIdListRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-记录主键List
	 */
	@ApiModelProperty(value = "批量删除-记录主键List")
	@NotEmpty
	private List<Long> recordIdList;
}