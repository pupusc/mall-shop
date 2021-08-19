package com.wanmi.sbc.goods.api.response.restrictedrecord;

import com.wanmi.sbc.goods.bean.vo.RestrictedRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售修改结果</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedRecordModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的限售信息
     */
    @ApiModelProperty(value = "已修改的限售信息")
    private RestrictedRecordVO restrictedRecordVO;
}
