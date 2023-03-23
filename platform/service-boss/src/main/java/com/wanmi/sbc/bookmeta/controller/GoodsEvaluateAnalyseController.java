package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.provider.GoodsEvaluateAnalyseProvider;
import com.wanmi.sbc.bookmeta.vo.GoodsEvaluateAnalyseReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/17/15:42
 * @Description:
 */
@RestController
@RequestMapping("goodsEvaluateAnalyse")
public class GoodsEvaluateAnalyseController {
    @Resource
    GoodsEvaluateAnalyseProvider goodsEvaluateAnalyseProvider;
    @Value("classpath:/download/goods_evaluate_analyse.xlsx")
    private org.springframework.core.io.Resource templateFile;

    @PostMapping("queryByPage")
    public BusinessResponse<List<GoodsEvaluateAnalyseBo>> queryByPage(@RequestBody GoodsEvaluateAnalyseReqVO reqVO) {
        GoodsEvaluateAnalyseBo convert = KsBeanUtil.convert(reqVO, GoodsEvaluateAnalyseBo.class);
        BusinessResponse<List<GoodsEvaluateAnalyseBo>> listBusinessResponse = goodsEvaluateAnalyseProvider.queryByPage(convert);
        return listBusinessResponse;
    }

    @PostMapping("export")
    public void export() {
        InputStream is = null;
        org.springframework.core.io.Resource file=templateFile;
        try{

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<GoodsEvaluateAnalyseBo> listBusinessResponse = goodsEvaluateAnalyseProvider.exportGoodsEvaluateAnalyse();
            AtomicInteger rowCount= new AtomicInteger(1);
            listBusinessResponse.stream().forEach(map -> {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.getEvaluateId().toString());
                row.createCell(1).setCellValue(map.getSpuId().toString());
                row.createCell(2).setCellValue(map.getSkuId().toString());
                row.createCell(3).setCellValue(map.getSkuName().toString());
                row.createCell(4).setCellValue(map.getEvaluateContent().toString());
                int cell;
                switch (map.getEvaluateContentKey().toString()){
                    case "物流快": cell = 5 ; break;
                    case "质量好": cell = 6 ; break;
                    case "做工精致": cell = 7 ; break;
                    case "很喜欢": cell = 8 ; break;
                    case "很好": cell = 9 ; break;
                    case "非常不错": cell = 10 ; break;
                    case "非常棒": cell = 11 ; break;
                    case "正版": cell = 12 ; break;
                    case "味道不错": cell = 13 ; break;
                    case "满意": cell = 14 ; break;
                    default: cell = 5; break;
                }
                row.createCell(cell).setCellValue(map.getEvaluateContentKey().toString());
            });
            wk.write(outputStream);
            String fileName = URLEncoder.encode("goods_evaluate_analyse.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers","Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
