package com.wanmi.sbc.virtualcoupon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeQueryProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponQueryProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.*;
import com.wanmi.sbc.goods.api.response.virtualcoupon.*;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.virtualcoupon.service.VirtualCouponCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;


@Api(description = "券码管理API", tags = "VirtualCouponCodeController")
@RestController
@RequestMapping(value = "/virtualcouponcode")
public class VirtualCouponCodeController {

    @Autowired
    private VirtualCouponCodeQueryProvider virtualCouponCodeQueryProvider;

    @Autowired
    private VirtualCouponCodeProvider virtualCouponCodeProvider;

    @Autowired
    private VirtualCouponQueryProvider virtualCouponQueryProvider;

    @Autowired
    private VirtualCouponCodeService virtualCouponCodeService;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "分页查询券码")
    @PostMapping("/page")
    public BaseResponse<VirtualCouponCodePageResponse> getPage(@RequestBody @Valid VirtualCouponCodePageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("createTime", "desc");
        pageReq.setStoreId(commonUtil.getStoreId());
        return virtualCouponCodeQueryProvider.page(pageReq);
    }

    @ApiOperation(value = "列表查询券码")
    @PostMapping("/list")
    public BaseResponse<VirtualCouponCodeListResponse> getList(@RequestBody @Valid VirtualCouponCodeListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("createTime", "desc");
        return virtualCouponCodeQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询券码")
    @GetMapping("/{id}")
    public BaseResponse<VirtualCouponCodeByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        VirtualCouponCodeByIdRequest idReq = new VirtualCouponCodeByIdRequest();
        idReq.setId(id);
        return virtualCouponCodeQueryProvider.getById(idReq);
    }

    @ApiOperation(value = "根据id删除券码")
    @DeleteMapping("/{couponId}/{id}")
    public BaseResponse deleteById(@PathVariable Long id, @PathVariable Long couponId) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        VirtualCouponCodeDelByIdRequest delByIdReq = new VirtualCouponCodeDelByIdRequest();
        delByIdReq.setId(id);
        delByIdReq.setCouponId(couponId);
        delByIdReq.setUserId(commonUtil.getOperatorId());
        delByIdReq.setStoreId(commonUtil.getStoreId());
        return virtualCouponCodeProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除券码")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid VirtualCouponCodeDelByIdListRequest delByIdListReq) {
        delByIdListReq.setUpdatePerson(commonUtil.getOperatorId());
        delByIdListReq.setStoreId(commonUtil.getStoreId());
        return virtualCouponCodeProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出券码列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        VirtualCouponCodeListRequest listReq = JSON.parseObject(decrypted, VirtualCouponCodeListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("createTime", "desc");
        listReq.setPageNum(0);
        listReq.setPageSize(5000);
        VirtualCouponVO virtualCouponVO = virtualCouponQueryProvider.getById(VirtualCouponByIdRequest.builder().id(listReq.getCouponId()).storeId(commonUtil.getStoreId()).build()).getContext().getVirtualCouponVO();
        List<VirtualCouponCodeVO> dataRecords = virtualCouponCodeQueryProvider.list(listReq).getContext().getVirtualCouponCodeVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("券码列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream(), virtualCouponVO);
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<VirtualCouponCodeVO> dataRecords, OutputStream outputStream, VirtualCouponVO virtualCouponVO) {
        ExcelHelper excelHelper = new ExcelHelper();

        String provideTypeName = "兑换码";
        if (virtualCouponVO.getProvideType() == 1) {
            provideTypeName = "券码";
        } else if (virtualCouponVO.getProvideType() == 2) {
            provideTypeName = "链接";
        }
        dataRecords.forEach(e -> {
            e.setExportTime(LocalDateTime.now());
            if (e.getStatus() == 0) {
                e.setExportStatus("未发放");
            } else if (e.getStatus() == 1) {
                e.setExportStatus("已发放");
            } else {
                e.setExportStatus("已过期");
            }
        });

        Column[] columns = {
                new Column(provideTypeName, new SpelColumnRender<VirtualCouponCodeVO>("couponNo")),
                new Column("批次号", new SpelColumnRender<VirtualCouponCodeVO>("batchNo")),
                new Column("有效期", new SpelColumnRender<VirtualCouponCodeVO>("validDays")),
                new Column("领取结束时间", new SpelColumnRender<VirtualCouponCodeVO>("receiveEndTime")),
                new Column("兑换开始时间", new SpelColumnRender<VirtualCouponCodeVO>("exchangeStartTime")),
                new Column("兑换结束时间", new SpelColumnRender<VirtualCouponCodeVO>("exchangeEndTime")),
                new Column("状态", new SpelColumnRender<VirtualCouponCodeVO>("exportStatus")),
                new Column("订单号", new SpelColumnRender<VirtualCouponCodeVO>("tid")),
                new Column("导出时间", new SpelColumnRender<VirtualCouponCodeVO>("exportTime")),
        };
        excelHelper.addSheet("券码列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

    /**
     * 下载模板
     */
    @ApiOperation(value = "下载模板")
    @RequestMapping(value = "/excel/template/{couponId}/{encrypted}", method = RequestMethod.GET)
    public void template(@PathVariable Long couponId, @PathVariable String encrypted) {
        VirtualCouponCodePageRequest request = new VirtualCouponCodePageRequest();
        request.setCouponId(couponId);
        request.setStoreId(commonUtil.getStoreId());
        String file = virtualCouponCodeQueryProvider.virtualCouponCodeExcelExportTemplate(request).getContext().getFile();

        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("卡券导入模板.xlsx", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * excel文件上传
     *
     * @param uploadFile
     * @return
     */
    @ApiOperation(value = "excel文件上传")
    @PostMapping(value = "/excel/upload")
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile, @RequestParam("couponId") String couponId) {
        return BaseResponse.success(virtualCouponCodeService.uploadFile(uploadFile, Long.parseLong(couponId)));
    }

    /**
     * 确认导入商品
     */
    @ApiOperation(value = "确认导入商品")
    @RequestMapping(value = "/excel/import/{couponId}", method = RequestMethod.GET)
    public BaseResponse<String> importExcel(@PathVariable Long couponId) {
        return BaseResponse.success(virtualCouponCodeService.importFile(couponId));
    }

    @ApiOperation(value = "下载模板")
    @RequestMapping(value = "/excel/err/{couponId}/{encrypted}", method = RequestMethod.GET)
    public void errExcelDownload(@PathVariable Long couponId, @PathVariable String encrypted) {
        VirtualCouponCodePageRequest request = new VirtualCouponCodePageRequest();
        request.setCouponId(couponId);
        request.setStoreId(commonUtil.getStoreId());
        String file = virtualCouponCodeService.exportErrExcel(couponId);
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode("错误表格.xlsx", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }
}
