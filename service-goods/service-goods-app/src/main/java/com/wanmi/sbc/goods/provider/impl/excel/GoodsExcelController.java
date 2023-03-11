package com.wanmi.sbc.goods.provider.impl.excel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.excel.GoodsExcelProvider;
import com.wanmi.sbc.goods.api.response.excel.GoodsExcelExportTemplateResponse;
import com.wanmi.sbc.goods.info.service.GoodsExcelService;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnGoodsAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnGoodsQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.TopicStoreyColumnGoodsUpdateRequest;
import com.wanmi.sbc.setting.bean.dto.TopicStoreyColumnGoodsDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * com.wanmi.sbc.goods.provider.impl.excel.GoodsExcelController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:19
 */
@RestController
@Validated
public class GoodsExcelController implements GoodsExcelProvider {

    @Autowired
    private GoodsExcelService goodsExcelService;

    @Autowired
    private TopicConfigProvider topicConfigProvider;
    /**
     * 获取商品excel模板
     * @return base64位文件流字符串 {@link GoodsExcelExportTemplateResponse}
     */
    @Override
    public BaseResponse<GoodsExcelExportTemplateResponse> exportTemplate() {
        return BaseResponse.success(GoodsExcelExportTemplateResponse.builder()
                .file(goodsExcelService.exportTemplate()).build());
    }

    @Override
    public BaseResponse loadExcel(MultipartFile multipartFile,Integer topicStoreyId,Integer topicStoreySearchId) {
        int addNum = 0;
        int updateNum = 0;
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
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
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
                    cell.setCellType(CellType.STRING);
                    cells[cellNum] = cell.getStringCellValue();
                }
                TopicStoreyColumnGoodsQueryRequest topicStoreyColumnGoodsQueryRequest = new TopicStoreyColumnGoodsQueryRequest();
                topicStoreyColumnGoodsQueryRequest.setTopicStoreyId(topicStoreyId);
                topicStoreyColumnGoodsQueryRequest.setSpuNo(cells[0]);
                List<TopicStoreyColumnGoodsDTO> content = topicConfigProvider.listStoryColumnGoodsByIdAndSpu(topicStoreyColumnGoodsQueryRequest).getContext();
                //没有就更新
                if (null == content || content.size() == 0) {
                    TopicStoreyColumnGoodsAddRequest topicStoreyColumnGoodsAddRequest = new TopicStoreyColumnGoodsAddRequest();
                    topicStoreyColumnGoodsAddRequest.setSpuNo(cells[0]);
                    topicStoreyColumnGoodsAddRequest.setGoodsName(cells[1]);
                    topicStoreyColumnGoodsAddRequest.setShowLabelTxt(cells[2]);
                    topicStoreyColumnGoodsAddRequest.setNumTxt(cells[3]);
                    topicStoreyColumnGoodsAddRequest.setSorting(Integer.parseInt(cells[4]));
                    topicStoreyColumnGoodsAddRequest.setTopicStoreyId(topicStoreyId);
                    topicStoreyColumnGoodsAddRequest.setTopicStoreySearchId(topicStoreySearchId);
                    try {
                        topicConfigProvider.addStoreyColumnGoods(topicStoreyColumnGoodsAddRequest);
                        addNum++;
                    } catch (Exception e) {
                        BaseResponse baseResponse = new BaseResponse<>();
                        return baseResponse.error( "导入失败");
                    }
                } else { //有就更新
                    TopicStoreyColumnGoodsUpdateRequest topicStoreyColumnGoodsUpdateRequest = new TopicStoreyColumnGoodsUpdateRequest();
                    topicStoreyColumnGoodsUpdateRequest.setId(content.get(0).getId());
                    topicStoreyColumnGoodsUpdateRequest.setGoodsName(cells[1]);
                    topicStoreyColumnGoodsUpdateRequest.setSorting(Integer.parseInt(cells[4]));
                    topicStoreyColumnGoodsUpdateRequest.setShowLabeTxt(cells[2]);
                    topicStoreyColumnGoodsUpdateRequest.setNumTxt(cells[3]);
                    try {
                        topicConfigProvider.updateStoreyColumnGoods(topicStoreyColumnGoodsUpdateRequest);
                        updateNum++;
                    } catch (Exception e) {
                        BaseResponse baseResponse=new BaseResponse<>();
                        return baseResponse.error( "更新失败");
                    }
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BaseResponse baseResponse=new BaseResponse<>();
            return baseResponse.info("操作成功", "成功增加" + addNum + "条," + "成功更新" + updateNum + "条");
        }
    }
}
