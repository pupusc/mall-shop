package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.GoodsEvaluateAnalyseBo;
import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.bo.SearchTermBo;
import com.wanmi.sbc.bookmeta.provider.SearchTermProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/16/13:43
 * @Description:
 */
@RestController
@RequestMapping("searchTerm")
public class SearchTermController {
    @Resource
    private SearchTermProvider searchTermProvider;

    @Value("classpath:/download/goods_evaluate_analyse.xlsx")
    private org.springframework.core.io.Resource templateLabelFile;

    @PostMapping("getSearchTermTree")
    public BusinessResponse<List<SearchTermBo>> getTree(@RequestBody SearchTermBo bo) {
        List<SearchTermBo> searchTermTree = searchTermProvider.getSearchTermTree(bo);
        return BusinessResponse.success(searchTermTree);
    }

    @PostMapping("delete")
    public BusinessResponse<Integer> delete(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.deleteSearchTerm(bo);
        return BusinessResponse.success(i);
    }

    @PostMapping("update")
    public BusinessResponse<Integer> update(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.updateSearchTerm(bo);
        return BusinessResponse.success(i);
    }

    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody SearchTermBo bo) {
        int i = searchTermProvider.addSearchTerm(bo);
        return BusinessResponse.success(i);
    }

    @PostMapping("import")
    public BusinessResponse<String> importGoodsEvaluateAnalyse(MultipartFile multipartFile) {

        String res = null;
        Workbook wb = null;
        try {
            try {
                wb = new HSSFWorkbook(new POIFSFileSystem(multipartFile.getInputStream()));
            } catch (Exception e) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());        //XSSF不能读取Excel2003以前（包括2003）的版本
            }

            Sheet sheet = wb.getSheetAt(0);

            if (sheet == null) {
                return null;
            }

            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了第一行的所有行
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getLastCellNum();
                String[] cells = new String[row.getLastCellNum()];

                //循环当前行
                for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell != null) {
                        cell.setCellType(CellType.STRING);
                        cells[cellNum] = cell.getStringCellValue();
                    } else {
                        cells[cellNum] = "";
                    }
                }
                if (cells.length < 5) {
                    continue;
                }
                List<GoodsEvaluateAnalyseBo> list = new ArrayList<>();
                for (int i = 5; i < cells.length; i++) {
                    if (StringUtils.isBlank(cells[i])) {
                        continue;
                    }
                    GoodsEvaluateAnalyseBo goodsEvaluateAnalyseBo = new GoodsEvaluateAnalyseBo();
                    goodsEvaluateAnalyseBo.setEvaluateId(cells[0]);
                    goodsEvaluateAnalyseBo.setSpuId(cells[1]);
                    goodsEvaluateAnalyseBo.setSkuId(cells[2]);
                    goodsEvaluateAnalyseBo.setSkuName(cells[3]);
                    goodsEvaluateAnalyseBo.setEvaluateContent(cells[4]);
                    goodsEvaluateAnalyseBo.setEvaluateContentKey(cells[i]);
                    list.add(goodsEvaluateAnalyseBo);
                }
                res = searchTermProvider.importGoodsEvaluateAnalyse(list).getContext();
            }
        } catch (Exception e) {
            return BusinessResponse.error("文件不符合要求");
        } finally {
            try {
                wb.close();
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (null != res) {
            if (res.contains("failed")) {
                return BusinessResponse.error(res);
            }
        }
        return BusinessResponse.success(res);
    }


}
