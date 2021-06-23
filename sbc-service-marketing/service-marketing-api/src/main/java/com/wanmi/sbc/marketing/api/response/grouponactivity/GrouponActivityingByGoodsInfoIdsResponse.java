package com.wanmi.sbc.marketing.api.response.grouponactivity;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:07 2019/5/24
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrouponActivityingByGoodsInfoIdsResponse implements Serializable {

    private static final long serialVersionUID = -3431142642261975387L;

    /**
     * 拼团商品map
     */
    @ApiModelProperty("拼团商品map")
    private Map<String, GrouponGoodsInfoVO> grouponGoodsInfoMap;

}
