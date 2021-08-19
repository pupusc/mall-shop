package com.wanmi.sbc.customer.api.response.store;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>店铺分页结果</p>
 * Created by of628-wenzhi on 2018-09-11-下午5:54.
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorePageResponse implements Serializable {
    private static final long serialVersionUID = -1681794911985218984L;

    /**
     * 带分页的店铺集合
     */
    @ApiModelProperty(value = "带分页的店铺集合")
    private MicroServicePage<StoreVO> storeVOPage;
}
