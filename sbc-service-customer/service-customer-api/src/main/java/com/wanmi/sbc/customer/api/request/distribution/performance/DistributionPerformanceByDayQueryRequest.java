package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.wanmi.sbc.customer.api.enums.DateCycleEnum;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.Validate;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.wanmi.sbc.common.util.ValidateUtil.DATE_RANGE_EX_MESSAGE;
import static com.wanmi.sbc.common.util.ValidateUtil.NULL_EX_MESSAGE;

/**
 * <p>分销业绩按天查询参数Bean</p>
 * Created by of628-wenzhi on 2019-04-17-18:19.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class DistributionPerformanceByDayQueryRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -1383655640398626781L;

    /**
     * 分销员id
     */
    @NotBlank
    @ApiModelProperty("分销员id")
    private String distributionId;

    /**
     * 时间周期
     */
    @NotNull
    @ApiModelProperty("时间周期")
    private DateCycleEnum dateCycleEnum;

    /**
     * 年份，若dateCycleEnum为MONTH，则此参数必填，范围必须是去年或今年
     */
    @ApiModelProperty("年份，若dateCycleEnum为MONTH，则此参数必填，范围必须是去年到今年")
    private Integer year;

    /**
     * 自然月份，若dateCycleEnum为MONTH，则此参数必填，范围1-12，若年份为今年，则不能为当前月
     */
    @ApiModelProperty("自然月，若dateCycleEnum为MONTH，则此参数必填，范围1-12，若年份为今年，则不能为当前月")
    @Range(min = 1, max = 12)
    private Integer month;

    @Override
    public void checkParam() {
        if (dateCycleEnum == DateCycleEnum.MONTH) {
            Validate.notNull(year, NULL_EX_MESSAGE, "year");
            Validate.notNull(month, NULL_EX_MESSAGE, "month");
            Validate.inclusiveBetween((long) LocalDate.now().minusYears(1).getYear(), (long) LocalDate.now().getYear(),
                    year);
            if (year == LocalDate.now().getYear()) {
                Validate.isTrue(LocalDate.now().getMonthValue() > month, DATE_RANGE_EX_MESSAGE, year, month);
            }
        }
    }


}
