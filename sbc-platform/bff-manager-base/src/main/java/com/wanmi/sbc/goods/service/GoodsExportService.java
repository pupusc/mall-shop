package com.wanmi.sbc.goods.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.goods.api.provider.brand.ContractBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.ContractCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsSpecQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.ContractBrandListRequest;
import com.wanmi.sbc.goods.api.request.cate.ContractCateListCateByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateGoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsSpecListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByStoreIdRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.common.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsExportService {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private ContractCateQueryProvider contractCateQueryProvider;

    @Autowired
    private FreightTemplateGoodsQueryProvider freightTemplateGoodsQueryProvider;

    @Autowired
    private GoodsSpecQueryProvider goodsSpecQueryProvider;

    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;

    @Autowired
    private ContractBrandQueryProvider contractBrandQueryProvider;

    public void export(List<GoodsExportVo> goodsExportVoList, OutputStream outputStream, boolean isSupplier) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("商品名称", new SpelColumnRender<GoodsExportVo>("goodsInfoName")),
                new Column("商品副标题",new SpelColumnRender<GoodsExportVo>("goodsSubtitle")),
                new Column("SPU编码", new SpelColumnRender<GoodsExportVo>("goodsNo")),
                new Column("平台分类", new SpelColumnRender<GoodsExportVo>("cateName")),
                new Column("店铺分类", new SpelColumnRender<GoodsExportVo>("storeCateName")),
                new Column("计量单位",new SpelColumnRender<GoodsExportVo>("goodsUnit")),
                new Column("商品品牌",new SpelColumnRender<GoodsExportVo>("brandName")),
                new Column("商品图片",new SpelColumnRender<GoodsExportVo>("goodsImg")),
                new Column("商品视频",new SpelColumnRender<GoodsExportVo>("goodsVideo")),
                new Column("SKU编码",new SpelColumnRender<GoodsExportVo>("goodsInfoNo")),
                new Column("SKU图片",new SpelColumnRender<GoodsExportVo>("goodsInfoImg")),
                new Column("库存",new SpelColumnRender<GoodsExportVo>("stock")),
                new Column("条形码",new SpelColumnRender<GoodsExportVo>("goodsInfoBarcode")),
                new Column("上下架",(cell, object) -> {
                    GoodsExportVo vo = (GoodsExportVo) object;
                    AddedFlag addedFlag = AddedFlag.fromValue(vo.getAddedFlag());
                    String cellValue = "";
                    switch (addedFlag) {
                        case NO:
                            cellValue = "未上架";
                            break;
                        case YES:
                            cellValue = "已上架";
                            break;
                        case PART:
                            cellValue = "部分上架";
                            break;
                        default:
                    }
                    cell.setCellValue(cellValue);
                }),
                new Column("重量（kg）",new SpelColumnRender<GoodsExportVo>("goodsWeight")),
                new Column("体积（m³）",new SpelColumnRender<GoodsExportVo>("goodsCubage")),
                new Column("SPU（ERP）",new SpelColumnRender<GoodsExportVo>("erpGoodsNo")),
                new Column("SKU（ERP）",new SpelColumnRender<GoodsExportVo>("erpGoodsInfoNo")),
                new Column("是否组合",new SpelColumnRender<GoodsExportVo>("combinedCommodity")),

        };

        if(isSupplier) {
            Column[] newColumn = {
                    new Column("销售类型",new SpelColumnRender<GoodsExportVo>("saleType == 0 ? '批发' : '零售'")),
                    new Column("运费模版",new SpelColumnRender<GoodsExportVo>("freightTempName")),
                    new Column("市场价(元)",new SpelColumnRender<GoodsExportVo>("marketPrice")),
                    new Column("积分价格",new SpelColumnRender<GoodsExportVo>("buyPoint")),
                    new Column("下单方法", (cell, object) -> {
                        String cellValue = "";
                        GoodsExportVo vo = (GoodsExportVo) object;
                        String goodsBuyTypes = vo.getGoodsBuyTypes();
                        if(StringUtils.isNotBlank(goodsBuyTypes)) {
                            cellValue =  Arrays.stream(goodsBuyTypes.split(","))
                                    .map(type -> type.equals("1") ? "加入购物车" : "立即购买").collect(Collectors.joining("｜"));
                        }
                        cell.setCellValue(cellValue);
                    }),

                    new Column("设价方式", (cell, object) -> {
                        String cellValue = "";
                        GoodsExportVo vo = (GoodsExportVo) object;
                        GoodsPriceType priceType = GoodsPriceType.fromValue(vo.getPriceType());
                        switch (priceType) {
                            case MARKET:
                                cellValue = "以市场价销售";
                                break;
                            case CUSTOMER:
                                cellValue = "以等级价销售";
                                break;
                            case STOCK:
                                cellValue = "以阶梯价销售";
                                break;
                            default:
                        }
                        cell.setCellValue(cellValue);
                    })
            };
            columns = ArrayUtils.addAll(columns, newColumn);
        } else {
            Column[] newColumn = {
                    new Column("供货价(元)",new SpelColumnRender<GoodsExportVo>("supplyPrice"))
            };
            columns = ArrayUtils.addAll(columns, newColumn);
        }

        //规格
        for (int i = 0; i < 5; i++) {
            int specIndex = i + 1;
            Column[] specColunms = {
                    new Column("规格" + specIndex, (cell, object) -> {
                        GoodsExportVo vo = (GoodsExportVo) object;
                        String cellValue = "";
                        if (CollectionUtils.isNotEmpty(vo.getGoodsSpecVOList()) && vo.getGoodsSpecVOList().size() >= specIndex) {
                            GoodsInfoSpecExportVO exportVO = vo.getGoodsSpecVOList().get(specIndex - 1);
                            cellValue = exportVO.getSpecName();
                        }
                        cell.setCellValue(cellValue);
                    }),
                    new Column("规格" + specIndex + "规格值", (cell, object) -> {
                        GoodsExportVo vo = (GoodsExportVo) object;
                        String cellValue = "";
                        if (CollectionUtils.isNotEmpty(vo.getGoodsSpecVOList()) && vo.getGoodsSpecVOList().size() >= specIndex) {
                            GoodsInfoSpecExportVO exportVO = vo.getGoodsSpecVOList().get(specIndex - 1);
                            cellValue = exportVO.getSpecDetailName();
                        }
                        cell.setCellValue(cellValue);
                    })

            };
            columns = ArrayUtils.addAll(columns, specColunms);
        }

        excelHelper
                .addSheet(
                        "商品导出",
                        columns,
                        goodsExportVoList
                );
        excelHelper.write(outputStream);

    }

    public List<GoodsExportVo> getExportData(List<GoodsVO> goodses, List<GoodsInfoVO> goodsInfos){
        List<GoodsExportVo> goodsExportVoList = new ArrayList<>();

        //SKU按照goodsId分组
        Map<String, List<GoodsInfoVO>> goodsMap = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfoVO::getGoodsId));

        //店铺分类
        StoreCateListByStoreIdRequest storeCateRequest = new StoreCateListByStoreIdRequest();
        storeCateRequest.setStoreId(commonUtil.getStoreId());
        List<StoreCateResponseVO> storeCateList = storeCateQueryProvider.listByStoreId(storeCateRequest).getContext().getStoreCateResponseVOList();

        //签约分类
        ContractCateListCateByStoreIdRequest contractCateRequest = new ContractCateListCateByStoreIdRequest();
        contractCateRequest.setStoreId(commonUtil.getStoreId());
        List<GoodsCateVO> contractCateListCate = contractCateQueryProvider.listCateByStoreId(contractCateRequest).getContext().getGoodsCateList();
        //品牌
        ContractBrandListRequest brandListRequest = new ContractBrandListRequest();
        brandListRequest.setStoreId(commonUtil.getStoreId());
        List<ContractBrandVO> brands = contractBrandQueryProvider.list(brandListRequest).getContext().getContractBrandVOList();

        //规格
        List<String> goodsId = goodses.stream().map(GoodsVO::getGoodsId).collect(Collectors.toList());
        GoodsSpecListByGoodsIdsRequest specRequest = GoodsSpecListByGoodsIdsRequest.builder().goodsIds(goodsId).build();
        Map<String, List<GoodsInfoSpecExportVO>> goodsInfoSpecMap = goodsSpecQueryProvider.listByGoodsInfoIdsForExport(specRequest).getContext().getGoodsInfoSpecMap();

        //运费模版
        List<Long> freightTempIds = goodses.stream().map(GoodsVO::getFreightTempId).collect(Collectors.toList());
        FreightTemplateGoodsListByIdsRequest freightTemplateRequest = FreightTemplateGoodsListByIdsRequest.builder().freightTempIds(freightTempIds).build();
        Map<Long, FreightTemplateGoodsVO> templateGoodsVOMap = freightTemplateGoodsQueryProvider
                .listByIds(freightTemplateRequest).getContext().getFreightTemplateGoodsVOList()
                .stream().collect(Collectors.toMap(FreightTemplateGoodsVO::getFreightTempId, Function.identity()));

        if(CollectionUtils.isNotEmpty(goodses)) {
            //遍历SPU
            goodses.forEach(goodsVO -> {
                //获取该SPU的SKU列表
                List<GoodsInfoVO> goodsInfoVOS = goodsMap.get(goodsVO.getGoodsId());

                //运费模版
                FreightTemplateGoodsVO freightTemplateGoodsVO = templateGoodsVOMap.get(goodsVO.getFreightTempId());
                //获取商品品牌
                Optional<ContractBrandVO> brandVOOptional = brands.stream().filter(brand -> brand.getGoodsBrand().getBrandId().equals(goodsVO.getBrandId())).findFirst();

                if(CollectionUtils.isNotEmpty(goodsInfoVOS)) {
                    goodsInfoVOS.forEach(goodsInfoVO -> {

                        //组装导出数据
                        GoodsExportVo goodsExportVo = KsBeanUtil.convert(goodsVO, GoodsExportVo.class);
                        KsBeanUtil.copyProperties(goodsInfoVO, goodsExportVo);
                        String contractCateName = this.getContractCate(contractCateListCate, goodsVO);
                        String storeCateName = this.getStoreCate(storeCateList, goodsVO);
                        goodsExportVo.setCateName(contractCateName);
                        goodsExportVo.setStoreCateName(storeCateName);
                        goodsExportVo.setGoodsSpecVOList(goodsInfoSpecMap.get(goodsInfoVO.getGoodsInfoId()));
                        if(Objects.nonNull(freightTemplateGoodsVO)) {
                            goodsExportVo.setFreightTempName(freightTemplateGoodsVO.getFreightTempName());
                        }

                        log.info("=========id:{}===商品类型：{}=====是否组合：{}=====GoodsType.REAL_GOODS.toValue()====",goodsInfoVO.getGoodsInfoId(),goodsInfoVO.getGoodsType(),goodsInfoVO.getCombinedCommodity(),GoodsType.REAL_GOODS.toValue());

                        //处理是否是组合商品
                        if (Objects.equals(GoodsType.REAL_GOODS.toValue(),goodsInfoVO.getGoodsType()) && goodsInfoVO.getCombinedCommodity()){
                            goodsExportVo.setCombinedCommodity("是");
                        }

                        if (Objects.equals(GoodsType.REAL_GOODS.toValue(),goodsInfoVO.getGoodsType()) && !goodsInfoVO.getCombinedCommodity()) {
                            goodsExportVo.setCombinedCommodity("否");
                        }

                        if (StringUtils.isNotBlank(goodsInfoVO.getErpGoodsInfoNo())){
                            goodsExportVo.setErpGoodsInfoNo(goodsInfoVO.getErpGoodsInfoNo());
                        }

                        if (StringUtils.isNotBlank(goodsInfoVO.getErpGoodsNo())){
                            goodsExportVo.setErpGoodsNo(goodsInfoVO.getErpGoodsNo());
                        }



                        brandVOOptional.ifPresent(contractBrandVO -> goodsExportVo.setBrandName(contractBrandVO.getGoodsBrand().getBrandName()));
                        goodsExportVoList.add(goodsExportVo);
                    });
                }
            });
        }
        return goodsExportVoList;
    }

    /**
     * 签约分类
     * @param goodsVO
     * @return
     */
    public String getContractCate(List<GoodsCateVO> goodsCateVOList , GoodsVO goodsVO){
        if(CollectionUtils.isEmpty(goodsCateVOList)) {
            throw new SbcRuntimeException("K-000001");
        }
        String cateName = goodsVO.getCateId().toString();
        //获取当前商品的分类
        Optional<GoodsCateVO> optional = goodsCateVOList.stream().filter(goodsCateVO -> goodsCateVO.getCateId().equals(goodsVO.getCateId())).findFirst();
        if(optional.isPresent()) {
            GoodsCateVO goodsCateVO = optional.get();
            //上级分类数组
            List<Long> cateIds = Arrays.stream(goodsCateVO.getCatePath().split("\\|")).map(Long::parseLong).collect(Collectors.toList());
            cateIds.add(goodsVO.getCateId());
            //筛选出上级分类和当前分类，按分类层级排序，拼接分类名称
            String name = goodsCateVOList.stream().filter(vo -> cateIds.contains(vo.getCateId()))
                    .sorted(Comparator.comparingInt(GoodsCateVO::getCateGrade))
                    .map(GoodsCateVO::getCateName).collect(Collectors.joining("_"));
            cateName = cateName.concat("_").concat(name);
        }
        return cateName;
    }

    /**
     * 店铺分类
     * @param goodsVO
     * @return
     */
    public String getStoreCate( List<StoreCateResponseVO> storeCateList, GoodsVO goodsVO) {
        List<Long> storeCateIds = goodsVO.getStoreCateIds();
        if(CollectionUtils.isEmpty(storeCateIds)) {
            return null;
        }

        String cateName = storeCateIds.stream().map(id -> this.getCateName(storeCateList, id)).collect(Collectors.joining(","));
        return cateName;
    }

    /**
     * 拼接分类
     * @param goodsCateVOList
     * @param cateId
     * @return
     */
    public String getCateName(List<StoreCateResponseVO> goodsCateVOList, Long cateId) {
        if(CollectionUtils.isEmpty(goodsCateVOList)) {
            throw new SbcRuntimeException("K-000001");
        }
        String cateName = cateId.toString();
        //获取当前商品的分类
        Optional<StoreCateResponseVO> optional = goodsCateVOList.stream().filter(goodsCateVO -> goodsCateVO.getStoreCateId().equals(cateId)).findFirst();
        if(optional.isPresent()) {
            StoreCateResponseVO goodsCateVO = optional.get();
            //上级分类数组
            List<Long> cateIds = Arrays.stream(goodsCateVO.getCatePath().split("\\|")).map(Long::parseLong).collect(Collectors.toList());
            cateIds.add(cateId);
            //筛选出上级分类和当前分类，按分类层级排序，拼接分类名称
            String name = goodsCateVOList.stream().filter(vo -> cateIds.contains(vo.getStoreCateId()))
                    .sorted(Comparator.comparingInt(StoreCateResponseVO::getCateGrade))
                    .map(StoreCateResponseVO::getCateName).collect(Collectors.joining("_"));
            cateName = cateName.concat("_").concat(name);
        }
        return cateName;
    }

}
