package com.wanmi.sbc.setting.service;

import com.wanmi.sbc.goods.api.provider.image.ImageProvider;
import com.wanmi.sbc.goods.api.request.image.ImagePageProviderRequest;
import com.wanmi.sbc.goods.api.response.image.ImageProviderResponse;
import com.wanmi.sbc.setting.api.provider.topic.TopicConfigProvider;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentAddRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.ColumnContentQueryRequest;
import com.wanmi.sbc.setting.api.request.topicconfig.MixedComponentGoodsAddRequest;
import com.wanmi.sbc.setting.bean.dto.ColumnContentDTO;
import com.wanmi.sbc.setting.bean.enums.BookType;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: todo-zh
 * @Author zh
 * @Date 2023/2/21 15:06
 */
@Service
public class ExcelService {

    @Autowired
    private ImageProvider imageProvider;

    @Autowired
    private TopicConfigProvider topicConfigProvider;

    @Transactional
    public String importExcel(MultipartFile file, MixedComponentGoodsAddRequest request) {
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
            //Integer topicId = (Integer) topicConfigProvider.addTopicStoreyColumn(request.getColumnAddRequest()).getContext();
            List<ColumnContentAddRequest> columnContentAddRequestList = new ArrayList<>();
            if (request.getBookType() == 1) {
                for (int i = 1; i <=rowNum; i++) {
                    XSSFRow row = sheet.getRow(i);
                    if(row == null) {
                        continue;
                    }
                    int j = 0;
                    //导入的spu编号
                    String spu = row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j));
                    //判断spu是否为空 不为空就继续往下走
                    if(StringUtils.isEmpty(spu)) {
                        continue;
                    }
                    if(StringUtils.isEmpty(spu)) {
                        //抛出异常告诉用户 SPU必须填写
                        String NOTSPU = "NO SPU";
                        return "spu编号为必填项!" + NOTSPU;
                    }
                    ColumnContentQueryRequest columnContentQueryRequest = new ColumnContentQueryRequest();
                    columnContentQueryRequest.setSpuId(spu);
                    List<ColumnContentDTO> context = topicConfigProvider.ListTopicStoreyColumnContent(columnContentQueryRequest).getContext();
                    ++j;
                    //导入的商品名称
                    String goodsName = row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j));
                    ++j;
                    //导入的市场价
                    BigDecimal marketPrice = new BigDecimal(row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j)));
                    ++j;
                    //导入的在线状态
                    Integer status = Integer.valueOf(row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j)));
                    ++j;
                    //导入的排序
                    Integer sorting = Integer.valueOf(row.getCell(j)==null ? "1" :new DataFormatter().formatCellValue(row.getCell(j)));
                    //构建了一个StudentConsultation对象 用来存储这个学生的信息
                    ColumnContentAddRequest columnContentAddRequest = new ColumnContentAddRequest();
                    columnContentAddRequest.setSpuId(spu);
                    columnContentAddRequest.setTopicStoreyId(194);
                    columnContentAddRequest.setGoodsName(goodsName);
                    columnContentAddRequest.setMarketPrice(marketPrice);
                    columnContentAddRequest.setSorting(sorting);
                    columnContentAddRequest.setType(BookType.BOOK.toValue());
                    columnContentAddRequest.setCreateTime(LocalDateTime.now());
                    columnContentAddRequest.setGoodsStatus(status);
                    columnContentAddRequestList.add(columnContentAddRequest);
                    //topicConfigProvider.addTopicStoreyColumnContent(columnContentAddRequest);
                }
            } else {
                for (int i = 1; i <=rowNum; i++) {
                    XSSFRow row = sheet.getRow(i);
                    if(row == null) {
                        continue;
                    }
                    int j = 0;
                    //导入的图片id
                    Long id = Long.valueOf(row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j)));
                    if(id == null) {
                        //抛出异常告诉用户 SPU必须填写
                        return "图片id为必填项!";
                    }
                    ImagePageProviderRequest imagePageProviderRequest = new ImagePageProviderRequest();
                    imagePageProviderRequest.setId(Integer.valueOf(id.toString()));
                    List<ImageProviderResponse> context = imageProvider.listNoPage(imagePageProviderRequest).getContext();
                    String imgUrl = context.size() != 0 ? context.get(0).getImgUrl() : null;
                    ++j;
                    //导入的spu编号
                    String spu = row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j));
                    ++j;
                    //导入的商品名称
                    String goodsName = row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j));
                    ++j;
                    //导入的跳转链接
                    String url = row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j));
                    //判断spu是否为空 不为空就继续往下走
                    if(StringUtils.isEmpty(spu) && StringUtils.isEmpty(url)) {
                        return "spu编号&跳转链接二选一必填!";
                    }
                    ++j;
                    //导入的排序
                    Integer sorting = Integer.valueOf(row.getCell(j)==null?"":new DataFormatter().formatCellValue(row.getCell(j)));
                    //判断排序是否为空 不为空就继续往下走
                    if(sorting == null) {
                        return "排序为必填项!" ;
                    }
                    //构建了一个StudentConsultation对象 用来存储这个学生的信息
                    ColumnContentAddRequest columnContentAddRequest = new ColumnContentAddRequest();
                    columnContentAddRequest.setSpuId(spu);
                    columnContentAddRequest.setLinkUrl(url);
                    columnContentAddRequest.setTopicStoreyId(194);
                    columnContentAddRequest.setGoodsName(goodsName);
                    columnContentAddRequest.setSorting(sorting);
                    columnContentAddRequest.setImageId(id);
                    columnContentAddRequest.setImageUrl(imgUrl);
                    columnContentAddRequest.setType(BookType.ADVERTISEMENT.toValue());
                    columnContentAddRequest.setCreateTime(LocalDateTime.now());
                    columnContentAddRequestList.add(columnContentAddRequest);
                    //topicConfigProvider.addTopicStoreyColumnContent(columnContentAddRequest);
                }
            }
            request.setColumnContent(columnContentAddRequestList);
            topicConfigProvider.addTopicStoreyColumnGoods(request);
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
