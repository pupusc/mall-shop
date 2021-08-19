package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分页结果</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分页结果
     */
    private MicroServicePage<SensitiveWordsVO> sensitiveWordsVOPage;
}
