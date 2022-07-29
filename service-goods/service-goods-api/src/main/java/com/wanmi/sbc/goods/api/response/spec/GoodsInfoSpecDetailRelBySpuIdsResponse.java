package com.wanmi.sbc.goods.api.response.spec;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoSpecDetailRelBySpuIdsResponse implements Serializable {
    private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOList;
}
