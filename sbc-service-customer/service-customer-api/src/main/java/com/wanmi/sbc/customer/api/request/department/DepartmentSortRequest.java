package com.wanmi.sbc.customer.api.request.department;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.customer.bean.dto.DepartmentSortDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 * <p>部门管理修改参数</p>
 * @author wanggang
 * @date 2020-02-26 19:02:40
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSortRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	private List<DepartmentSortDTO> list;

	private Integer sourceIndex;

	private Integer targetIndex;




}