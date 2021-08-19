package com.wanmi.sbc.goods.adjustprice;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.customer.api.constant.CustomerLevelErrorCode;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelBaseVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.adjust.PriceAdjustService;
import com.wanmi.sbc.goods.adjust.request.PriceAdjustConfirmRequest;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceDetailDeleteRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.CustomerLevelPriceAdjustDetailModifyRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsLevelPriceDTO;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>商品批量调整客户等级价操作BFF</p>
 * Created by of628-wenzhi on 2020-12-18-11:04 上午.
 */
@RestController
@RequestMapping("/goods/price-adjust/customer-level-price")
@Validated
@Api(description = "商品批量调整等级价API", tags = "LevelPriceAdjustController")
public class LevelPriceAdjustController {

    @Autowired
    private PriceAdjustService priceAdjustService;

    @Autowired
    private PriceAdjustmentRecordDetailProvider priceAdjustmentRecordDetailProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    private final static String TAMPLATE_FILE_NAME = "批量调整等级价导入模板.xlsx";

    /**
     * 等级价调整excel模板下载
     *
     * @param encrypted
     * @return
     */
    @ApiOperation(value = "客户等级价调整excel模板下载")
    @RequestMapping(value = "/template/{encrypted}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "鉴权加密串", required = true)
    public void template(@PathVariable String encrypted) {
        PriceAdjustmentTemplateExportRequest request = PriceAdjustmentTemplateExportRequest.builder()
                .priceAdjustmentType(PriceAdjustmentType.LEVEL).build();
        priceAdjustService.downloadAdjustPriceTemplate(request, TAMPLATE_FILE_NAME);
    }


    /**
     * 等级价调整excel文件上传
     *
     * @param uploadFile
     * @return
     */
    @ApiOperation(value = "客户等级价调价excel文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(priceAdjustService.uploadPriceAdjustFile(uploadFile));
    }

    @ApiOperation(value = "客户等级价调价详情移除")
    @RequestMapping(method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody @Valid AdjustPriceDetailDeleteRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.delete(request);
    }

    @ApiOperation(value = "客户等级价调价确认导入")
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<String> importDo() {
        return BaseResponse.success(priceAdjustService.importPriceAdjustFile(analysisFunctionByLevelPriceAdjust,
                PriceAdjustmentType.LEVEL));
    }

    @ApiOperation(value = "客户等级价调价详情编辑")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid CustomerLevelPriceAdjustDetailModifyRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.modifyCustomerLevelPrice(request);
    }

    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @RequestMapping(value = "/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        priceAdjustService.downErrorFile(ext);
    }

    /**
     * 市场价确认调价
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "市场价确认调价")
    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public BaseResponse confirmAdjust(@RequestBody @Valid PriceAdjustConfirmRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        priceAdjustService.adjustPriceConfirm(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 校验并解析等级价调价详情数据
     *
     * @param workbook
     * @return
     */
    private final Function<Workbook, List<PriceAdjustmentRecordDetailDTO>> analysisFunctionByLevelPriceAdjust = (workbook) -> {
        List<PriceAdjustmentRecordDetailDTO> dataList = new ArrayList<>();
        boolean isError = false;
        Sheet sheet1 = workbook.getSheetAt(0);
        Sheet sheet2 = workbook.getSheetAt(1);
        //获得当前sheet1的开始行
        int firstRowNum = sheet1.getFirstRowNum();
        //获得当前sheet1的结束行
        int lastRowNum = sheet1.getLastRowNum();

        //获得当前sheet2的结束行
        int lastRowNum2 = sheet2.getLastRowNum();
        //从0开始
        int maxCell = lastRowNum2 + 1 + 6;
        Map<String, Cell> goodsInfoKeyMap = new HashMap<>();
        List<Long> levelIds = new ArrayList<>(lastRowNum2 + 1);
        //获取levelIds
        for (int rowNum = sheet2.getFirstRowNum(); rowNum <= lastRowNum2; rowNum++) {
            Row row = sheet2.getRow(rowNum);
            if (Objects.isNull(row)) {
                continue;
            }
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            String levelId = ExcelHelper.getValue(cell);
            if (StringUtils.isNotBlank(levelId)) {
                levelIds.add(new BigDecimal(levelId).longValue());
            }
        }
        List<GoodsLevelPriceDTO> levelPriceList;
        //校验客户等级
        Long storeId = commonUtil.getStoreId();
        StoreVO storeVO = storeQueryProvider.getById(new StoreByIdRequest(storeId)).getContext().getStoreVO();
        List<Long> sourceLevelIds;
        if (storeVO.getCompanyType() == BoolFlag.NO) {
            //自营取平台等级
            List<CustomerLevelBaseVO> levelList = customerLevelQueryProvider.listAllCustomerLevelNew().getContext().getCustomerLevelVOList();
            if (levelList.isEmpty()) {
                throw new SbcRuntimeException(CustomerLevelErrorCode.NOT_EXIST);
            }
            sourceLevelIds = levelList.stream().mapToLong(CustomerLevelBaseVO::getCustomerLevelId).boxed().collect(Collectors.toList());
        } else {
            //第三方店铺取自定义等级
            List<StoreLevelVO> levelList = storeLevelQueryProvider.listAllStoreLevelByStoreId(StoreLevelListRequest.builder().storeId(storeId).build())
                    .getContext().getStoreLevelVOList();
            if (levelList.isEmpty()) {
                throw new SbcRuntimeException(CustomerLevelErrorCode.NOT_EXIST);
            }
            sourceLevelIds = levelList.stream().mapToLong(StoreLevelVO::getStoreLevelId).boxed().collect(Collectors.toList());
        }
        sourceLevelIds.add(0, 0L);
        //先做等级数量校验
        if(sourceLevelIds.size() != levelIds.size()){
            throw new SbcRuntimeException(CommonErrorCode.LEVEL_CHANGED_HINT);
        }
        //校验客户等级是否存在
        levelIds.forEach(i->{
            if(!sourceLevelIds.contains(i)){
                //数量相同，如果模板中的等级id都存在于现有等级中，则认为等级信息一致
                throw new SbcRuntimeException(CommonErrorCode.LEVEL_CHANGED_HINT);
            }
        });
        //获取调价信息
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet1.getRow(rowNum);
            if (Objects.isNull(row)) {
                continue;
            }
            Cell[] cells = new Cell[maxCell];
            boolean isNotEmpty = false;
            for (int i = 0; i < maxCell; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                cells[i] = cell;
                if (StringUtils.isNotBlank(ExcelHelper.getValue(cell))) {
                    isNotEmpty = true;
                }
            }
            //列数据都为空，则跳过去
            if (!isNotEmpty) {
                continue;
            }
            PriceAdjustmentRecordDetailDTO detail = new PriceAdjustmentRecordDetailDTO();
            detail.setGoodsInfoNo(ExcelHelper.getValue(cells[0]).trim());
            detail.setGoodsInfoName(ExcelHelper.getValue(cells[1]).trim());
            detail.setGoodsSpecText(ExcelHelper.getValue(cells[2]));
            String saleTypeVal = ExcelHelper.getValue(cells[3]);
            String marketingPriceVal = ExcelHelper.getValue(cells[4]);
            String isAloneFlagVal = ExcelHelper.getValue(cells[5]);
            //校验SKU NO
            if (StringUtils.isBlank(detail.getGoodsInfoNo())) {
                ExcelHelper.setError(workbook, cells[0], "此项必填");
                isError = true;
            } else if (!ValidateUtil.isBetweenLen(detail.getGoodsInfoNo(), 1, 20)) {
                ExcelHelper.setError(workbook, cells[0], "长度必须1-20个字");
                isError = true;
            } else if (!ValidateUtil.isNotChs(detail.getGoodsInfoNo())) {
                ExcelHelper.setError(workbook, cells[0], "仅允许英文、数字、特殊字符");
                isError = true;
            } else if (goodsInfoKeyMap.containsKey(detail.getGoodsInfoNo())) {
                ExcelHelper.setError(workbook, cells[0], "SKU编码重复");
                isError = true;
            } else {
                //记录skuNo列，用于检查数据是否存在和排重
                goodsInfoKeyMap.put(detail.getGoodsInfoNo(), cells[0]);
            }
            //校验SKU name
            if (StringUtils.isNotBlank(detail.getGoodsInfoName())) {
                if (!ValidateUtil.isBetweenLen(detail.getGoodsInfoName(), 1, 40)) {
                    ExcelHelper.setError(workbook, cells[1], "长度必须1-40个字");
                    isError = true;
                } else if (ValidateUtil.containsEmoji(detail.getGoodsInfoName())) {
                    ExcelHelper.setError(workbook, cells[1], "含有非法字符");
                    isError = true;
                }
            }
            //校验规格值
            if (StringUtils.isNotBlank(detail.getGoodsSpecText())) {
                if (!ValidateUtil.isBetweenLen(detail.getGoodsSpecText(), 0, 20)) {
                    ExcelHelper.setError(workbook, cells[2], "长度必须0-20个字");
                    isError = true;
                } else if (!ValidateUtil.isChsEngNum(detail.getGoodsSpecText()) && !ValidateUtil.isFloatNum(detail.getGoodsSpecText())) {
                    ExcelHelper.setError(workbook, cells[2], "仅允许中英文、数字");
                    isError = true;
                }
            }
            //校验销售类型
            if (StringUtils.isNotBlank(saleTypeVal)) {
                if (!"零售".equals(saleTypeVal) && !"批发".equals(saleTypeVal)) {
                    ExcelHelper.setError(workbook, cells[3], "选项不合法");
                    isError = true;
                } else {
                    detail.setSaleType("批发".equals(saleTypeVal) ? SaleType.WHOLESALE : SaleType.RETAIL);
                }
            }
            //校验调整后的市场价
            if (StringUtils.isBlank(marketingPriceVal)) {
                ExcelHelper.setError(workbook, cells[4], "此项必填");
                isError = true;
            } else if (!ValidateUtil.isNum(marketingPriceVal) && !ValidateUtil.isFloatNum(marketingPriceVal)) {
                ExcelHelper.setError(workbook, cells[4], "只能填写0和正数，允许两位小数");
                isError = true;
            } else if (new BigDecimal(marketingPriceVal).compareTo(new BigDecimal("9999999.99")) > 0) {
                ExcelHelper.setError(workbook, cells[4], "只能在0-9999999.99范围内");
                isError = true;
            } else {
                detail.setAdjustedMarketPrice(new BigDecimal(marketingPriceVal));
            }
            //校验是否独立设价
            if (StringUtils.isNotBlank(isAloneFlagVal)) {
                if (!"是".equals(isAloneFlagVal) && !"否".equals(isAloneFlagVal)) {
                    ExcelHelper.setError(workbook, cells[5], "选项不合法");
                    isError = true;
                } else {
                    detail.setAloneFlag("是".equals(isAloneFlagVal));
                }
            }
            //解析等级价信息
            levelPriceList = new ArrayList<>();
           /* //市场价和等级价不能同时为空
            boolean flag = StringUtils.isBlank(marketingPriceVal);
            List<Cell> errorCell = new ArrayList<>();*/
            for (int i = 0; i < levelIds.size(); i++) {
                Cell levelPriceCell = cells[i + 6];
                String levelPriceVal = ExcelHelper.getValue(levelPriceCell);
                if (StringUtils.isNotBlank(levelPriceVal)) {
//                    flag = false;
                    if (!ValidateUtil.isNum(levelPriceVal) && !ValidateUtil.isFloatNum(levelPriceVal)) {
                        ExcelHelper.setError(workbook, levelPriceCell, "只能填写0和正数，允许两位小数");
                        isError = true;
                    } else if (new BigDecimal(levelPriceVal).compareTo(new BigDecimal("9999999.99")) > 0) {
                        ExcelHelper.setError(workbook, levelPriceCell, "只能在0-9999999.99范围内");
                        isError = true;
                    }
                } /*else {
                    if (flag) {
                        errorCell.add(levelPriceCell);
                    }
                }*/
                if (!isError) {
                    GoodsLevelPriceDTO levelPrice = new GoodsLevelPriceDTO();
                    levelPrice.setLevelId(levelIds.get(i));
                    levelPrice.setType(PriceType.SKU);
                    levelPrice.setPrice(StringUtils.isBlank(levelPriceVal) ? null : new BigDecimal(levelPriceVal));
                    levelPriceList.add(levelPrice);
                }
            }
           /* if (flag) {
                ExcelHelper.setError(workbook, cells[4], "商品的市场价和等级价不可全为空");
                errorCell.forEach(i -> ExcelHelper.setError(workbook, i, "商品的市场价和等级价不可全为空"));
                isError = true;
            }*/
            if (!isError) {
                detail.setLeverPrice(JSONObject.toJSONString(levelPriceList));
                dataList.add(detail);
            }
        }
        if (!isError) {
            //检查sku是否存在
            List<String> skuNos = goodsInfoQueryProvider.listMarketingPriceByNos(GoodsInfoMarketingPriceByNosRequest.builder()
                    .goodsInfoNos(new ArrayList<>(goodsInfoKeyMap.keySet()))
                    .storeId(commonUtil.getStoreId()).build())
                    .getContext().getDataList().stream().map(GoodsInfoMarketingPriceDTO::getGoodsInfoNo)
                    .collect(Collectors.toList());
            for (Map.Entry<String, Cell> entry : goodsInfoKeyMap.entrySet()) {
                if (!skuNos.contains(entry.getKey())) {
                    ExcelHelper.setError(workbook, entry.getValue(), "该商品不存在！");
                    isError = true;
                }
            }
        }
        if (isError) {
            throw new SbcRuntimeException(CommonErrorCode.IMPORTED_DATA_ERROR);
        }
        return dataList;
    };

}
