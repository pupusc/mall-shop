package com.wanmi.sbc.customer;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.customer.request.CustomerImportExcelRequest;
import com.wanmi.sbc.customer.response.CustomerImportExcelResponse;
import com.wanmi.sbc.customer.service.CustomerImportExcelService;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunGetResourceResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;


/**
 * 会员导入
 */
@Api(description = "会员导入API", tags = "CustomerImportController")
@RestController
@RequestMapping("/customer/customerImport")
@Slf4j
public class CustomerImportController {

    @Value("classpath:customer_import_template.xls")
    private Resource templateFile;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CustomerImportExcelService customerImportExcelService;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    /**
     * 会员导入模板下载
     *
     * @param encrypted
     */
    @ApiOperation(value = "会员导入模板下载")
    @RequestMapping(value = "/downloadTemplate/{encrypted}", method = RequestMethod.GET)
    public void export(@PathVariable String encrypted) {
        if (templateFile == null || !templateFile.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        InputStream is = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Workbook wk = null;
        try {
            is = templateFile.getInputStream();
            wk = WorkbookFactory.create(is);
            wk.write(byteArrayOutputStream);
            String file = new BASE64Encoder().encode(byteArrayOutputStream.toByteArray());
            if (StringUtils.isNotBlank(file)) {
                String fileName = URLEncoder.encode("会员导入模板.xls", "UTF-8");
                HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";" +
                        "filename*=\"utf-8''%s\"", fileName, fileName));
                HttpUtil.getResponse().getOutputStream().write(new BASE64Decoder().decodeBuffer(file));
            }
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                log.error("会员导入模板转Base64位异常", e);
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("读取会员导入模板异常", e);
                }
            }
            try {
                if(wk != null){
                    wk.close();
                }
            }catch (IOException e){
                log.error("会员导入模板下载关闭Workbook异常", e);
            }

        }
    }

    /**
     * 上传文件
     */
    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/excel/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(this.upload(uploadFile, commonUtil.getOperatorId()));
    }

    /**
     * 确认导入会员
     *
     * @param ext 文件格式 {@link String}
     * @return
     */
    @ApiOperation(value = "确认导入会员")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext",
            value = "文件名后缀", required = true)
    @RequestMapping(value = "/import/{ext}/{sendMsgFlag}", method = RequestMethod.GET)
    public BaseResponse<CustomerImportExcelResponse> importCustomer(@PathVariable String ext, @PathVariable Boolean sendMsgFlag) {
        if (!("xls".equalsIgnoreCase(ext) || "xlsx".equalsIgnoreCase(ext))) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        CustomerImportExcelRequest excelRequest = new CustomerImportExcelRequest();
        excelRequest.setExt(ext);
        excelRequest.setSendMsgFlag(sendMsgFlag);
        excelRequest.setUserId(commonUtil.getOperatorId());
        CustomerImportExcelResponse response = customerImportExcelService.importCustomer(excelRequest);
        //操作日志记录
        operateLogMQUtil.convertAndSend("会员导入", "批量导入", "批量导入");
        return BaseResponse.success(response);
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
        this.downloadErrExcel(commonUtil.getOperatorId(), ext);
    }

    /**
     * 上传文件
     *
     * @param file   文件
     * @param userId 操作员id
     * @return 文件格式
     */
    private String upload(MultipartFile file, String userId) {
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

        try {
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(file.getBytes())
                    .resourceName(file.getOriginalFilename())
                    .resourceKey(Constants.CUSTOMER_IMPORT_EXCEL_DIR.concat(userId))
                    .build();
            yunServiceProvider.uploadFile(yunUploadResourceRequest);
        } catch (IOException e) {
            log.error("上传文件到云失败{}", userId, e);
        }
        return fileExt;
    }

    /**
     * 下载Excel错误文档
     *
     * @param userId 用户Id
     * @param ext    文件扩展名
     */
    private void downloadErrExcel(String userId, String ext) {
        YunGetResourceResponse yunGetResourceResponse = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(Constants.CUSTOMER_IMPORT_EXCEL_ERR_DIR.concat(userId))
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
