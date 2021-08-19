package com.wanmi.sbc.elastic.api.response.sensitivewords;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.sensitivewords.EsSensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSensitiveWordsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分页结果
     */
    private MicroServicePage<EsSensitiveWordsVO> sensitiveWordsVOPage;
}
