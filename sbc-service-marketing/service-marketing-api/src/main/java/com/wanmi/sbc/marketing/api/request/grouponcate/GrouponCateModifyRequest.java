package com.wanmi.sbc.marketing.api.request.grouponcate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表修改参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@Data
public class GrouponCateModifyRequest implements Serializable {
    private static final long serialVersionUID = -8023610371813448839L;

    /**
     * 拼团分类Id
     */
    @ApiModelProperty(value = "拼团分类Id")
    @NotBlank
    @Length(max = 32)
    private String grouponCateId;

    /**
     * 拼团分类名称
     */
    @NotBlank
    @Length(max = 20)
    private String grouponCateName;

    /**
     * 修改人
     */
    @Length(max = 32)
    private String updatePerson;
}