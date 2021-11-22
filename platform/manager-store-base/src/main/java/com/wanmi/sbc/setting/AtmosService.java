package com.wanmi.sbc.setting;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.ResultCode;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsAtmosphereRequest;
import com.wanmi.sbc.goods.adjust.PriceAdjustExcuteService;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.adjustprice.PriceAdjustmentImportProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.quartz.service.TaskJobService;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereDeleteRequest;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunFileDeleteRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
public class AtmosService {

    @Value("${yun.file.path.env.profile}")
    private String env;

    private final static String ATMOS_DIR = "atoms_setting_dir";

    private final static int IMPORT_FILE_MAX_SIZE = 5;

    private final static int IMPORT_COUNT_LIIT = 1000;
    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private AtmosphereProvider atmosphereProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;


    /**
     * 氛围文件上传
     *
     * @param uploadFile
     * @return
     */
    public String uploadAtmosFile(MultipartFile uploadFile) {
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
        String resourceKey = String.format("%s/%s/%s/%s", ATMOS_DIR, env, commonUtil.getStoreId(),commonUtil.getOperatorId());
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
            log.error("批量上传氛围失败,resourceKey={}", resourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        return fileExt;
    }


    /**
     * 氛围确认导入
     *
     * @param analysisFunction 数据校验与解析逻辑
     * @return
     */
    public String importAtmosFile(Function<Workbook, List<AtmosphereDTO>> analysisFunction) {
        String resourceKey = String.format("%s/%s/%s/%s", ATMOS_DIR, env, commonUtil.getStoreId(),
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

            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }
            // 不算第一行
            if (lastRowNum > IMPORT_COUNT_LIIT ) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, new Object[]{IMPORT_COUNT_LIIT});
            }
            //解析数据
            List<AtmosphereDTO> details;
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
            saveAtmosConfig(details);
            //云服务删除原文件
            yunServiceProvider.deleteFile(new YunFileDeleteRequest(Collections.singletonList(resourceKey)));
            return "";
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
            log.error("氛围配置错误文件上传至云空间失败，errorResourceKey:{}", errorResourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
        }
    }

    private void saveAtmosConfig(List<AtmosphereDTO> details){
        atmosphereProvider.add(details);
    }

    public BaseResponse<MicroServicePage<AtmosphereDTO>> page(AtmosphereQueryRequest request){
        return atmosphereProvider.page(request);
    }

    public BaseResponse delete(AtmosphereDeleteRequest request){
        BaseResponse response =  atmosphereProvider.delete(request);
        if(response!= null && response.getCode().equals(ResultCode.SUCCESSFUL)){
            //更新es-如果当前是最新一条记录
            EsGoodsAtmosphereRequest esRequest = new EsGoodsAtmosphereRequest();
            esRequest.setGoodsInfoIds(Arrays.asList(request.getSkuId()));
            esGoodsInfoElasticProvider.disableAtmosphere(esRequest);
        }
        return BaseResponse.SUCCESSFUL();
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

        String errorResourceKey = String.format("%s/%s/%s/%s", ATMOS_DIR, env, commonUtil.getStoreId(),
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

}
