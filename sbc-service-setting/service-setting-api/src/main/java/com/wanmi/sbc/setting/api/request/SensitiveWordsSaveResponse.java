package com.wanmi.sbc.setting.api.request;

import com.wanmi.sbc.setting.bean.vo.SensitiveWordsVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author houshuai
 * @date 2020/12/11 17:36
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensitiveWordsSaveResponse implements Serializable {

    private static final long serialVersionUID = -5125253725563096911L;

    private Integer count;

    private List<SensitiveWordsVO> sensitiveWordsList;

}
