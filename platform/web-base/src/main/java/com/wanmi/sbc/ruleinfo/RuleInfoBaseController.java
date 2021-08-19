package com.wanmi.sbc.ruleinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.RuleType;
import com.wanmi.sbc.goods.api.constant.RuleCacheType;
import com.wanmi.sbc.goods.api.provider.ruleinfo.RuleInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoByRuleTypeRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoListRequest;
import com.wanmi.sbc.goods.bean.vo.RuleInfoVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.ruleinfo.response.RuleInfoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Api(description = "规则说明", tags = "RuleInfoBaseController")
@RestController
@RequestMapping(value = "/ruleinfo")
public class RuleInfoBaseController {

    @Autowired
    private RuleInfoQueryProvider ruleInfoQueryProvider;

    @Autowired
    private RedisService redisService;


    @ApiOperation(value = "获取规则信息")
    @PostMapping("/getRuleContent")
    public BaseResponse<String> getRuleInfo(@RequestBody RuleInfoByRuleTypeRequest request) {

        String content = redisService.getString(RuleCacheType.RULE_KEY + request.getRuleType().toValue());
        if (StringUtils.isNotEmpty(content) && RuleCacheType.RULE_KEY_VALUE.equals(content)) {
            return BaseResponse.success("");
        }
        List<RuleInfoVO> ruleInfoVOS = ruleInfoQueryProvider.list(RuleInfoListRequest.builder().
                ruleType(request.getRuleType()).build()).getContext().getRuleInfoVOList();
        if (CollectionUtils.isEmpty(ruleInfoVOS)) {
            return BaseResponse.success("");
        }
        content = ruleInfoVOS.get(0).getRuleContent();
        if (StringUtils.isNotEmpty(content)) {
            redisService.setString(RuleCacheType.RULE_KEY + request.getRuleType().toValue(), ruleInfoVOS.get(0).getRuleContent());
        } else {
            redisService.setString(RuleCacheType.RULE_KEY + request.getRuleType().toValue(), RuleCacheType.RULE_KEY_VALUE);
        }
        return BaseResponse.success(content);
    }


    @ApiOperation(value = "获取预售/预约规则信息")
    @GetMapping("/getRuleContentAll")
    public BaseResponse<List<RuleInfoResponse>> getRuleContentAll() {
        List<RuleInfoResponse> ruleInfoResponses = new ArrayList<>();
        ruleInfoResponses.add(RuleInfoResponse.builder().type(RuleType.APPOINTMENT).
                content(getRuleContent(RuleInfoByRuleTypeRequest.builder().ruleType(RuleType.APPOINTMENT).build())).build());
        ruleInfoResponses.add(RuleInfoResponse.builder().type(RuleType.SALE).
                content(getRuleContent(RuleInfoByRuleTypeRequest.builder().ruleType(RuleType.SALE).build())).build());
        return BaseResponse.success(ruleInfoResponses);
    }


    private String getRuleContent(RuleInfoByRuleTypeRequest request) {
        String content = redisService.getString(RuleCacheType.RULE_KEY + request.getRuleType().toValue());
        if (StringUtils.isNotEmpty(content) && RuleCacheType.RULE_KEY_VALUE.equals(content)) {
            return "";
        }
        List<RuleInfoVO> ruleInfoVOS = ruleInfoQueryProvider.list(RuleInfoListRequest.builder().
                ruleType(request.getRuleType()).build()).getContext().getRuleInfoVOList();
        if (CollectionUtils.isEmpty(ruleInfoVOS)) {
            return "";
        }
        content = ruleInfoVOS.get(0).getRuleContent();
        if (StringUtils.isNotEmpty(content)) {
            redisService.setString(RuleCacheType.RULE_KEY + request.getRuleType().toValue(), ruleInfoVOS.get(0).getRuleContent());
        } else {
            redisService.setString(RuleCacheType.RULE_KEY + request.getRuleType().toValue(), RuleCacheType.RULE_KEY_VALUE);
        }
        return content;
    }

}
