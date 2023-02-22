package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaBookAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookEditPublishInfoReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryByPageResBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookQueryPublishInfoResBO;
import com.wanmi.sbc.bookmeta.enums.BookFigureTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaBookProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookEditPublishInfoVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 书籍(MetaBook)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Slf4j
@RestController
@RequestMapping("/metaBook")
public class MetaBookController {
    /**
     * 书籍-服务对象
     */
    @Resource
    private MetaBookProvider metaBookProvider;

    @Value("classpath:/download/book_lable.xlsx")
    private org.springframework.core.io.Resource templateFile;

    /**
     * 书籍-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookQueryByPageResVO>> queryByPage(@RequestBody @Valid MetaBookQueryByPageReqVO pageRequest) {
        MetaBookQueryByPageReqBO pageReqBO = new MetaBookQueryByPageReqBO();
        pageReqBO.setIsbn(pageRequest.getIsbn());
        pageReqBO.setNameLike(pageRequest.getName());
        pageReqBO.setAuthorLike(pageRequest.getAuthorName());
        pageReqBO.setPublisherLike(pageRequest.getPublisherName());
        pageReqBO.setPage(pageRequest.getPage());
        pageReqBO.setFillFigureName(true);
        pageReqBO.setFillPublisherName(true);

        BusinessResponse<List<MetaBookQueryByPageResBO>> resultBO = this.metaBookProvider.queryByPage(pageReqBO);
        if (!CommonErrorCode.SUCCESSFUL.equals(resultBO.getCode())) {
            return BusinessResponse.error(resultBO.getCode(), resultBO.getMessage());
        }
        if (CollectionUtils.isEmpty(resultBO.getContext())) {
            return BusinessResponse.success(Collections.EMPTY_LIST, resultBO.getPage());
        }

        List<MetaBookQueryByPageResVO> resultVO = resultBO.getContext().stream().map(item -> {
            MetaBookQueryByPageResVO resVO = new MetaBookQueryByPageResVO();
            BeanUtils.copyProperties(item, resVO);
            //人物
            if (CollectionUtils.isNotEmpty(item.getBookFigures())) {
                List<MetaBookQueryByPageResBO.BookFigure> authors = item.getBookFigures().stream()
                        .filter(figure -> BookFigureTypeEnum.AUTHOR.getCode().equals(figure.getFigureType())).collect(Collectors.toList());
                resVO.setAuthorName(authors.stream().map(author -> author.getFigureName()).collect(Collectors.joining("/")));
            }
            return resVO;
        }).collect(Collectors.toList());

        return BusinessResponse.success(resultVO, resultBO.getPage());
    }

    /**
     * 书籍-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookQueryByIdResVO> queryById(@RequestBody @Valid IntegerIdVO id) {
        BusinessResponse<MetaBookQueryByIdResBO> response = this.metaBookProvider.queryById(id.getId());
        if (!CommonErrorCode.SUCCESSFUL.equals(response.getCode())) {
            return BusinessResponse.error(response.getCode(), response.getMessage());
        }

        MetaBookQueryByIdResBO resBO = response.getContext();
        MetaBookQueryByIdResVO resVO = new MetaBookQueryByIdResVO();
        resVO.setId(resBO.getId());
        resVO.setIsbn(resBO.getIsbn());
        resVO.setName(resBO.getName());
        resVO.setSubName(resBO.getSubName());
        resVO.setOriginName(resBO.getOriginName());
        resVO.setAuthorList(new ArrayList<>());
        resVO.setPainterList(new ArrayList<>());
        resVO.setTranslatorList(new ArrayList<>());
        //人物分组
        if (CollectionUtils.isNotEmpty(resBO.getBookFigures())) {
            for (MetaBookQueryByIdResBO.BookFigure bookFigure : resBO.getBookFigures()) {
                MetaBookQueryByIdResVO.Figure figure = new MetaBookQueryByIdResVO.Figure();
                figure.setId(bookFigure.getFigureId());
                figure.setName(bookFigure.getFigureName());
                if (BookFigureTypeEnum.AUTHOR.getCode().equals(bookFigure.getFigureType())) {
                    resVO.getAuthorList().add(figure);
                } else if (BookFigureTypeEnum.PAINTER.getCode().equals(bookFigure.getFigureType())) {
                    resVO.getPainterList().add(figure);
                } else if (BookFigureTypeEnum.TRANSLATOR.getCode().equals(bookFigure.getFigureType())) {
                    resVO.getTranslatorList().add(figure);
                } else {
                    log.error("错误的书籍人物类型, type={}", bookFigure.getFigureType());
                }
            }
        }
        //标签分组
        Map<Integer, String> labelNameMap = new HashMap<>();
        Map<Integer, Map<Integer, List<MetaBookQueryByIdResVO.Label>>> ppLabelGroup = new HashMap<>();
        for (MetaBookQueryByIdResBO.BookLabel bookLabel : resBO.getBookLabels()) {
            if (bookLabel.getLabelCates() == null || bookLabel.getLabelCates().size() < 2) {
                log.error("标签的目录分类少于2级, id={}, path={}", bookLabel.getLabelId(), bookLabel.getLabelPath());
                continue;
            }
            Integer firstCateKey = bookLabel.getLabelCates().get(0).getId();
            Integer secondCateKey = bookLabel.getLabelCates().get(1).getId();
            labelNameMap.put(firstCateKey, bookLabel.getLabelCates().get(0).getName());
            labelNameMap.put(secondCateKey, bookLabel.getLabelCates().get(1).getName());
            if (ppLabelGroup.get(firstCateKey) == null) {
                ppLabelGroup.put(firstCateKey, new HashMap<>());
            }
            Map<Integer, List<MetaBookQueryByIdResVO.Label>> pLabelGroup = ppLabelGroup.get(firstCateKey);
            if (pLabelGroup.get(secondCateKey) == null) {
                pLabelGroup.put(secondCateKey, new ArrayList<>());
            }
            MetaBookQueryByIdResVO.Label label = new MetaBookQueryByIdResVO.Label();
            label.setId(bookLabel.getLabelId());
            label.setName(bookLabel.getLabelName());
            label.setPath(bookLabel.getLabelPath());
            pLabelGroup.get(secondCateKey).add(label);
        }
        resVO.setLabelCateList(new ArrayList<>());
        for (Map.Entry<Integer, Map<Integer, List<MetaBookQueryByIdResVO.Label>>> firstEntry : ppLabelGroup.entrySet()) {
            for (Map.Entry<Integer, List<MetaBookQueryByIdResVO.Label>> secondEntry : firstEntry.getValue().entrySet()) {
                MetaBookQueryByIdResVO.LabelCate labelCate = new MetaBookQueryByIdResVO.LabelCate();
                labelCate.setFirstCateId(firstEntry.getKey());
                labelCate.setFirstCateName(labelNameMap.get(firstEntry.getKey()));
                labelCate.setSecondCateId(secondEntry.getKey());
                labelCate.setSecondCateName(labelNameMap.get(secondEntry.getKey()));
                labelCate.setLabelList(secondEntry.getValue());
                resVO.getLabelCateList().add(labelCate);
            }
        }

        return BusinessResponse.success(resVO);
    }

    /**
     * 书籍-新增基础信息
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody @Valid MetaBookAddReqVO addReqVO) {
        MetaBookAddReqBO addReqBO = new MetaBookAddReqBO();
        BeanUtils.copyProperties(addReqVO, addReqBO);

        List<MetaBookAddReqBO.Figure> figures = new ArrayList<>();
        wrapBookFigure(figures, addReqVO.getAuthorIdList(), BookFigureTypeEnum.AUTHOR.getCode());
        wrapBookFigure(figures, addReqVO.getTranslatorIdList(), BookFigureTypeEnum.TRANSLATOR.getCode());
        wrapBookFigure(figures, addReqVO.getPainterIdList(), BookFigureTypeEnum.PAINTER.getCode());
        addReqBO.setFigures(figures);
        return this.metaBookProvider.insert(addReqBO);
    }

    /**
     * 书籍-编辑基础信息
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody @Valid MetaBookEditReqVO editReqVO) {
        MetaBookEditReqBO editReqBO = new MetaBookEditReqBO();
        BeanUtils.copyProperties(editReqVO, editReqBO);
        editReqBO.setLabelIds(editReqVO.getLabelIdList());

        List<MetaBookAddReqBO.Figure> figures = new ArrayList<>();
        wrapBookFigure(figures, editReqVO.getAuthorIdList(), BookFigureTypeEnum.AUTHOR.getCode());
        wrapBookFigure(figures, editReqVO.getTranslatorIdList(), BookFigureTypeEnum.TRANSLATOR.getCode());
        wrapBookFigure(figures, editReqVO.getPainterIdList(), BookFigureTypeEnum.PAINTER.getCode());
        editReqBO.setFigures(figures);
        return this.metaBookProvider.update(editReqBO);
    }

    /**
     * 书籍-编辑出版信息
     */
    @PostMapping("editPublishInfo")
    public BusinessResponse<Boolean> editPublishInfo(@RequestBody @Valid MetaBookEditPublishInfoVO editReqVO) {
        MetaBookEditPublishInfoReqBO editReqBO = new MetaBookEditPublishInfoReqBO();
        BeanUtils.copyProperties(editReqVO, editReqBO);
        return this.metaBookProvider.updatePublishInfo(editReqBO);
    }

    /**
     * 书籍-查询出版信息
     */
    @PostMapping("queryPublishInfo")
    public BusinessResponse<MetaBookEditPublishInfoVO> queryPublishInfo(@RequestBody @Valid IntegerIdVO id) {
        MetaBookQueryPublishInfoResBO resultBO = this.metaBookProvider.queryPublishInfo(id.getId()).getContext();
        MetaBookEditPublishInfoVO resultVO = new MetaBookEditPublishInfoVO();
        if (resultBO != null) {
            BeanUtils.copyProperties(resultBO, resultVO);
        }
        return BusinessResponse.success(resultVO);
    }

    /**
     * 书籍-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody @Valid IntegerIdVO id) {
        return this.metaBookProvider.deleteById(id.getId());
    }

    private void wrapBookFigure(List<MetaBookAddReqBO.Figure> figures, List<Integer> figureIds, Integer figureType) {
        if (CollectionUtils.isEmpty(figureIds)) {
            return;
        }
        for (Integer figureId : figureIds) {
            MetaBookAddReqBO.Figure figure = new MetaBookAddReqBO.Figure();
            figure.setFigureId(figureId);
            figure.setFigureType(figureType);
            figures.add(figure);
        }
    }

    /**
     * 下载模板
     */
    @PostMapping("/templateBookLable")
    public void templateBookLable() {
        org.springframework.core.io.Resource file=templateFile;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is)){
            Sheet expressCompanySheet = wk.getSheetAt(1);
            List<Map> bookLableMap = metaBookProvider.queryBookLable();
            AtomicInteger rowCount= new AtomicInteger();
            bookLableMap.stream().forEach(map -> {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.get("isbn").toString());
                row.createCell(1).setCellValue(map.get("book_name").toString());
                row.createCell(2).setCellValue(map.get("label_id").toString());
                row.createCell(3).setCellValue(map.get("label_name").toString());
                row.createCell(4).setCellValue(map.get("book_id").toString());
            });
            wk.write(outputStream);
            String fileName = URLEncoder.encode("book_lable.xlsx", "UTF-8");
            HttpUtil.getResponse().setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            HttpUtil.getResponse().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, e);
        }
    }
}

