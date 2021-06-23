package com.wanmi.sbc.elastic.api.request.goods;

import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsGoodsBrandSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    List<GoodsBrandVO> goodsBrandVOList;
}