package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 * @date 2020/12/18 17:42
 * @description <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationLogAddResponse implements Serializable {

    private static final long serialVersionUID = -8057689823220598070L;

    private OperationLogVO operationLogVO;
}
