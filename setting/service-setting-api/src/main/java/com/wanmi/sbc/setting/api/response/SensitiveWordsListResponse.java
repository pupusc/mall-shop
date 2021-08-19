package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;

/**
 * <p>列表结果</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 列表结果
     */
    private List<SensitiveWordsVO> sensitiveWordsVOList;
}
