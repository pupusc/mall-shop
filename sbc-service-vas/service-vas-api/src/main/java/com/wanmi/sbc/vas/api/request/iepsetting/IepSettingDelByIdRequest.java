package com.wanmi.sbc.vas.api.request.iepsetting;

import com.wanmi.sbc.vas.api.request.VasBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除企业购设置请求参数</p>
 * @author 宋汉林
 * @date 2020-03-02 20:15:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IepSettingDelByIdRequest extends VasBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     *  id 
     */
    @ApiModelProperty(value = " id ")
    @NotNull
    private String id;
}
