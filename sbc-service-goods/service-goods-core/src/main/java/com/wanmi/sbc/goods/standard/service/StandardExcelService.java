package com.wanmi.sbc.goods.standard.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.request.standard.StandardGoodsBatchAddRequest;
import com.wanmi.sbc.goods.bean.dto.BatchStandardImageDTO;
import com.wanmi.sbc.goods.bean.dto.BatchStandardSkuDTO;
import com.wanmi.sbc.goods.bean.dto.BatchStandardSpecDTO;
import com.wanmi.sbc.goods.bean.dto.BatchStandardSpecDetailDTO;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.info.service.S2bGoodsExcelService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardimages.repository.StandardImageRepository;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecDetailRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Service
public class StandardExcelService {

    @Value("classpath:standard_template.xls")
    private Resource templateFile;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private StandardImageRepository standardImageRepository;

    @Autowired
    private StandardSpecRepository standardSpecRepository;

    @Autowired
    private StandardSpecDetailRepository standardSpecDetailRepository;

    @Autowired
    private StandardSkuSpecDetailRelRepository standardSkuSpecDetailRelRepository;

    @Autowired
    private S2bGoodsExcelService s2bGoodsExcelService;

    /**
     * 导入模板
     *
     * @return
     */
    @Transactional
    public List<String> batchAdd(StandardGoodsBatchAddRequest request) {
        List<String> goodsIds = new ArrayList<>();
        Map<String, List<BatchStandardSkuDTO>> skuMap = request.getSkuList().stream()
                .collect(Collectors.groupingBy(BatchStandardSkuDTO::getMockGoodsId));

        Map<String, List<BatchStandardSpecDTO>> allSpecs = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getSpecList())) {
            allSpecs.putAll(request.getSpecList().stream()
                    .collect(Collectors.groupingBy(BatchStandardSpecDTO::getMockGoodsId)));
        }

        Map<String, List<BatchStandardSpecDetailDTO>> allSpecDetails = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getSpecList())) {
            allSpecDetails.putAll(request.getSpecDetailList().stream()
                    .collect(Collectors.groupingBy(BatchStandardSpecDetailDTO::getMockGoodsId)));
        }

        Map<String, List<BatchStandardImageDTO>> images = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getImageList())) {
            images.putAll(request.getImageList().stream()
                    .collect(Collectors.groupingBy(BatchStandardImageDTO::getMockGoodsId)));
        }

        request.getGoodsList().forEach(goods -> {
            goods.setCreateTime(LocalDateTime.now());
            goods.setUpdateTime(goods.getCreateTime());
            goods.setDelFlag(DeleteFlag.NO);
            goods.setGoodsSource(GoodsSource.PLATFORM.toValue());

            List<BatchStandardSkuDTO> goodsInfoList = skuMap.get(goods.getGoodsName());
            goods.setMoreSpecFlag(Constants.no);
            if (goodsInfoList.stream().anyMatch(goodsInfo ->
                    CollectionUtils.isNotEmpty(goodsInfo.getMockSpecDetailIds()))) {
                goods.setMoreSpecFlag(Constants.yes);
            }

            String goodsId = standardGoodsRepository.save(KsBeanUtil.convert(goods, StandardGoods.class)).getGoodsId();
            goodsIds.add(goodsId);

            List<BatchStandardSpecDTO> specs = allSpecs.getOrDefault(goods.getGoodsName(), Collections.emptyList());
            List<BatchStandardSpecDetailDTO> specDetails = allSpecDetails.getOrDefault(goods.getGoodsName(),
                    Collections.emptyList());

            //如果是多规格
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                //新增含有规格值的规格
                specs.stream()
                        .filter(goodsSpec -> specDetails.stream()
                                .anyMatch(goodsSpecDetail -> goodsSpecDetail.getMockSpecId().equals(goodsSpec.getMockSpecId())))
                        .forEach(goodsSpec -> {
                            goodsSpec.setCreateTime(goods.getCreateTime());
                            goodsSpec.setUpdateTime(goods.getCreateTime());
                            goodsSpec.setGoodsId(goodsId);
                            goodsSpec.setDelFlag(DeleteFlag.NO);
                            goodsSpec.setSpecId(standardSpecRepository.save(
                                    KsBeanUtil.convert(goodsSpec, StandardSpec.class)).getSpecId());
                        });
                //新增规格值
                specDetails.forEach(goodsSpecDetail -> {
                    Optional<BatchStandardSpecDTO> specOpt = specs.stream().filter(goodsSpec -> goodsSpec
                            .getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).findFirst();
                    if (specOpt.isPresent()) {
                        goodsSpecDetail.setCreateTime(goods.getCreateTime());
                        goodsSpecDetail.setUpdateTime(goods.getCreateTime());
                        goodsSpecDetail.setGoodsId(goodsId);
                        goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                        goodsSpecDetail.setSpecId(specOpt.get().getSpecId());
                        goodsSpecDetail.setSpecDetailId(standardSpecDetailRepository.save(
                                KsBeanUtil.convert(goodsSpecDetail, StandardSpecDetail.class))
                                .getSpecDetailId());
                    }
                });
            }

            goodsInfoList.forEach(goodsInfo -> {
                goodsInfo.setGoodsId(goodsId);
                goodsInfo.setGoodsInfoName(goods.getGoodsName());
                goodsInfo.setCreateTime(goods.getCreateTime());
                goodsInfo.setUpdateTime(goods.getCreateTime());
                goodsInfo.setDelFlag(goods.getDelFlag());
                String skuId = standardSkuRepository.save(KsBeanUtil.convert(goodsInfo, StandardSku.class))
                        .getGoodsInfoId();
                goodsInfo.setGoodsInfoId(skuId);
                //存储规格
                //如果是多规格,新增SKU与规格明细值的关联表
                if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                    for (BatchStandardSpecDTO spec : specs) {
                        if (goodsInfo.getMockSpecIds().contains(spec.getMockSpecId())) {
                            for (BatchStandardSpecDetailDTO detail : specDetails) {
                                if (spec.getMockSpecId().equals(detail.getMockSpecId()) && goodsInfo
                                        .getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                    StandardSkuSpecDetailRel detailRel = new StandardSkuSpecDetailRel();
                                    detailRel.setGoodsId(goodsId);
                                    detailRel.setGoodsInfoId(skuId);
                                    detailRel.setSpecId(spec.getSpecId());
                                    detailRel.setSpecDetailId(detail.getSpecDetailId());
                                    detailRel.setDetailName(detail.getDetailName());
                                    detailRel.setCreateTime(detail.getCreateTime());
                                    detailRel.setUpdateTime(detail.getUpdateTime());
                                    detailRel.setDelFlag(detail.getDelFlag());
                                    standardSkuSpecDetailRelRepository.save(detailRel);
                                }
                            }
                        }
                    }
                }
            });

            //批量保存
            List<BatchStandardImageDTO> imageUrls = images.get(goods.getGoodsName());
            if (CollectionUtils.isNotEmpty(imageUrls)) {
                imageUrls.forEach(img -> {
                    img.setGoodsId(goodsId);
                    img.setCreateTime(goods.getCreateTime());
                    img.setUpdateTime(goods.getCreateTime());
                    img.setDelFlag(goods.getDelFlag());
                    standardImageRepository.save(KsBeanUtil.convert(img, StandardImage.class));
                });
            }
        });
        return goodsIds;
    }


    /**
     * 导出商品库模板
     * 加载xls已有模板，填充商品分类、品牌数据，实现excel下拉列表
     * @return base64位文件字符串
     */
    public String exportTemplate() {
        if (templateFile == null || !templateFile.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }

        //根据店铺获取品牌
        List<GoodsBrand> brands = goodsBrandRepository.findAll(GoodsBrandQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
        //根据店获取平台类目
        List<GoodsCate> cates = goodsCateRepository.queryLeaf();

        try (InputStream is = templateFile.getInputStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             Workbook wk = WorkbookFactory.create(is)) {
            Sheet cateSheet = wk.getSheetAt(1);
            //填放分类数据
            int cateSize = cates.size();
            for (int i = 0; i < cateSize; i++) {
                GoodsCate cate = cates.get(i);
                // 查询商品分类所有父分类名称
                String queryParentCate = s2bGoodsExcelService.queryParentCate(cate.getCateId());
                cateSheet.createRow(i).createCell(0).setCellValue(String.valueOf(cate.getCateId()).concat("_").concat(queryParentCate));
            }

            Sheet brandSheet = wk.getSheetAt(2);
            int brandSize = brands.size();
            for (int i = 0; i < brandSize; i++) {
                GoodsBrand brand = brands.get(i);
                brandSheet.createRow(i).createCell(0).setCellValue(String.valueOf(brand.getBrandId()).concat("_").concat(brand.getBrandName()));
            }
            wk.write(baos);
            return new BASE64Encoder().encode(baos.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }
}