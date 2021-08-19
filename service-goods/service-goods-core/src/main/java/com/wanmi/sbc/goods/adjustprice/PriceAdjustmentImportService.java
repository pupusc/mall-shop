package com.wanmi.sbc.goods.adjustprice;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.constant.CustomerLevelErrorCode;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.storelevel.StoreLevelListRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelBaseVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>商品批量改价导入逻辑</p>
 * Created by of628-wenzhi on 2020-12-11-7:17 下午.
 */
@Service
@Slf4j
public class PriceAdjustmentImportService {

    @Value("classpath:adjust_marketing_price_template.xlsx")
    private Resource marketingPriceTemplateFile;

    @Value("classpath:adjust_supply_price_template.xlsx")
    private Resource supplyPriceTemplateFile;

    @Value("classpath:adjust_level_price_template.xlsx")
    private Resource customerLevelPriceTemplateFile;

    @Value("classpath:adjust_interval_price_template.xlsx")
    private Resource intervalPriceTemplateFile;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private StoreLevelQueryProvider storeLevelQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    private static final String _LEVEL_CELL_TEXT_ = "调整后等级价（%s）";

    private static final String _LEVEL_CELL_COMMENT = "选填，只能填写0和正数，允许两位小数，不超过9999999.99\n";

    /**
     * 批量设价模板导出
     *
     * @return
     */
    public String exportAdjustmentPriceTemplate(PriceAdjustmentType type, Long storeId) {
        Resource resource = null;
        switch (type) {
            case MARKET:
                resource = marketingPriceTemplateFile;
                break;
            case SUPPLY:
                resource = supplyPriceTemplateFile;
                break;
            case LEVEL:
                resource = customerLevelPriceTemplateFile;
                break;
            case STOCK:
                resource = intervalPriceTemplateFile;
                break;
        }
        if (Objects.nonNull(resource) && !resource.exists()) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            assert resource != null;
            try (InputStream is = resource.getInputStream();
                 Workbook wk = WorkbookFactory.create(is)) {
                if (PriceAdjustmentType.LEVEL == type) {
                    writeRowHeadForLevel(wk, storeId);
                }
                wk.write(baos);
                return new BASE64Encoder().encode(baos.toByteArray());
            }
        } catch (SbcRuntimeException e) {
            log.error("上传批量调价模板失败，storeId={},type={},", storeId, type, e);
            throw e;
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }

    }


    private void writeRowHeadForLevel(Workbook wk, Long storeId) {
        //获取客户等级
        StoreVO storeVO = storeQueryProvider.getById(new StoreByIdRequest(storeId)).getContext().getStoreVO();
        List<String> levelNames;
        List<Long> levelIds;
        if (storeVO.getCompanyType() == BoolFlag.NO) {
            //自营取平台等级
            List<CustomerLevelBaseVO> levelList = customerLevelQueryProvider.listAllCustomerLevelNew().getContext().getCustomerLevelVOList();
            if (levelList.isEmpty()) {
                throw new SbcRuntimeException(CustomerLevelErrorCode.NOT_EXIST);
            }
            levelIds = levelList.stream().mapToLong(CustomerLevelBaseVO::getCustomerLevelId).boxed().collect(Collectors.toList());
            levelNames = levelList.stream().map(CustomerLevelBaseVO::getCustomerLevelName).collect(Collectors.toList());
        } else {
            //第三方店铺取自定义等级
            List<StoreLevelVO> levelList = storeLevelQueryProvider.listAllStoreLevelByStoreId(StoreLevelListRequest.builder().storeId(storeId).build())
                    .getContext().getStoreLevelVOList();
            if (levelList.isEmpty()) {
                throw new SbcRuntimeException(CustomerLevelErrorCode.NOT_EXIST);
            }
            levelIds = levelList.stream().mapToLong(StoreLevelVO::getStoreLevelId).boxed().collect(Collectors.toList());
            levelNames = levelList.stream().map(StoreLevelVO::getLevelName).collect(Collectors.toList());
        }
        levelIds.add(0, 0L);
        levelNames.add(0, "全平台客户");

        //填充levelNames;
        Sheet sheet1 = wk.getSheetAt(0);
        Row headRow = sheet1.getRow(0);
        CellStyle textStyle = wk.createCellStyle();
        DataFormat format = wk.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        for (int i = 0; i < levelNames.size(); i++) {
            //从第6个单元格index开始
            int levelCellIndex = i + 6;
            sheet1.setColumnWidth(levelCellIndex, 25 * 256);
//            sheet1.setDefaultColumnStyle(levelCellIndex, textStyle);
            Cell cell = headRow.getCell(levelCellIndex);
            if (cell == null) {
                cell = headRow.createCell(levelCellIndex);
            }
            cell.setCellValue(String.format(_LEVEL_CELL_TEXT_, levelNames.get(i)));
            //当前单元格索引起始值
            int beginRowIndex = cell.getRowIndex() + 1;
            int beginCelIndex = cell.getColumnIndex() + 1;

            //范围性单元格终止值
            int endRowIndex = beginRowIndex + 1;
            int endCelIndex = beginCelIndex + 6;
            Comment t_comment = cell.getCellComment();
            if (t_comment == null) {
                Drawing patriarch = sheet1.createDrawingPatriarch();
                t_comment = patriarch.createCellComment(patriarch
                        .createAnchor(0, 0, 0, 0, (short) beginCelIndex, beginRowIndex,
                                (short) endCelIndex, endRowIndex));
            }
            RichTextString richTextString = t_comment.getString();
            String commentText = (richTextString == null || StringUtils
                    .isEmpty(richTextString.getString())) ? _LEVEL_CELL_COMMENT : richTextString.getString();

            if (richTextString instanceof HSSFRichTextString) {
                t_comment.setString(new HSSFRichTextString(commentText));
            } else {
                t_comment.setString(new XSSFRichTextString(commentText));
            }
            cell.setCellComment(t_comment);
        }
        //填充levelIds;
        Sheet sheet2 = wk.getSheetAt(1);
        for (int i = 0; i < levelIds.size(); i++) {
            Row row = sheet2.getRow(i);
            if (row == null) {
                row = sheet2.createRow(i);
            }
            Cell cell = row.getCell(0);
            if (cell == null) {
                cell = row.createCell(0);
            }
            cell.setCellValue(levelIds.get(i));
        }

    }


}
