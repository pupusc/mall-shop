package com.wanmi.sbc.crm.api.request.rfmstatistic;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.bean.enums.ScoreType;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-10-16
 * \* Time: 14:04
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfmScoreStatisticRequest extends BaseQueryRequest {

    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate statDate;

    private Integer type;

    private List<RFMType> typeList;

    private ScoreType scoreType;

    private BigDecimal score;

    private String customerId;
}
