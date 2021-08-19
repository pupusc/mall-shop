package com.wanmi.sbc.goods.api.response.presellsale;

import com.wanmi.sbc.goods.bean.vo.PresellSaleVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@ApiModel
@Data
public class PresellSaleResponse extends PresellSaleVO implements Serializable {

    private static final long serialVersionUID = -3871463478142348744L;


}
