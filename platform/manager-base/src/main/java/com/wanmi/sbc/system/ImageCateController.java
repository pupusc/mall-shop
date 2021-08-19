package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.systemresourcecate.SystemResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemresourcecate.SystemResourceCateSaveProvider;
import com.wanmi.sbc.setting.api.request.systemresourcecate.*;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateAddResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateByIdResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateModifyResponse;
import com.wanmi.sbc.setting.bean.vo.SystemResourceCateVO;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * 图片分类服务
 * Created by daiyitian on 17/4/12.
 *
 * 废弃--后期用素材库代替
 */
@Api(tags = "ImageCateController", description = "图片分类服务 Api")
@RestController
public class ImageCateController {


    @Autowired
    private OperateLogMQUtil operateLogMQUtil;


    @Autowired
    private SystemResourceCateQueryProvider systemResourceCateQueryProvider;

    @Autowired
    private SystemResourceCateSaveProvider systemResourceCateSaveProvider;
    /**
     * 查询图片分类
     *
     * @return
     */
    @ApiOperation(value = "查询图片分类")
    @RequestMapping(value = {"/system/imageCates"}, method = RequestMethod.GET)
    public ResponseEntity<List> list() {
        BaseResponse<SystemResourceCateListResponse> response = systemResourceCateQueryProvider.list
                (SystemResourceCateListRequest.builder().delFlag(DeleteFlag.NO).build());
        return ResponseEntity.ok(response.getContext().getSystemResourceCateVOList());
    }

    /**
     * 新增图片分类
     */
    @ApiOperation(value = "新增图片分类")
    @RequestMapping(value = "/system/imageCate", method = RequestMethod.POST)
    public BaseResponse<SystemResourceCateAddResponse> add(@RequestBody @Valid SystemResourceCateAddRequest addReq) {
        if (addReq == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BaseResponse<SystemResourceCateAddResponse> response = systemResourceCateSaveProvider.add(addReq);

        //记录操作发日志
        if (nonNull(addReq.getCateParentId())) {
            operateLogMQUtil.convertAndSend("设置", "新增子分类", "新增子分类：" + addReq.getCateName());
        } else {
            operateLogMQUtil.convertAndSend("设置", "新增一级分类", "新增一级分类：" + addReq.getCateName());
        }

        return response;
    }


    /**
     * 编辑图片分类
     */
    @ApiOperation(value = "编辑图片分类")
    @RequestMapping(value = "/system/imageCate", method = RequestMethod.PUT)
    public BaseResponse<SystemResourceCateModifyResponse> modify(@RequestBody @Valid SystemResourceCateModifyRequest
                                                                         modifyReq) {

        //查找cateId对应的信息
        Optional<SystemResourceCateVO> cateInfoOptional = this.queryCateInfoByCateId(modifyReq.getCateId());
        BaseResponse<SystemResourceCateModifyResponse> response = systemResourceCateSaveProvider.modify(modifyReq);

        //记录操作日志
        cateInfoOptional.ifPresent(map -> operateLogMQUtil.convertAndSend("设置", "编辑分类",
                "分类名称：" + map.getCateName() + " 改成 " + modifyReq.getCateName()));
        return response;

    }

    /**
     * 检测图片分类是否有子类
     */
    @ApiOperation(value = "检测图片分类是否有子类")
    @RequestMapping(value = "/system/imageCate/child", method = RequestMethod.POST)
    public BaseResponse checkChild(@RequestBody SystemResourceCateCheckChildRequest request) {

        if (request.getCateId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        BaseResponse<Integer> response = systemResourceCateQueryProvider.checkChild(request);

        return response;

    }

    /**
     * 检测图片分类是否有子类图片
     */
    @ApiOperation(value = "检测图片分类是否有子类图片")
    @RequestMapping(value = "/system/imageCate/image", method = RequestMethod.POST)
    public BaseResponse checkImage(@RequestBody SystemResourceCateCheckResourceRequest request) {

        BaseResponse<Integer> response = systemResourceCateQueryProvider.checkResource(request);
        return response;
    }


    /**
     * 删除图片分类
     */
    @ApiOperation(value = "删除图片分类")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "cateId", value = "图片分类id", required = true)
    @RequestMapping(value = "/system/imageCate/{cateId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long cateId) {

        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        //查找cateId对应的信息
        Optional<SystemResourceCateVO> cateInfoOptional = this.queryCateInfoByCateId(cateId);

        SystemResourceCateDelByIdRequest delByIdReq = new SystemResourceCateDelByIdRequest();
        delByIdReq.setCateId(cateId);
        BaseResponse response = systemResourceCateSaveProvider.deleteById(delByIdReq);

        //操作日志记录
        cateInfoOptional.ifPresent(map -> operateLogMQUtil.convertAndSend("设置", "删除分类", "删除分类：" + map.getCateName()));
        return response;
    }

    /**
     * 根据cateId查询CateInfo信息
     *
     * @param cateId
     * @return
     */
    private Optional<SystemResourceCateVO> queryCateInfoByCateId(Long cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        SystemResourceCateByIdRequest idReq = new SystemResourceCateByIdRequest();
        idReq.setCateId(cateId);
        BaseResponse<SystemResourceCateByIdResponse> response = systemResourceCateQueryProvider.getById(idReq);
        SystemResourceCateVO systemResourceCateVO = response.getContext().getSystemResourceCateVO();
        if (Objects.isNull(systemResourceCateVO)) {
            return Optional.empty();
        }
        return Optional.of(systemResourceCateVO);
    }
}
