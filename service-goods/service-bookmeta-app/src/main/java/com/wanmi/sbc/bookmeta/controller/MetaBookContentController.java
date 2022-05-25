package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.bookmeta.enums.BookContentTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaBookContentProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentByBookIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookContentQueryByPageResVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
@Slf4j
@RestController
@RequestMapping("metaBookContent")
public class MetaBookContentController {
    /**
     * 书籍内容描述-服务对象
     */
    @Resource
    private MetaBookContentProvider metaBookContentProvider;

    /**
     * 书籍内容描述-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookContentQueryByPageResVO>> queryByPage(@RequestBody MetaBookContentQueryByPageReqVO pageRequest) {
        MetaBookContentQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookContentQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookContent>> list = this.metaBookContentProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍内容描述-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookContentQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookContent> resBO = this.metaBookContentProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍内容描述-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookContentAddReqVO addReqVO) {
        MetaBookContent addReqBO = new MetaBookContent();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookContentProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍内容描述-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookContentEditReqVO editReqVO) {
        MetaBookContent editReqVBO = new MetaBookContent();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookContentProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍内容描述-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookContentProvider.deleteById(id.getId());
    }


    /**
     * 书籍内容查询（出版内容）
     */
    @PostMapping("queryByBookId")
    public BusinessResponse<MetaBookContentByBookIdVO> queryByBookId(@RequestBody IntegerIdVO id) {
        List<MetaBookContent> boList = this.metaBookContentProvider.queryByBookId(id.getId()).getContext();
        MetaBookContentByBookIdVO voResult = new MetaBookContentByBookIdVO();

        if (CollectionUtils.isEmpty(boList)) {
            return BusinessResponse.success(voResult);
        }

        voResult.setBookId(id.getId());
        for (MetaBookContent item : boList) {
            MetaBookContentByBookIdVO.MetaBookContentVO content = new MetaBookContentByBookIdVO.MetaBookContentVO();
            content.setId(item.getId());
            content.setFigureId(item.getFigureId());
            content.setType(item.getType());
            content.setContent(item.getContent());
            if (BookContentTypeEnum.INTRODUCE.getCode().equals(item.getType()) && voResult.getIntroduce() == null) {
                voResult.setIntroduce(content);
            } else if (BookContentTypeEnum.CATALOGUE.getCode().equals(item.getType()) && voResult.getCatalogue() == null) {
                voResult.setCatalogue(content);
            } else if (BookContentTypeEnum.PREFACE.getCode().equals(item.getType()) && voResult.getPreface() == null) {
                voResult.setPreface(content);
            } else if (BookContentTypeEnum.EXTRACT.getCode().equals(item.getType())) {
                voResult.getExtractList().add(content);
            } else if (BookContentTypeEnum.PRELUDE.getCode().equals(item.getType())) {
                voResult.getPreludeList().add(content);
            } else {
                log.error("书籍错误的内容类型，type={}", item.getType());
            }
        }
        return BusinessResponse.success(voResult);
    }

    /**
     * 书籍内容编辑（出版内容）
     */
    @PostMapping("editByBookId")
    public BusinessResponse<Boolean> editByBookId(@RequestBody @Valid MetaBookContentByBookIdVO editReqVO) {
        List<MetaBookContentByBookIdReqBO> boList = new ArrayList<>();
        wrapEditBO(editReqVO.getBookId(), editReqVO.getIntroduce(), boList, BookContentTypeEnum.INTRODUCE.getCode());
        wrapEditBO(editReqVO.getBookId(), editReqVO.getCatalogue(), boList, BookContentTypeEnum.CATALOGUE.getCode());
        wrapEditBO(editReqVO.getBookId(), editReqVO.getPreface(), boList, BookContentTypeEnum.PREFACE.getCode());
        editReqVO.getExtractList().forEach(item -> wrapEditBO(editReqVO.getBookId(), item, boList, BookContentTypeEnum.EXTRACT.getCode()));
        editReqVO.getPreludeList().forEach(item -> wrapEditBO(editReqVO.getBookId(), item, boList, BookContentTypeEnum.PRELUDE.getCode()));
        return this.metaBookContentProvider.editByBookId(editReqVO.getBookId(), boList);
    }

    private void wrapEditBO(Integer bookId, MetaBookContentByBookIdVO.MetaBookContentVO contentVO, List<MetaBookContentByBookIdReqBO> boList, Integer contentType) {
        if (contentVO != null) {
            MetaBookContentByBookIdReqBO contentBO = new MetaBookContentByBookIdReqBO();
            BeanUtils.copyProperties(contentVO, contentBO);
            contentBO.setBookId(bookId);
            contentBO.setType(contentType);
            boList.add(contentBO);
        }
    }
}

