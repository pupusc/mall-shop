package com.wanmi.sbc.customer.api.response.storelevel;

import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yang
 * @since 2019/3/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StroeLevelInfoByIdResponse implements Serializable {


    private static final long serialVersionUID = 6263682822129922514L;
    /**
     * 店铺等级信息，包括自营非自营，非自营店铺转成自营店铺字段信息
     */
    private CustomerLevelVO customerLevelVO;
}
