package com.wanmi.sbc.trade.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.order.api.provider.returnorder.ReturnOrderQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.ProviderTradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.returnorder.ReturnOrderByConditionRequest;
import com.wanmi.sbc.order.api.request.trade.LogisticsRepeatRequest;
import com.wanmi.sbc.order.api.request.trade.ProviderTradeGetByIdListRequest;
import com.wanmi.sbc.order.api.request.trade.TradeBatchDeliverRequest;
import com.wanmi.sbc.order.bean.dto.TradeBatchDeliverDTO;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdListRequest;
import com.wanmi.sbc.order.bean.dto.LogisticsDTO;
import com.wanmi.sbc.order.bean.enums.*;
import com.wanmi.sbc.order.bean.vo.ProviderTradeVO;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaQueryProvider;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import com.wanmi.sbc.setting.api.response.yunservice.YunGetResourceResponse;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.setting.bean.vo.StoreExpressCompanyRelaVO;
import com.wanmi.sbc.trade.request.BatchDeliverCheckRequest;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by mac on 2017/5/6.
 */
@Slf4j
@Service
public class TradeBatchDeliverService {

    private final Integer PAGE_SIZE=  500;

    private final Integer IMPORT_ORDER_MAX_SIZE = 5;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private StoreExpressCompanyRelaQueryProvider storeExpressCompanyRelaQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private ProviderTradeQueryProvider providerTradeQueryProvider;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private ReturnOrderQueryProvider returnOrderQueryProvider;


    /**
     * 上传文件
     * @param file 文件
     * @param userId 操作员id
     * @return 文件格式
     */
    public String upload(MultipartFile file, String userId){
        if (file == null || file.isEmpty()) {
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        if (!(fileExt.equalsIgnoreCase("xls") || fileExt.equalsIgnoreCase("xlsx"))) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_EXT_ERROR);
        }

        if (file.getSize() > IMPORT_ORDER_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_ORDER_MAX_SIZE});
        }

        String resourceKey = Constants.BATCH_DELIVER_EXCEL_DIR.concat(userId);
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

    public void checkExcelForBatchDeliver(BatchDeliverCheckRequest request){
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(Constants.BATCH_DELIVER_EXCEL_DIR.concat(request.getUserId()))
                .build()).getContext().getContent();
        if (content == null) {
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        } else if (content.length > IMPORT_ORDER_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{IMPORT_ORDER_MAX_SIZE});
        }
        String ext = request.getExt();

        //创建Workbook工作薄对象，表示整个excel
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }
            //检测文档正确性
            this.checkExcel(workbook);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }else if(lastRowNum > 1000){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED,"文件数据超过1000条，请修改");
            }

            boolean isError = false;
            //<tradeId, cells>
            Map<String, List<Cell>> deliverOrderInfo = new LinkedHashMap<>();
            List<String> tradeIdList = new ArrayList<>();
            List<String> logisticsNoList = new ArrayList<>();
            List<String> logisticsStandardCodeList = new ArrayList<>();

            //获取店铺物流设置
            StoreExpressCompanyRelaListRequest queryRopRequest = new StoreExpressCompanyRelaListRequest();
            queryRopRequest.setStoreId(request.getStoreId());
            StoreExpressCompanyRelaListResponse response = storeExpressCompanyRelaQueryProvider.list(queryRopRequest).getContext();
            List<StoreExpressCompanyRelaVO> storeExpressCompanyRelaVOList;
            if(Objects.nonNull(response)){
                storeExpressCompanyRelaVOList = response.getStoreExpressCompanyRelaVOList();
            }else{
                storeExpressCompanyRelaVOList = new ArrayList<>();
            }
            List<ExpressCompanyVO> expressCompanyVOS =
                    storeExpressCompanyRelaVOList.stream().map(StoreExpressCompanyRelaVO::getExpressCompany).collect(Collectors.toList());
            List<String> expressNameList = expressCompanyVOS.stream().map(ExpressCompanyVO::getExpressName).collect(Collectors.toList());

            //循环除了第一行的所有行,进行数据的基本校验
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                boolean isNotEmpty = false;
                Cell[] cells = new Cell[4];
                for (int i = 0; i < 4; i++) {
                    Cell cell = row.getCell(i);
                    if (cell == null) {
                        cell = row.createCell(i);
                    }
                    cell.setCellType(CellType.STRING);
                    cells[i] = cell;
                    if (StringUtils.isNotBlank(ExcelHelper.getValue(cell))) {
                        isNotEmpty = true;
                    }
                }
                //数据都为空，则跳过去
                if (!isNotEmpty) {
                    continue;
                }
                String tradeId = ExcelHelper.getValue(cells[0]);
                if (StringUtils.isBlank(tradeId)){
                    ExcelHelper.setError(workbook, cells[0], "此项必填");
                    isError = true;
                }else if(ValidateUtil.isOverLen(tradeId,22)
                        || (Platform.SUPPLIER == request.getPlatform() && !tradeId.startsWith("O"))
                        || (Platform.PROVIDER == request.getPlatform() && !tradeId.startsWith("P"))){
                    ExcelHelper.setError(workbook, cells[0], "请填写正确的订单号");
                    isError = true;
                }else if(tradeIdList.contains(tradeId)){
                    ExcelHelper.setError(workbook, cells[0], "订单号重复");
                    isError = true;
                }else{
                    tradeIdList.add(tradeId.trim());
                }

                String expressName = ExcelHelper.getValue(cells[1]);
                if (StringUtils.isBlank(expressName)){
                    ExcelHelper.setError(workbook, cells[1], "请选择物流公司");
                    isError = true;
                }else if(!expressNameList.contains(expressName)){
                    ExcelHelper.setError(workbook, cells[1], "物流公司错误");
                    isError = true;
                }else {
                    ExpressCompanyVO expressCompanyVO = expressCompanyVOS.stream()
                            .filter(v -> v.getExpressName().equals(expressName))
                            .findFirst().get();
                    logisticsStandardCodeList.add(expressCompanyVO.getExpressCode());
                }

                String logisticsNo = ExcelHelper.getValue(cells[2]);
                if (StringUtils.isBlank(logisticsNo)){
                    ExcelHelper.setError(workbook, cells[2], "此项必填");
                    isError = true;
                }else if(ValidateUtil.isOverLen(logisticsNo, 50)){
                    ExcelHelper.setError(workbook, cells[2], "长度必须1-50位");
                    isError = true;
                }else if(logisticsNoList.contains(logisticsNo)){
                    ExcelHelper.setError(workbook, cells[2], "物流单号重复");
                    isError = true;
                }else if(!ValidateUtil.isNumAndEng(logisticsNo)){
                    ExcelHelper.setError(workbook, cells[2], "物流单号含有特殊字符");
                    isError = true;
                }else {
                    logisticsNoList.add(logisticsNo.trim());
                }

                String deliverDate = ExcelHelper.getValue(cells[3]);
                if (StringUtils.isBlank(deliverDate)){
                    ExcelHelper.setError(workbook, cells[3], "请填写发货日期");
                    isError = true;
                }else if(deliverDate.length() != 8 || !ValidateUtil.isNum(deliverDate) || !this.verifyDeliverDate(deliverDate)){
                    ExcelHelper.setError(workbook, cells[3], "请正确填写发货日期");
                    isError = true;
                }else if(checkDeliverDate(deliverDate)){
                    ExcelHelper.setError(workbook, cells[3], "请填写今日或今日之前的日期");
                    isError = true;
                }
                if (!isError){
                    deliverOrderInfo.put(tradeId, Arrays.asList(cells));
                }
            }
            if (isError){
                this.errorExcel(request.getUserId().concat(".").concat(ext), workbook);
                throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, new Object[]{ext});
            }
            List<String> checkList = new ArrayList<>();
            List<String> errorStatusTradeIdList = new ArrayList<>();
            List<String> partShippedTradeIdList = new ArrayList<>();
            List<String> onlyProviderTradeIdList = new ArrayList<>();
            List<String> exitTradeIdList = new ArrayList<>();
            List<String> returnedTradeIdList;
            List<LogisticsDTO> logisticsDTOList = new ArrayList<>();
            //分批校验订单数据
            for (int i = 0; i < tradeIdList.size(); i++){
                checkList.add(tradeIdList.get(i));
                String logisticNo = logisticsNoList.get(i);
                String logisticsStandardCode = logisticsStandardCodeList.get(i);
                logisticsDTOList.add(LogisticsDTO.builder().logisticNo(logisticNo).logisticStandardCode(logisticsStandardCode).build());
                if (checkList.size() == PAGE_SIZE || i == tradeIdList.size() - 1){
                    //批量校验订单号是否存在;订单状态
                    this.checkTradeIdAndTradeState(checkList, errorStatusTradeIdList, onlyProviderTradeIdList,
                            partShippedTradeIdList, exitTradeIdList, request.getStoreId(), request.getPlatform());

                    //批量校验物流号重复
                    List<String> repeatedLogisticNoList = tradeQueryProvider.getRepeatLogisticNo(LogisticsRepeatRequest.builder().logisticsDTOList(logisticsDTOList).build()).getContext().getLogisticNoList();

                    //批量校验订单是否申请售后
                    returnedTradeIdList = this.verifyAfterProcessing(exitTradeIdList);

                    if (CollectionUtils.isNotEmpty(checkList) || CollectionUtils.isNotEmpty(errorStatusTradeIdList)
                            || CollectionUtils.isNotEmpty(returnedTradeIdList) || CollectionUtils.isNotEmpty(repeatedLogisticNoList)
                            || CollectionUtils.isNotEmpty(partShippedTradeIdList) || CollectionUtils.isNotEmpty(onlyProviderTradeIdList)){
                        for (int n = 0; n < checkList.size(); n++){
                            Cell cell = deliverOrderInfo.get(checkList.get(n)).get(0);
                            ExcelHelper.setError(workbook, cell, "订单编号不存在");
                            isError = true;
                        }
                        for (int n = 0; n < errorStatusTradeIdList.size(); n++){
                            Cell cell = deliverOrderInfo.get(errorStatusTradeIdList.get(n)).get(0);
                            ExcelHelper.setError(workbook, cell, "订单状态非“待发货”状态");
                            isError = true;
                        }
                        for (int n = 0; n < partShippedTradeIdList.size(); n++){
                            Cell cell = deliverOrderInfo.get(partShippedTradeIdList.get(n)).get(0);
                            ExcelHelper.setError(workbook, cell, "部分发货的订单不支持批量发货");
                            isError = true;
                        }
                        for (int n = 0; n < returnedTradeIdList.size(); n++){
                            Cell cell = deliverOrderInfo.get(returnedTradeIdList.get(n)).get(0);
                            ExcelHelper.setError(workbook, cell, "当前订单已申请退款，不可发货");
                            isError = true;
                        }
                        for (int n = 0; n < repeatedLogisticNoList.size(); n++){
                            Cell cell = deliverOrderInfo.get(tradeIdList.get(logisticsNoList.indexOf(repeatedLogisticNoList.get(n)))).get(2);
                            ExcelHelper.setError(workbook, cell, "物流单号已被使用");
                            isError = true;
                        }
                        for (int n = 0; n < onlyProviderTradeIdList.size(); n++){
                            Cell cell = deliverOrderInfo.get(onlyProviderTradeIdList.get(n)).get(0);
                            ExcelHelper.setError(workbook, cell, "不支持对供应商订单进行发货");
                            isError = true;
                        }
                        if (isError){
                            this.errorExcel(request.getUserId().concat(".").concat(ext), workbook);
                            throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, new Object[]{ext});
                        }
                    }
                    checkList.clear();
                    errorStatusTradeIdList.clear();
                    exitTradeIdList.clear();
                    returnedTradeIdList.clear();
                    partShippedTradeIdList.clear();
                    logisticsDTOList.clear();
                }
            }
            if (isError){
                this.errorExcel(request.getUserId().concat(".").concat(ext), workbook);
                throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, new Object[]{ext});
            }

            //发货
            List<TradeBatchDeliverDTO> deliverDTOList = new ArrayList<>();
            tradeIdList.forEach(tradeId -> {
                List<Cell> cells = deliverOrderInfo.get(tradeId);
                ExpressCompanyVO expressCompanyVO = expressCompanyVOS.stream()
                        .filter(v -> v.getExpressName().equals(ExcelHelper.getValue(cells.get(1))))
                        .findFirst().get();

                TradeBatchDeliverDTO deliverDTO = new TradeBatchDeliverDTO();
                deliverDTO.setTid(tradeId);
                deliverDTO.setDeliverTime(LocalDateTime.parse(cells.get(3) + " 00:00:00", DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_8)));
                deliverDTO.setLogistics(LogisticsDTO.builder()
                        .logisticCompanyId(expressCompanyVO.getExpressCompanyId().toString())
                        .logisticNo(ExcelHelper.getValue(cells.get(2)))
                        .logisticStandardCode(expressCompanyVO.getExpressCode())
                        .logisticCompanyName(expressCompanyVO.getExpressName())
                        .build());
                deliverDTOList.add(deliverDTO);
            });
            tradeProvider.batchDeliver(TradeBatchDeliverRequest.builder().batchDeliverDTOList(deliverDTOList).operator(commonUtil.getOperator()).build());
        } catch (SbcRuntimeException e) {
            log.error("批量发货Excel导入异常", e);
            throw e;
        } catch (Exception e) {
            log.error("批量发货Excel导入异常", e);
            throw new SbcRuntimeException(CommonErrorCode.IMPORT_FAIL);
        }
    }

    /**
     * 验证EXCEL
     * @param workbook
     */
    public void checkExcel(Workbook workbook){
        try {
            Sheet sheet1 = workbook.getSheetAt(0);
            Row row = sheet1.getRow(0);
            if(!(row.getCell(0).getStringCellValue().contains("订单号")
                    && row.getCell(1).getStringCellValue().contains("物流公司")
                    && row.getCell(2).getStringCellValue().contains("物流单号")
                    && row.getCell(3).getStringCellValue().contains("发货日期"))){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }
        }catch (Exception e){
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }
    }

    /**
     * 校验发货日期是否是未来日期
     * @param deliverDateStr
     * @return
     */
    public boolean checkDeliverDate(String deliverDateStr){
        try {
            LocalDate deliverDate = LocalDate.parse(deliverDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return deliverDate.isAfter(LocalDate.now());
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

    /**
     * 校验发货日期是否格式
     * @param deliverDateStr
     * @return
     */
    public boolean verifyDeliverDate(String deliverDateStr){
        try {
            LocalDate deliverDate = LocalDate.parse(deliverDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
            return true;
        }catch (Exception e){
            return Boolean.FALSE;
        }
    }

    /**
     * 校验订单编号是否存在及状态是否符合发货
     * @param checkList
     * @param errorStatusTradeIdList 状态不符的tradeIdList
     * @param onlyProviderTradeIdList 只有供应商订单
     * @param exitTradeIdList 商家发货->订单id;供应商发货->子单的parentId。用于校验订单是否存在售后
     * @param storeId
     * @param platform 平台类型
     * @return
     */
    public void checkTradeIdAndTradeState(List<String> checkList, List<String> errorStatusTradeIdList, List<String> onlyProviderTradeIdList, List<String> partShippedTradeIdList, List<String> exitTradeIdList, Long storeId, Platform platform){
        List<String> otherStoreTradeIdList = null;
        if (Platform.SUPPLIER == platform){
            //根据O订单和storeId查询子订单
            List<TradeVO> tradeVOList = tradeQueryProvider.getTradeListByIds(TradeGetByIdListRequest.builder()
                    .tidList(checkList).boolFlag(BoolFlag.YES).build()).getContext().getTradeVOList();
            exitTradeIdList.addAll(tradeVOList.stream().map(TradeVO::getId).collect(Collectors.toList()));
            //过滤出不属于当前店铺的订单id
            otherStoreTradeIdList = tradeVOList.stream()
                    .filter(tradeVO -> !storeId.equals(tradeVO.getSupplier().getStoreId()))
                    .map(TradeVO::getId).collect(Collectors.toList());
            //O单中只有供应商子单
            onlyProviderTradeIdList.addAll(tradeVOList.stream()
                    .filter(tradeVO -> {
                        if(CollectionUtils.isNotEmpty(tradeVO.getProviderTradeVOList())){
                            Optional<ProviderTradeVO> providerTradeVO = tradeVO.getProviderTradeVOList().stream().filter(v -> v.getId().startsWith("S")).findFirst();
                            return !providerTradeVO.isPresent();
                        }
                        return false;
                    }).map(TradeVO::getId).collect(Collectors.toList()));

            //1.1 校验订单状态,非“待发货”状态则不能发货,部分发货订单特殊处理
            errorStatusTradeIdList.addAll(tradeVOList.stream().filter(tradeVO ->
                    !(tradeVO.getTradeState().getFlowState().equals(FlowState.AUDIT)
                            && (tradeVO.getTradeState().getPayState().equals(PayState.PAID) || tradeVO.getPaymentOrder() == PaymentOrder.NO_LIMIT)))
                    .filter(tradeVO -> {
                        //O单为部分发货，则校验S子单是否是全部发货，子单为部分发货的校验在下方单独处理
                        if (tradeVO.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED
                                && CollectionUtils.isNotEmpty(tradeVO.getProviderTradeVOList())){
                            Optional<ProviderTradeVO> providerTradeVOOptional
                                    = tradeVO.getProviderTradeVOList().stream().filter(v -> v.getSupplier().getStoreId().equals(storeId)).findFirst();
                            if(providerTradeVOOptional.isPresent()){
                                return providerTradeVOOptional.get().getTradeState().getDeliverStatus() == DeliverStatus.SHIPPED;
                            }
                        }else if(tradeVO.getTradeState().getFlowState() != FlowState.DELIVERED_PART){
                              return true;
                        }
                        return false;
                    })
                    .map(TradeVO::getId).collect(Collectors.toList()));
            //1.2 所发的订单为部分发货状态则校验提示：部分发货的订单不支持批量发货
            partShippedTradeIdList.addAll(tradeVOList.stream().filter(tradeVO -> {
                //无子单
                if (tradeVO.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED
                        && CollectionUtils.isEmpty(tradeVO.getProviderTradeVOList())){
                    return true;
                }else if (tradeVO.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED
                        && CollectionUtils.isNotEmpty(tradeVO.getProviderTradeVOList())){
                    //有子单
                    Optional<ProviderTradeVO> providerTradeVOOptional
                            = tradeVO.getProviderTradeVOList().stream().filter(v -> v.getSupplier().getStoreId().equals(storeId)).findFirst();
                    if(providerTradeVOOptional.isPresent()){
                        return providerTradeVOOptional.get().getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED;
                    }
                }
                return false;
            }).map(TradeVO::getId).collect(Collectors.toList()));

            //2.1checkList去除存在的订单
            checkList.removeAll(exitTradeIdList);
        }else if(Platform.PROVIDER == platform) {
            List<ProviderTradeVO> providerTradeVOList = providerTradeQueryProvider
                    .providerGetByIds(ProviderTradeGetByIdListRequest.builder().idList(checkList).build())
                    .getContext().getProviderTradeVOList();
            exitTradeIdList.addAll(providerTradeVOList.stream().map(ProviderTradeVO::getParentId).collect(Collectors.toList()));
            //过滤出不属于当前店铺的订单id
            otherStoreTradeIdList = providerTradeVOList.stream()
                    .filter(tradeVO -> !storeId.equals(tradeVO.getSupplier().getStoreId()))
                    .map(ProviderTradeVO::getId).collect(Collectors.toList());
            //1.1校验订单状态,非“待发货”状态则不能发货；部分发货状态的订单单独处理
            errorStatusTradeIdList.addAll(providerTradeVOList.stream().filter(tradeVO ->
                    !(tradeVO.getTradeState().getFlowState().equals(FlowState.AUDIT)
                            && (tradeVO.getTradeState().getDeliverStatus().equals(DeliverStatus.NOT_YET_SHIPPED))
                            && (tradeVO.getTradeState().getPayState().equals(PayState.PAID) || tradeVO.getPaymentOrder() == PaymentOrder.NO_LIMIT)
                    ))
                    .filter(tradeVO -> tradeVO.getTradeState().getFlowState() != FlowState.DELIVERED_PART)
                    .map(ProviderTradeVO::getId).collect(Collectors.toList()));

            //1.2校验部分发货子单
            partShippedTradeIdList.addAll(providerTradeVOList.stream()
                    .filter(tradeVO -> tradeVO.getTradeState().getDeliverStatus() == DeliverStatus.PART_SHIPPED)
                    .map(ProviderTradeVO::getId).collect(Collectors.toList()));

            //2.1checkList去除存在的订单
            checkList.removeAll(providerTradeVOList.stream().map(ProviderTradeVO::getId).collect(Collectors.toList()));
        }
        //2.2添加不属于当前店铺的订单===>checkList剩下非法的订单id
        checkList.addAll(otherStoreTradeIdList);
    }

    /**
     * 通过"O"订单校验是否存在退单
     * @param tradeIdList
     * @return 存在退单的订单号
     */
    public List<String> verifyAfterProcessing(List<String> tradeIdList){
        if (CollectionUtils.isEmpty(tradeIdList)){
            return new ArrayList<>();
        }
        //存在着"O"订单会有多个退单的场景
        List<ReturnOrderVO> returnOrderList =
                returnOrderQueryProvider.listByCondition(ReturnOrderByConditionRequest.builder().tids(tradeIdList).build()).getContext().getReturnOrderList();
        if (Platform.SUPPLIER == commonUtil.getOperator().getPlatform()){
            return returnOrderList.stream().filter(item -> item.getReturnFlowState() != ReturnFlowState.VOID
                    && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                    && item.getReturnFlowState() != ReturnFlowState.COMPLETED)
                    .map(ReturnOrderVO::getTid)
                    .distinct()
                    .collect(Collectors.toList());
        }else {
            return returnOrderList.stream().filter(item -> item.getReturnFlowState() != ReturnFlowState.VOID
                    && item.getReturnFlowState() != ReturnFlowState.REJECT_REFUND
                    && item.getReturnFlowState() != ReturnFlowState.COMPLETED
                    && StringUtils.equals(commonUtil.getStoreId().toString(), item.getProviderId()))
                    .map(ReturnOrderVO::getPtid)
                    .collect(Collectors.toList());
        }
    }

    /**
     * EXCEL错误文件-本地生成
     * @param newFileName 新文件名
     * @param wk Excel对象
     * @return 新文件名
     * @throws SbcRuntimeException
     */
    public String errorExcel(String newFileName, Workbook wk) throws SbcRuntimeException {
        String userId = commonUtil.getOperatorId();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            wk.write(os);
            YunUploadResourceRequest yunUploadResourceRequest = YunUploadResourceRequest
                    .builder()
                    .resourceType(ResourceType.EXCEL)
                    .content(os.toByteArray())
                    .resourceName(newFileName)
                    .resourceKey(Constants.BATCH_DELIVER_ERR_EXCEL_DIR.concat(userId))
                    .build();
            yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
            return newFileName;
        } catch (IOException e) {
            log.error("生成的错误文件上传至云空间失败", e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
    }

    /**
     * 下载Excel错误文档
     * @param userId 用户Id
     * @param ext 文件扩展名
     */
    public void downErrExcel(String userId, String ext){
        //商品导入错误文件resourceKey
        String resourceKey = Constants.BATCH_DELIVER_ERR_EXCEL_DIR.concat(userId);
        YunGetResourceResponse yunGetResourceResponse = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(resourceKey)
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
        ){
            //下载错误文档时强制清除页面文档缓存
            HttpServletResponse response = HttpUtil.getResponse();
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("expries",-1);
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
