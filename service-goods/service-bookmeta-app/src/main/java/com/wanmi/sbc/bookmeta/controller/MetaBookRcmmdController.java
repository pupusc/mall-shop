package com.wanmi.sbc.bookmeta.controller;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookRcmmd;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdAddReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdByBookIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdEditReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdQueryByIdResVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdQueryByPageResVO;
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
 * 书籍推荐(MetaBookRcmmd)表控制层
 * @menu 图书基础库
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@Slf4j
@RestController
@RequestMapping("metaBookRcmmd")
public class MetaBookRcmmdController {
    /**
     * 书籍推荐-服务对象
     */
    @Resource
    private MetaBookRcmmdProvider metaBookRcmmdProvider;

    /**
     * 书籍推荐-分页查询
     *
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("queryByPage")
    public BusinessResponse<List<MetaBookRcmmdQueryByPageResVO>> queryByPage(@RequestBody MetaBookRcmmdQueryByPageReqVO pageRequest) {
        MetaBookRcmmdQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmdQueryByPageReqBO.class);
        BusinessResponse<List<MetaBookRcmmd>> list = this.metaBookRcmmdProvider.queryByPage(pageReqBO);
        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
    }

    /**
     * 书籍推荐-主键查询
     *
     * @param id 主键
     * @return 单条数据
     */
    @PostMapping("queryById")
    public BusinessResponse<MetaBookRcmmdQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
        BusinessResponse<MetaBookRcmmd> resBO = this.metaBookRcmmdProvider.queryById(id.getId());
        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
    }

    /**
     * 书籍推荐-新增数据
     *
     * @param addReqVO 实体
     * @return 新增结果
     */
    @PostMapping("add")
    public BusinessResponse<Integer> add(@RequestBody MetaBookRcmmdAddReqVO addReqVO) {
        MetaBookRcmmd addReqBO = new MetaBookRcmmd();
        BeanUtils.copyProperties(addReqVO, addReqBO);
        return BusinessResponse.success(this.metaBookRcmmdProvider.insert(addReqBO).getContext());
    }

    /**
     * 书籍推荐-编辑数据
     *
     * @param editReqVO 实体
     * @return 编辑结果
     */
    @PostMapping("edit")
    public BusinessResponse<Boolean> edit(@RequestBody MetaBookRcmmdEditReqVO editReqVO) {
        MetaBookRcmmd editReqVBO = new MetaBookRcmmd();
        BeanUtils.copyProperties(editReqVO, editReqVBO);
        this.metaBookRcmmdProvider.update(editReqVBO);
        return BusinessResponse.success(true);
    }

    /**
     * 书籍推荐-删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @PostMapping("deleteById")
    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
        return this.metaBookRcmmdProvider.deleteById(id.getId());
    }

    /**
     * 书籍推荐-书籍id查询
     */
    @PostMapping("queryByBookId")
    public BusinessResponse<MetaBookRcmmdByBookIdVO> queryByBookId(@RequestBody IntegerIdVO id) {
        MetaBookRcmmdByBookIdReqBO boResult = this.metaBookRcmmdProvider.queryByBookId(id.getId()).getContext();
        MetaBookRcmmdByBookIdVO voResult = new MetaBookRcmmdByBookIdVO();

        if (boResult == null) {
            return BusinessResponse.success(voResult);
        }
        voResult.setBookId(id.getId());
        voResult.setFitAgeMax(boResult.getFitAgeMax());
        voResult.setFitAgeMin(boResult.getFitAgeMin());
        voResult.setFitTargetIdList(boResult.getFitTargetIds());

        for (MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO item : boResult.getBookRcmmds()) {
            MetaBookRcmmdByBookIdVO.MetaBookRcmmdVO rcmmdVO = new MetaBookRcmmdByBookIdVO.MetaBookRcmmdVO();
            rcmmdVO.setId(item.getId());
            rcmmdVO.setBizId(item.getBizId());
            rcmmdVO.setBizType(item.getBizType());
            rcmmdVO.setBizTime(item.getBizTime());
            rcmmdVO.setDescr(item.getDescr());
            if (BookRcmmdTypeEnum.AWARD.getCode().equals(item.getBizType())) {
                voResult.getAwardList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.EDITOR.getCode().equals(item.getBizType())) {
                voResult.getEditorList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.MEDIA.getCode().equals(item.getBizType())) {
                voResult.getMediaList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.ORGAN.getCode().equals(item.getBizType())) {
                voResult.getOrganList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.EXPERT.getCode().equals(item.getBizType())) {
                voResult.getExpertList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.QUOTE.getCode().equals(item.getBizType())) {
                voResult.getQuoteList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.DRAFT.getCode().equals(item.getBizType())) {
                voResult.getDraftList().add(rcmmdVO);
            } else {
                log.error("书籍错误的推荐类型，type={}", item.getBizType());
            }
        }

        return BusinessResponse.success(voResult);
    }

    /**
     * 书籍推荐-编辑数据
     */
    @PostMapping("editByBookId")
    public BusinessResponse<Boolean> editByBookId(@RequestBody @Valid MetaBookRcmmdByBookIdVO editReqVO) {
        MetaBookRcmmdByBookIdReqBO editBO = new MetaBookRcmmdByBookIdReqBO();
        editBO.setBookId(editReqVO.getBookId());
        editBO.setBookRcmmds(new ArrayList<>());
        editBO.setFitAgeMax(editReqVO.getFitAgeMax());
        editBO.setFitAgeMin(editReqVO.getFitAgeMin());
        editBO.setFitTargetIds(editReqVO.getFitTargetIdList());
        List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> boList = editBO.getBookRcmmds();
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.AWARD.getCode(), editReqVO.getAwardList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.EDITOR.getCode(), editReqVO.getEditorList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.MEDIA.getCode(), editReqVO.getMediaList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.ORGAN.getCode(), editReqVO.getOrganList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.EXPERT.getCode(), editReqVO.getExpertList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.QUOTE.getCode(), editReqVO.getQuoteList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.DRAFT.getCode(), editReqVO.getDraftList(), boList);

        this.metaBookRcmmdProvider.editByBookId(editBO);
        return BusinessResponse.success(true);
    }

    private void wrapEditBO(Integer bookId, Integer type, List<MetaBookRcmmdByBookIdVO.MetaBookRcmmdVO> voList, List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> boList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        for (MetaBookRcmmdByBookIdVO.MetaBookRcmmdVO vo : voList) {
            MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO bo = new MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO();
            BeanUtils.copyProperties(vo, bo);
            bo.setBookId(bookId);
            bo.setBizId(vo.getBizId());
            bo.setBizType(type);
            bo.setBizTime(vo.getBizTime());
            boList.add(bo);
        }
    }
}

