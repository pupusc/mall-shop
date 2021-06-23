package com.wanmi.sbc.setting.api.response.qqloginset;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.QqLoginSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>qq登录信息分页结果</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * qq登录信息分页结果
     */
    @ApiModelProperty(value = "qq登录信息分页结果")
    private MicroServicePage<QqLoginSetVO> qqLoginSetVOPage;
}
