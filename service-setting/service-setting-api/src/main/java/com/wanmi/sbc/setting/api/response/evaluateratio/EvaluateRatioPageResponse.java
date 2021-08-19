package com.wanmi.sbc.setting.api.response.evaluateratio;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>商品评价系数设置分页结果</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价系数设置分页结果
     */
    private MicroServicePage<EvaluateRatioVO> evaluateRatioVOPage;
}
