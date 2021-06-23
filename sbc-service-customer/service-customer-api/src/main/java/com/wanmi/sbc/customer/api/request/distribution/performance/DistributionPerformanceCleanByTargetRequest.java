package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.wanmi.sbc.common.util.ValidateUtil.DATE_RANGE_EX_MESSAGE;

/**
 * <p>根据指定年和月清理业绩数据请求参数</p>
 * Created by of628-wenzhi on 2019-04-18-17:32.
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel
@Data
public class DistributionPerformanceCleanByTargetRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -2631798520427043021L;

    /**
     * 年
     */
    @ApiModelProperty("年")
    @NotNull
    private Integer year;

    /**
     * 月
     */
    @ApiModelProperty("月")
    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;

    @Override
    public void checkParam() {
        LocalDate date = LocalDate.of(year, month, LocalDate.now().getDayOfMonth());
        Validate.isTrue(year <= LocalDate.now().getYear() && LocalDate.now().minusMonths(6).compareTo(date) > 0
                , DATE_RANGE_EX_MESSAGE, year, month);
    }

}
