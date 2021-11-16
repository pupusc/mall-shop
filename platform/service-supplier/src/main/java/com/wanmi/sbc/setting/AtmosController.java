package com.wanmi.sbc.setting;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
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
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Api(tags = "AtmosController", description = "氛围配置")
@RestController
@RequestMapping("/atoms")
public class AtmosController {
    @Autowired
    private AtmosService atmosService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "上传氛围")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(atmosService.uploadAtmosFile(uploadFile));
    }


    /**
     * 确认导入模版
     */
    @ApiOperation(value = "确认导入模版")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext", value = "后缀", required = true)
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<String> implGoods() {
        String res = atmosService.importAtmosFile(analysisFunctionByAtmos);
        return BaseResponse.success(res);
    }


    private final Function<Workbook, List<AtmosphereDTO>> analysisFunctionByAtmos = (workbook) -> {
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        boolean isError = false;
        int maxCell = 4;
        Map<String, Cell> skuNos = new HashMap<>();
        //this.checkExcel(workbook);
        //循环除了第一行的所有行
        List<AtmosphereDTO> list = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
            //获得当前行
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            boolean isNotEmpty = false;
            Cell[] cells = new Cell[maxCell];
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
            //数据都为空，则跳过去
            if (!isNotEmpty) {
                continue;
            }
            AtmosphereDTO atmosphereDTO = new AtmosphereDTO();
            String startTime = ExcelHelper.getValue(cells[0]).trim();
            String endTime = ExcelHelper.getValue(cells[1]).trim();
            String atmosTypeValue = ExcelHelper.getValue(cells[2]).trim();
            atmosphereDTO.setImageUrl(ExcelHelper.getValue(cells[5]).trim());
            String elementOne = ExcelHelper.getValue(cells[6]).trim();
            String elementTwo = ExcelHelper.getValue(cells[7]).trim();
            String elementThird = ExcelHelper.getValue(cells[8]).trim();
            String elementFour = ExcelHelper.getValue(cells[9]).trim();
            //开始结束时间校验
            try{
                LocalDateTime sTime = LocalDateTime.parse(startTime,df);
                atmosphereDTO.setStartTime(sTime);
            }catch (Exception e){
                ExcelHelper.setError(workbook, cells[0], "氛围开始时间格式不正确");
                isError = true;
            }

            try{
                LocalDateTime eTime = LocalDateTime.parse(endTime,df);
                atmosphereDTO.setEndTime(eTime);
            }catch (Exception e){
                ExcelHelper.setError(workbook, cells[1], "氛围结束时间格式不正确");
                isError = true;
            }
            //校验类型
            if (StringUtils.isNotBlank(atmosTypeValue)) {
                if (!"积分氛围".equals(atmosTypeValue) && !"通用氛围".equals(atmosTypeValue)) {
                    ExcelHelper.setError(workbook, cells[2], "选项不合法");
                    isError = true;
                } else {
                    atmosphereDTO.setAtmosType("积分氛围".equals(atmosTypeValue) ? 1 : 2);
                }
            }
            //SKU编码
            String goodsInfoNo = ExcelHelper.getValue(cells[3]);
            if (StringUtils.isBlank(goodsInfoNo)) {
                ExcelHelper.setError(workbook, cells[3], "必填，请填写要调价的商品sku编码");
                isError = true;
            } else if (goodsInfoNo.length() > 20) {
                ExcelHelper.setError(workbook, cells[3], "长度必须1-20个字");
                isError = true;
            } else if (!ValidateUtil.isNotChs(goodsInfoNo)) {
                ExcelHelper.setError(workbook, cells[3], "仅允许英文、数字、特殊字符");
                isError = true;
            } else if (skuNos.containsKey(goodsInfoNo)) {
                ExcelHelper.setError(workbook, cells[3], "文档中出现重复的SKU编码");
                isError = true;
            } else {
                atmosphereDTO.setSkuNo(goodsInfoNo);
            }

            //商品名称
            String goodsInfoName = ExcelHelper.getValue(cells[4]);
            if (StringUtils.isNotBlank(goodsInfoName)) {
                if (!ValidateUtil.isBetweenLen(goodsInfoName, 1, 40)) {
                    ExcelHelper.setError(workbook, cells[4], "长度必须1-40个字");
                    isError = true;
                } else if (ValidateUtil.containsEmoji(goodsInfoName)) {
                    ExcelHelper.setError(workbook, cells[4], "含有非法字符");
                    isError = true;
                } else {
                    atmosphereDTO.setGoodsName(goodsInfoName);
                }
            }
            if (!isError) {
                skuNos.put(atmosphereDTO.getSkuNo(), cells[3]);
            }
            //文字
            Map<Integer,String> map  = new HashMap<>(16);
            map.put(1,elementOne);
            map.put(2,elementTwo);
            map.put(3,elementThird);
            map.put(4,elementFour);
            atmosphereDTO.setDesc(JSON.toJSONString(map));
            list.add(atmosphereDTO);

        }

        List<String> nos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().delFlag
                (DeleteFlag.NO.toValue()).goodsInfoNos(new ArrayList<>(skuNos.keySet())).storeId(commonUtil.getStoreId())
                .build()).getContext().getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoNo).collect(Collectors.toList());

        //商品是否存在
        for (Map.Entry<String, Cell> entry : skuNos.entrySet()) {
            Cell cell = skuNos.get(entry.getKey());
            if (!nos.contains(entry.getKey())) {
                ExcelHelper.setError(workbook, cell, "商品不存在");
                isError = true;
            }
        }
        if (isError) {
            throw new SbcRuntimeException(CommonErrorCode.IMPORTED_DATA_ERROR);
        }
        return list;
    };
}
