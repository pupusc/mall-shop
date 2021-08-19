package com.wanmi.sbc.goods.api.response.groupongoodsinfo;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoByActivityIdAndGoodsIdVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class GrouponGoodsInfoBatchByActivityIdAndGoodsIdResponse implements Serializable {
	private static final long serialVersionUID = 1L;


	private List<GrouponGoodsInfoByActivityIdAndGoodsIdVO> list;
}