package com.wanmi.sbc.message.api.response.pushdetail;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.message.bean.vo.PushDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>推送详情分页结果</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDetailPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 推送详情分页结果
     */
    @ApiModelProperty(value = "推送详情分页结果")
    private MicroServicePage<PushDetailVO> pushDetailVOPage;
}
