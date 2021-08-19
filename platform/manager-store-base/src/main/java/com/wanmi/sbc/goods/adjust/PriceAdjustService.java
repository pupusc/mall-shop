package com.wanmi.sbc.goods.adjust;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.goods.adjust.request.PriceAdjustConfirmRequest;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.adjustprice.PriceAdjustmentImportProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceConfirmRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceNowRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordAddRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordByIdRequest;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailAddBatchRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsIntervalPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsLevelPriceDTO;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import com.wanmi.sbc.quartz.service.TaskJobService;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunFileDeleteRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>商品批量改价Store web层通用逻辑</p>
 * Created by of628-wenzhi on 2020-12-14-2:14 下午.
 */
@Service
@Slf4j
public class PriceAdjustService {

    @Value("${yun.file.path.env.profile}")
    private String env;

    private final static String PRICE_ADJUST_DIR = "price_adjust_dir";

    private final static int IMPORT_FILE_MAX_SIZE = 5;

    private final static int IMPORT_COUNT_LIIT = 1000;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private PriceAdjustmentRecordDetailProvider detailProvider;

    @Autowired
    private PriceAdjustmentRecordProvider recordProvider;

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private TaskJobService taskJobService;

    @Autowired
    private PriceAdjustmentImportProvider adjustmentImportProvider;

    @Autowired
    private PriceAdjustmentRecordQueryProvider priceAdjustmentRecordQueryProvider;

    @Autowired
    private PriceAdjustExcuteService priceAdjustExcuteService;

    /**
     * 批量设价文件上传
     *
     * @param uploadFile
     * @return
     */
    public String uploadPriceAdjustFile(MultipartFile uploadFile) {
        if (uploadFile.isEmpty()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
        }

        String fileExt = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if (!(fileExt.equalsIgnoreCase("xls") || fileExt.equalsIgnoreCase("xlsx"))) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_EXT_ERROR);
        }

        if (uploadFile.getSize() > IMPORT_FILE_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_FILE_MAX_SIZE});
        }
        //price_adjust_dir/{env}/{storeId}/{customerId}
        String resourceKey = String.format("%s/%s/%s/%s", PRICE_ADJUST_DIR, env, commonUtil.getStoreId(),
                commonUtil.getOperatorId());
        try {
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(uploadFile.getBytes())
                    .resourceName(uploadFile.getOriginalFilename())
                    .resourceKey(resourceKey)
                    .build();
            yunServiceProvider.uploadFileExcel(yunUploadResourceRequest);
        } catch (IOException e) {
            log.error("批量设价文件上传失败,resourceKey={}", resourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        return fileExt;
    }


    /**
     * 批量设价文件确认导入
     *
     * @param analysisFunction 数据校验与解析逻辑
     * @param type             调价类型
     * @return
     */
    public String importPriceAdjustFile(Function<Workbook, List<PriceAdjustmentRecordDetailDTO>> analysisFunction,
                                        PriceAdjustmentType type) {
        //price_adjust_dir/{env}/{storeId}/{customerId}
        String resourceKey = String.format("%s/%s/%s/%s", PRICE_ADJUST_DIR, env, commonUtil.getStoreId(),
                commonUtil.getOperatorId());
        //下载文件
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(resourceKey)
                .build()).getContext().getContent();

        if (Objects.isNull(content)) {
            throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
        }
        if (content.length > Constants.IMPORT_GOODS_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_FILE_MAX_SIZE});
        }
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }
            //检测模板正确性
            this.checkExcel(workbook, type);


            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }
            // 不算第一行
            if (lastRowNum > IMPORT_COUNT_LIIT ) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, new Object[]{IMPORT_COUNT_LIIT});
            }
            //解析数据
            List<PriceAdjustmentRecordDetailDTO> details;
            try {
                details = analysisFunction.apply(workbook);
            } catch (SbcRuntimeException se) {
                if (CommonErrorCode.IMPORTED_DATA_ERROR.equals(se.getErrorCode())) {
                    //云服务删除原文件
                    yunServiceProvider.deleteFile(new YunFileDeleteRequest(Collections.singletonList(resourceKey)));
                    //上传错误提示excel文件
                    String errorFileUrl = uploadErrorFile(resourceKey, workbook);
                    se.setData(errorFileUrl);
                }
                throw se;
            }

            //改价记录入库
            String recordId = saveRecord(details.size(), type).getId();

            //改价详情入库
            saveDetails(details, type, recordId, commonUtil.getStoreId());

            //云服务删除原文件
            yunServiceProvider.deleteFile(new YunFileDeleteRequest(Collections.singletonList(resourceKey)));
            return recordId;
        } catch (SbcRuntimeException e) {
            log.error("批量设价文件导入异常，resourceKey={}", resourceKey, e);
            if (CommonErrorCode.FAILED.equals(e.getErrorCode())){
                throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
            }
            throw e;
        } catch (Exception e) {
            log.error("批量设价文件导入异常，resourceKey={},异常信息：{}", resourceKey, e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
        }
    }

    /**
     * 确认调价通用逻辑
     *
     * @param request
     */
    @Transactional
    public void adjustPriceConfirm(PriceAdjustConfirmRequest request) {
        Long storeId = commonUtil.getStoreId();
        if (request.getIsNow()) {
            //立即生效
            detailProvider.adjustPriceNow(new AdjustPriceNowRequest(request.getAdjustNo(), storeId));
            //执行调价
            priceAdjustExcuteService.excuteAdjustPrice(request, storeId);
        } else {
            //调价单确认
            detailProvider.adjustPriceConfirm(new AdjustPriceConfirmRequest(request.getAdjustNo(), storeId, request.getStartTime()));
            //定时任务延时执行
            taskJobService.priceAdjustTaskJob(request.getAdjustNo(), request.getStartTime());
        }
    }

    /**
     * 调价模板下载
     *
     * @param request
     */
    public void downloadAdjustPriceTemplate(PriceAdjustmentTemplateExportRequest request, String templateFileName) {
        request.setStoreId(commonUtil.getStoreId());
        String file = adjustmentImportProvider.exportAjustmentPriceTemplate(request).getContext().getFileOutputStream();
        if (StringUtils.isNotBlank(file)) {
            try {
                String fileName = URLEncoder.encode(templateFileName, "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            } catch (Exception e) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED);
            }
        }
    }

    /**
     * 下载错误表格
     *
     * @return
     */
    public void downErrorFile(String ext) {

        if (!(ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx"))) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_EXT_ERROR);
        }

        String errorResourceKey = String.format("%s/%s/%s/%s", PRICE_ADJUST_DIR, env, commonUtil.getStoreId(),
                commonUtil.getOperatorId()).concat("_error");
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(errorResourceKey)
                .build()).getContext().getContent();
        if (content == null) {
            throw new SbcRuntimeException(CommonErrorCode.ERROR_FILE_LOST);
        }
        try (
                InputStream is = new ByteArrayInputStream(content);
                ServletOutputStream os = HttpUtil.getResponse().getOutputStream()
        ) {
            //下载错误文档时强制清除页面文档缓存\
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

    /**
     * 越权校验
     *
     * @param adjustRecordNo
     */
    public void checkOperator(String adjustRecordNo) {
        Operator operator = commonUtil.getOperator();
        PriceAdjustmentRecordVO data = priceAdjustmentRecordQueryProvider.getById(new PriceAdjustmentRecordByIdRequest(adjustRecordNo,
                Long.valueOf(operator.getStoreId()))).getContext().getPriceAdjustmentRecordVO();

        if (Objects.isNull(data) || !data.getCreatePerson().equals(operator.getUserId())) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
    }

    /**
     * 保存调价记录
     *
     * @param skuSize
     * @param type
     */
    private PriceAdjustmentRecordVO saveRecord(long skuSize, PriceAdjustmentType type) {
        Operator operator = commonUtil.getOperator();
        PriceAdjustmentRecordAddRequest request = PriceAdjustmentRecordAddRequest.builder()
                .id(generatorService.generateAPId())
                .confirmFlag(0)
                .createPerson(operator.getUserId())
                .creatorAccount(operator.getAccount())
                .storeId(Long.valueOf(operator.getStoreId()))
                .creatorName(operator.getName())
                .priceAdjustmentType(type)
                .goodsNum(skuSize)
                .build();
        return recordProvider.add(request).getContext().getPriceAdjustmentRecordVO();
    }

    /**
     * 保存调价详情
     *
     * @param details
     */
    private void saveDetails(List<PriceAdjustmentRecordDetailDTO> details, PriceAdjustmentType type, String recordId, Long storeId) {
        List<String> skuNos = details.stream().map(PriceAdjustmentRecordDetailDTO::getGoodsInfoNo).collect(Collectors.toList());
        Map<String, GoodsInfoMarketingPriceDTO> marketingPriceMap = goodsInfoQueryProvider.listMarketingPriceByNos(new GoodsInfoMarketingPriceByNosRequest(
                skuNos, storeId)).getContext().getDataList().stream().collect(Collectors.toMap(GoodsInfoMarketingPriceDTO::getGoodsInfoNo, Function.identity()));
        switch (type) {
            case MARKET:
                buildMarketPriceDetails(details, marketingPriceMap, recordId);
                break;
            case SUPPLY:
                buildSupplyPriceDetails(details, marketingPriceMap, recordId);
                break;
            case LEVEL:
                buildLevelPriceDetails(details, marketingPriceMap, recordId);
                break;
            case STOCK:
                buildIntervalPriceDetails(details, marketingPriceMap, recordId);
                break;
            default:
        }
        //批量保存调价详情
        detailProvider.addBatch(new PriceAdjustmentRecordDetailAddBatchRequest(details));
    }

    private void buildIntervalPriceDetails(List<PriceAdjustmentRecordDetailDTO> details, Map<String, GoodsInfoMarketingPriceDTO> marketingPriceMap, String recordId) {
        details.forEach(i -> {
            GoodsInfoMarketingPriceDTO item = marketingPriceMap.get(i.getGoodsInfoNo());
            i.setGoodsInfoId(item.getGoodsInfoId());
            i.setGoodsId(item.getGoodsId());
            //设置SKU原市场价
            i.setOriginalMarketPrice(item.getMarketingPrice() != null ? item.getMarketingPrice() : BigDecimal.ZERO);
            //设置区间价
            List<GoodsIntervalPriceDTO> intervalPriceList = JSONObject.parseArray(i.getIntervalPrice(), GoodsIntervalPriceDTO.class);
            intervalPriceList.forEach(l -> {
                l.setGoodsId(item.getGoodsId());
                l.setGoodsInfoId(item.getGoodsInfoId());
            });
            i.setIntervalPrice(JSONObject.toJSONString(intervalPriceList));
            //设置状态
            i.setConfirmFlag(DefaultFlag.NO);
            i.setAdjustResult(PriceAdjustmentResult.UNDO);
            i.setPriceAdjustmentNo(recordId);
        });
        //相同SPU下的SKU，出现不同的销售类型则以第一个SKU的销售类型覆盖
        saleTypeReset(details);
    }


    private void buildLevelPriceDetails(List<PriceAdjustmentRecordDetailDTO> details, Map<String, GoodsInfoMarketingPriceDTO> marketingPriceMap, String recordId) {
        details.forEach(i -> {
            GoodsInfoMarketingPriceDTO item = marketingPriceMap.get(i.getGoodsInfoNo());
            i.setGoodsInfoId(item.getGoodsInfoId());
            i.setGoodsId(item.getGoodsId());
            //设置SKU原市场价
            i.setOriginalMarketPrice(item.getMarketingPrice() != null ? item.getMarketingPrice() : BigDecimal.ZERO);
            //设置级别价
            List<GoodsLevelPriceDTO> levelPriceList = JSONObject.parseArray(i.getLeverPrice(), GoodsLevelPriceDTO.class);
            levelPriceList.forEach(l -> {
                l.setGoodsId(item.getGoodsId());
                l.setGoodsInfoId(item.getGoodsInfoId());
            });
            i.setLeverPrice(JSONObject.toJSONString(levelPriceList));
            //设置状态
            i.setConfirmFlag(DefaultFlag.NO);
            i.setAdjustResult(PriceAdjustmentResult.UNDO);
            i.setPriceAdjustmentNo(recordId);
        });
        //相同SPU下的SKU，出现不同的销售类型则以第一个SKU的销售类型覆盖
        saleTypeReset(details);
    }

    /**
     * 市场价
     *
     * @param details
     * @return
     */
    private void buildMarketPriceDetails(List<PriceAdjustmentRecordDetailDTO> details, Map<String,
            GoodsInfoMarketingPriceDTO> marketingPriceMap, String recordId) {

        details.forEach(i -> {
            GoodsInfoMarketingPriceDTO item = marketingPriceMap.get(i.getGoodsInfoNo());
            i.setGoodsInfoId(item.getGoodsInfoId());
            i.setGoodsId(item.getGoodsId());
            //设置SKU原市场价
            i.setOriginalMarketPrice(item.getMarketingPrice() != null ? item.getMarketingPrice() : BigDecimal.ZERO);
            //设置差价
            i.setPriceDifference(i.getAdjustedMarketPrice().subtract(i.getOriginalMarketPrice()));
            i.setConfirmFlag(DefaultFlag.NO);
            i.setAdjustResult(PriceAdjustmentResult.UNDO);
            i.setPriceAdjustmentNo(recordId);
        });
        //相同SPU下的SKU，出现不同的销售类型则以第一个SKU的销售类型覆盖
        saleTypeReset(details);

    }

    /**
     * 供货价
     *
     * @param details
     * @param marketingPriceMap
     * @return
     */
    private void buildSupplyPriceDetails(List<PriceAdjustmentRecordDetailDTO> details, Map<String,
            GoodsInfoMarketingPriceDTO> marketingPriceMap, String recordId) {
        details.forEach(i -> {
            GoodsInfoMarketingPriceDTO item = marketingPriceMap.get(i.getGoodsInfoNo());
            i.setGoodsInfoId(item.getGoodsInfoId());
            i.setGoodsId(item.getGoodsId());
            i.setConfirmFlag(DefaultFlag.NO);
            i.setAdjustResult(PriceAdjustmentResult.UNDO);
            i.setPriceAdjustmentNo(recordId);
            i.setGoodsInfoName(item.getGoodsInfoName());
            i.setSupplyPrice(item.getSupplyPrice() == null ? BigDecimal.ZERO : item.getSupplyPrice());
            i.setPriceDifference(i.getAdjustSupplyPrice().subtract(i.getSupplyPrice()));

        });

    }

    /**
     * 上传改价错误提示文件
     *
     * @param resourceKey
     * @param workbook
     * @return
     */
    private String uploadErrorFile(String resourceKey, Workbook workbook) {
        String errorResourceKey = resourceKey.concat("_error");
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            workbook.write(os);
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(os.toByteArray())
                    .resourceKey(errorResourceKey)
                    .build();
            //文件校验错误，返回错误提示文件URL
            return yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
        } catch (IOException e) {
            log.error("批量改价错误文件上传至云空间失败，errorResourceKey:{}", errorResourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
        }
    }

    private void saleTypeReset(List<PriceAdjustmentRecordDetailDTO> details) {
        //相同SPU下的SKU，出现不同的销售类型则以第一个SKU的销售类型覆盖
        LinkedHashMap<String, List<PriceAdjustmentRecordDetailDTO>> detailsMap = details.stream().collect(Collectors.
                groupingBy(PriceAdjustmentRecordDetailDTO::getGoodsId, LinkedHashMap::new, Collectors.toList()));
        detailsMap.values().forEach(
                value -> {
                    PriceAdjustmentRecordDetailDTO firstData = value.get(0);
                    if (Objects.nonNull(firstData)) {
                        value.forEach(i -> i.setSaleType(firstData.getSaleType()));
                    }
                }
        );
    }

    /**
     * 验证EXCEL
     * @param workbook
     */
    public void checkExcel(Workbook workbook, PriceAdjustmentType type){
        try {
            Sheet sheet1 = workbook.getSheetAt(0);
            Row row = sheet1.getRow(0);
            if (PriceAdjustmentType.MARKET == type && !row.getCell(5).getStringCellValue().contains("调整后市场价")){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }else if(PriceAdjustmentType.LEVEL == type && !(row.getCell(5).getStringCellValue().contains("sku保持独立设价")
                    && row.getCell(6).getStringCellValue().contains("调整后等级价"))){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }else if(PriceAdjustmentType.STOCK == type && !(row.getCell(5).getStringCellValue().contains("sku保持独立设价")
                    && row.getCell(6).getStringCellValue().contains("订货区间"))){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }else if(PriceAdjustmentType.SUPPLY == type && !row.getCell(3).getStringCellValue().contains("调整后供货价")){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }
        }catch (Exception e){
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }
    }
}
