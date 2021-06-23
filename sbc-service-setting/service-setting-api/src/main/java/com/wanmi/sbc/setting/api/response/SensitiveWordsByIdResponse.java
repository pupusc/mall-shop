package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）信息response</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensitiveWordsByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 信息
     */
    private SensitiveWordsVO sensitiveWordsVO;
}
