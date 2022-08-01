package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaZoneAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByIdResBO$Book;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByPageResBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEnableReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaFigure;
import com.wanmi.sbc.bookmeta.entity.MetaZone;
import com.wanmi.sbc.bookmeta.entity.MetaZoneBook;
import com.wanmi.sbc.bookmeta.enums.ZoneStatusEnum;
import com.wanmi.sbc.bookmeta.mapper.MetaZoneBookMapper;
import com.wanmi.sbc.bookmeta.mapper.MetaZoneMapper;
import com.wanmi.sbc.bookmeta.provider.MetaZoneProvider;
import com.wanmi.sbc.bookmeta.service.MetaBookInfoParam;
import com.wanmi.sbc.bookmeta.service.MetaBookInfoResult;
import com.wanmi.sbc.bookmeta.service.MetaBookService;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图书分区(MetaZone)表服务实现类
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@Validated
@RestController
public class MetaZoneProviderImpl implements MetaZoneProvider {
    @Resource
    private MetaZoneMapper metaZoneMapper;
    @Autowired
    private MetaZoneBookMapper metaZoneBookMapper;
    @Autowired
    private MetaBookService metaBookService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BusinessResponse<MetaZoneByIdResBO> queryById(Integer id) {
        MetaZone metaZone = this.metaZoneMapper.selectByPrimaryKey(id);
        if (metaZone == null || metaZone.getDelFlag() != 0) {
            return BusinessResponse.success(null);
        }

        MetaZoneByIdResBO resBO = new MetaZoneByIdResBO();
        resBO.setType(metaZone.getType());
        resBO.setName(metaZone.getName());
        resBO.setDescr(metaZone.getDescr());
        resBO.setBooks(new ArrayList<>());
        //查询关联图书
        MetaZoneBook metaZoneBook = new MetaZoneBook();
        metaZoneBook.setZoneId(id);
        metaZoneBook.setDelFlag(0);
        List<MetaZoneBook> books = this.metaZoneBookMapper.select(metaZoneBook);
        for (MetaZoneBook bookDTO : books) {
            MetaZoneByIdResBO$Book bookBO = new MetaZoneByIdResBO$Book();
            bookBO.setId(bookDTO.getBookId());
            //获取书籍信息
            MetaBookInfoParam param = new MetaBookInfoParam();
            param.setId(bookDTO.getBookId());
            param.setQueryAuthor(true);
            MetaBookInfoResult bookInfo = metaBookService.getPackInfoById(param);
            if (bookInfo != null) {
                bookBO.setIsbn(bookInfo.getIsbn());
                bookBO.setName(bookInfo.getName());
                if (bookInfo.getAuthors() != null) {
                    bookBO.setAuthorName(bookInfo.getAuthors().stream().map(MetaFigure::getName).collect(Collectors.joining("/")));
                }
            }
            resBO.getBooks().add(bookBO);
        }
        return BusinessResponse.success(resBO);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @Override
    public BusinessResponse<List<MetaZoneByPageResBO>> queryByPage(@Valid MetaZoneQueryByPageReqBO pageRequest) {
        Page page = pageRequest.getPage();
        MetaZone metaZone = JSON.parseObject(JSON.toJSONString(pageRequest), MetaZone.class);
        metaZone.setDelFlag(0);

        page.setTotalCount(this.metaZoneMapper.queryCount(metaZone));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }

        List<MetaZone> dtos = this.metaZoneMapper.queryList(metaZone, page.getOffset(), page.getPageSize());
        return BusinessResponse.success(DO2BOUtils.objA2objB4List(dtos, MetaZoneByPageResBO.class), page);
    }

    /**
     * 新增数据
     *
     * @param metaZone 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Integer> insert(@Valid MetaZoneAddReqBO metaZone) {
        MetaZone oprateDTO = new MetaZone();
        oprateDTO.setType(metaZone.getType());
        oprateDTO.setName(metaZone.getName());
        oprateDTO.setDescr(metaZone.getDescr());

        //图书分区
        oprateDTO.setStatus(ZoneStatusEnum.ENABLE.getCode()); //默认启用
        if (this.metaZoneMapper.insertSelective(oprateDTO) != 1) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "图书分区新增失败");
        }
        //分区关联图书
        insertMetaZoneBook(oprateDTO.getId(), metaZone.getBooks());
        return BusinessResponse.success(oprateDTO.getId());
    }

    /**
     * 修改数据
     *
     * @param metaZone 实例对象
     * @return 实例对象
     */
    @Transactional
    @Override
    public BusinessResponse<Boolean> update(@Valid MetaZoneEditReqBO metaZone) {
        //更新主题
        MetaZone oprateDTO = new MetaZone();
        oprateDTO.setId(metaZone.getId());
        oprateDTO.setType(metaZone.getType());
        oprateDTO.setName(metaZone.getName());
        oprateDTO.setDescr(metaZone.getDescr());
        this.metaZoneMapper.updateByPrimaryKeySelective(oprateDTO);
        //分区关联图书
        MetaZoneBook deleteBook = new MetaZoneBook();
        deleteBook.setZoneId(metaZone.getId());
        this.metaZoneBookMapper.delete(deleteBook);
        insertMetaZoneBook(oprateDTO.getId(), metaZone.getBooks());
        return BusinessResponse.success(true);
    }

    private void insertMetaZoneBook(Integer zoneId, List<Integer> bookIds) {
        if (CollectionUtils.isEmpty(bookIds)) {
            return;
        }
        Date now = new Date();
        List<MetaZoneBook> books = new ArrayList<>();
        for (Integer bookId : bookIds) {
            MetaZoneBook book = new MetaZoneBook();
            book.setZoneId(zoneId);
            book.setBookId(bookId);
            book.setDelFlag(0);
            book.setCreateTime(now);
            book.setUpdateTime(now);
            books.add(book);
        }
        this.metaZoneBookMapper.insertBatch(books);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public BusinessResponse<Boolean> deleteById(Integer id) {
        MetaZone deleteZone = new MetaZone();
        deleteZone.setId(id);
        deleteZone.setDelFlag(1);
        this.metaZoneMapper.updateByPrimaryKeySelective(deleteZone);

        MetaZoneBook deleteBook = new MetaZoneBook();
        deleteBook.setZoneId(id);
        this.metaZoneBookMapper.deleteByExample(deleteBook);
        return BusinessResponse.success(true);
    }

    @Override
    public BusinessResponse<Boolean> enable(MetaZoneEnableReqBO metaZone) {
        //更新主题
        MetaZone oprateDTO = new MetaZone();
        oprateDTO.setId(metaZone.getId());
        oprateDTO.setStatus(metaZone.isFlag() ? ZoneStatusEnum.ENABLE.getCode() : ZoneStatusEnum.DISABLE.getCode());
        this.metaZoneMapper.updateByPrimaryKeySelective(oprateDTO);
        return BusinessResponse.success(true);
    }
}
