package com.wanmi.sbc.goods.adjustprice;

import com.alibaba.fastjson.JSONObject;
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
import com.wanmi.sbc.goods.api.request.adjustprice.IntervalPriceAdjustDetailModifyRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsIntervalPriceDTO;
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
 * <p>商品阶梯价批量调价Controller</p>
 * Created by of628-wenzhi on 2020-12-23-2:42 下午.
 */
@RestController
@RequestMapping("/goods/price-adjust/interval-price")
@Validated
@Api(description = "商品批量调整区间价API", tags = "LevelPriceAdjustController")
public class IntervalPriceAdjustController {
    private final static String TAMPLATE_FILE_NAME = "批量调整阶梯价导入模板.xlsx";

    @Autowired
    private PriceAdjustService priceAdjustService;

    @Autowired
    private PriceAdjustmentRecordDetailProvider priceAdjustmentRecordDetailProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 区间价调整excel模板下载
     *
     * @param encrypted
     * @return
     */
    @ApiOperation(value = "商品区间价调整excel模板下载")
    @RequestMapping(value = "/template/{encrypted}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "鉴权加密串", required = true)
    public void template(@PathVariable String encrypted) {
        PriceAdjustmentTemplateExportRequest request = PriceAdjustmentTemplateExportRequest.builder()
                .priceAdjustmentType(PriceAdjustmentType.STOCK).build();
        priceAdjustService.downloadAdjustPriceTemplate(request, TAMPLATE_FILE_NAME);
    }

    /**
     * 区间价调整excel文件上传
     *
     * @param uploadFile
     * @return
     */
    @ApiOperation(value = "商品区间价调价excel文件上传")
    @PostMapping(value = "/upload")
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(priceAdjustService.uploadPriceAdjustFile(uploadFile));
    }

    /**
     * 删除调价详情
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "商品区间价调价详情移除")
    @DeleteMapping
    public BaseResponse delete(@RequestBody @Valid AdjustPriceDetailDeleteRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.delete(request);
    }

    /**
     * 确认导入
     *
     * @return
     */
    @ApiOperation(value = "商品区间价调价确认导入")
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<String> importDo() {
        return BaseResponse.success(priceAdjustService.importPriceAdjustFile(analysisFunctionByIntervalPriceAdjust,
                PriceAdjustmentType.STOCK));
    }

    /**
     * 编译调价详情
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "商品区间价调价详情编辑")
    @PutMapping
    public BaseResponse edit(@RequestBody @Valid IntervalPriceAdjustDetailModifyRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        return priceAdjustmentRecordDetailProvider.modifyIntervalPrice(request);
    }

    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @GetMapping(value = "/err/{ext}/{decrypted}")
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        priceAdjustService.downErrorFile(ext);
    }

    /**
     * 区间价确认调价
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "商品区间价确认调价")
    @PostMapping(value = "/do")
    public BaseResponse confirmAdjust(@RequestBody @Valid PriceAdjustConfirmRequest request) {
        priceAdjustService.checkOperator(request.getAdjustNo());
        priceAdjustService.adjustPriceConfirm(request);
        return BaseResponse.SUCCESSFUL();
    }

    private final Function<Workbook, List<PriceAdjustmentRecordDetailDTO>> analysisFunctionByIntervalPriceAdjust = (workbook) -> {
        List<PriceAdjustmentRecordDetailDTO> dataList = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        //获得当前sheet的开始行
        int firstRowNum = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        int maxCell = 16;
        Map<String, Cell> goodsInfoKeyMap = new HashMap<>();
        boolean isError = false;
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
            //基础信息
            PriceAdjustmentRecordDetailDTO detail = new PriceAdjustmentRecordDetailDTO();
            detail.setGoodsInfoNo(ExcelHelper.getValue(cells[0]).trim());
            detail.setGoodsInfoName(ExcelHelper.getValue(cells[1]).trim());
            detail.setGoodsSpecText(ExcelHelper.getValue(cells[2]));
            String marketingPriceVal = ExcelHelper.getValue(cells[4]).trim();
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
            //销售类型默认均为批发
            detail.setSaleType(SaleType.WHOLESALE);
            //校验调整后的市场价
            if (StringUtils.isNotBlank(marketingPriceVal)) {
                if ((!ValidateUtil.isNum(marketingPriceVal) && !ValidateUtil.isFloatNum(marketingPriceVal))) {
                    ExcelHelper.setError(workbook, cells[4], "只能填写0和正数，允许两位小数");
                    isError = true;
                } else if (new BigDecimal(marketingPriceVal).compareTo(new BigDecimal("9999999.99")) > 0) {
                    ExcelHelper.setError(workbook, cells[4], "只能在0-9999999.99范围内");
                    isError = true;
                } else {
                    detail.setAdjustedMarketPrice(new BigDecimal(marketingPriceVal));
                }
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
            List<GoodsIntervalPriceDTO> intervalPriceList = new ArrayList<>(5);
            //校验区间信息
            List<Long> countList = new ArrayList<>();
            //记录下最后一个设置了区间信息的index，用于校验跨区间
            int index = 6;
            for (int i = 6; i < maxCell; i += 2) {
                //记录上一个已设置区间信息的索引
                int y = index;
                //默认当前区间设置了区间信息
                index = i;
                String count = ExcelHelper.getValue(cells[i]).trim();
                String price = ExcelHelper.getValue(cells[i + 1]).trim();
                count = StringUtils.isBlank(count) && i == 6 ? "1" : count;
                String countTemp = null;
                if (StringUtils.isBlank(count)) {
                    if (StringUtils.isNotBlank(price)) {
                        ExcelHelper.setError(workbook, cells[i], "区间未填写");
                        isError = true;
                    } else {
                        //当前未设置任何区间信息，重置为上一个已设置区间信息的index
                        index = y;
                    }
                } else if (!ValidateUtil.isNum(countTemp = new BigDecimal(count).longValue() + "") || Integer.parseInt(countTemp) > 999999) {
                    ExcelHelper.setError(workbook, cells[i], "只能填写0-999999的整数");
                    isError = true;
                } else if (countList.contains(Long.valueOf(countTemp))) {
                    ExcelHelper.setError(workbook, cells[i], "区间重复");
                    isError = true;
                } else {
                    countList.add(Long.valueOf(countTemp));
                }
                if (StringUtils.isBlank(price)) {
                    if (StringUtils.isNotBlank(count)) {
                        ExcelHelper.setError(workbook, cells[i + 1], "区间价未填写");
                        isError = true;
                    }
                } else if (!ValidateUtil.isNum(price) && !ValidateUtil.isFloatNum(price)) {
                    ExcelHelper.setError(workbook, cells[i + 1], "只能填写0和正数，允许两位小数");
                    isError = true;
                } else if (new BigDecimal(price).compareTo(new BigDecimal("9999999.99")) > 0) {
                    ExcelHelper.setError(workbook, cells[i + 1], "只能在0-9999999.99范围内");
                    isError = true;
                }
                if (!isError) {
                    GoodsIntervalPriceDTO intervalPrice = new GoodsIntervalPriceDTO();
                    intervalPrice.setPrice(StringUtils.isNotBlank(price) ? new BigDecimal(price) : null);
                    intervalPrice.setCount(StringUtils.isNotBlank(countTemp) ? Long.valueOf(countTemp) : null);
                    intervalPrice.setType(PriceType.SKU);
                    intervalPriceList.add(intervalPrice);
                }
            }
            //校验跨区间
            for (int i = index - 2; i > 6; i -= 2) {
                String count = ExcelHelper.getValue(cells[i]).trim();
                String price = ExcelHelper.getValue(cells[i + 1]).trim();
                if (StringUtils.isBlank(count) && StringUtils.isBlank(price)) {
                    ExcelHelper.setError(workbook, cells[i], "订货区间请按顺序设置");
                    ExcelHelper.setError(workbook, cells[i + 1], "订货区间请按顺序设置");
                    isError = true;
                }
            }
            if (!isError) {
                detail.setIntervalPrice(JSONObject.toJSONString(intervalPriceList));
                dataList.add(detail);
            }

        }
        if (!isError) {
            //检查sku是否存在
            List<String> skuNos = goodsInfoQueryProvider.listMarketingPriceByNos(new GoodsInfoMarketingPriceByNosRequest(
                    new ArrayList<>(goodsInfoKeyMap.keySet()), commonUtil.getStoreId())).getContext().getDataList().stream().map(GoodsInfoMarketingPriceDTO::getGoodsInfoNo)
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
