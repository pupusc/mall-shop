package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateProvider;
import com.wanmi.sbc.setting.api.provider.GrowthValueBasicRuleProvider;
import com.wanmi.sbc.setting.api.provider.GrowthValueBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.provider.PointsBasicRuleQueryProvider;
import com.wanmi.sbc.setting.api.request.GrowthValueBasicRuleModifyRequest;
import com.wanmi.sbc.setting.bean.enums.GrowthValueRule;
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
 * 成长值Controller
 * Created by YINXIANZHI on 2019/02/22.
 */
@Api(tags = "GrowthValueBasicRuleController", description = "成长值设置 Api")
@RestController
@RequestMapping("/growthValue/basicRules")
public class GrowthValueBasicRuleController {

    @Autowired
    private GrowthValueBasicRuleQueryProvider growthValueQueryProvider;

    @Autowired
    private GrowthValueBasicRuleProvider growthValueProvider;

    @Autowired
    private PointsBasicRuleQueryProvider pointsBasicRuleQueryProvider;

    @Autowired
    private GoodsCateProvider goodsCateProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询成长值基础获取规则
     * @return
     */
    @ApiOperation(value = "查询成长值基础获取规则")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List<ConfigVO>> listBasicRules() {
        return BaseResponse.success(growthValueQueryProvider.listGrowthValueBasicRule().getContext().getConfigVOList());
    }

    /**
     * 修改成长值基础获取规则
     * @param request
     * @return
     */
    @ApiOperation(value = "修改成长值基础获取规则")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse updateOrderSettingConfigs(@RequestBody GrowthValueBasicRuleModifyRequest request) {
        if (Objects.isNull(request) || CollectionUtils.isEmpty(request.getGrowthValueBasicRuleDTOList()) || Objects.isNull(request.getRule())) {
            throw new SbcRuntimeException("K-091101");
        } else {
            request.getGrowthValueBasicRuleDTOList().forEach(growthValueBasicRuleDTO -> {
                if (Objects.isNull(growthValueBasicRuleDTO.getStatus()) || Objects.isNull(growthValueBasicRuleDTO.getContext())) {
                    throw new SbcRuntimeException("K-091101");
                }
            });
        }
        //判断是否是同步积分规则
        if(GrowthValueRule.SYNCHRONIZE_POINTS.equals(request.getRule())){
            //同步购物规则
            goodsCateProvider.synchronizePointsRule();
        }
        growthValueProvider.modifyGrowthValueBasicRule(request);

        //操作日志记录
        operateLogMQUtil.convertAndSend("设置", "修改成长值基础获取规则", "修改成长值基础获取规则");
        return BaseResponse.SUCCESSFUL();
    }
}
