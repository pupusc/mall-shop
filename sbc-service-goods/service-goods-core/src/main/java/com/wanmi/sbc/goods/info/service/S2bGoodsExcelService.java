package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.brand.model.root.ContractBrand;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.ContractBrandRepository;
import com.wanmi.sbc.goods.brand.request.ContractBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.ContractCateRepository;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.repository.StoreCateRepository;
import com.wanmi.sbc.goods.storecate.request.StoreCateQueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Service
public class S2bGoodsExcelService {

    @Value("classpath:supplier_iep_goods_template.xls")
    private Resource templateFileIEP;

    @Value("classpath:supplier_goods_template.xls")
    private Resource templateFile;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private ContractCateRepository contractCateRepository;

    @Autowired
    private StoreCateRepository storeCateRepository;

    @Autowired
    private ContractBrandRepository contractBrandRepository;

    /**
     * 导出模板
     * 加载xls已有模板，填充商品分类、品牌数据，实现excel下拉列表
     *
     * @return base64位文件字符串
     */
    public String exportTemplate(Long storeId,int type) {
        Resource file = type == NumberUtils.INTEGER_ZERO ? templateFile : templateFileIEP;
        if (file == null || !file.exists())   {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }

        //根据店铺获取品牌
        ContractBrandQueryRequest contractBrandQueryRequest = new ContractBrandQueryRequest();
        contractBrandQueryRequest.setStoreId(storeId);
        List<GoodsBrand> brands = contractBrandRepository.findAll(contractBrandQueryRequest.getWhereCriteria()).stream()
                .filter(contractBrand -> contractBrand.getGoodsBrand() != null && StringUtils.isNotBlank(contractBrand.getGoodsBrand().getBrandName()))
                .map(ContractBrand::getGoodsBrand).collect(Collectors.toList());

        //根据店获取平台类目
        ContractCateQueryRequest cateQueryRequest = new ContractCateQueryRequest();
        cateQueryRequest.setStoreId(storeId);
        List<Long> cateIds = contractCateRepository.findAll(cateQueryRequest.getWhereCriteria()).stream().map(ContractCate::getGoodsCate).map(GoodsCate::getCateId).collect(Collectors.toList());
        List<GoodsCate> cates = goodsCateRepository.queryLeaf(cateIds);

        //根据店铺获取店铺分类
        StoreCateQueryRequest queryRequest = new StoreCateQueryRequest();
        queryRequest.setStoreId(storeId);
        queryRequest.setDelFlag(DeleteFlag.NO);
        List<StoreCate> storeCates = storeCateRepository.findAll(queryRequest.getWhereCriteria());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream is = file.getInputStream();
             Workbook wk = WorkbookFactory.create(is)) {
            Sheet cateSheet = wk.getSheetAt(1);
            //填放分类数据
            int cateSize = cates.size();
            for (int i = 0; i < cateSize; i++) {
                GoodsCate cate = cates.get(i);
                // 查询商品分类所有父分类名称
                String allCateName = queryParentCate(cate.getCateId());
                cateSheet.createRow(i).createCell(0).setCellValue(String.valueOf(cate.getCateId()).concat("_").concat(allCateName));
            }

            Sheet brandSheet = wk.getSheetAt(2);
            int brandSize = brands.size();
            for (int i = 0; i < brandSize; i++) {
                GoodsBrand brand = brands.get(i);
                brandSheet.createRow(i).createCell(0).setCellValue(String.valueOf(brand.getBrandId()).concat("_").concat(brand.getBrandName()));
            }

            //填放分类数据
            Sheet storeCateSheet = wk.getSheetAt(3);
            int storeCateSize = storeCates.size();
            for (int i = 0; i < storeCateSize; i++) {
                StoreCate cate = storeCates.get(i);
                // 查询店铺分类所有父分类名称
                String storeCateName = queryStoreCate(cate.getStoreCateId());
                storeCateSheet.createRow(i).createCell(0).setCellValue(String.valueOf(cate.getStoreCateId()).concat("_").concat(storeCateName));
            }
            wk.write(baos);
            return new BASE64Encoder().encode(baos.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }

    /**
     * 递归查询商品分类所有父分类名称
     *
     * @param cateId
     * @return
     */
    public String queryParentCate(Long cateId) {
        GoodsCate goodsCate = goodsCateRepository.findById(cateId).orElse(new GoodsCate());
        if (goodsCate.getCateParentId() != 0) {
            String queryParentCate = queryParentCate(goodsCate.getCateParentId());
            return queryParentCate + "-" + goodsCate.getCateName();
        }
        return goodsCate.getCateName();
    }

    /**
     * 递归查询所有店铺分类所有父分类名称
     *
     * @param storeCateId
     * @return
     */
    private String queryStoreCate(Long storeCateId){
        StoreCate storeCate = storeCateRepository.findById(storeCateId).orElse(new StoreCate());
        if (storeCate.getCateParentId() != 0) {
            String queryParentCate = queryStoreCate(storeCate.getCateParentId());
            return queryParentCate + "-" + storeCate.getCateName();
        }
        return storeCate.getCateName();
    }
}