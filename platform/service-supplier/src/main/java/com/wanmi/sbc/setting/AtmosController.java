package com.wanmi.sbc.setting;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.ValidateUtil;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.PriceAdjustmentTemplateExportRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.dto.PriceAdjustmentRecordDetailDTO;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.setting.api.provider.AtmosphereProvider;
import com.wanmi.sbc.setting.api.request.AtmosphereDeleteRequest;
import com.wanmi.sbc.setting.api.request.AtmosphereQueryRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import com.wanmi.sbc.setting.bean.dto.AtmosphereDTO;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.setting.bean.vo.StoreExpressCompanyRelaVO;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @menu 氛围配置
 * @tag atoms
 * @status undone
 */
@Api(tags = "AtmosController", description = "氛围配置")
@RestController
@RequestMapping("/atmos")
@Slf4j
public class AtmosController {
    @Autowired
    private AtmosService atmosService;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    private final static String TAMPLATE_FILE_NAME = "氛围导入模板.xlsx";

    @Value("classpath:/download/atmosphere.xlsx")
    private Resource templateFile;
    /**
     * 氛围excel模板下载
     * @menu 氛围配置
     * @param encrypted
     * @return
     */
    @ApiOperation(value = "氛围excel模板下载")
    @RequestMapping(value = "/template/{encrypted}", method = RequestMethod.GET)
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "encrypted",
            value = "鉴权加密串", required = true)
    public void template(@PathVariable String encrypted) {
        Resource file = templateFile;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is)){
            wk.write(outputStream);
            String fileName = URLEncoder.encode("氛围导入模板.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }

    /**
     * @description 上传氛围表格
     * @menu 氛围配置
     * @param uploadFile
     * @status undone
     */
    @ApiOperation(value = "上传氛围")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public BaseResponse<String> upload(@RequestParam("uploadFile") MultipartFile uploadFile) {
        return BaseResponse.success(atmosService.uploadAtmosFile(uploadFile));
    }


    /**
     * @description 确认导入模版
     * @menu 氛围配置
     * @status undone
     */
    @ApiOperation(value = "确认导入模版")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "ext", value = "后缀", required = true)
    @RequestMapping(value = "/import", method = RequestMethod.GET)
    @GlobalTransactional
    public BaseResponse<String> importAtmos() {
        String res = atmosService.importAtmosFile(analysisFunctionByAtmos);
        return BaseResponse.success(res);
    }

    /**
     * @description 氛围列表
     * @menu 氛围配置
     * @status undone
     */
    @ApiOperation(value = "氛围列表")
    @PostMapping(value = "/page")
    public BaseResponse<MicroServicePage<AtmosphereDTO>> page(@RequestBody AtmosphereQueryRequest request) {
        return atmosService.page(request);
    }

    /**
     * @description 删除氛围
     * @menu 氛围配置
     * @status undone
     */
    @ApiOperation(value = "删除氛围")
    @PostMapping(value = "/delete")
    public BaseResponse delete(@RequestBody AtmosphereDeleteRequest request) {
        return atmosService.delete(request);
    }


    /**
     * 下载错误文档
     */
    @ApiOperation(value = "下载错误文档")
    @RequestMapping(value = "/err/{ext}/{decrypted}", method = RequestMethod.GET)
    public void downErrExcel(@PathVariable String ext, @PathVariable String decrypted) {
        atmosService.downErrorFile(ext);
    }



    private final Function<Workbook, List<AtmosphereDTO>> analysisFunctionByAtmos = (workbook) -> {
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        boolean isError = false;
        int maxCell = 10;
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
            String elementThree = ExcelHelper.getValue(cells[8]).trim();
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
                    atmosphereDTO.setAtmosType("积分氛围".equals(atmosTypeValue) ? 2 : 1);
                }
            }
            //SKU编码
            String goodsInfoNo = ExcelHelper.getValue(cells[3]);
            if (StringUtils.isBlank(goodsInfoNo)) {
                ExcelHelper.setError(workbook, cells[3], "必填，请填写要上传的商品sku编码");
                isError = true;
            } else if (goodsInfoNo.length() > 20) {
                ExcelHelper.setError(workbook, cells[3], "长度必须1-20个字");
                isError = true;
            } else if (!ValidateUtil.isNotChs(goodsInfoNo)) {
                ExcelHelper.setError(workbook, cells[3], "仅允许英文、数字、特殊字符");
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
                    atmosphereDTO.setGoodsInfoName(goodsInfoName);
                }
            }
            if (!isError) {
                skuNos.put(atmosphereDTO.getSkuNo(), cells[3]);
            }
            //文字
            Map<String,String> map  = new HashMap<>(16);
            map.put("elementOne",elementOne);
            map.put("elementTwo",elementTwo);
            map.put("elementThree",elementThree);
            map.put("elementFour",elementFour);
            atmosphereDTO.setElementDesc(JSON.toJSONString(map));
            list.add(atmosphereDTO);

        }
        if (!isError) {
            BaseResponse<GoodsInfoListByConditionResponse> responseBaseResponse = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().delFlag
                    (DeleteFlag.NO.toValue()).goodsInfoNos(new ArrayList<>(skuNos.keySet())).storeId(commonUtil.getStoreId())
                    .build());
            for (Map.Entry<String, Cell> entry : skuNos.entrySet()) {
                Cell cell = entry.getValue();
                if(responseBaseResponse == null || responseBaseResponse.getContext() ==null || CollectionUtils.isEmpty(responseBaseResponse.getContext().getGoodsInfos())){
                    ExcelHelper.setError(workbook, cell, "商品不存在");
                    isError = true;
                    continue;
                }
                Optional<GoodsInfoVO> goodsInfoVO = responseBaseResponse.getContext().getGoodsInfos().stream().filter(p->p.getGoodsInfoNo().equals(entry.getKey())).findFirst();
                if(!goodsInfoVO.isPresent()){
                    ExcelHelper.setError(workbook, cell, "商品不存在");
                    isError = true;
                    continue;
                }
                list.stream().filter(p->p.getSkuNo().equals(entry.getKey())).forEach(p->{
                    p.setSkuId(goodsInfoVO.get().getGoodsInfoId());
                });
            }
        }
        if (isError) {
            throw new SbcRuntimeException(CommonErrorCode.IMPORTED_DATA_ERROR);
        }
        return list;
    };
}
