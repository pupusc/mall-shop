package com.wanmi.sbc.setting.api.response.page;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>查询缓存在数据库中的首页dom返回对象</p>
 *
 * @author lq
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MagicPageMainQueryResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 建站首页dom字符串
     */
    @ApiModelProperty(value = "建站首页dom字符串")
    private Integer id;

    /**
     * 建站首页dom字符串
     */
    @ApiModelProperty(value = "建站首页dom字符串")
    private String htmlString;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}
