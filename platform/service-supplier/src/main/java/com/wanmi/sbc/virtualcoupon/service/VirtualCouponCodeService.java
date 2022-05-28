package com.wanmi.sbc.virtualcoupon.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponCodeProvider;
import com.wanmi.sbc.goods.api.provider.virtualcoupon.VirtualCouponQueryProvider;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponByIdRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponCodeAddRequest;
import com.wanmi.sbc.goods.api.response.virtualcoupon.VirtualCouponByIdResponse;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponCodeVO;
import com.wanmi.sbc.goods.bean.vo.VirtualCouponVO;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunFileDeleteRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunGetResourceResponse;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class VirtualCouponCodeService {

    @Value("${yun.file.path.env.profile}")
    private String env;

    private final static String VIRTUAL_COUPON_CODE_DIR = "virtual_coupon_code_dir";

    private final static int IMPORT_FILE_MAX_SIZE = 5;

    private final static int IMPORT_COUNT_LIIT = 2000;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private VirtualCouponQueryProvider virtualCouponQueryProvider;

    @Autowired
    private VirtualCouponCodeProvider virtualCouponCodeProvider;

    @Autowired
    private RedisTemplate redisTemplate;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 上传文件
     *
     * @param uploadFile
     * @return
     */
    public String uploadFile(MultipartFile uploadFile, Long couponId) {

        BaseResponse<VirtualCouponByIdResponse> response = virtualCouponQueryProvider.getById(VirtualCouponByIdRequest.builder().id(couponId).storeId(commonUtil.getStoreId()).build());

        if (!CommonErrorCode.SUCCESSFUL.equals(response.getCode())) {
            throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, "请返回列表页重新进入导入页!");
        }

        if (uploadFile.isEmpty()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
        }
        String fileExt = uploadFile.getOriginalFilename().substring(uploadFile.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if (!(fileExt.equalsIgnoreCase("xlsx"))) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_EXT_ERROR);
        }
        if (uploadFile.getSize() > IMPORT_FILE_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_FILE_MAX_SIZE});
        }

        String resourceKey = String.format("%s/%s/%s/%s/%s", VIRTUAL_COUPON_CODE_DIR, env, commonUtil.getStoreId(),
                commonUtil.getOperatorId(), couponId);

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
     * 确认导入文件
     */
    public String importFile(Long couponId) {

        BaseResponse<VirtualCouponByIdResponse> response = virtualCouponQueryProvider.getById(VirtualCouponByIdRequest.builder().id(couponId).storeId(commonUtil.getStoreId()).build());

        if (!CommonErrorCode.SUCCESSFUL.equals(response.getCode())) {
            throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, "请返回列表页重新进入导入页!");
        }

        VirtualCouponVO virtualCouponVO = response.getContext().getVirtualCouponVO();

        String resourceKey = String.format("%s/%s/%s/%s/%s", VIRTUAL_COUPON_CODE_DIR, env, commonUtil.getStoreId(), commonUtil.getOperatorId(), couponId);
        //下载文件
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(resourceKey)
                .build()).getContext().getContent();

        if (Objects.isNull(content)) {
            throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
        }
        if (content.length > IMPORT_FILE_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_FILE_MAX_SIZE});
        }
        //检测去重
        Set<String> set = new HashSet<>();
        //解析数据
        List<VirtualCouponCodeVO> list = new ArrayList<>();

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        String key = RedisKeyConstant.VIRTUAL_COUPON_NO_CHECK_KEY ;
        List<String> nos = new ArrayList<>();
        virtualCouponCodeProvider.initCouponCodeNoCache(couponId);
        nos.addAll((Set<String>) redisTemplate.opsForSet().members(key));

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
            if (lastRowNum > IMPORT_COUNT_LIIT) {
                throw new SbcRuntimeException(CommonErrorCode.DATA_OUT_LIIT, new Object[]{IMPORT_COUNT_LIIT});
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            boolean isError = false;
            for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                if (rowNum == 0) {
                    this.checkExcel(row, virtualCouponVO);
                    continue;
                }
                boolean isNotEmpty = false;
                Cell[] cells = new Cell[7];
                for (int i = 0; i < 7; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        cell = row.createCell(i);
                    }
                    if (i==0 || i ==1) {
                        cell.setCellType(CellType.STRING);
                    }
                    cells[i] = cell;
                    if (i < 6 && StringUtils.isNotBlank(ExcelHelper.getValue(cell))) {
                        isNotEmpty = true;
                    }
                }
                //数据都为空，则跳过去
                if (!isNotEmpty) {
                    continue;
                }
                VirtualCouponCodeVO virtualCouponCodeVO = new VirtualCouponCodeVO();
                virtualCouponCodeVO.setCouponId(couponId);
                virtualCouponCodeVO.setProvideType(virtualCouponVO.getProvideType());
                virtualCouponCodeVO.setCreateTime(LocalDateTime.now());
                virtualCouponCodeVO.setCreatePerson(commonUtil.getOperatorId());
                virtualCouponCodeVO.setUpdateTime(LocalDateTime.now());
                virtualCouponCodeVO.setUpdatePerson(commonUtil.getOperatorId());
                int index = 0;

                Cell cell = row.getCell(index);
                if (cell == null) {
                    ExcelHelper.setError(workbook, row.createCell(index), "券码必填");
                    isError = true;
                } else {
                    cell.setCellType(CellType.STRING);
                    String couponNo = cell.getStringCellValue();
                    if (StringUtils.isBlank(couponNo)) {
                        ExcelHelper.setError(workbook, cell, "券码必填");
                        isError = true;
                    } else if (couponNo.length() > 40) {
                        if (virtualCouponVO.getProvideType() == 0) {
                            ExcelHelper.setError(workbook, cell, "券码长度在1-40个字");
                            isError = true;
                        } else if (virtualCouponVO.getProvideType() == 2 && couponNo.length() > 150) {
                            ExcelHelper.setError(workbook, cell, "链接长度在1-150个字");
                            isError = true;
                        }
                    } else if (!set.add(couponNo)) {
                        ExcelHelper.setError(workbook, cell, "券码/链接Excel已存在");
                        isError = true;
                    } else if (nos.contains(couponNo)) {
                        ExcelHelper.setError(workbook, cell, "券码/链接库中已存在的值");
                        isError = true;
                    }
                    virtualCouponCodeVO.setCouponNo(couponNo);
                }
                index++;

                if (virtualCouponVO.getProvideType() == 1) {
                    //券码+密钥
                    Cell cell1 = row.getCell(index);
                    if (cell1 == null) {
                        cell1 = row.createCell(index);
                        ExcelHelper.setError(workbook, cell1, "密钥必填");
                        isError = true;
                    } else {
                        cell1.setCellType(CellType.STRING);
                        String couponSecret = cell1.getStringCellValue();
                        if (StringUtils.isBlank(couponSecret)) {
                            ExcelHelper.setError(workbook, cell1, "密钥必填");
                            isError = true;
                        } else if (couponSecret.length() > 40) {
                            ExcelHelper.setError(workbook, cell1, "密钥长度在1-40个字");
                            isError = true;
                        }
                        virtualCouponCodeVO.setCouponSecret(couponSecret);
                    }
                    index++;
                }

                Cell cell2 = row.getCell(index);
                if (cell2 != null && StringUtils.isNotBlank(ExcelHelper.getValue(cell2))) {
                    int validDays = 0;
                    try {
                        validDays = (int) cell2.getNumericCellValue();
                    } catch (Exception e) {
                        validDays = Integer.valueOf(ExcelHelper.getValue(cell2));
                    }
                    if (validDays < 0 || validDays > 999999) {
                        ExcelHelper.setError(workbook, cell2, "有效期必须在0~999999");
                        isError = true;
                    }
                    virtualCouponCodeVO.setValidDays(validDays);
                } else {
                    virtualCouponCodeVO.setValidDays(999);
                }
                index++;


                Date getEndDate = null;
                Cell cell3 = row.getCell(index);
                if (cell3 == null) {
                    cell3 = row.createCell(index);
                    ExcelHelper.setError(workbook, cell3, "领取结束时间不能为空");
                    isError = true;
                } else {
                    getEndDate = cell3.getDateCellValue();
                    if (getEndDate != null) {
                        LocalDateTime parse = LocalDateTime.parse(sdf.format(getEndDate), dft);
                        virtualCouponCodeVO.setReceiveEndTime(parse);
                    }
                }
                index++;

                Cell cell4 = row.getCell(index);
                Date startDate = null;
                if (cell4 != null && StringUtils.isNotBlank(ExcelHelper.getValue(cell4))) {
                    startDate = cell4.getDateCellValue();
                    if (startDate != null) {
                        LocalDateTime parse = LocalDateTime.parse(sdf.format(startDate), dft);
                        virtualCouponCodeVO.setExchangeStartTime(parse);
                    }
                }

                index++;
                boolean cell5Flag = false;
                Cell cell5 = row.getCell(index);
                Date endDate = null;
                if (cell5 != null && StringUtils.isNotBlank(ExcelHelper.getValue(cell5))) {
                    if (cell5.getCellTypeEnum() != CellType.NUMERIC) {
                        ExcelHelper.setError(workbook, cell5, "兑换结束时间必须大于兑换开始时间");
                        isError = true;
                        cell5Flag = true;
                    } else {
                        endDate = cell5.getDateCellValue();
                        if (endDate != null) {
                            LocalDateTime parse = LocalDateTime.parse(sdf.format(endDate), dft);
                            virtualCouponCodeVO.setExchangeEndTime(parse);
                        }
                    }
                }

                if (startDate != null && endDate != null && startDate.after(endDate)) {
                    ExcelHelper.setError(workbook, cell5, "兑换结束时间必须大于兑换开始时间");
                    String format = dateFormat.format(endDate);
                    cell5.setCellValue(format);
                    isError = true;
                    cell5Flag = true;
                }
                if (getEndDate != null && endDate != null && getEndDate.after(endDate) && !cell5Flag) {
                    ExcelHelper.setError(workbook, cell5, "兑换结束时间必须大于领取结束时间");
                    String format = dateFormat.format(endDate);
                    cell5.setCellValue(format);
                    isError = true;
                }
                if (!isError)
                    list.add(virtualCouponCodeVO);
            }
            if (isError) {
                String errorFile = uploadErrorFile(resourceKey, workbook);
                SbcRuntimeException sbcRuntimeException = new SbcRuntimeException(CommonErrorCode.IMPORTED_DATA_ERROR);
                sbcRuntimeException.setData(errorFile);
                throw sbcRuntimeException;
            }
            virtualCouponCodeProvider.add(VirtualCouponCodeAddRequest.builder().virtualCouponCodes(list).couponId(couponId).storeId(commonUtil.getStoreId()).build());
            return "";
        } catch (SbcRuntimeException e) {
            log.error("券码文件导入异常，resourceKey={}", resourceKey, e);
            if (CommonErrorCode.FAILED.equals(e.getErrorCode())) {
                throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
            }
            throw e;
        } catch (Exception e) {
            log.error("券码文件导入异常，resourceKey={}", resourceKey, e);
            throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
        } finally {
            yunServiceProvider.deleteFile(new YunFileDeleteRequest(Collections.singletonList(resourceKey)));
        }
    }

    private void checkExcel(Row row, VirtualCouponVO virtualCouponVO) {
        if (virtualCouponVO.getProvideType() == 0) {
            this.checkCell(row, 0, "*兑换码");
            this.checkCell(row, 1, "有效期");
            this.checkCell(row, 2, "*领取结束时间");
            this.checkCell(row, 3, "兑换开始时间");
            this.checkCell(row, 4, "兑换结束时间");
        } else if (virtualCouponVO.getProvideType() == 1) {
            this.checkCell(row, 0, "*券码");
            this.checkCell(row, 1, "*密钥");
            this.checkCell(row, 2, "有效期");
            this.checkCell(row, 3, "*领取结束时间");
            this.checkCell(row, 4, "兑换开始时间");
            this.checkCell(row, 5, "兑换结束时间");
        } else {
            this.checkCell(row, 0, "*链接");
            this.checkCell(row, 1, "有效期");
            this.checkCell(row, 2, "*领取结束时间");
            this.checkCell(row, 3, "兑换开始时间");
            this.checkCell(row, 4, "兑换结束时间");
        }
    }

    private void checkCell(Row row, int i, String s) {

        if (row == null) {
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }

        Cell cell = row.getCell(i);
        if (cell == null) {
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }
        String stringCellValue = cell.getStringCellValue();
        if (StringUtils.isBlank(stringCellValue)) {
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }

        if (!stringCellValue.equals(s)) {
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }

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

    public String exportErrExcel(Long couponId) {
        String resourceKey = String.format("%s/%s/%s/%s/%s", VIRTUAL_COUPON_CODE_DIR, env, commonUtil.getStoreId(), commonUtil.getOperatorId(), couponId);
        String error = resourceKey.concat("_error");
        YunGetResourceResponse context = yunServiceProvider.getFile(YunGetResourceRequest.builder().resourceKey(error).build()).getContext();
        try {
            return new BASE64Encoder().encode(context.getContent());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }
}
