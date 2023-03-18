package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.*;
import com.wanmi.sbc.bookmeta.enums.BookFigureTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaBookLabelProvider;
import com.wanmi.sbc.bookmeta.provider.MetaBookProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookEditPublishInfoVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookQueryByPageResVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    @Resource
    private MetaBookLabelProvider metaBookLabelProvider;

    @Value("classpath:/download/book_lable.xlsx")
    private org.springframework.core.io.Resource templateFile;

    @Value("classpath:/download/book.xlsx")
    private org.springframework.core.io.Resource templateBookFile;

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
        resVO.setTradeId(resBO.getTradeId());
        resVO.setPrice(resBO.getPrice());
        resVO.setTradeName(resBO.getTradeName());
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
        addReqBO.setPrice(addReqVO.getPrice());
        addReqBO.setTradeId(addReqVO.getTradeId());
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
        editReqBO.setPrice(editReqVO.getPrice());
        editReqBO.setTradeId(editReqVO.getTradeId());
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
     *书名和标签名的分页模糊查询
     * @return
     */
    @PostMapping("/bookLabelQuery")
    public MetaBookQueryByPageBo bookLabelQueryByPage(@RequestBody MetaBookQueryByPageBo metaBookQueryByPageBo) {
        return metaBookProvider.queryBookLabelBypage(metaBookQueryByPageBo);
    }

    /**
     *商详推荐内容
     * @return
     */
    @PostMapping("/getRecommentKey")
    public BusinessResponse<MetaBookRecommentKeyBo> getRecommentKey(@RequestParam(value = "spuId") String spuId) {
        return metaBookProvider.getRecommentKey(spuId);
    }

    /**
     * 下载模板
     */
    @PostMapping("/templateBookLable")
    public void templateBookLable() {
        InputStream is = null;
        org.springframework.core.io.Resource file=templateFile;
        try{

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<Map> bookLableMap = metaBookProvider.queryBookLable();
            AtomicInteger rowCount= new AtomicInteger(1);
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

    /**
     * 下载模板
     */
    @PostMapping("/loadExcel")
    public BaseResponse loadExcel(MultipartFile multipartFile) {
        int addNum = 0;
        int updateNum = 0;
        String res=null;
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
            for (int rowNum = firstRowNum+1; rowNum <= lastRowNum; rowNum++) {
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
                MetaBookLabelBO metaBookLabelBO=new MetaBookLabelBO();
                metaBookLabelBO.setBookId(Integer.parseInt(cells[4]));
                metaBookLabelBO.setLabelId(Integer.parseInt(cells[2]));
                metaBookLabelBO.setDelFlag(0);
                res = metaBookLabelProvider.importBookLabel(metaBookLabelBO).getContext();
                if(null!=res) {
                    String[] split = res.split(",");
                    addNum = addNum + Integer.parseInt(split[1]);
                    updateNum = updateNum + Integer.parseInt(split[0]);
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


    /**
     * 下载模板
     */
    @PostMapping("/templateBook")
    public void templateBook() {
        InputStream is = null;
        org.springframework.core.io.Resource file=templateBookFile;
        try{

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            is = file.getInputStream();
            Workbook wk = WorkbookFactory.create(is);

            Sheet expressCompanySheet = wk.getSheetAt(0);
            List<Map> bookMap = metaBookProvider.queryAllBook();
            AtomicInteger rowCount= new AtomicInteger(1);
            bookMap.stream().forEach(map -> {
                Row row = expressCompanySheet.createRow(rowCount.getAndIncrement());
                row.createCell(0).setCellValue(map.get("id").toString());
                row.createCell(1).setCellValue(map.get("name").toString());
            });
            wk.write(outputStream);
            String fileName = URLEncoder.encode("book.xlsx", "UTF-8");
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

