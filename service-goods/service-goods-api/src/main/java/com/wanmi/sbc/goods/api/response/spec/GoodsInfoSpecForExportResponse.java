package com.wanmi.sbc.goods.api.response.spec;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecExportVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsInfoSpecForExportResponse implements Serializable {

    private static final long serialVersionUID = 3777485709913304389L;

    @ApiModelProperty(value = "规格列表")
    private Map<String, List<GoodsInfoSpecExportVO>> goodsInfoSpecMap;

}
