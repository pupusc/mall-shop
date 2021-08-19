package com.wanmi.sbc.message.api.request.smssignfileinfo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>短信签名文件信息新增参数</p>
 *
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSignFileInfoAddRequest extends SmsBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 短信签名id
     */
    @ApiModelProperty(value = "短信签名id")
    @Max(9223372036854775807L)
    private Long smsSignId;

    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件路径")
    @Length(max = 255)
    @NotBlank
    private String fileUrl;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    @Length(max = 255)
    @NotBlank
    private String fileName;

    /**
     * 删除标识，0：未删除，1：已删除
     */
    @ApiModelProperty(value = "删除标识，0：未删除，1：已删除")
    private DeleteFlag delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}