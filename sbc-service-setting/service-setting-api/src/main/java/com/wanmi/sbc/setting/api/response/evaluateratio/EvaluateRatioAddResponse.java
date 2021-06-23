package com.wanmi.sbc.setting.api.response.evaluateratio;

import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>商品评价系数设置新增结果</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的商品评价系数设置信息
     */
    private EvaluateRatioVO evaluateRatioVO;
}
