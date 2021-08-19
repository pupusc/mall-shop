package com.wanmi.sbc.order.api.response.distribution;

import com.wanmi.sbc.order.bean.vo.ConsumeRecordVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 新增消费记录返回体
 * @Autho qiaokang
 * @Date：2019-03-05 18:45:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ConsumeRecordAddResponse implements Serializable {

    private static final long serialVersionUID = -6929093364799263696L;

    /**
     * 消费记录信息
     */
    @ApiModelProperty(value = "消费记录信息")
    ConsumeRecordVo consumeRecordVo;

}
