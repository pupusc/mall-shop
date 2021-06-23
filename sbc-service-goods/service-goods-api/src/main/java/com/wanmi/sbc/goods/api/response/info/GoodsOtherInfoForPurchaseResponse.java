package com.wanmi.sbc.goods.api.response.info;

import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据ids查询商品主要信息response
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOtherInfoForPurchaseResponse implements Serializable {

    private static final long serialVersionUID = 6243312906897496835L;

    /**
     * 客户店铺等级map
     */
    private Map<Long, CommonLevelVO> levelMap = new HashMap<>();

    /**
     * 单品列表
     */
    private List<GoodsInfoVO> goodsInfoList = new ArrayList<>();
}
