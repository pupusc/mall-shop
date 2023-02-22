package com.wanmi.sbc.setting.topicconfig.service;

import com.alibaba.druid.util.StringUtils;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentAddRequest;
import com.wanmi.sbc.setting.topicconfig.model.root.TopicStoreyColumnContent;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/2/21 15:06
 */
@Service
public class ExcelService {

    @Autowired
    private TopicConfigService topicConfigService;

    @Transactional
    public String importExcel(MultipartFile file, Integer topicStoreyColumnId, Integer bookType) {
        XSSFWorkbook wb = null;
        try {
            InputStream inputStream = file.getInputStream();
            wb = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = wb.getSheetAt(0);
            int rowNum = sheet.getLastRowNum(); // 行
            int columnNum = 0; // 列
            if(rowNum!=0&&sheet.getRow(0)!=null) {
                columnNum = sheet.getRow(0).getPhysicalNumberOfCells();
            }
            if (bookType == 1) {
                for (int i = 2; i <=rowNum; i++) {
                    XSSFRow row = sheet.getRow(i);
                    if(row == null) {
                        continue;
                    }
                    int j = 0;
                    //导入的spu编号
                    String spu = row.getCell(j)==null?"":row.getCell(j).getStringCellValue();
                    //判断spu是否为空 不为空就继续往下走
                    if(StringUtils.isEmpty(spu)) {
                        continue;
                    }
                    if(StringUtils.isEmpty(spu)) {
                        //抛出异常告诉用户 SPU必须填写
                        String NOTSPU = "NO SPU";
                        return "spu编号为必填项!" + NOTSPU;
                    }
                    ++j;
                    //导入的商品名称
                    String goodsName = row.getCell(j)==null?"":row.getCell(j).getStringCellValue();
                    ++j;
                    //导入的市场价
                    BigDecimal marketPrice = new BigDecimal(row.getCell(j)==null?"":row.getCell(j).getStringCellValue());
                    ++j;
                    //导入的在线状态
                    Integer status = Integer.valueOf(row.getCell(j)==null?"":row.getCell(j).getStringCellValue());
                    ++j;
                    //导入的排序
                    Integer sorting = Integer.valueOf(row.getCell(j)==null?"0":row.getCell(j).getStringCellValue());
                    //构建了一个StudentConsultation对象 用来存储这个学生的信息
                    ColumnContentAddRequest columnContentAddRequest = new ColumnContentAddRequest();
                    columnContentAddRequest.setSpuId(spu);
                    columnContentAddRequest.setGoodsName(goodsName);
                    columnContentAddRequest.setMarketPrice(marketPrice);
                    columnContentAddRequest.setSorting(sorting);
                    columnContentAddRequest.setTopicStoreySearchId(topicStoreyColumnId);
                    columnContentAddRequest.setType(1);
                    columnContentAddRequest.setCreateTime(LocalDateTime.now());
                    columnContentAddRequest.setGoodsStatus(status);
                    topicConfigService.addTopicStoreyColumnContent(columnContentAddRequest);
                }
            } else {
                for (int i = 2; i <=rowNum; i++) {
                    XSSFRow row = sheet.getRow(i);
                    if(row == null) {
                        continue;
                    }
                    int j = 0;
                    //导入的图片id
                    Long id = Long.valueOf(row.getCell(j)==null?"":row.getCell(j).getStringCellValue());
                    ++j;
                    //导入的spu编号
                    String spu = row.getCell(j)==null?"":row.getCell(j).getStringCellValue();
                    //判断spu是否为空 不为空就继续往下走
                    if(StringUtils.isEmpty(spu)) {
                        continue;
                    }
                    if(StringUtils.isEmpty(spu)) {
                        //抛出异常告诉用户 SPU必须填写
                        String NOTSPU = "NO SPU";
                        return "spu编号为必填项!" + NOTSPU;
                    }
                    ++j;
                    //导入的商品名称
                    String goodsName = row.getCell(j)==null?"":row.getCell(j).getStringCellValue();
                    ++j;

                    //导入的在线状态
                    String url = row.getCell(j)==null?"":row.getCell(j).getStringCellValue();
                    ++j;
                    //导入的排序
                    Integer sorting = Integer.valueOf(row.getCell(j)==null?"":row.getCell(j).getStringCellValue());
                    //判断排序是否为空 不为空就继续往下走
                    if(sorting == null) {
                        continue;
                    }
                    if(sorting == null) {
                        //抛出异常告诉用户 SPU必须填写
                        String NOTSPU = "NO SPU";
                        return "spu编号为必填项!" + NOTSPU;
                    }
                    //构建了一个StudentConsultation对象 用来存储这个学生的信息
                    ColumnContentAddRequest columnContentAddRequest = new ColumnContentAddRequest();
                    columnContentAddRequest.setSpuId(spu);
                    columnContentAddRequest.setLinkUrl(url);
                    columnContentAddRequest.setGoodsName(goodsName);
                    columnContentAddRequest.setSorting(sorting);
                    columnContentAddRequest.setImageId(id);
                    columnContentAddRequest.setTopicStoreySearchId(topicStoreyColumnId);
                    columnContentAddRequest.setType(1);
                    columnContentAddRequest.setCreateTime(LocalDateTime.now());
                    topicConfigService.addTopicStoreyColumnContent(columnContentAddRequest);
                }
            }
            wb.close();
            inputStream.close();
            return "导入成功！";
        }
        catch (IllegalArgumentException e){
            return "请输入正确的跟进方式！" + e.getMessage();
        }catch (IllegalStateException e){
            e.printStackTrace();
            return "只能输入汉字！" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "导入失败，请检查数据是否符合导入要求！" + e.getMessage();
        }
    }

}
