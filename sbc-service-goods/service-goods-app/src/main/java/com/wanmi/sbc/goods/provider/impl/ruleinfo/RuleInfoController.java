package com.wanmi.sbc.goods.provider.impl.ruleinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.ruleinfo.RuleInfoProvider;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoAddRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoDelByIdListRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoDelByIdRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoModifyRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoAddResponse;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoModifyResponse;
import com.wanmi.sbc.goods.ruleinfo.model.root.RuleInfo;
import com.wanmi.sbc.goods.ruleinfo.service.RuleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>规则说明保存服务接口实现</p>
 *
 * @author zxd
 * @date 2020-05-25 18:55:56
 */
@RestController
@Validated
public class RuleInfoController implements RuleInfoProvider {
    @Autowired
    private RuleInfoService ruleInfoService;

    @Override
    public BaseResponse<RuleInfoAddResponse> add(@RequestBody @Valid RuleInfoAddRequest ruleInfoAddRequest) {
        RuleInfo ruleInfo = KsBeanUtil.convert(ruleInfoAddRequest, RuleInfo.class);
        if (ruleInfoAddRequest.getRuleContent().length() > 500) {
            throw new SbcRuntimeException("K-000009");
        }
        return BaseResponse.success(new RuleInfoAddResponse(
                ruleInfoService.wrapperVo(ruleInfoService.add(ruleInfo))));
    }

    @Override
    public BaseResponse<RuleInfoModifyResponse> modify(@RequestBody @Valid RuleInfoModifyRequest request) {
        RuleInfo rule = ruleInfoService.getOne(request.getId());
        if (Objects.isNull(rule)) {
            throw new SbcRuntimeException("K-000009");
        }
        if (request.getRuleContent().length() > 500) {
            throw new SbcRuntimeException("K-000009");
        }
        rule.setRuleContent(request.getRuleContent());
        return BaseResponse.success(new RuleInfoModifyResponse(
                ruleInfoService.wrapperVo(ruleInfoService.modify(rule))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid RuleInfoDelByIdRequest ruleInfoDelByIdRequest) {
        RuleInfo ruleInfo = KsBeanUtil.convert(ruleInfoDelByIdRequest, RuleInfo.class);
        ruleInfo.setDelFlag(DeleteFlag.YES);
        ruleInfoService.deleteById(ruleInfo);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid RuleInfoDelByIdListRequest ruleInfoDelByIdListRequest) {
        List<RuleInfo> ruleInfoList = ruleInfoDelByIdListRequest.getIdList().stream()
                .map(Id -> {
                    RuleInfo ruleInfo = KsBeanUtil.convert(Id, RuleInfo.class);
                    ruleInfo.setDelFlag(DeleteFlag.YES);
                    return ruleInfo;
                }).collect(Collectors.toList());
        ruleInfoService.deleteByIdList(ruleInfoList);
        return BaseResponse.SUCCESSFUL();
    }

}

