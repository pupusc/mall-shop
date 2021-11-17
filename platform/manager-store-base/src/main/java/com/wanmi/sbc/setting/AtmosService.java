package com.wanmi.sbc.setting;

import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.goods.adjust.PriceAdjustExcuteService;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.adjustprice.PriceAdjustmentImportProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecord.PriceAdjustmentRecordQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.quartz.service.TaskJobService;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunFileDeleteRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    /**
     * 批量设价文件上传
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
}
