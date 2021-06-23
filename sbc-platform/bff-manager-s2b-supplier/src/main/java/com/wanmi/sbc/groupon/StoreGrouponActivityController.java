package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.groupon.EsGrouponActivityProvider;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityAddReqquest;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivitySaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityAddRequest;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityModifyRequest;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityAddResponse;
import com.wanmi.sbc.marketing.api.response.grouponactivity.GrouponActivityModifyResponse;
import com.wanmi.sbc.marketing.bean.vo.EsGrouponActivityVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午1:36 2019/5/16
 * @Description:
 */
@Api(description = "S2B的拼团活动服务", tags = "GrouponActivityController")
@RestController
@RequestMapping("/groupon/activity")
public class StoreGrouponActivityController {

    @Autowired
    private GrouponActivitySaveProvider grouponActivitySaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EsGrouponActivityProvider  esGrouponActivityProvider;


    /**
     * 添加拼团活动
     */
    @ApiOperation(value = "批量审核通过拼团活动")
    @MultiSubmit
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid GrouponActivityAddRequest request) {

        // 添加拼团
        GrouponActivityAddResponse response = grouponActivitySaveProvider.add(request).getContext();
        List<EsGrouponActivityVO> activityVOList = response.getEsGrouponActivityVOList();
        //拼团信息同步es
        if(CollectionUtils.isNotEmpty(activityVOList)){
            EsGrouponActivityAddReqquest addReqquest = EsGrouponActivityAddReqquest.builder().esGrouponActivityVOList(activityVOList).build();
            esGrouponActivityProvider.add(addReqquest);
        }

        // 记录日志
        operateLogMQUtil.convertAndSend(
                "营销", "添加拼团活动", StringUtils.join(response.getGrouponActivityInfos(), "，"));

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改拼团活动
     */
    @ApiOperation(value = "修改拼团活动")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse modify(@RequestBody @Valid GrouponActivityModifyRequest request) {

        GrouponActivityModifyResponse context = grouponActivitySaveProvider.modify(request)
                .getContext();
        // 修改拼团活动
        String grouponActivityInfo = context.getGrouponActivityInfo();
        EsGrouponActivityVO grouponActivityVO = context.getGrouponActivityVO();
        //拼团信息同步es
        if(Objects.nonNull(grouponActivityVO)){
            EsGrouponActivityAddReqquest addReqquest = EsGrouponActivityAddReqquest.builder().esGrouponActivityVOList(Collections.singletonList(grouponActivityVO)).build();
            esGrouponActivityProvider.add(addReqquest);
        }

        // 记录日志
        operateLogMQUtil.convertAndSend("营销", "修改拼团活动", grouponActivityInfo);

        return BaseResponse.SUCCESSFUL();
    }

}
