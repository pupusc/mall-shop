package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.PointsBasicRuleProvider;
import com.wanmi.sbc.setting.api.provider.PointsBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.request.PointsBasicRuleModifyRequest;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 积分获取Controller
 * Created by YINXIANZHI on 2019/03/25.
 */
@Api(tags = "PointsBasicRuleController", description = "积分获取 Api")
@RestController
@RequestMapping("/points/basicRules")
public class PointsBasicRuleController {

    @Autowired
    private PointsBasicRuleQueryProvider pointsBasicRuleQueryProvider;

    @Autowired
    private PointsBasicRuleProvider pointsBasicRuleProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询积分基础获取规则
     * @return
     */
    @ApiOperation(value = "查询积分基础获取规则")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List<ConfigVO>> listBasicRules() {
        return BaseResponse.success(pointsBasicRuleQueryProvider.listPointsBasicRule().getContext().getConfigVOList());
    }

    /**
     * 修改积分基础获取规则
     * @param request
     * @return
     */
    @ApiOperation(value = "修改积分基础获取规则")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse updatePointsGetConfigs(@RequestBody PointsBasicRuleModifyRequest request) {
        if (Objects.isNull(request) || CollectionUtils.isEmpty(request.getPointsBasicRuleDTOList())) {
            throw new SbcRuntimeException("K-091201");
        } else {
            request.getPointsBasicRuleDTOList().forEach(pointsBasicRuleDTO -> {
                if (Objects.isNull(pointsBasicRuleDTO.getStatus()) || Objects.isNull(pointsBasicRuleDTO.getContext())) {
                    throw new SbcRuntimeException("K-091201");
                }
            });
        }
        pointsBasicRuleProvider.modifyPointsBasicRule(request);

        //操作日志记录
        operateLogMQUtil.convertAndSend("设置", "修改积分基础获取规则", "修改积分基础获取规则");
        return BaseResponse.SUCCESSFUL();
    }
}
