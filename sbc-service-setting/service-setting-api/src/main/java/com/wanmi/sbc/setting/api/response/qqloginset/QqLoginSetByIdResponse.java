package com.wanmi.sbc.setting.api.response.qqloginset;

import com.wanmi.sbc.setting.bean.vo.QqLoginSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）qq登录信息信息response</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * qq登录信息信息
     */
    @ApiModelProperty(value = "qq登录信息信息")
    private QqLoginSetVO qqLoginSetVO;
}
