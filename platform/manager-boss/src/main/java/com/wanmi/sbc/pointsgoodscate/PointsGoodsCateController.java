package com.wanmi.sbc.pointsgoodscate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.pointsgoodscate.PointsGoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoodscate.PointsGoodsCateSaveProvider;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.*;
import com.wanmi.sbc.goods.api.response.pointsgoodscate.*;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Api(description = "积分商品分类表管理API", tags = "PointsGoodsCateController")
@RestController
@RequestMapping(value = "/pointsgoodscate")
public class PointsGoodsCateController {

    @Autowired
    private PointsGoodsCateQueryProvider pointsGoodsCateQueryProvider;

    @Autowired
    private PointsGoodsCateSaveProvider pointsGoodsCateSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @ApiOperation(value = "分页查询积分商品分类表")
    @PostMapping("/page")
    public BaseResponse<PointsGoodsCatePageResponse> getPage(@RequestBody @Valid PointsGoodsCatePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("cateId", "desc");
        return pointsGoodsCateQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询积分商品分类表")
    @PostMapping("/list")
    public BaseResponse<PointsGoodsCateListResponse> getList(@RequestBody @Valid PointsGoodsCateListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("cateId", "desc");
        return pointsGoodsCateQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询积分商品分类表")
    @GetMapping("/{cateId}")
    public BaseResponse<PointsGoodsCateByIdResponse> getById(@PathVariable Integer cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsGoodsCateByIdRequest idReq = new PointsGoodsCateByIdRequest();
        idReq.setCateId(cateId);
        return pointsGoodsCateQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增积分商品分类表")
    @PostMapping("/add")
    public BaseResponse<PointsGoodsCateAddResponse> add(@RequestBody @Valid PointsGoodsCateAddRequest addReq) {
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());

        operateLogMQUtil.convertAndSend("营销", "积分商品分类", "添加积分商品分类: " + addReq.getCateName());
        return pointsGoodsCateSaveProvider.add(addReq);
    }

    @ApiOperation(value = "修改积分商品分类表")
    @PutMapping("/modify")
    public BaseResponse<PointsGoodsCateModifyResponse> modify(@RequestBody @Valid PointsGoodsCateModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        operateLogMQUtil.convertAndSend("营销", "积分商品分类", "修改积分商品分类: " + modifyReq.getCateName());
        return pointsGoodsCateSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除积分商品分类表")
    @DeleteMapping("/{cateId}")
    public BaseResponse deleteById(@PathVariable Integer cateId) {
        if (cateId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsGoodsCateDelByIdRequest delByIdReq = new PointsGoodsCateDelByIdRequest();
        delByIdReq.setCateId(cateId);
        operateLogMQUtil.convertAndSend("营销", "积分商品分类", "删除积分商品分类Id: " + cateId);
        return pointsGoodsCateSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "积分商品分类拖拽排序")
    @PutMapping(value = "/editSort")
    public BaseResponse editSort(@RequestBody PointsGoodsCateSortRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setUpdateTime(LocalDateTime.now());
        return pointsGoodsCateSaveProvider.editSort(request);
    }
}
