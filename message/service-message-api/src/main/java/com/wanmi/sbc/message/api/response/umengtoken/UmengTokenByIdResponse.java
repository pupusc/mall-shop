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
 * <p>根据id查询任意（包含已删除）友盟推送设备与会员关系信息response</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengTokenByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 友盟推送设备与会员关系信息
     */
    @ApiModelProperty(value = "友盟推送设备与会员关系信息")
    private UmengTokenVO umengTokenVO;
}
