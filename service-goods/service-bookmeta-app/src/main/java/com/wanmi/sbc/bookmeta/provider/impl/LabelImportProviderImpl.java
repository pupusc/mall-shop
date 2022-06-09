package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.enums.LabelTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Liang Jun
 * @date 2022-06-08 17:06:00
 */
@Slf4j
@RestController
public class LabelImportProviderImpl {

    @Autowired
    private MetaLabelMapper metaLabelMapper;
    private Map<String, MetaLabel> cate1Map = new HashMap<>();
    private Map<String, MetaLabel> cate2Map = new HashMap<>();
    private Map<String, MetaLabel> labelMap = new HashMap<>();

    @Transactional
    @PostMapping("/goods/${application.goods.version}/metaLabel/import")
    public BusinessResponse<Boolean> importBook(String fileName) throws Exception {
        log.info("========================图书基础库，导入标签数据开始：========================");
        long timeBgn = System.currentTimeMillis();
        try {
            Workbook workbook = WorkbookFactory.create(new File(fileName));
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return BusinessResponse.error(CommonErrorCode.FAILED, "表格sheet不存在");
            }
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum < 1) {
                return BusinessResponse.error(CommonErrorCode.FAILED, "表格内容为空");
            }
            initCache();
            importLabel(sheet, firstRowNum, lastRowNum);
        } finally {
            destroy();
        }
        long timeEnd = System.currentTimeMillis();
        log.info("========================图书基础库，导入标签数据完成！耗时：{}ms========================", timeEnd-timeBgn);
        return BusinessResponse.success(true);
    }

    private void importLabel(Sheet sheet, int firstRowNum, int lastRowNum) {
        int maxCell = ExcelCellInfo.max_size_col;
        //循环除了第一行的所有行
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            log.info("导入单条数据开始：row={}", rowNum);
            //获得当前行
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                log.info("空行忽略不处理...");
                continue;
            }
            boolean isEmpty = true;
            for (int i = 0; i < maxCell; i++) {
                Cell cell = row.getCell(i) == null ? row.createCell(i) : row.getCell(i);
                if (StringUtils.isNotBlank(ExcelHelper.getValue(cell))) {
                    isEmpty = false;
                }
            }
            //数据都为空，则跳过去
            if (isEmpty) {
                log.info("空行忽略不处理...");
                continue;
            }

            String cate1Name = ExcelHelper.getValue(row.getCell(ExcelCellInfo.index_cate1_name));
            String cate2Name = ExcelHelper.getValue(row.getCell(ExcelCellInfo.index_cate2_name));
            String labelName = ExcelHelper.getValue(row.getCell(ExcelCellInfo.index_label_name));
            String labelDesc = ExcelHelper.getValue(row.getCell(ExcelCellInfo.index_label_desc));
            //必填项验证
            if (StringUtils.isBlank(cate1Name)) {
                log.error("导入数据失败：一级分类不能为空, row={}", rowNum);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "一级分类不能为空");
            }
            if (StringUtils.isBlank(cate2Name)) {
                log.error("导入数据失败：二级分类不能为空, row={}", rowNum);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "二级分类不能为空");
            }
            if (StringUtils.isBlank(labelName)) {
                log.error("导入数据失败：标签不能为空, row={}", rowNum);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "标签不能为空");
            }
            if (labelMap.get(labelName) != null) {
                log.error("导入数据失败：标签名称重复存在:{}, row={}", labelName, rowNum);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "标签名称重复存在");
            }
            createLabel(cate1Name, cate2Name, labelName, labelDesc);
            log.info("导入单条数据结束：row={}", rowNum);
        }
    }

    private void createLabel(String cate1Name, String cate2Name, String labelName, String labelDesc) {
        if (cate2Map.get(cate2Name) == null) {
            if (cate1Map.get(cate1Name) == null) {
                MetaLabel cate1 = new MetaLabel();
                cate1.setParentId(0);
                cate1.setType(LabelTypeEnum.CATEGORY.getCode());
                cate1.setScene(0);
                cate1.setSeq(0);
                cate1.setStatus(1);
                cate1.setPath("");
                cate1.setName(cate1Name);
                cate1.setDescr("一级分类");
                this.metaLabelMapper.insertSelective(cate1);
                cate1Map.put(cate1Name, cate1);
            }
            MetaLabel cate2 = new MetaLabel();
            cate2.setParentId(cate1Map.get(cate1Name).getId());
            cate2.setType(LabelTypeEnum.CATEGORY.getCode());
            cate2.setScene(0);
            cate2.setSeq(0);
            cate2.setStatus(1);
            cate2.setPath(cate1Map.get(cate1Name).getId().toString());
            cate2.setName(cate2Name);
            cate2.setDescr("二级分类");
            this.metaLabelMapper.insertSelective(cate2);
            cate2Map.put(cate2Name, cate2);
        }
        MetaLabel cate2 = cate2Map.get(cate2Name);
        MetaLabel label = new MetaLabel();
        label.setParentId(cate2.getId());
        label.setType(LabelTypeEnum.LABEL.getCode());
        label.setScene(0);
        label.setSeq(0);
        label.setStatus(1);
        label.setPath(cate2.getPath() + "_" + cate2.getId());
        label.setName(labelName);
        label.setDescr(labelDesc);
        this.metaLabelMapper.insertSelective(label);
        cate2Map.put(labelName, label);
    }

    private void initCache() {
        MetaLabel metaLabel = new MetaLabel();
        metaLabel.setDelFlag(0);
        List<MetaLabel> labels = this.metaLabelMapper.select(metaLabel);
        for (MetaLabel label : labels) {
            if (LabelTypeEnum.LABEL.getCode().equals(label.getType())) {
                labelMap.put(label.getName(), label);
                continue;
            }
            if (LabelTypeEnum.CATEGORY.getCode().equals(label.getType())) {
                if (label.getParentId() == null || label.getParentId() == 0) {
                    cate1Map.put(label.getName(), label);
                } else {
                    cate2Map.put(label.getName(), label);
                }
            }
        }
    }

    private void destroy() {
        cate1Map.clear();
        cate2Map.clear();
        labelMap.clear();
    }

    private static class ExcelCellInfo {
        public static int max_size_col = 4;
        public static int index_cate1_name = 0;
        public static int index_cate2_name = 1;
        public static int index_label_name = 2;
        public static int index_label_desc = 3;
    }
}
