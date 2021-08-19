package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/11 19:06
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensitiveWordsDeleteResponse implements Serializable {

    private static final long serialVersionUID = -565320735129575198L;

    private List<SensitiveWordsVO> sensitiveWordsList;

}
