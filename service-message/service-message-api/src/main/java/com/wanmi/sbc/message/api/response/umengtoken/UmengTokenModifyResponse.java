package com.wanmi.sbc.message.api.response.umengtoken;

import com.wanmi.sbc.message.bean.vo.UmengTokenVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>友盟推送设备与会员关系修改结果</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengTokenModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的友盟推送设备与会员关系信息
     */
    @ApiModelProperty(value = "已修改的友盟推送设备与会员关系信息")
    private UmengTokenVO umengTokenVO;
}
