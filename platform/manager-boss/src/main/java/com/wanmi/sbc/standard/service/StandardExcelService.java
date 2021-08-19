package com.wanmi.sbc.standard.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.*;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.standard.StandardExcelProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandListRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsBatchAddRequest;
import com.wanmi.sbc.goods.bean.dto.*;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.service.GoodsExcelService;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.yunservice.YunGetResourceRequest;
import com.wanmi.sbc.sensitivewords.service.SensitiveWordService;
import com.wanmi.sbc.standard.request.StandardExcelImplGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Slf4j
@Service
public class StandardExcelService {

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private StandardExcelProvider standardExcelProvider;

    @Autowired
    private GoodsExcelService goodsExcelService;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private SensitiveWordService sensitiveWordService;

    /**
     * 导入模板
     *
     * @return
     */
    @Transactional
    public List<String> implGoods(StandardExcelImplGoodsRequest goodsRequest) {
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
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }

            //检测文档正确性
            this.goodsExcelService.checkExcel(workbook);

            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                throw new SbcRuntimeException(GoodsImportErrorCode.EMPTY_ERROR);
            }

            //SkuMap<Spu名称,Spu对象>
            Map<String, BatchStandardGoodsDTO> goodses = new LinkedHashMap<>();
            //SkuMap<Spu名称,Sku对象>
            Map<String, List<BatchStandardSkuDTO>> skus = new LinkedHashMap<>();
            //SkuMap<Spu名称,图片对象>
            Map<String, List<String>> images = new LinkedHashMap<>();

            //存储Spu名称以及单元格对象，验证规格和规格值
            Map<String, Cell> spuSpecs = new HashMap<>();

            //规格值<Spu名称,规格项集合>
            Map<String, List<BatchStandardSpecDTO>> allSpecs = new HashMap<>();

            //规格值<规格项模拟编号_规格值名称,规格值模拟编号>，保证在同一个Spu下、同一规格项下，不允许出现重复规格值
            Map<String, Long> specDetailMap = new HashMap<>();
            //规格值<Spu名称,规格值集合>
            Map<String, List<BatchStandardSpecDetailDTO>> allSpecDetails = new HashMap<>();

            Map<Long, Boolean> goodsCateMap = goodsCateQueryProvider.listLeaf().getContext().getGoodsCateList().stream()
                    .collect(Collectors.toMap(GoodsCateVO::getCateId, c -> Boolean.TRUE));

            Set<Long> brandSet = goodsBrandQueryProvider.list(GoodsBrandListRequest.builder()
                    .delFlag(DeleteFlag.NO.toValue()).build()).getContext().getGoodsBrandVOList().stream()
                    .map(GoodsBrandVO::getBrandId).collect(Collectors.toSet());

            //所有敏感词
            Set<String> badwords = sensitiveWordService.getAllBadWord();

            //规格列索引
            int[] specColNum = {8, 10, 12, 14, 16};
            int[] specDetailColNum = {9, 11, 13, 15, 17};
            int maxCell = 21;

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

                BatchStandardGoodsDTO goods = new BatchStandardGoodsDTO();
                goods.setGoodsName(ExcelHelper.getValue(cells[0]));
                goods.setMockGoodsId(goods.getGoodsName());
                //商品名称
                if (StringUtils.isBlank(goods.getGoodsName())) {
                    ExcelHelper.setError(workbook, cells[0], "此项必填");
                    isError = true;
                } else if (!ValidateUtil.isBetweenLen(goods.getGoodsName(), 1, 40)) {
                    ExcelHelper.setError(workbook, cells[0], "长度必须1-40个字");
                    isError = true;
                } else if (ValidateUtil.containsEmoji(goods.getGoodsName())) {
                    ExcelHelper.setError(workbook, cells[0], "含有非法字符");
                    isError = true;
                } else {
                    //敏感词验证
                    String badWord = sensitiveWordService.getBadWordTxt(badwords, goods.getGoodsName());
                    if (badWord != null) {
                        ExcelHelper.setError(workbook, cells[0], "商品名称".concat(badWord));
                        isError = true;
                    }
                }
                //是否第一个SPU
                boolean isFirstGoods = false;
                if (!goodses.containsKey(goods.getGoodsName())){
                    isFirstGoods = true;
                }
                //如果第一个SPU商品处理
                if (isFirstGoods) {
                    goods.setCateId(NumberUtils.toLong(ExcelHelper.getValue(cells[1]).split("_")[0]));
                    if (goods.getCateId() == 0 || (!goodsCateMap.containsKey(goods.getCateId()))) {
                        ExcelHelper.setError(workbook, cells[1], "请选择平台类目或平台类目不存在");
                        isError = true;
                    }

                    goods.setGoodsUnit(ExcelHelper.getValue(cells[2]));
                    if (StringUtils.isBlank(goods.getGoodsUnit())) {
                        ExcelHelper.setError(workbook, cells[2], "此项必填");
                        isError = true;
                    } else if (ValidateUtil.isOverLen(goods.getGoodsUnit(), 10)) {
                        ExcelHelper.setError(workbook, cells[2], "长度必须1-10个字");
                        isError = true;
                    } else if (!ValidateUtil.isChsEng(goods.getGoodsUnit())) {
                        ExcelHelper.setError(workbook, cells[2], "仅允许中文、英文");
                        isError = true;
                    }


                    Long brandId = NumberUtils.toLong(ExcelHelper.getValue(cells[3]).split("_")[0]);
                    if (brandId > 0 && brandSet.contains(brandId)) {
                        goods.setBrandId(brandId);
                    }
                }

                String imageUrlStr = ExcelHelper.getValue(cells[4]);
                String[] imageUrls = imageUrlStr.split("\\|");
                List<String> imageArr = new ArrayList<>();
                if (imageUrls.length > 0) {
                    if (!images.containsKey(goods.getGoodsName())) {
                        if (imageUrls.length > 10) {
                            ExcelHelper.setError(workbook, cells[4], "最多传10张图片");
                            isError = true;
                        }
                        boolean isFirst = true;
                        for (int i = 0; i < imageUrls.length; i++) {
                            if(imageUrls[i].length() > 255){
                                ExcelHelper.setError(workbook, cells[4], "商品图片链接长度不能超过255");
                                isError = true;
                            }
                            if (WebUtil.isImage(imageUrls[i])) {
                                if (isFirst) {
                                    goods.setGoodsImg(imageUrls[i]);
                                    isFirst = false;
                                }
                                imageArr.add(imageUrls[i]);
                            }
                        }
                        images.put(goods.getGoodsName(), imageArr);
                    }
                }

                //SPU视频链接地址
                goods.setGoodsVideo(ExcelHelper.getValue(cells[5]));
                if (StringUtils.isNotBlank(goods.getGoodsVideo()) && !goods.getGoodsVideo().endsWith("mp4")) {
                    ExcelHelper.setError(workbook, cells[5], "视频仅支持mp4格式");
                    isError = true;
                }
                if(goods.getGoodsVideo().length() > 255){
                    ExcelHelper.setError(workbook, cells[5], "商品视频链接长度不能超过255");
                    isError = true;
                }
                String details = ExcelHelper.getValue(cells[6]);
                if (StringUtils.isNotBlank(details)) {
                    //敏感词验证
                    String badWord = sensitiveWordService.getBadWordTxt(badwords, details);
                    if (badWord != null) {
                        ExcelHelper.setError(workbook, cells[6], "商品详情".concat(badWord));
                        isError = true;
                    }

                    goods.setGoodsDetail(Arrays.stream(details.split("\\|")).map(s -> {
                        if (s.startsWith("http")) {
                            return String.format("<img src='%s'/><br/>", s);
                        }
                        return String.format("<p>%s</p>", s);
                    }).collect(Collectors.joining()));
                }

                BatchStandardSkuDTO goodsInfo = new BatchStandardSkuDTO();
                goodsInfo.setMockGoodsId(goods.getMockGoodsId());
                goodsInfo.setMockSpecIds(new ArrayList<>());
                goodsInfo.setMockSpecDetailIds(new ArrayList<>());

                //SKU图片
                goodsInfo.setGoodsInfoImg(ExcelHelper.getValue(cells[7]));
                if (StringUtils.isNotBlank(goodsInfo.getGoodsInfoImg()) && !WebUtil.isImage(goodsInfo.getGoodsInfoImg())) {//如果不是图片链接
                    goodsInfo.setGoodsInfoImg(null);
                }
                if(goodsInfo.getGoodsInfoImg().length() > 255){
                    ExcelHelper.setError(workbook, cells[7], "商品SKU图片链接长度不能超过255");
                    isError = true;
                }

                //处理同一SPU下第一条的规格项
                if (isFirstGoods) {
                    for (int i : specColNum) {
                        BatchStandardSpecDTO spec = new BatchStandardSpecDTO();
                        spec.setMockGoodsId(goods.getMockGoodsId());
                        spec.setMockSpecId(NumberUtils.toLong(String.valueOf(rowNum).concat(String.valueOf(i))));
                        spec.setSpecName(ExcelHelper.getValue(cells[i]));
                        if (StringUtils.isNotBlank(ExcelHelper.getValue(cells[i + 1]))) {
                            if (StringUtils.isBlank(spec.getSpecName())) {
                                ExcelHelper.setError(workbook, cells[i], "此项必填");
                                isError = true;
                            } else if (ValidateUtil.isOverLen(spec.getSpecName(), 10)) {
                                ExcelHelper.setError(workbook, cells[i], "长度必须0-10个字");
                                isError = true;
                            } else if (ValidateUtil.containsEmoji(spec.getSpecName())) {
                                ExcelHelper.setError(workbook, cells[i], "含有非法字符");
                                isError = true;
                            } else {
                                //敏感词验证
                                String badWord = sensitiveWordService.getBadWordTxt(badwords, spec.getSpecName());
                                if (badWord != null) {
                                    ExcelHelper.setError(workbook, cells[i], "规格".concat(badWord));
                                    isError = true;
                                }
                            }
                        }

                        allSpecs.merge(goods.getGoodsName(), new ArrayList(Arrays.asList(spec)), (s1, s2) -> {
                            s1.addAll(s2);
                            return s1;
                        });
                    }
                    spuSpecs.put(goods.getGoodsName(), cells[specColNum[0]]);
                } else {
                    Arrays.stream(specColNum).forEach(i -> cells[i].setCellValue(""));
                }

                //处理规格值
                for (int i = 0; i < specDetailColNum.length; i++) {
                    BatchStandardSpecDTO spec = allSpecs.get(goods.getGoodsName()).get(i);
                    BatchStandardSpecDetailDTO specDetail = new BatchStandardSpecDetailDTO();
                    specDetail.setMockGoodsId(goods.getMockGoodsId());
                    specDetail.setDetailName(ExcelHelper.getValue(cells[specDetailColNum[i]]));
                    specDetail.setMockSpecId(spec.getMockSpecId());
                    specDetail.setMockSpecDetailId(NumberUtils.toLong(String.valueOf(rowNum).concat(String.valueOf(i))));

                    //设置SKU与规格的扁平数据
                    goodsInfo.getMockSpecIds().add(specDetail.getMockSpecId());

                    //不为空的规格项相应的规格值必须填写
                    if (StringUtils.isNotBlank(spec.getSpecName()) && StringUtils.isBlank(specDetail.getDetailName())) {
                        ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "此项必填");
                        isError = true;
                    }

                    //明细不为空
                    if (StringUtils.isNotBlank(specDetail.getDetailName())) {
                        if (ValidateUtil.isOverLen(specDetail.getDetailName(), 20)) {
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "长度必须0-20个字");
                            isError = true;
                        } else if (ValidateUtil.containsEmoji(specDetail.getDetailName())) {
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "含有非法字符");
                            isError = true;
                        } else {
                            //敏感词验证
                            String badWord = sensitiveWordService.getBadWordTxt(badwords, specDetail.getDetailName());
                            if (badWord != null) {
                                ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "规格值".concat(badWord));
                                isError = true;
                            }
                        }

                        String key =
                                String.valueOf(specDetail.getMockSpecId()).concat("_").concat(specDetail.getDetailName());
                        //保证在同一个Spu下、同一规格项下，不允许出现重复规格值
                        //存在同一个规格值，取其模拟Id
                        if (specDetailMap.containsKey(key)) {
                            specDetail.setMockSpecDetailId(specDetailMap.get(key));
                        } else {
                            //不存在，则放明细模拟ID和明细
                            specDetailMap.put(key, specDetail.getMockSpecDetailId());
                            allSpecDetails.merge(goods.getGoodsName(), new ArrayList(Arrays.asList(specDetail)), (s1,
                                                                                                                  s2) -> {
                                s1.addAll(s2);
                                return s1;
                            });
                        }

                        //设置SKU与规格值的扁平数据
                        goodsInfo.getMockSpecDetailIds().add(specDetail.getMockSpecDetailId());

                        if (allSpecDetails.get(goods.getGoodsName()).stream().filter(goodsSpecDetail -> specDetail.getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).count() > 20) {
                            ExcelHelper.setError(workbook, cells[specDetailColNum[i]], "在同一规格项内，不同的规格值不允许超过20个");
                            isError = true;
                        }
                    }
                }

                if (CollectionUtils.isNotEmpty(goodsInfo.getMockSpecDetailIds())
                        && skus.getOrDefault(goods.getGoodsName(), new ArrayList<>()).stream()
                        .map(BatchStandardSkuDTO::getMockSpecDetailIds)
                        .filter(r -> r.size() == goodsInfo.getMockSpecDetailIds().size()
                                && r.containsAll(goodsInfo.getMockSpecDetailIds())).count() > 0) {
                    Arrays.stream(specDetailColNum).forEach(i -> {
                        ExcelHelper.setError(workbook, cells[i], "不允许出现所有规格值完全一致的商品");
                    });
                    isError = true;
                }

                //市场价
                String marketPrice = ExcelHelper.getValue(cells[18]);
                goodsInfo.setMarketPrice(StringUtils.isBlank(marketPrice) ? BigDecimal.ZERO :
                        new BigDecimal(marketPrice));
                goods.setMarketPrice(goodsInfo.getMarketPrice());
                if (StringUtils.isNotBlank(marketPrice)) {
                    if (goodsInfo.getMarketPrice().compareTo(BigDecimal.ZERO) < 0 || goodsInfo.getMarketPrice().compareTo(new BigDecimal("9999999.99")) > 0) {
                        ExcelHelper.setError(workbook, cells[18], "必须在0-9999999.99范围内");
                        isError = true;
                    }
                } else {
                    ExcelHelper.setError(workbook, cells[18], "此项必填");
                    isError = true;
                }

                if (isFirstGoods) {
                    //重量
                    String weightStr = ExcelHelper.getValue(cells[19]);
                    if (StringUtils.isNotBlank(weightStr)) {
                        goods.setGoodsWeight(new BigDecimal(weightStr).setScale(3, BigDecimal.ROUND_HALF_UP));
                        if (goods.getGoodsWeight().compareTo(new BigDecimal("0.001")) < 0 || goods.getGoodsWeight().compareTo(new BigDecimal("9999.999")) > 0) {

                            ExcelHelper.setError(workbook, cells[19], "必须在0.001-9999.999范围内");
                            isError = true;
                        }
                    } else {
                        ExcelHelper.setError(workbook, cells[19], "此项必填");
                        isError = true;
                    }

                    //体积
                    String cubageStr = ExcelHelper.getValue(cells[20]);
                    if (StringUtils.isNotBlank(cubageStr)) {
                        goods.setGoodsCubage(new BigDecimal(cubageStr).setScale(6, BigDecimal.ROUND_HALF_UP));
                        if (goods.getGoodsCubage().compareTo(new BigDecimal("0.000001")) < 0 || goods.getGoodsCubage().compareTo(new BigDecimal("999.999999")) > 0) {
                            ExcelHelper.setError(workbook, cells[20], "必须在0.000001-999.999999范围内");
                            isError = true;
                        }
                    } else {
                        ExcelHelper.setError(workbook, cells[20], "此项必填");
                        isError = true;
                    }

                    goodses.put(goods.getGoodsName(), goods);
                } else {
                    if (skus.getOrDefault(goods.getGoodsName(), new ArrayList<>()).size() >= 50) {
                        ExcelHelper.setError(workbook, cells[0], "同一商品名称的商品不允许超过50条");
                        isError = true;
                    }
                }

                skus.merge(goods.getGoodsName(), new ArrayList(Arrays.asList(goodsInfo)), (s1, s2) -> {
                    s1.addAll(s2);
                    return s1;
                });
            }

            //针对多个SKU验证规格或规格值是否为空
            for (BatchStandardGoodsDTO goods : goodses.values()) {
                List<BatchStandardSkuDTO> goodsInfoList = skus.get(goods.getGoodsName());
                if (goodsInfoList.size() > 1) {
                    boolean isFirst = false;
                    for (BatchStandardSkuDTO goodsInfo : goodsInfoList) {
                        if (CollectionUtils.isEmpty(goodsInfo.getMockSpecIds()) && (!isFirst)) {
                            ExcelHelper.setError(workbook, spuSpecs.get(goods.getGoodsName()), "规格不允许为空");
                            isFirst = true;
                            isError = true;
                        }
                    }
                }
            }

            if (isError) {
                goodsExcelService.errorExcel(goodsRequest.getUserId().concat(".").concat(ext), workbook);
                throw new SbcRuntimeException(GoodsImportErrorCode.CUSTOM_ERROR, new Object[]{ext});
            }

            //指定图片与goods的mockGoodsId关系
            List<BatchStandardImageDTO> imageDTOList = new ArrayList<>();
            goodses.values().forEach(goods -> {
                if (images.containsKey(goods.getGoodsName())) {
                    imageDTOList.addAll(images.get(goods.getGoodsName()).stream().filter(StringUtils::isNotBlank).map(s -> {
                        BatchStandardImageDTO image = new BatchStandardImageDTO();
                        image.setMockGoodsId(goods.getMockGoodsId());
                        image.setArtworkUrl(s);
                        return image;
                    }).collect(Collectors.toList()));
                }
            });

            return standardExcelProvider.batchAdd(StandardGoodsBatchAddRequest.builder()
                    .goodsList(new ArrayList<>(goodses.values()))
                    .skuList(skus.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .specList(allSpecs.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .specDetailList(allSpecDetails.values().stream().flatMap(Collection::stream).collect(Collectors.toList()))
                    .imageList(imageDTOList).build()).getContext().getStandardIds();
        } catch (SbcRuntimeException e) {
            log.error("商品导入异常", e);
            throw e;
        } catch (Exception e) {
            log.error("商品导入异常", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }
}
