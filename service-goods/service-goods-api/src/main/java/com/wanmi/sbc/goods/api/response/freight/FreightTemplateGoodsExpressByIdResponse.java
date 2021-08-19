package com.wanmi.sbc.goods.api.response.freight;

import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsExpressVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class FreightTemplateGoodsExpressByIdResponse extends FreightTemplateGoodsExpressVO implements Serializable {

    private static final long serialVersionUID = -5280954422120078679L;
}
