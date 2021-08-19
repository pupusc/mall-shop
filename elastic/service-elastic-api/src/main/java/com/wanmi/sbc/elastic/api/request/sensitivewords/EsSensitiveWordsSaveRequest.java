package com.wanmi.sbc.elastic.api.request.sensitivewords;

import com.wanmi.sbc.elastic.bean.vo.sensitivewords.EsSensitiveWordsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/11 18:03
 * @description <p> </p>
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EsSensitiveWordsSaveRequest implements Serializable {

    private static final long serialVersionUID = 3453309906464151490L;

    private List<EsSensitiveWordsVO> sensitiveWordsList;

}
