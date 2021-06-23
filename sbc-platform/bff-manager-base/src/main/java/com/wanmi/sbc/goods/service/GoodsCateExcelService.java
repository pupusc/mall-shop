package com.wanmi.sbc.goods.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateExcelProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateExcelImportRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunGetResourceResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.common.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: songhanlin
 * @Date: Created In 11:15 2018-12-18
 * @Description: 商品分类excel导入导出
 */
@Slf4j
@Service
public class GoodsCateExcelService {
    /**
     * 操作日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsCateExcelService.class);

    @Value("classpath:goods_cate_template.xls")
    private Resource templateFile;

    @Autowired
    private GoodsCateExcelProvider goodsCateExcelProvider;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    public void importGoodsCate(String userId, String ext){
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(Constants.GOODS_CATE_EXCEL_DIR.concat(userId))
                .build()).getContext().getContent();

        if (content == null) {
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        } else if (content.length > Constants.IMPORT_GOODS_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{Constants.IMPORT_GOODS_MAX_SIZE});
        } else {
            try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
                Sheet sheet = workbook.getSheetAt(0);
                if (sheet == null) {
                    throw new SbcRuntimeException("K-030405");
                } else {
                    this.checkExcel(workbook);
                    int firstRowNum = sheet.getFirstRowNum();
                    int lastRowNum = sheet.getLastRowNum();
                    if (lastRowNum < 1) {
                        throw new SbcRuntimeException("K-030405");
                    } else {
                        GoodsCateListByConditionRequest goodsCateListByConditionRequest = new GoodsCateListByConditionRequest();
                        goodsCateListByConditionRequest.setDelFlag(DeleteFlag.NO.toValue());
                        List<GoodsCateVO> goodsCateList = goodsCateQueryProvider.listByCondition(goodsCateListByConditionRequest).getContext().getGoodsCateVOList();
                        if (CollectionUtils.isNotEmpty(goodsCateList)) {
                            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
                        }
                        int maxCell = 6;
                        long cateId = 1;
                        Boolean isError = false;
                        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; ++rowNum) {
                            Row row = sheet.getRow(rowNum);
                            if (row != null) {
                                Cell[] cells = new Cell[maxCell];
                                for (int i = 0; i < maxCell; i++) {
                                    Cell cell = row.getCell(i);
                                    if (cell == null) {
                                        cell = row.createCell(i);
                                    }
                                    cells[i] = cell;
                                }
                                // 一级类目
                                GoodsCateVO goodsCate = new GoodsCateVO();
                                isError = this.checkCells(workbook, cells, 0, goodsCate, isError);
                                isError = this.checkCells(workbook, cells, 1, goodsCate, isError);
                                goodsCate.setCateId(cateId);
                                goodsCate.setCateParentId(0L);
                                goodsCateList.stream().filter(cate -> cate.getCateParentId().intValue() == 0 && StringUtils.equals(goodsCate.getCateName(), cate.getCateName())).
                                        findFirst().ifPresent(cate -> {
                                    goodsCate.setCateId(null);
                                });
                                if (goodsCate.getCateId() != null) {
                                    goodsCateList.add(goodsCate);
                                    cateId++;
                                }

                                //二级类目
                                GoodsCateVO goodsCate1 = new GoodsCateVO();
                                isError = this.checkCells(workbook, cells, 2, goodsCate1, isError);
                                isError = this.checkCells(workbook, cells, 3, goodsCate1, isError);
                                GoodsCateVO parentGoodsCate = goodsCate.getCateId() == null
                                        ? goodsCateList.stream().filter(cate -> cate.getCateParentId().intValue() == 0
                                        && StringUtils.equals(cate.getCateName(), goodsCate.getCateName())).findFirst().get()
                                        : goodsCate;
                                goodsCate1.setCateId(cateId);
                                goodsCate1.setCateParentId(parentGoodsCate.getCateId());
                                goodsCateList.stream().filter(cate -> cate.getCateParentId().equals(parentGoodsCate.getCateId()) && StringUtils.equals(goodsCate1.getCateName(), cate.getCateName())).
                                        findFirst().ifPresent(cate -> {
                                    goodsCate1.setCateId(null);
                                });
                                if (goodsCate1.getCateId() != null) {
                                    goodsCateList.add(goodsCate1);
                                    cateId++;
                                }
                                // 三级类目
                                GoodsCateVO goodsCate2 = new GoodsCateVO();
                                isError = this.checkCells(workbook, cells, 4, goodsCate2, isError);
                                isError = this.checkCells(workbook, cells, 5, goodsCate2, isError);
                                GoodsCateVO parentGoodsCate1 = goodsCate1.getCateId() == null
                                        ? goodsCateList.stream().filter(cate -> cate.getCateParentId().equals(parentGoodsCate.getCateId())
                                        && StringUtils.equals(cate.getCateName(), goodsCate1.getCateName())).findFirst().get()
                                        : goodsCate1;
                                goodsCate2.setCateId(cateId);
                                goodsCate2.setCateParentId(parentGoodsCate1.getCateId());
                                goodsCateList.stream().filter(cate -> cate.getCateParentId().equals(parentGoodsCate1.getCateId()) && StringUtils.equals(goodsCate2.getCateName(), cate.getCateName()))
                                        .findFirst().ifPresent(cate -> goodsCate2.setCateId(null));
                                if (goodsCate2.getCateId() != null) {
                                    goodsCateList.add(goodsCate2);
                                    cateId++;
                                }

                            }
                        }
                        //上传文件有错误内容
                        if (isError) {
                            this.errorExcel(userId.concat(".").concat(ext), workbook);
                            throw new SbcRuntimeException("K-030404", new Object[]{ext});
                        }
                        GoodsCateExcelImportRequest goodsCateExcelImportRequest = new GoodsCateExcelImportRequest();
                        goodsCateExcelImportRequest.setGoodsCateList(goodsCateList);
                        goodsCateExcelProvider.importGoodsCate(goodsCateExcelImportRequest);


                    }
                }
            } catch (SbcRuntimeException var52) {
                log.error("商品类目导入异常", var52);
                throw var52;
            } catch (Exception var53) {
                log.error("商品类目导入异常", var53);
                throw new SbcRuntimeException("K-000001", var53);
            }
        }
    }

    public String errorExcel(String newFileName, Workbook wk) throws SbcRuntimeException {
        String userId = commonUtil.getOperatorId();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            wk.write(os);
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(os.toByteArray())
                    .resourceName(newFileName)
                    .resourceKey(Constants.GOODS_CATE_ERR_EXCEL_DIR.concat(userId))
                    .build();
            yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
            return newFileName;
        } catch (IOException e) {
            log.error("生成的错误文件上传至云空间失败", e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
    }


    public void checkExcel(Workbook workbook) {
        try {
            Sheet sheet1 = workbook.getSheetAt(0);
            Row row = sheet1.getRow(0);
            Sheet sheet2 = workbook.getSheetAt(1);
            if (!row.getCell(0).getStringCellValue().contains("一级类目名称")) {
                throw new SbcRuntimeException("K-030406");
            }
        } catch (Exception var5) {
            throw new SbcRuntimeException("K-030406");
        }
    }

    private boolean checkCells(Workbook workbook, Cell[] cells, int num, GoodsCateVO goodsCate, boolean isError) {
        if (goodsCate.getCateName() == null) {
            Double grade = Math.ceil((double) (num + 1) / 2);
            goodsCate.setCateGrade(grade.intValue());
        }
        if (num == 0 || num == 2 || num == 4) {
            if (StringUtils.isBlank(ExcelHelper.getValue(cells[num]))) {
                isError = true;
                ExcelHelper.setError(workbook, cells[num], "此项必填");
            } else if (ExcelHelper.getValue(cells[num]).trim().length() > 20) {
                isError = true;
                ExcelHelper.setError(workbook, cells[num], "长度必须1-20个字");
            } else if (ValidateUtil.containsEmoji(ExcelHelper.getValue(cells[num]))) {
                isError = true;
                ExcelHelper.setError(workbook, cells[num], "含有非法字符");
            }
            goodsCate.setCateName(ExcelHelper.getValue(cells[num]).trim());
        } else {
            if (StringUtils.isNotBlank(ExcelHelper.getValue(cells[num]))) {
                BigDecimal cateRate = null;
                try {
                    cateRate = new BigDecimal(ExcelHelper.getValue(cells[num]).trim());
                    if (cateRate.compareTo(new BigDecimal("0")) == -1 || cateRate.compareTo(new BigDecimal("100")) == 1) {
                        isError = true;
                        ExcelHelper.setError(workbook, cells[num], "请填写0-100的整数");
                    }
                } catch (Exception e) {
                    isError = true;
                    ExcelHelper.setError(workbook, cells[num], "请填写0-100的整数");
                    cateRate = null;
                }
                if (cateRate == null) {
                    goodsCate.setIsParentCateRate(DefaultFlag.YES);
                }
                goodsCate.setCateRate(cateRate == null ? BigDecimal.ZERO : cateRate);
            } else {
                if (num == 5) {
                    isError = true;
                    ExcelHelper.setError(workbook, cells[num], "此项必填");
                }
            }
        }
        return isError;
    }
    /**
     * 商品类目模板下载
     *
     * @return base64位文件字符串
     */
    public void exportTemplate() {
        if (templateFile == null || !templateFile.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        InputStream is = null;
        Workbook wk = null;
        try {
            String fileName = URLEncoder.encode("商品类目导入模板.xls", "UTF-8");
            is = templateFile.getInputStream();
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                    "filename*=\"utf-8''%s\"", fileName, fileName));
            wk = WorkbookFactory.create(is);
            wk.write(HttpUtil.getResponse().getOutputStream());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("读取导入模板异常", e);
                }
            }
            try{
                if(wk != null){
                    wk.close();
                }
            }catch (IOException e){
                LOGGER.error("读取导入模板Workbook关闭异常", e);
            }
        }
    }


    /**
     * 上传商品类目模板
     *
     * @param file
     * @param userId
     * @return
     */
    public String upload(MultipartFile file, String userId) {
        if (file == null || file.isEmpty()) {
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if (!(fileExt.equalsIgnoreCase("xls") || fileExt.equalsIgnoreCase("xlsx"))) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_EXT_ERROR);
        }

        if (file.getSize() > Constants.IMPORT_GOODS_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{Constants.IMPORT_GOODS_MAX_SIZE});
        }

        String resourceKey = Constants.GOODS_CATE_EXCEL_DIR.concat(userId);
        try {
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(file.getBytes())
                    .resourceName(file.getOriginalFilename())
                    .resourceKey(resourceKey)
                    .build();
            String resourceUrl = yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
        } catch (IOException e) {
            log.error("Excel文件上传到云空间失败->resourceKey为:".concat(resourceKey), e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        return fileExt;
    }

    /**
     * 下载Excel错误文档
     *
     * @param userId 用户Id
     * @param ext    文件扩展名
     */
    public void downErrExcel(String userId, String ext) {
        YunGetResourceResponse yunGetResourceResponse = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(Constants.GOODS_CATE_ERR_EXCEL_DIR.concat(userId))
                .build()).getContext();
        if (yunGetResourceResponse == null) {
            throw new SbcRuntimeException(CommonErrorCode.ERROR_FILE_LOST);
        }
        byte[] content = yunGetResourceResponse.getContent();
        if (content == null) {
            throw new SbcRuntimeException(CommonErrorCode.ERROR_FILE_LOST);
        }
        try (
                InputStream is = new ByteArrayInputStream(content);
                ServletOutputStream os = HttpUtil.getResponse().getOutputStream()
        ) {
            //下载错误文档时强制清除页面文档缓存
            HttpServletResponse response = HttpUtil.getResponse();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("expries", -1);
            String fileName = URLEncoder.encode("错误表格.".concat(ext), "UTF-8");
            response.setHeader("Content-Disposition",
                    String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));

            byte b[] = new byte[1024];
            //读取文件，存入字节数组b，返回读取到的字符数，存入read,默认每次将b数组装满
            int read = is.read(b);
            while (read != -1) {
                os.write(b, 0, read);
                read = is.read(b);
            }
            HttpUtil.getResponse().flushBuffer();
        } catch (Exception e) {
            log.error("下载EXCEL文件异常->", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }


    }


}
