package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateQueryProvider;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateSaveProvider;
import com.wanmi.sbc.marketing.api.request.grouponcate.*;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * S2B的拼团分类服务
 */
@RestController
@RequestMapping("/groupon/cate")
@Api(description = "S2B平台端-拼团分类服务", tags = "GrouponCateController")
public class GrouponCateController {

    @Autowired
    private GrouponCateSaveProvider grouponCateSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    /**
     * 查询拼团分类
     *
     * @param grouponCateId 拼团分类id
     */
    /*@ApiOperation(value = "查询拼团分类")
    @RequestMapping(value = "/{grouponCateId}", method = RequestMethod.GET)
    public BaseResponse<GrouponCateByIdResponse> info(@PathVariable String grouponCateId) {
        return grouponCateQueryProvider.getById(new GrouponCateByIdRequest(grouponCateId));
    }*/

    /**
     * 新增拼团分类
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "新增拼团分类")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse addGrouponCate(@RequestBody @Valid GrouponCateAddRequest request) {
        request.setCreatePerson(commonUtil.getOperatorId());
        grouponCateSaveProvider.add(request);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "新增拼团分类",
                "分类名称:" + request.getGrouponCateName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改拼团分类
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "修改拼团分类")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse modifyGrouponCate(@RequestBody @Valid GrouponCateModifyRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        grouponCateSaveProvider.modify(request);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "修改拼团分类",
                "分类ID：" + request.getGrouponCateId() + "，分类名称:" + request.getGrouponCateName());

        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除拼团分类
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "删除拼团分类")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public BaseResponse delGrouponCate(@RequestBody GrouponCateDelByIdRequest request) {
        request.setDelPerson(commonUtil.getOperatorId());
        grouponCateSaveProvider.deleteById(request);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "删除拼团分类",
                "分类ID:" + request.getGrouponCateId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 拼团分类拖拽排序
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "拼团分类拖拽排序")
    @RequestMapping(value = "/sort", method = RequestMethod.POST)
    public BaseResponse dragSort(@RequestBody GrouponCateSortRequest request) {
        grouponCateSaveProvider.dragSort(request);

        //记录操作日志
        operateLogMQUtil.convertAndSend("营销", "拼团分类拖拽排序",
                "分类：" + request.getGrouponCateSortVOList().toString());
        return BaseResponse.SUCCESSFUL();
    }
}
