package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.wanmi.sbc.common.util.ValidateUtil.DATE_EX_MESSAGE;

/**
 * <p>从分销业绩中查询去重后的分销员id集合请求参数</p>
 * Created by of628-wenzhi on 2019-04-23-15:19.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class DistinctDistributionIdsQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = -3568075152433886999L;

    /**
     * 开始日期，开始日期必须小于结束日期
     */
    @ApiModelProperty("开始日期，开始日期必须小于结束日期")
    @NotNull
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate startDate;

    /**
     * 结束日期，开始日期必须小于结束日期
     */
    @ApiModelProperty("结束日期，开始日期必须小于结束日期")
    @NotNull
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate endDate;

    @Override
    public void checkParam() {
        Validate.isTrue(endDate.compareTo(startDate) >= 0, DATE_EX_MESSAGE,
                startDate + "-" + endDate);
    }
}
