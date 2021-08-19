package com.wanmi.sbc.goods.api.response.enterprise;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-03-11 17:18
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseByGoodsIdResponse implements Serializable {

    private List<GoodsInfoVO> goodsInfoVOList;
}
