package com.wanmi.sbc.goods.adjustprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.adjust.PriceAdjustService;
import com.wanmi.sbc.goods.adjust.request.PriceAdjustConfirmRequest;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceDetailDeleteRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.MarketingPriceAdjustDetailModifyRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
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
 * <p>商品批量调整市场价操作BFF</p>
 * Created by of628-wenzhi on 2020-12-11-2:41 下午.
 */
@RestController
@RequestMapping("/goods/price-adjust/marketing-price")
@Api(description = "商品批量调整市场价API", tags = "MarketingPriceAdjustController")
@Validated
public class MarketingPriceAdjustController {

    @Autowired
    private PriceAdjustService priceAdjustService;

    @Autowired
    private PriceAdjustmentRecordDetailProvider priceAdjustmentRecordDetailProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;



    private final static String TAMPLATE_FILE_NAME = "批量调整市场价导入模板.xlsx";


    /**
     * 市场价调整excel模板下载
     *
     * @param encrypted
     * @return
     */
    @ApiOperation(value = "市场价调整excel模板下载")
    @RequestMapping(value = "/template/{encrypted}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "鉴权加密串", required = true)
    public void template(@PathVariable String encrypted) {
        PriceAdjustmentTemplateExportRequest request = PriceAdjustmentTemplateExportRequest.builder()
                .priceAdjustmentType(PriceAdjustmentType.MARKET).build();
        priceAdjustService.downloadAdjustPriceTemplate(request, TAMPLATE_FILE_NAME);
    }

    /**
     * 市场价调整excel文件上传
     *
     * @param uploadFile
     * @return
     */
    @ApiOperation(value = "市场价调价excel文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(priceAdjustService.uploadPriceAdjustFile(uploadFile));
    }

    @ApiOperation(value = "市场价调价详情编辑")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody @Valid MarketingPriceAdjustDetailModifyRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.modifyMarketingPrice(request);
    }

    @ApiOperation(value = "市场价调价详情移除")
    @RequestMapping(method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestBody @Valid AdjustPriceDetailDeleteRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.delete(request);
    }

    @ApiOperation(value = "市场价调价确认导入")
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<String> importDo() {
        return BaseResponse.success(priceAdjustService.importPriceAdjustFile(analysisFunctionByMarketingPriceAdjust,
                PriceAdjustmentType.MARKET));
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
     * 校验并解析市场价调价详情数据
     *
     * @param workbook
     * @return
     */
    private final Function<Workbook, List<PriceAdjustmentRecordDetailDTO>> analysisFunctionByMarketingPriceAdjust = (workbook) -> {
        List<PriceAdjustmentRecordDetailDTO> dataList = new ArrayList<>();
        boolean isError = false;
        Sheet sheet = workbook.getSheetAt(0);
        //获得当前sheet的开始行
        int firstRowNum = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        int maxCell = 6;
        Map<String, Cell> goodsInfoKeyMap = new HashMap<>();
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
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
            String priceTypeVal = ExcelHelper.getValue(cells[4]);
            String marketingPriceVal = ExcelHelper.getValue(cells[5]);
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
            //校验是否以市场价销售
            if (StringUtils.isNotBlank(priceTypeVal)) {
                if (!"是".equals(priceTypeVal)) {
                    ExcelHelper.setError(workbook, cells[4], "选项不合法");
                    isError = true;
                } else {
                    detail.setPriceType(GoodsPriceType.MARKET);
                }
            }
            //校验调整后的市场价
            if (StringUtils.isBlank(marketingPriceVal)) {
                ExcelHelper.setError(workbook, cells[5], "此项必填");
                isError = true;
            } else if (!ValidateUtil.isNum(marketingPriceVal) && !ValidateUtil.isFloatNum(marketingPriceVal)) {
                ExcelHelper.setError(workbook, cells[5], "只能填写0和正数，允许两位小数");
                isError = true;
            } else if (new BigDecimal(marketingPriceVal).compareTo(new BigDecimal("9999999.99")) > 0) {
                ExcelHelper.setError(workbook, cells[5], "只能在0-9999999.99范围内");
                isError = true;
            } else {
                detail.setAdjustedMarketPrice(new BigDecimal(marketingPriceVal));
            }
            if (!isError) {
                dataList.add(detail);
            }
        }
        if (!isError) {
            //检查sku是否存在
            List<String> skuNos = goodsInfoQueryProvider.listMarketingPriceByNos(new GoodsInfoMarketingPriceByNosRequest(
                    new ArrayList<>(goodsInfoKeyMap.keySet()), commonUtil.getStoreId())).getContext().getDataList()
                    .stream().map(GoodsInfoMarketingPriceDTO::getGoodsInfoNo)
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
