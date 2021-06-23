package com.wanmi.sbc.marketing.api.request.grouponcate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.marketing.api.request.market.MarketingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>拼团活动信息表新增参数</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@ApiModel
@Data
public class GrouponCateAddRequest implements Serializable {
    private static final long serialVersionUID = 716860747509492139L;

    /**
     * 拼团分类名称
     */
    @ApiModelProperty(value = "拼团分类名称")
    @NotBlank
    @Length(max = 20)
    private String grouponCateName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @Length(max = 32)
    private String createPerson;

}