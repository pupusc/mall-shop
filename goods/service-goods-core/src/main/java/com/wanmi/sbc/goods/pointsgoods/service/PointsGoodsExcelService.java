package com.wanmi.sbc.goods.pointsgoods.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateQueryRequest;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import com.wanmi.sbc.goods.pointsgoodscate.service.PointsGoodsCateService;
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
 * 积分商品
 *
 * @author yang
 * @since 2019/5/21
 */
@Service("PointsGoodsExcelService")
public class PointsGoodsExcelService {

    @Value("classpath:points_goods_template.xls")
    private Resource templateFile;

    @Autowired
    private PointsGoodsCateService pointsGoodsCateService;

    /**
     * 导出积分商品模板
     *
     * @return
     */
    public String exportTemlate() {
        if (templateFile == null || !templateFile.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        List<PointsGoodsCate> cates = pointsGoodsCateService.list(PointsGoodsCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO)
                .build());
        try (InputStream inputStream = templateFile.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Workbook wk = WorkbookFactory.create(inputStream)) {
            Sheet cateSheet = wk.getSheetAt(1);
            // 填放分类数据
            int cateSize = cates.size();
            for (int i = 0; i < cateSize; i++) {
                PointsGoodsCate cate = cates.get(i);
                cateSheet.createRow(i).createCell(0).setCellValue(String.valueOf(cate.getCateId()).concat("_").concat(cate.getCateName()));
            }
            wk.write(byteArrayOutputStream);
            return new BASE64Encoder().encode(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }
}
