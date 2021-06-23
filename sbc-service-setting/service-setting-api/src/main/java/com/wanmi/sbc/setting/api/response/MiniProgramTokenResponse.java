package com.wanmi.sbc.setting.api.response;

import com.wanmi.sbc.setting.bean.vo.OperationLogVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @program: saas_h
 * @description:
 * @author: Mr.Tian
 * @create: 2020-08-05 19:45
 **/


@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MiniProgramTokenResponse implements Serializable {
    private static final long serialVersionUID = -3364468641156806354L;

    /**
     * 操作日志列表
     */
    @ApiModelProperty(value = "小程序accessToken")
    private String accessToken;
}
