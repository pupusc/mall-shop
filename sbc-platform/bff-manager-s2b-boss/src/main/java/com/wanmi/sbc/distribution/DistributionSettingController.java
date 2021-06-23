package com.wanmi.sbc.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.request.distribution.*;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionSettingGetResponse;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 分销设置controller
 *
 * @Author: gaomuwei
 * @Date: Created In 下午2:44 2019/2/19
 * @Description:
 */
@Api(description = "分销设置服务" ,tags ="DistributionSettingController")
@RestController
@RequestMapping("/distribution-setting")
@Validated
public class DistributionSettingController {

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    @Autowired
    private DistributionSettingProvider distributionSettingProvider;


    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询分销设置API
     *
     * @return
     */
    @ApiOperation(value = "查询分销设置")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<DistributionSettingGetResponse> findOne() {
        return distributionSettingQueryProvider.getSetting();
    }

    /**
     * 保存社交分销开关状态API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存社交分销开关状态")
    @RequestMapping(value = "/save-open-flag", method = RequestMethod.PUT)
    public BaseResponse saveOpenFlag(@RequestBody @Valid DistributionOpenFlagSaveRequest request) {
        BaseResponse response = distributionSettingProvider.saveOpenFlag(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存社交分销开关状态", "分销开关状态:"+request.getOpenFlag());
        return response;
    }

    /**
     * 保存基础设置API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存基础设置")
    @RequestMapping(value = "/save-basic",method = RequestMethod.PUT)
    public BaseResponse saveBasic(@RequestBody @Valid DistributionBasicSettingSaveRequest request) {
        BaseResponse response = distributionSettingProvider.saveBasic(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存分销基础设置", "保存分销基础设置");
        return response;
    }

    /**
     * 保存分销员招募设置API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存分销员招募设置")
    @RequestMapping(value = "/save-recruit",method = RequestMethod.PUT)
    public BaseResponse saveRecruit(@RequestBody @Valid DistributionRecruitSettingSaveRequest request) {
        BaseResponse response = distributionSettingProvider.saveRecruit(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存分销员招募设置", "保存分销员招募设置");
        return response;
    }

    /**
     * 保存奖励模式设置API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存奖励模式设置")
    @RequestMapping(value = "/save-reward",method = RequestMethod.PUT)
    public BaseResponse saveReward(@RequestBody @Valid DistributionRewardSettingSaveRequest request) {
        BaseResponse response = distributionSettingProvider.saveReward(request);
        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存奖励模式设置", "保存奖励模式设置");
        return response;
    }

    /**
     * 保存多级分销设置API
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "保存多级分销设置")
    @RequestMapping(value = "/save-multistage",method = RequestMethod.PUT)
    public BaseResponse saveMultistage(@RequestBody @Valid DistributionMultistageSettingSaveRequest request) {
        BaseResponse response = distributionSettingProvider.saveMultistage(request);
        // 记录操作日志
        operateLogMQUtil.convertAndSend("营销", "保存多级分销设置", "保存多级分销设置");
        return response;
    }
}
