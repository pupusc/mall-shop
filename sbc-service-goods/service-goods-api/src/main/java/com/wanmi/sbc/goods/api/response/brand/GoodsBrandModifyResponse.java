package com.wanmi.sbc.goods.api.response.brand;

import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * 品牌修改响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsBrandModifyResponse extends GoodsBrandVO implements Serializable {

    private static final long serialVersionUID = -6942228033110682924L;
}
