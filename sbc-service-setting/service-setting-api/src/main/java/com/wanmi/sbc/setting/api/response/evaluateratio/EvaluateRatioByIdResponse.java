package com.wanmi.sbc.setting.api.response.evaluateratio;

import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）商品评价系数设置信息response</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateRatioByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价系数设置信息
     */
    private EvaluateRatioVO evaluateRatioVO;
}
