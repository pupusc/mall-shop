package com.wanmi.sbc.goods.api.request.groupongoodsinfo;

import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoByActivityIdAndGoodsIdDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>根据拼团活动ID、SPU编号集合查询拼团价格最小的拼团SKU信息</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest implements Serializable {
	private static final long serialVersionUID = 1L;


	@NotNull
	private List<GrouponGoodsInfoByActivityIdAndGoodsIdDTO> list;
}