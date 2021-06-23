package com.wanmi.sbc.elastic.api.request.settlement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.elastic.bean.dto.settlement.EsSettlementDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author yangzhen
 * @Description //结算单信息
 * @Date 14:29 2020/12/11
 * @Param
 * @return
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsSettlementListRequest implements Serializable {

    private static final long serialVersionUID = 4555211803309442026L;

    /**
     *结算信息批量
     **/
    private List<EsSettlementDTO>  lists;

}
