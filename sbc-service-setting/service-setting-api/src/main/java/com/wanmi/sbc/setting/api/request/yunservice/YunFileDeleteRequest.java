package com.wanmi.sbc.setting.api.request.yunservice;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>云文件删除请求参数</p>
 * Created by of628-wenzhi on 2020-12-14-3:08 下午.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class YunFileDeleteRequest extends SettingBaseRequest {
    private static final long serialVersionUID = -5113104322986460293L;

    @ApiModelProperty(value = "待删除的远程云文件key集合")
    @NotEmpty
    private List<String> resourceKeys;
}
