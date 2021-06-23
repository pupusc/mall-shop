package com.wanmi.sbc.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsExcelProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.pointsgoods.*;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsPageResponse;
import com.wanmi.sbc.pointsgoods.request.PointsGoodsImportExcelRequest;
import com.wanmi.sbc.pointsgoods.service.PointsGoodsImportExcelService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;


@Api(description = "积分商品表管理API", tags = "PointsGoodsController")
@RestController
@RequestMapping(value = "/pointsgoods")
public class PointsGoodsController {

    @Autowired
    private PointsGoodsQueryProvider pointsGoodsQueryProvider;

    @Autowired
    private PointsGoodsSaveProvider pointsGoodsSaveProvider;

    @Autowired
    private PointsGoodsExcelProvider pointsGoodsExcelProvider;

    @Autowired
    private PointsGoodsImportExcelService pointsGoodsImportExcelService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询积分商品表")
    @PostMapping("/page")
    public BaseResponse<PointsGoodsPageResponse> page(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq) {
        pointsGoodsPageReq.putSort("beginTime", SortType.DESC.toValue());
        return pointsGoodsQueryProvider.pageNew(pointsGoodsPageReq);
    }

    @ApiOperation(value = "根据id查询积分商品表")
    @GetMapping("/{pointsGoodsId}")
    public BaseResponse<PointsGoodsByIdResponse> getById(@PathVariable String pointsGoodsId) {
        if (pointsGoodsId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsGoodsByIdRequest idReq = new PointsGoodsByIdRequest();
        idReq.setPointsGoodsId(pointsGoodsId);
        return pointsGoodsQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "新增积分商品表")
    @PostMapping("/add")
    public BaseResponse<PointsGoodsAddResponse> add(@RequestBody @Valid PointsGoodsAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        return pointsGoodsSaveProvider.add(addReq);
    }

    @ApiOperation(value = "批量新增积分商品")
    @PostMapping("/batchAdd")
    public BaseResponse batchAdd(@RequestBody @Valid PointsGoodsAddListRequest request) {
        List<PointsGoodsAddRequest> addRequestList = request.getPointsGoodsAddRequestList();
        addRequestList.forEach(addRequest -> {
            addRequest.setBeginTime(request.getBeginTime());
            addRequest.setEndTime(request.getEndTime());
            addRequest.setStatus(EnableStatus.ENABLE);
            addRequest.setDelFlag(DeleteFlag.NO);
            addRequest.setSales((long) 0);
            addRequest.setCreatePerson(commonUtil.getOperatorId());
            addRequest.setCreateTime(LocalDateTime.now());
        });
        operateLogMQUtil.convertAndSend("营销", "积分商品列表", "添加积分商品");
        pointsGoodsSaveProvider.batchAdd(request);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改积分商品表")
    @PutMapping("/modify")
    public BaseResponse<PointsGoodsModifyResponse> modify(@RequestBody @Valid PointsGoodsModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return pointsGoodsSaveProvider.modify(modifyReq);
    }

    @ApiOperation(value = "根据id删除积分商品表")
    @DeleteMapping("/{pointsGoodsId}")
    public BaseResponse deleteById(@PathVariable String pointsGoodsId) {
        if (pointsGoodsId == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        PointsGoodsDelByIdRequest delByIdReq = new PointsGoodsDelByIdRequest();
        delByIdReq.setPointsGoodsId(pointsGoodsId);
        return pointsGoodsSaveProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除积分商品表")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteById(@RequestBody @Valid PointsGoodsDelByIdListRequest delByIdListReq) {
        return pointsGoodsSaveProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "启用停用积分商品")
    @PutMapping("/modifyStatus")
    public BaseResponse modifyStatus(@RequestBody @Valid PointsGoodsSwitchRequest request) {
        request.setUpdatePerson(commonUtil.getOperatorId());
        request.setUpdateTime(LocalDateTime.now());
        return pointsGoodsSaveProvider.modifyStatus(request);
    }

    @ApiOperation(value = "下载积分商品模板")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "加密", required = true)
    @RequestMapping(value = "excel/template/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable String encrypted) {
        String file = pointsGoodsExcelProvider.exportTemplate().getContext().getFile();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("积分商品导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(pointsGoodsImportExcelService.upload(uploadFile, commonUtil.getOperatorId()));
    }

    /**
     * 确认导入积分商品
     *
     * @param ext 文件格式 {@link String}
     * @return
     */
    @ApiOperation(value = "确认导入商品库商品")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext",
            value = "文件名后缀", required = true)
    @RequestMapping(value = "/import/{ext}", method = RequestMethod.GET)
    public BaseResponse<Boolean> implGoods(@PathVariable String ext) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        PointsGoodsImportExcelRequest pointsGoodsImportExcelRequest = new PointsGoodsImportExcelRequest();
        pointsGoodsImportExcelRequest.setExt(ext);
        pointsGoodsImportExcelRequest.setUserId(commonUtil.getOperatorId());
        pointsGoodsImportExcelService.implGoods(pointsGoodsImportExcelRequest);
        //操作日志记录
        operateLogMQUtil.convertAndSend("积分商品", "批量导入", "批量导入");
        return BaseResponse.success(Boolean.TRUE);
    }

    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "ext", value = "后缀", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "decrypted", value = "解密", required = true)
    })
    @RequestMapping(value = "/excel/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        pointsGoodsImportExcelService.downErrExcel(commonUtil.getOperatorId(), ext);
    }
}
