package com.wanmi.sbc.ruleinfo;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.ruleinfo.RuleInfoProvider;
import com.wanmi.sbc.goods.api.provider.ruleinfo.RuleInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoAddRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoByRuleTypeRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoListRequest;
import com.wanmi.sbc.goods.api.request.ruleinfo.RuleInfoModifyRequest;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoAddResponse;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.ruleinfo.RuleInfoModifyResponse;
import com.wanmi.sbc.goods.bean.vo.RuleInfoVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(description = "规则说明管理API", tags = "RuleInfoController")
@RestController
@RequestMapping(value = "/ruleinfo")
public class RuleInfoController {

    @Autowired
    private RuleInfoQueryProvider ruleInfoQueryProvider;

    @Autowired
    private RuleInfoProvider ruleInfoProvider;

    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "根据类型查询预约或预售规则说明")
    @PostMapping("/getRuleInfo")
    public BaseResponse<RuleInfoByIdResponse> getRuleInfo(@RequestBody RuleInfoByRuleTypeRequest request) {
        List<RuleInfoVO> ruleInfoVOS = ruleInfoQueryProvider.list(RuleInfoListRequest.builder().
                ruleType(request.getRuleType()).build()).getContext().getRuleInfoVOList();
        if (CollectionUtils.isEmpty(ruleInfoVOS)) {
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.success(RuleInfoByIdResponse.builder().ruleInfoVO(ruleInfoVOS.get(0)).build());
    }

    @ApiOperation(value = "新增规则说明")
    @PostMapping("/add")
    public BaseResponse<RuleInfoAddResponse> add(@RequestBody @Valid RuleInfoAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        return ruleInfoProvider.add(addReq);
    }

    @ApiOperation(value = "修改规则说明")
    @PutMapping(value = "/modify")
    public BaseResponse<RuleInfoModifyResponse> modify(@RequestBody RuleInfoModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        return ruleInfoProvider.modify(modifyReq);
    }


    /*@ApiOperation(value = "根据idList批量删除规则说明")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid RuleInfoDelByIdListRequest delByIdListReq) {
        return ruleInfoProvider.deleteByIdList(delByIdListReq);
    }*/

}
