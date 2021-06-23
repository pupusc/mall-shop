package com.wanmi.sbc.message.api.response.pushdetail;

import com.wanmi.sbc.message.bean.vo.PushDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）推送详情信息response</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDetailByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 推送详情信息
     */
    @ApiModelProperty(value = "推送详情信息")
    private PushDetailVO pushDetailVO;
}
