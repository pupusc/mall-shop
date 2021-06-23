package com.wanmi.sbc.customer.api.request.distribution.performance;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.dto.DistributionPerformanceDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static com.wanmi.sbc.common.util.ValidateUtil.DATE_EX_MESSAGE;

/**
 * <p>日分销业绩录入请求参数bean</p>
 * Created by of628-wenzhi on 2019-04-18-15:58.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class DistributionPerformanceEnteringRequest extends CustomerBaseRequest {

    private static final long serialVersionUID = -6769602654087357061L;

    @ApiModelProperty("日分销业绩数据")
    @NotNull
    @Valid
    private DistributionPerformanceDTO data;

    @Override
    public void checkParam() {
        Validate.isTrue(data.getTargetDate().compareTo(LocalDate.now()) <= 0, DATE_EX_MESSAGE,
                data.getTargetDate());
    }
}
