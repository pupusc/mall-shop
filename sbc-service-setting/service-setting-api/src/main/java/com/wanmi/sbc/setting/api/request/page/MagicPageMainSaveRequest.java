package com.wanmi.sbc.setting.api.request.page;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>缓存魔方首页dom到数据库请求对象</p>
 *
 * @author lq
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MagicPageMainSaveRequest extends SettingBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "dom")
    @NotBlank
    private String htmlString;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @Length(max = 45)
    private String operatePerson;

}