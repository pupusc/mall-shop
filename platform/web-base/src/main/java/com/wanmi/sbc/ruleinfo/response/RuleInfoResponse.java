package com.wanmi.sbc.ruleinfo.response;


import com.wanmi.sbc.common.enums.RuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 预售/预约规则信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleInfoResponse {


    private RuleType type;

    private String content;


}
