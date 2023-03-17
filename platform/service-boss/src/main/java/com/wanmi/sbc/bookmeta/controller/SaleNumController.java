package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaBookLabelBO;
import com.wanmi.sbc.bookmeta.bo.SaleNumBO;
import com.wanmi.sbc.bookmeta.provider.SaleNumProvider;
import com.wanmi.sbc.bookmeta.vo.SalesNumReqVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile;
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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
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
 * @Date: 2023/03/10/12:48
 * @Description:
 */
@RestController
@RequestMapping("/SaleNum")
public class SaleNumController {
    @Resource
    SaleNumProvider saleNumProvider;

    @Value("classpath:/download/sale_num.xlsx")
    private org.springframework.core.io.Resource templateLabelFile;

    @PostMapping("/getAllSaleNum")
    public void templateSaleNum() {
        InputStream is = null;
        org.springframework.core.io.Resource file = templateLabelFile;
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);
            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<Map> bookMap = saleNumProvider.queryAllSaleNum();
            AtomicInteger rowCount = new AtomicInteger(1);
            for (Map map : bookMap) {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                if (map.containsKey("sku_id")) {
                    row.createCell(0).setCellValue(map.get("sku_id").toString());
                }
                if (map.containsKey("sku_name")) {
                    row.createCell(1).setCellValue(map.get("sku_name").toString());
                }
                if (map.containsKey("sales_num")) {
                    row.createCell(2).setCellValue(map.get("sales_num").toString());
                }
                if (map.containsKey("fix_price")) {
                    row.createCell(3).setCellValue(map.get("fix_price").toString());
                }
            }
            wk.write(outputStream);
            String fileName = URLEncoder.encode("sale_num.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/uploadSaleNum")
    public BusinessResponse<String> SaleNumUpload(MultipartFile multipartFile) {
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
                    cell.setCellType(CellType.STRING);
                    cells[cellNum] = cell.getStringCellValue();
                }
                SaleNumBO saleNumBO = new SaleNumBO();
                saleNumBO.setSkuId((cells[0]));
                saleNumBO.setSkuName((cells[1]));
                saleNumBO.setSalesNum(Integer.parseInt(cells[2]));
                if (lastCellNum>3 && StringUtils.isNotBlank(cells[3])){
                    saleNumBO.setFixPrice(Double.parseDouble(cells[3]));
                }else {
                    saleNumBO.setFixPrice(0);
                }
                res = saleNumProvider.importSaleNum(saleNumBO).getContext();
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
            if (res.contains("failed")){
            return BusinessResponse.error(res);
            }
        }
        return BusinessResponse.success(res);
    }

    @PostMapping("/updateSaleNum")
    public BusinessResponse<Integer> Update(@RequestBody SaleNumBO saleNumBO) {
        return saleNumProvider.updateSaleNum(saleNumBO);
    }

    @PostMapping("/getSaleNum")
    public BusinessResponse<List<SaleNumBO>> get(@RequestBody SalesNumReqVO saleNumVO) {
        SaleNumBO convert = KsBeanUtil.convert(saleNumVO, SaleNumBO.class);
        return saleNumProvider.getSaleNum(convert);
    }
}
