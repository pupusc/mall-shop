package com.wanmi.sbc.goods.adjust;

import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>商品批量改价文件上传通用逻辑</p>
 * Created by of628-wenzhi on 2020-12-14-2:14 下午.
 */
@Service
@Slf4j
public class PriceAdjustUploadService {

    @Value("${yun.file.path.env.profile}")
    private String env;

    private final static String MARKETING_PRICE_ADJUST_DIR = "price_adjust_dir";

    private final static int IMPORT_FILE_MAX_SIZE = 5;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 批量设价文件上传
     *
     * @param uploadFile
     * @return
     */
    public String uploadMarketingPriceFile(MultipartFile uploadFile) {
        if (uploadFile.isEmpty()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
        }
        if (uploadFile.getSize() > IMPORT_FILE_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_FILE_MAX_SIZE});
        }
        String resourceUrl;
        //marketing_price_adjust_dir/{env}/{storeId}/{customerId}
        String resourceKey = String.format("%s/%s/%s/%s", MARKETING_PRICE_ADJUST_DIR, env, commonUtil.getStoreId(),
                commonUtil.getOperatorId());
        try {
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(uploadFile.getBytes())
                    .resourceName(uploadFile.getOriginalFilename())
                    .resourceKey(resourceKey)
                    .build();
            resourceUrl = yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
        } catch (IOException e) {
            log.error("批量设价文件上传失败,resourceKey={}", resourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        return resourceUrl;
    }
}
