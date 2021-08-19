package com.wanmi.sbc.goods.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsCateErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.common.GoodsCommonProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonBatchAddRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.bean.dto.*;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.request.GoodsExcelImportRequest;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.yunservice.YunGetResourceResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Slf4j
@Service
public class GoodsExcelService {

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsCommonProvider goodsCommonProvider;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 导入模板
     *
     * @return
     */
    @Transactional
    public List<String> implGoods(GoodsExcelImportRequest goodsRequest) {
        String ext = goodsRequest.getExt();
        byte[] content = yunServiceProvider.getFile(YunGetResourceRequest.builder()
                .resourceKey(Constants.GOODS_EXCEL_DIR.concat(goodsRequest.getUserId()))
                .build()).getContext().getContent();
        if (content == null) {
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
        }
        if (content.length > Constants.IMPORT_GOODS_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{Constants.IMPORT_GOODS_MAX_SIZE});
        }

        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(content))) {
            //创建Workbook工作薄对象，表示整个excel
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
            if(lastRowNum < 1){
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }

            GoodsCateListByConditionRequest request = new GoodsCateListByConditionRequest();
            request.setIsDefault(Constants.yes);
            request.setDelFlag(DeleteFlag.NO.toValue());
            Long defaultCateId = goodsCateQueryProvider.listByCondition(request).getContext().getGoodsCateVOList().stream()
                    .map(GoodsCateVO::getCateId).findFirst()
                    .orElseThrow(() -> new SbcRuntimeException(GoodsCateErrorCode.DEFAULT_CATE_NOT_EXIST));

            //SkuMap<Spu编号,Spu对象>
            Map<String, BatchGoodsDTO> goodses = new LinkedHashMap<>();
            //SkuMap<Spu编号,Sku对象>
            Map<String, List<BatchGoodsInfoDTO>> skus = new LinkedHashMap<>();
            //SkuMap<Spu编号,图片对象>
            Map<String, List<String>> images = new LinkedHashMap<>();

            //存储编码以及单元格对象，验证重复
            Map<String, List<Cell>> spuNos = new HashMap<>();
            Map<String, List<Cell>> skuNos = new HashMap<>();

            //存储编码以及单元格对象，验证规格和规格值
            Map<String, Cell> spuNoSpecs = new HashMap<>();
            Map<String, Cell> spuNoSpecDetails = new HashMap<>();

            //规格值<Spu编号,规格项集合>
            Map<String, List<BatchGoodsSpecDTO>> allSpecs = new HashMap<>();

            //规格值<规格项模拟编号_规格值名称,规格值模拟编号>，保证在同一个Spu下、同一规格项下，不允许出现重复规格值
            Map<String, Long> specDetailMap = new HashMap<>();
            //规格值<Spu编号,规格值集合>
            Map<String, List<BatchGoodsSpecDetailDTO>> allSpecDetails = new HashMap<>();

            Map<Long,Boolean> map = goodsCateQueryProvider.listLeaf().getContext().getGoodsCateList().parallelStream()
                    .collect(Collectors.toMap(GoodsCateVO::getCateId, c -> Boolean.TRUE));

            //规格列索引
            int[] specColNum = {9, 11, 13, 15, 17};
            int[] specDetailColNum = {10, 12, 14, 16, 18};
            int maxCell = 25;

            boolean isError = false;

            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                Cell[] cells = new Cell[maxCell];
                boolean isNotEmpty = false;
                for(int i = 0;i<maxCell;i++){
                    Cell cell = row.getCell(i);
                    if(cell == null) {
                        cell = row.createCell(i);
                    }
                    cells[i] = cell;
                    if(StringUtils.isNotBlank(ExcelHelper.getValue(cell))){
                        isNotEmpty = true;
                    }
                }
                //数据都为空，则跳过去
                if(!isNotEmpty){
                    continue;
                }

                BatchGoodsDTO goods = new BatchGoodsDTO();
                goods.setGoodsName(ExcelHelper.getValue(cells[0]));
                goods.setGoodsNo(ExcelHelper.getValue(cells[1]));
                goods.setMockGoodsId(goods.getGoodsNo());
                goods.setStoreId(goodsRequest.getStoreId());
                goods.setCompanyInfoId(goodsRequest.getCompanyInfoId());
                goods.setSupplierName(goodsRequest.getSupplierName());
                goods.setCompanyType(goodsRequest.getCompanyType());
                if(StringUtils.isBlank(goods.getGoodsNo())){
                    ExcelHelper.setError(workbook, cells[1], "此项必填");
                    isError = true;
                    goods.setGoodsNo("DEMO".concat(String.valueOf(rowNum)));
                }else if(!ValidateUtil.isBetweenLen(goods.getGoodsNo(), 1, 20)){
                    ExcelHelper.setError(workbook, cells[1], "长度必须1-20个字");
                    isError = true;
                }else if(!ValidateUtil.isNotChs(goods.getGoodsNo())){
                    ExcelHelper.setError(workbook, cells[1], "仅允许英文、数字、特殊字符");
                    isError = true;
                }

                goods.setCateId(NumberUtils.toLong(ExcelHelper.getValue(cells[2]).split("_")[0]));
                if(goods.getCateId() == 0 || (!map.containsKey(goods.getCateId()))){
                    goods.setCateId(defaultCateId);
                }

                goods.setGoodsUnit(ExcelHelper.getValue(cells[3]));
                if(StringUtils.isNotBlank(goods.getGoodsUnit())){
                    if(ValidateUtil.isOverLen(goods.getGoodsUnit(), 10)){
                        ExcelHelper.setError(workbook, cells[3], "长度必须0-10个字");
                        isError = true;
                    }else if(!ValidateUtil.isChsEng(goods.getGoodsUnit())){
                        ExcelHelper.setError(workbook, cells[3], "仅允许中文、英文");
                        isError = true;
                    }
                }

                Long brandId = NumberUtils.toLong(ExcelHelper.getValue(cells[4]).split("_")[0]);
                if(brandId > 0) {
                    goods.setBrandId(brandId);
                }

                String imageUrlStr = ExcelHelper.getValue(cells[5]);
                String[] imageUrls = imageUrlStr.split("\\|");
                List<String> imageArr = new ArrayList<>();
                if (imageUrls.length > 0) {
                    if (!images.containsKey(goods.getGoodsNo())) {
                        if(imageUrls.length > 10){
                            ExcelHelper.setError(workbook, cells[5], "最多传10张图片");
                            isError = true;
                        }
                        boolean isFirst = true;
                        for(int i=0;i<imageUrls.length;i++) {
                            if(WebUtil.isImage(imageUrls[i])) {
                                if (isFirst) {
                                    goods.setGoodsImg(imageUrls[i]);
                                    isFirst = false;
                                }
                                imageArr.add(imageUrls[i]);
                            }
                        }
                        images.put(goods.getGoodsNo(), imageArr);
                    }
                }

                String details = ExcelHelper.getValue(cells[6]);
                if (StringUtils.isNotBlank(details)) {
                    goods.setGoodsDetail(Arrays.stream(details.split("\\|")).map(s -> {
                        if(s.startsWith("http") && WebUtil.isImage(s)){
                            return String.format("<img src='%s'/><br/>", s);
                        }
                        return String.format("<p>%s</p>", s);
                    }).collect(Collectors.joining()));
                }

                BatchGoodsInfoDTO goodsInfo = new BatchGoodsInfoDTO();
                goodsInfo.setMockGoodsId(goods.getMockGoodsId());
                goodsInfo.setMockSpecIds(new ArrayList<>());
                goodsInfo.setMockSpecDetailIds(new ArrayList<>());

                //SKU编码
                String skuNo = ExcelHelper.getValue(cells[7]);
                if(StringUtils.isNotBlank(skuNo)){
                    if(!ValidateUtil.isBetweenLen(skuNo, 1, 20)){
                        ExcelHelper.setError(workbook, cells[7], "长度必须1-20个字");
                        isError = true;
                    }else if(!ValidateUtil.isNotChs(skuNo)){
                        ExcelHelper.setError(workbook, cells[7], "仅允许英文、数字、特殊字符");
                        isError = true;
                    }
                }else{
                    skuNo = this.getSkuNo(skuNos);
                    cells[7].setCellValue(skuNo);
                }

                if(skuNos.containsKey(skuNo)){
                    ExcelHelper.setError(workbook, cells[7], "文档中出现重复的SKU编码");
                    isError = true;
                }

                goodsInfo.setGoodsInfoNo(skuNo);

                //SKU图片
                goodsInfo.setGoodsInfoImg(ExcelHelper.getValue(cells[8]));
                if(StringUtils.isNotBlank(goodsInfo.getGoodsInfoImg()) && StringUtils.strip(goodsInfo.getGoodsInfoImg()).lastIndexOf("http") != 0){
                    ExcelHelper.setError(workbook, cells[8], "只上传一张图片");
                    isError = true;
                }else if(!WebUtil.isImage(goodsInfo.getGoodsInfoImg())){//如果不是图片链接
                    goodsInfo.setGoodsInfoImg(null);
                }

                //处理同一SPU下第一条的规格项
                if (!goodses.containsKey(goods.getGoodsNo())) {
                    for (int i : specColNum) {
                        BatchGoodsSpecDTO spec = new BatchGoodsSpecDTO();
                        spec.setMockGoodsId(goods.getMockGoodsId());
                        spec.setMockSpecId(NumberUtils.toLong(String.valueOf(rowNum).concat(String.valueOf(i))));
                        spec.setSpecName(ExcelHelper.getValue(cells[i]));
                        if(StringUtils.isNotBlank(ExcelHelper.getValue(cells[i+1]))){
                            if(StringUtils.isBlank(spec.getSpecName())) {
                                ExcelHelper.setError(workbook, cells[i], "此项必填");
                                isError = true;
                            }else if(ValidateUtil.isOverLen(spec.getSpecName(), 10)){
                                ExcelHelper.setError(workbook, cells[i], "长度必须0-10个字");
                                isError = true;
                            } else if (ValidateUtil.containsEmoji(spec.getSpecName())) {
                                ExcelHelper.setError(workbook, cells[i], "含有非法字符");
                                isError = true;
                            }
                        }

                        allSpecs.merge(goods.getGoodsNo(), new ArrayList(Arrays.asList(spec)), (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        });
                    }
                    spuNoSpecs.put(goods.getGoodsNo(), cells[specColNum[0]]);
                }else{
                    Arrays.stream(specColNum).forEach(i -> cells[i].setCellValue(""));
                }

                //处理规格值
                for (int i = 0 ;i< specDetailColNum.length; i++) {
                    BatchGoodsSpecDTO spec = allSpecs.get(goods.getGoodsNo()).get(i);
                    BatchGoodsSpecDetailDTO specDetail = new BatchGoodsSpecDetailDTO();
                    specDetail.setMockGoodsId(goods.getMockGoodsId());
                    specDetail.setDetailName(ExcelHelper.getValue(cells[specDetailColNum[i]]));
                    specDetail.setMockSpecId(spec.getMockSpecId());
                    specDetail.setMockSpecDetailId(NumberUtils.toLong(String.valueOf(rowNum).concat(String.valueOf(i))));

                    //设置SKU与规格的扁平数据
                    goodsInfo.getMockSpecIds().add(specDetail.getMockSpecId());

                    spuNoSpecDetails.put(goodsInfo.getGoodsInfoNo(), cells[specDetailColNum[0]]);

                    //不为空的规格项相应的规格值必须填写
                    if(StringUtils.isNotBlank(spec.getSpecName()) && StringUtils.isBlank(specDetail.getDetailName())){
                        ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "此项必填");
                        isError = true;
                    }

                    //明细不为空
                    if(StringUtils.isNotBlank(specDetail.getDetailName())){
                        if(ValidateUtil.isOverLen(specDetail.getDetailName(), 20)){
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "长度必须0-20个字");
                            isError = true;
                        } else if (ValidateUtil.containsEmoji(specDetail.getDetailName())) {
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "含有非法字符");
                            isError = true;
                        }

                        String key = String.valueOf(specDetail.getMockSpecId()).concat("_").concat(specDetail.getDetailName());
                        //保证在同一个Spu下、同一规格项下，不允许出现重复规格值
                        //存在同一个规格值，取其模拟Id
                        if(specDetailMap.containsKey(key)){
                            specDetail.setMockSpecDetailId(specDetailMap.get(key));
                        }else{
                            //不存在，则放明细模拟ID和明细
                            specDetailMap.put(key, specDetail.getMockSpecDetailId());
                            allSpecDetails.merge(goods.getGoodsNo(), new ArrayList(Arrays.asList(specDetail)), (s1, s2) -> {s1.addAll(s2);return s1;});
                        }

                        //设置SKU与规格值的扁平数据
                        goodsInfo.getMockSpecDetailIds().add(specDetail.getMockSpecDetailId());

                        if(allSpecDetails.get(goods.getGoodsNo()).stream().filter(goodsSpecDetail -> specDetail.getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).count() > 20){
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "在同一规格项内，不同的规格值不允许超过20个");
                            isError = true;
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(goodsInfo.getMockSpecDetailIds())
                        && skus.getOrDefault(goods.getGoodsNo(), new ArrayList<>()).stream()
                        .map(BatchGoodsInfoDTO::getMockSpecDetailIds)
                        .filter(r -> r.size() == goodsInfo.getMockSpecDetailIds().size()
                                && r.containsAll(goodsInfo.getMockSpecDetailIds())).count() > 0) {
                    Arrays.stream(specDetailColNum).forEach(i -> {
                        ExcelHelper.setError(workbook, cells[i], "不允许出现所有规格值完全一致的商品");
                    });
                    isError = true;
                }


                //库存
                String stock = ExcelHelper.getValue(cells[19]);
                goodsInfo.setStock(StringUtils.isBlank(stock)?0:new BigDecimal(stock).longValue());
                if(goodsInfo.getStock() < 0 || goodsInfo.getStock() > 9999999){
                    ExcelHelper.setError(workbook, cells[19], "必须在0-9999999范围内");
                    isError = true;
                }

                //条形码
                goodsInfo.setGoodsInfoBarcode(ExcelHelper.getValue(cells[20]));
                if(StringUtils.isNotBlank(goodsInfo.getGoodsInfoBarcode()) && ValidateUtil.isOverLen(goodsInfo.getGoodsInfoBarcode(), 20)){
                    ExcelHelper.setError(workbook, cells[20], "长度必须0-20个字");
                    isError = true;
                }

                //市场价
                String marketPrice = ExcelHelper.getValue(cells[21]);
                goodsInfo.setMarketPrice(StringUtils.isBlank(marketPrice)?BigDecimal.ZERO:new BigDecimal(marketPrice));
                goods.setMarketPrice(goodsInfo.getMarketPrice());
                if(StringUtils.isNotBlank(marketPrice)){
                    if(goodsInfo.getMarketPrice().compareTo(BigDecimal.ZERO) < 0 || goodsInfo.getMarketPrice().compareTo(new BigDecimal("9999999.99")) > 0){
                        ExcelHelper.setError(workbook, cells[21], "必须在0-9999999.99范围内");
                        isError = true;
                    }
                } else {
                    ExcelHelper.setError(workbook, cells[21], "此项必填");
                    isError = true;
                }

                //成本价
                String costPrice = ExcelHelper.getValue(cells[22]);
                goodsInfo.setCostPrice(StringUtils.isBlank(costPrice) ? BigDecimal.ZERO: new BigDecimal(costPrice));
                goods.setCostPrice(goodsInfo.getCostPrice());
                if(goodsInfo.getCostPrice().compareTo(BigDecimal.ZERO) < 0 || goodsInfo.getCostPrice().compareTo(new BigDecimal("9999999.99")) > 0){
                    ExcelHelper.setError(workbook, cells[22], "必须在0-9999999.99范围内");
                    isError = true;
                }

                //上下架
                String addedFlagStr = ExcelHelper.getValue(cells[23]);

                if ("上架".equals(addedFlagStr.trim())) {
                    goodsInfo.setAddedFlag(AddedFlag.YES.toValue());
                } else if("下架".equals(addedFlagStr.trim())){
                    goodsInfo.setAddedFlag(AddedFlag.NO.toValue());
                } else{
                    ExcelHelper.setError(workbook, cells[23], "必须在[上架、下架]范围内");
                    isError = true;
                }

                //积分价格
                String buyPointStr = ExcelHelper.getValue(cells[24]);
                if (StringUtils.isNotBlank(buyPointStr)) {
                    goodsInfo.setBuyPoint(new BigDecimal(buyPointStr).longValue());
                }else{
                    goodsInfo.setBuyPoint(0L);
                }


                if (!goodses.containsKey(goods.getGoodsNo())) {
                    //商品名称
                    if(StringUtils.isBlank(goods.getGoodsName())){
                        ExcelHelper.setError(workbook, cells[0], "此项必填");
                        isError = true;
                    }else if(!ValidateUtil.isBetweenLen(goods.getGoodsName(), 1, 40)){
                        ExcelHelper.setError(workbook, cells[0], "长度必须1-40个字");
                        isError = true;
                    }else if(ValidateUtil.containsEmoji(goods.getGoodsName())){
                        ExcelHelper.setError(workbook, cells[0], "含有非法字符");
                        isError = true;
                    }

                    goodses.put(goods.getGoodsNo(), goods);
                } else {
                    if(skus.getOrDefault(goods.getGoodsNo(),new ArrayList<>()).size() >= 50){
                        ExcelHelper.setError(workbook, cells[1], "同一SPU的商品不允许超过50条");
                        isError = true;
                    }
                }

                skus.merge(goods.getGoodsNo(), new ArrayList(Arrays.asList(goodsInfo)), (s1, s2) -> {s1.addAll(s2);return s1;});

                spuNos.merge(goods.getGoodsNo(), new ArrayList(Arrays.asList(cells[1])), (s1, s2) -> {
                    s1.addAll(s2);
                    return s1;
                });

                skuNos.merge(goodsInfo.getGoodsInfoNo(), new ArrayList(Arrays.asList(cells[7])), (s1, s2) -> {s1.addAll(s2);return s1;});
            }

            List<GoodsVO> goodsList = goodsQueryProvider.listByCondition(GoodsByConditionRequest.builder()
                    .delFlag(DeleteFlag.NO.toValue()).goodsNos(new ArrayList<>(spuNos.keySet())).build()).getContext().getGoodsVOList();

            //设置重复错误提示
            if (CollectionUtils.isNotEmpty(goodsList)) {
                goodsList.forEach(goods -> {
                    spuNos.get(goods.getGoodsNo()).stream().forEach(cell -> {
                        //为单元格设置重复错误提示
                        ExcelHelper.setError(workbook, cell, "该编码重复");
                    });
                });
                isError = true;
            }

            List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listByCondition(GoodsInfoListByConditionRequest.builder().delFlag
                    (DeleteFlag.NO.toValue()).goodsInfoNos(new ArrayList<>(skuNos.keySet()))
                    .build()).getContext().getGoodsInfos();
            //设置重复错误提示
            if (CollectionUtils.isNotEmpty(goodsInfos)) {
                goodsInfos.forEach(sku -> {
                    skuNos.get(sku.getGoodsInfoNo()).stream().forEach(cell -> {
                        //为单元格设置重复错误提示
                        ExcelHelper.setError(workbook, cell, "该编码重复");
                    });
                });
                isError = true;
            }

            //针对多个SKU验证规格或规格值是否为空
            for(BatchGoodsDTO goods : goodses.values()) {
                List<BatchGoodsInfoDTO> goodsInfoList = skus.get(goods.getGoodsNo());
                if(goodsInfoList.size() > 1){
                    boolean isFirst = false;
                    for(BatchGoodsInfoDTO goodsInfo: goodsInfoList){
                        if(CollectionUtils.isEmpty(goodsInfo.getMockSpecIds()) && (!isFirst)){
                            ExcelHelper.setError(workbook, spuNoSpecs.get(goods.getGoodsNo()), "规格不允许为空");
                            isFirst = true;
                            isError = true;
                        }

                        if(CollectionUtils.isEmpty(goodsInfo.getMockSpecDetailIds())){
                            //为单元格设置重复错误提示
                            ExcelHelper.setError(workbook, spuNoSpecDetails.get(goodsInfo.getGoodsInfoNo()), "规格值不允许为空");
                            isError = true;
                        }
                    }
                }
            }

            if(isError){
                this.errorExcel(goodsRequest.getUserId().concat(".").concat(ext), workbook);
                throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, new Object[]{ext});
            }

            //指定图片与goods的mockGoodsId关系
            List<BatchGoodsImageDTO> imageDTOList = new ArrayList<>();
            goodses.values().forEach(goods -> {
                if (images.containsKey(goods.getGoodsNo())) {
                    imageDTOList.addAll(images.get(goods.getGoodsNo()).stream().filter(StringUtils::isNotBlank).map(s -> {
                        BatchGoodsImageDTO image = new BatchGoodsImageDTO();
                        image.setMockGoodsId(goods.getMockGoodsId());
                        image.setArtworkUrl(s);
                        return image;
                    }).collect(Collectors.toList()));
                }
            });

            //批量保存
            return goodsCommonProvider.batchAdd(GoodsCommonBatchAddRequest.builder()
                    .goodsList(new ArrayList<>(goodses.values()))
                    .goodsInfoList(skus.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .specList(allSpecs.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .specDetailList(allSpecDetails.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .imageList(imageDTOList).build()).getContext().getSkuNoList();
        } catch (SbcRuntimeException e) {
            log.error("商品导入异常", e);
            throw e;
        } catch (Exception e) {
            log.error("商品导入异常", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }

    /**
     * 下载Excel错误文档
     * @param userId 用户Id
     * @param ext 文件扩展名
     */
    public void downErrExcel(String userId, String ext){
        //商品导入错误文件resourceKey
        String resourceKey = Constants.GOODS_ERR_EXCEL_DIR.concat(userId);
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

        if (file.getSize() > Constants.IMPORT_GOODS_MAX_SIZE * 1024 * 1024) {
            throw new SbcRuntimeException(GoodsImportErrorCode.FILE_MAX_SIZE, new Object[]{Constants.IMPORT_GOODS_MAX_SIZE});
        }

        String resourceKey = Constants.GOODS_EXCEL_DIR.concat(userId);
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
                    .resourceKey(Constants.GOODS_ERR_EXCEL_DIR.concat(userId))
                    .build();
            yunServiceProvider.uploadFileExcel(yunUploadResourceRequest).getContext();
            return newFileName;
        } catch (IOException e) {
            log.error("生成的错误文件上传至云空间失败", e);
            throw new SbcRuntimeException(CommonErrorCode.UPLOAD_FILE_ERROR);
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
            Sheet sheet2 = workbook.getSheetAt(1);
            if(!(row.getCell(0).getStringCellValue().contains("商品名称") && sheet2.getSheetName().contains("数据"))){
                throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
            }
        }catch (Exception e){
            throw new SbcRuntimeException(GoodsImportErrorCode.DATA_FILE_FAILD);
        }
    }

    /**
     * 生成不存在skuNoList中的Sku编码
     * @param skuNoList 已存在的sku编码
     * @return sku编码
     */
    /**
     * 递归式，获取SkuNo，防止重复
     * @param existsMap
     * @return
     */
    public String getSkuNo(Map<String,List<Cell>> existsMap){
        String skuNo = getSkuNo();
        if(existsMap.containsKey(skuNo)){
            return getSkuNo(existsMap);
        }
        return skuNo;
    }

    /**
     * 获取Sku编码
     * @return Sku编码
     */
    private String getSkuNo() {
        return "8".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3));
    }
}
