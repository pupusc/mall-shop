package com.wanmi.sbc.message.api.response.appmessage;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表分页结果</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessagePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * App站内信消息发送表分页结果
     */
    @ApiModelProperty(value = "App站内信消息发送表分页结果")
    private MicroServicePage<AppMessageVO> appMessageVOPage;

    @ApiModelProperty(value = "服务通知消息数量（未读）")
    private Integer NoticeNum;

    @ApiModelProperty(value = "优惠促销消息数量（未读）")
    private Integer PreferentialNum;
}
