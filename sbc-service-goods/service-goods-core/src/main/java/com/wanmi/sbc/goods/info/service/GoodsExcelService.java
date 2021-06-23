package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
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

/**
 * 商品EXCEL处理服务
 * Created by dyt on 2017/8/17.
 */
@Service
public class GoodsExcelService {

    @Value("classpath:goods_template.xlsx")
    private Resource templateFile;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    /**
     * 导出模板
     * 加载xls已有模板，填充商品分类、品牌数据，实现excel下拉列表
     * @return base64位文件字符串
     */
    public String exportTemplate() {
        if (templateFile == null || !templateFile.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        List<GoodsCate> cates = goodsCateRepository.queryLeaf();
        List<GoodsBrand> brands = goodsBrandRepository.findAll(GoodsBrandQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream is = templateFile.getInputStream();
             Workbook wk = WorkbookFactory.create(is)) {
            Sheet sheet = wk.getSheetAt(1);
            //填放分类数据
            int cateSize = cates.size();
            for (int i = 0; i < cateSize; i++) {
                GoodsCate cate = cates.get(i);
                sheet.createRow(i).createCell(0).setCellValue(String.valueOf(cate.getCateId()).concat("_").concat(cate.getCateName()));
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