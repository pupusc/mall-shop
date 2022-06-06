package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.entity.MetaBook;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.entity.MetaBookFigure;
import com.wanmi.sbc.bookmeta.entity.MetaBookLabel;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.entity.MetaDataDict;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaLabel;
import com.wanmi.sbc.bookmeta.entity.MetaPublisher;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.enums.BookFigureTypeEnum;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.enums.DataDictCateEnum;
import com.wanmi.sbc.bookmeta.enums.FigureTypeEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaBookContentMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaBookRcmmdMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaDataDictMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaFigureMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaLabelMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaPublisherMapper;
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
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @date 2022-06-06 16:24:00
 */
@Slf4j
@RestController
public class BookImportProviderImpl {
    @Autowired
    private MetaBookMapper metaBookMapper;
    @Autowired
    private MetaFigureMapper metaFigureMapper;
    @Autowired
    private MetaPublisherMapper metaPublisherMapper;
    @Autowired
    private MetaBookFigureMapper metaBookFigureMapper;
    @Autowired
    private MetaBookContentMapper metaBookContentMapper;
    @Autowired
    private MetaBookRcmmdMapper metaBookRcmmdMapper;
    @Autowired
    private MetaDataDictMapper metaDataDictMapper;
    @Autowired
    private MetaLabelMapper metaLabelMapper;
    @Autowired
    private MetaBookLabelMapper metaBookLabelMapper;

    Map<String, MetaPublisher> publisherMap = new HashMap<>();
    Map<String, MetaDataDict> bindMap = new HashMap<>();
    Map<String, MetaDataDict> paperMap = new HashMap<>();
    Map<String, MetaFigure> figureMap = new HashMap<>();
    Map<String, MetaLabel> labelMap = new HashMap<>();

    @Transactional
    @PostMapping("/goods/${application.goods.version}/metaBook/import")
    public BusinessResponse<Boolean> importBook(String fileName) throws Exception {
        log.info("========================图书基础库，导入数据开始：========================");
        Workbook workbook = WorkbookFactory.create(new File(fileName));

        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "表格sheet不存在");
        }
        //获得当前sheet的开始行
        int firstRowNum = sheet.getFirstRowNum();
        //获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < 1) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "表格内容为空");
        }

        initCache();
        int maxCell = ExcelCellInfo.max_size;
        //循环除了第一行的所有行
        for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
            //获得当前行
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }
            Cell[] cells = new Cell[maxCell];
            boolean isNotEmpty = false;
            for (int i = 0; i < maxCell; i++) {
                Cell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                cells[i] = cell;
                if (StringUtils.isNotBlank(ExcelHelper.getValue(cell))) {
                    isNotEmpty = true;
                }
            }
            //数据都为空，则跳过去
            if (!isNotEmpty) {
                continue;
            }
            String isbn = cells[ExcelCellInfo.index_isbn].getStringCellValue();
            String name = cells[ExcelCellInfo.index_name].getStringCellValue();
            //必填项验证
            if (StringUtils.isBlank(isbn)) {
                log.error("导入数据失败：书籍的isbn不能为空, row={}", rowNum);
                continue;
            }
            //重复isbn验证
            if (checkIsRepeat(isbn)) {
                log.error("导入数据失败：书籍的isbn已经存在, row={}", rowNum);
                continue;
            }
            if (StringUtils.isBlank(name)) {
                log.error("导入数据失败：书籍的name不能为空, row={}", rowNum);
                continue;
            }

            //书籍
            MetaBook book = new MetaBook();
            book.setIsbn(isbn);
            book.setName(name);
            book.setPrice(cells[ExcelCellInfo.index_price].getNumericCellValue());
            book.setSubName(cells[ExcelCellInfo.index_sub_name].getStringCellValue());
            book.setOriginName(cells[ExcelCellInfo.index_origin_name].getStringCellValue());
            //出版社
            book.setPublisherId(createPublisher(cells[ExcelCellInfo.index_publisher].getStringCellValue()));
            //装帧
            book.setBindId(createBind(cells[ExcelCellInfo.index_bind].getStringCellValue()));
            book.setPublishTime(parsePublishTime(cells[ExcelCellInfo.index_publish_time].getStringCellValue()));
            book.setPageCount(parsePageCount(cells[ExcelCellInfo.index_page_count].getStringCellValue()));
            book.setPaperId(createPaper(cells[ExcelCellInfo.index_paper].getStringCellValue()));
            this.metaBookMapper.insertSelective(book);

            if (this.metaBookMapper.insertSelective(book) != 1) {
                log.error("导入数据失败：插入数据库没有成功，row={}", rowNum);
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "生成书籍数据失败");
            }

            //作者、作者简介
            createFigure(book.getId(), cells[ExcelCellInfo.index_author].getStringCellValue(), cells[ExcelCellInfo.index_author_descr].getStringCellValue(),
                    FigureTypeEnum.AUTHOR, BookFigureTypeEnum.AUTHOR);
            //译者
            createFigure(book.getId(), cells[ExcelCellInfo.index_translator].getStringCellValue(), null, FigureTypeEnum.AUTHOR, BookFigureTypeEnum.TRANSLATOR);
            //简介
            createContent(book.getId(), cells[ExcelCellInfo.index_introduce].getStringCellValue(), BookContentTypeEnum.INTRODUCE);
            //目录
            createContent(book.getId(), cells[ExcelCellInfo.index_catalogue].getStringCellValue(), BookContentTypeEnum.CATALOGUE);
            //前言
            createContent(book.getId(), cells[ExcelCellInfo.index_preface].getStringCellValue(), BookContentTypeEnum.PREFACE);
            //编辑推荐
            createRcmmd(book.getId(), cells[ExcelCellInfo.index_editor_rcmmd].getStringCellValue(), BookRcmmdTypeEnum.EDITOR);
            //媒体推荐
            createRcmmd(book.getId(), cells[ExcelCellInfo.index_media_rcmmd].getStringCellValue(), BookRcmmdTypeEnum.MEDIA);
            //标签
            createLabel(book.getId(), cells[ExcelCellInfo.index_label].getStringCellValue());
            log.info("导入单条数据结束：row={}", rowNum);
        }

        destroy();
        log.info("========================图书基础库，导入数据完成！========================");
        return BusinessResponse.success(true);
    }

    private void createLabel(Integer bookId, String str) {
        if (StringUtils.isBlank(str)) {
            return;
        }
        Date now = new Date();
        List<String> labels = Arrays.asList(str.split(","));
        List<MetaBookLabel> bookLabels = labels.stream().filter(item -> Objects.nonNull(labelMap.get(item))).map(item -> {
            MetaBookLabel bookLabel = new MetaBookLabel();
            bookLabel.setBookId(bookId);
            bookLabel.setLabelId(labelMap.get(item).getId());
            bookLabel.setDelFlag(0);
            bookLabel.setCreateTime(now);
            bookLabel.setUpdateTime(now);
            return bookLabel;
        }).collect(Collectors.toList());
        this.metaBookLabelMapper.insertBatch(bookLabels);
    }

    private void createRcmmd(Integer bookId, String content, BookRcmmdTypeEnum rcmmdTypeEnum) {
        if (StringUtils.isBlank(content)) {
            return;
        }
        if (content.length() > 10000) {
            log.error("推荐内容({})长度超出存储范围，将进行截断处理，bookId={}", rcmmdTypeEnum.getDesc(), bookId);
            content = content.substring(0, 10000);
        }
        MetaBookRcmmd metaBookRcmmd = new MetaBookRcmmd();
        metaBookRcmmd.setBookId(bookId);
        metaBookRcmmd.setBizType(rcmmdTypeEnum.getCode());
        metaBookRcmmd.setDescr(content);
        this.metaBookRcmmdMapper.insertSelective(metaBookRcmmd);
    }

    private void createContent(Integer bookId, String content, BookContentTypeEnum contentTypeEnum) {
        if (StringUtils.isBlank(content)) {
            return;
        }
        if (content.length() > 10000) {
            log.error("出版内容({})长度超出存储范围，将进行截断处理，bookId={}", contentTypeEnum.getDesc(), bookId);
            content = content.substring(0, 10000);
        }
        MetaBookContent metaBookContent = new MetaBookContent();
        metaBookContent.setBookId(bookId);
        metaBookContent.setContent(content);
        metaBookContent.setType(contentTypeEnum.getCode());
        this.metaBookContentMapper.insertSelective(metaBookContent);
    }

    private void createFigure(Integer bookId, String name, String desc, FigureTypeEnum figureTypeEnum, BookFigureTypeEnum bookFigureTypeEnum) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        if (figureMap.get(name) == null) {
            MetaFigure metaFigure = new MetaFigure();
            metaFigure.setName(name);
            metaFigure.setIntroduce(desc);
            metaFigure.setType(figureTypeEnum.getCode());
            this.metaFigureMapper.insertSelective(metaFigure);
            if (metaFigure.getId() == null) {
                throw new SbcRuntimeException(CommonErrorCode.FAILED, "生成作者/译者人物失败");
            }
            figureMap.put(name, metaFigure);
        }
        Integer authorId = figureMap.get(name).getId();
        MetaBookFigure bookFigure = new MetaBookFigure();
        bookFigure.setBookId(bookId);
        bookFigure.setFigureId(authorId);
        bookFigure.setFigureType(bookFigureTypeEnum.getCode());
        this.metaBookFigureMapper.insertSelective(bookFigure);
    }

    private Integer parsePageCount(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Integer result = null;
        try {
            result = Integer.valueOf(str);
        } catch (Exception e) {
            log.warn("");
        }
        return result;
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date parsePublishTime(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        if (str.length() < 10) {
            return null;
        }
        Date date = null;
        try {
            date = dateFormat.parse(str.substring(0, 10));
        } catch (ParseException e) {
            log.warn("出版日期格式解析错误，date = {}", str);
        }
        return date;
    }

    private void initCache() {
        publisherMap = loadPublishers();
        bindMap = loadBinds();
        paperMap = loadPapers();
        figureMap = loadAuthors();
        labelMap = loadLabels();
    }

    private void destroy() {
        publisherMap.clear();
        bindMap.clear();
        paperMap.clear();
        figureMap.clear();
        labelMap.clear();
    }

    private Map<String, MetaLabel> loadLabels() {
        MetaLabel metaLabel = new MetaLabel();
        metaLabel.setDelFlag(0);
        List<MetaLabel> labels = this.metaLabelMapper.select(metaLabel);
        return labels.stream().collect(Collectors.toMap(MetaLabel::getName, item->item, (a,b)->a));
    }

    private Map<String, MetaFigure> loadAuthors() {
        MetaFigure metaFigure = new MetaFigure();
        metaFigure.setDelFlag(0);
        List<MetaFigure> figures = this.metaFigureMapper.select(metaFigure);
        return figures.stream().collect(Collectors.toMap(MetaFigure::getName, item->item, (a,b)->a));
    }

    private Map<String, MetaDataDict> loadPapers() {
        MetaDataDict metaDataDict = new MetaDataDict();
        metaDataDict.setDelFlag(0);
        metaDataDict.setCate(DataDictCateEnum.BOOK_PAPER.getCode());
        List<MetaDataDict> dataDicts = this.metaDataDictMapper.select(metaDataDict);
        return dataDicts.stream().collect(Collectors.toMap(MetaDataDict::getName, item->item, (a,b)->a));
    }

    private Map<String, MetaDataDict> loadBinds() {
        MetaDataDict metaDataDict = new MetaDataDict();
        metaDataDict.setDelFlag(0);
        metaDataDict.setCate(DataDictCateEnum.BOOK_BIND.getCode());
        List<MetaDataDict> dataDicts = this.metaDataDictMapper.select(metaDataDict);
        return dataDicts.stream().collect(Collectors.toMap(MetaDataDict::getName, item->item, (a,b)->a));
    }

    private Map<String, MetaPublisher> loadPublishers() {
        MetaPublisher metaPublisher = new MetaPublisher();
        metaPublisher.setDelFlag(0);
        List<MetaPublisher> publishers = this.metaPublisherMapper.select(metaPublisher);
        return publishers.stream().collect(Collectors.toMap(MetaPublisher::getName, item->item, (a,b)->a));
    }

    private Integer createPublisher(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (publisherMap.get(name) != null) {
            return publisherMap.get(name).getId();
        }
        MetaPublisher metaPublisher = new MetaPublisher();
        metaPublisher.setName(name);
        metaPublisherMapper.insertSelective(metaPublisher);
        if (metaPublisher.getId() == null) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "生成供应商失败");
        }
        publisherMap.put(name, metaPublisher);
        return metaPublisher.getId();
    }

    private Integer createBind(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (bindMap.get(name) != null) {
            return Integer.valueOf(bindMap.get(name).getValue());
        }
        return null;
    }

    private Integer createPaper(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (paperMap.get(name) != null) {
            return Integer.valueOf(paperMap.get(name).getValue());
        }
        return null;
    }

    private boolean checkIsRepeat(String isbn) {
        Example example = new Example(MetaBook.class);
        example.createCriteria().andEqualTo("isbn", isbn).andNotEqualTo("delFlag", 1);
        return this.metaBookMapper.selectCountByExample(example) > 0;
    }

    private static class ExcelCellInfo {
        static int max_size = 21;
        static int index_isbn = 0;
        static int index_price = 1;
        static int index_name = 2;
        static int index_sub_name = 3;
        static int index_origin_name = 4;
        static int index_author = 5;
        static int index_author_descr = 6;
        static int index_translator = 7;
        static int index_publisher = 8;
        static int index_bind = 9;
        static int index_publish_time = 10;
        static int index_page_count = 11;
        static int index_folio = 12;
        static int index_paper = 13;
        static int index_introduce = 14;
        static int index_catalogue = 15;
        static int index_preface = 16;
        static int index_editor_rcmmd = 17;
        static int index_media_rcmmd = 18;
        static int index_label = 19;
    }
}
