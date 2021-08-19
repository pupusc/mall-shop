package com.wanmi.sbc.message.utils.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by mac on 2017/5/6.
 */
@Slf4j
public class ExcelHelper<T> {

    /**
     *
     */
    private final HSSFWorkbook work;

    public ExcelHelper() {
        this.work = new HSSFWorkbook();
    }

    /**
     *
     * @param sheetName
     * @param dataList
     */
    public void addSheet(String sheetName, Column[] columns, List<T> dataList) {
        int rowIndex = 0;
        HSSFSheet sheet = work.createSheet(sheetName);
        HSSFRow headRow = sheet.createRow(rowIndex++);

        //头
        int cellIndex = 0;
        for (Column c : columns){
            HSSFCell cell = headRow.createCell(cellIndex++);
            cell.setCellValue(c.getHeader());
        }

        //体
        for (T data : dataList) {
            cellIndex = 0;
            HSSFRow row = sheet.createRow(rowIndex++);
            for (Column column : columns){
                HSSFCell cell = row.createCell(cellIndex++);
                column.getRender().render(cell, data);
            }
        }
    }

    /**
     *
     * @param op
     */
    public void write(OutputStream op) throws IOException {
        this.work.write(op);
    }
}
