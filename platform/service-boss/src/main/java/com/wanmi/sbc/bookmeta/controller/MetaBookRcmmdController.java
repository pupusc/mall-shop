package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookRcmmdByBookIdResBO;
import com.wanmi.sbc.bookmeta.enums.BookRcmmdTypeEnum;
import com.wanmi.sbc.bookmeta.provider.MetaBookRcmmdProvider;
import com.wanmi.sbc.bookmeta.vo.IntegerIdVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdByBookIdReqVO;
import com.wanmi.sbc.bookmeta.vo.MetaBookRcmmdByBookIdResVO;
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
import java.util.stream.Collectors;

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

//    /**
//     * 书籍推荐-分页查询
//     *
//     * @param pageRequest 分页对象
//     * @return 查询结果
//     */
//    @PostMapping("queryByPage")
//    public BusinessResponse<List<MetaBookRcmmdQueryByPageResVO>> queryByPage(@RequestBody MetaBookRcmmdQueryByPageReqVO pageRequest) {
//        MetaBookRcmmdQueryByPageReqBO pageReqBO = JSON.parseObject(JSON.toJSONString(pageRequest), MetaBookRcmmdQueryByPageReqBO.class);
//        BusinessResponse<List<MetaBookRcmmdBO>> list = this.metaBookRcmmdProvider.queryByPage(pageReqBO);
//        return JSON.parseObject(JSON.toJSONString(list), BusinessResponse.class);
//    }
//
//    /**
//     * 书籍推荐-主键查询
//     *
//     * @param id 主键
//     * @return 单条数据
//     */
//    @PostMapping("queryById")
//    public BusinessResponse<MetaBookRcmmdQueryByIdResVO> queryById(@RequestBody IntegerIdVO id) {
//        BusinessResponse<MetaBookRcmmdBO> resBO = this.metaBookRcmmdProvider.queryById(id.getId());
//        return JSON.parseObject(JSON.toJSONString(resBO), BusinessResponse.class);
//    }
//
//    /**
//     * 书籍推荐-新增数据
//     *
//     * @param addReqVO 实体
//     * @return 新增结果
//     */
//    @PostMapping("add")
//    public BusinessResponse<Integer> add(@RequestBody MetaBookRcmmdAddReqVO addReqVO) {
//        MetaBookRcmmdBO addReqBO = new MetaBookRcmmdBO();
//        BeanUtils.copyProperties(addReqVO, addReqBO);
//        return BusinessResponse.success(this.metaBookRcmmdProvider.insert(addReqBO).getContext());
//    }
//
//    /**
//     * 书籍推荐-编辑数据
//     *
//     * @param editReqVO 实体
//     * @return 编辑结果
//     */
//    @PostMapping("edit")
//    public BusinessResponse<Boolean> edit(@RequestBody MetaBookRcmmdEditReqVO editReqVO) {
//        MetaBookRcmmdBO editReqVBO = new MetaBookRcmmdBO();
//        BeanUtils.copyProperties(editReqVO, editReqVBO);
//        this.metaBookRcmmdProvider.update(editReqVBO);
//        return BusinessResponse.success(true);
//    }
//
//    /**
//     * 书籍推荐-删除数据
//     *
//     * @param id 主键
//     * @return 删除是否成功
//     */
//    @PostMapping("deleteById")
//    public BusinessResponse<Boolean> deleteById(@RequestBody IntegerIdVO id) {
//        return this.metaBookRcmmdProvider.deleteById(id.getId());
//    }

    /**
     * 书籍推荐-书籍id查询
     */
    @PostMapping("queryByBookId")
    public BusinessResponse<MetaBookRcmmdByBookIdResVO> queryByBookId(@RequestBody IntegerIdVO id) {
        MetaBookRcmmdByBookIdResBO boResult = this.metaBookRcmmdProvider.queryByBookId(id.getId()).getContext();
        MetaBookRcmmdByBookIdResVO voResult = new MetaBookRcmmdByBookIdResVO();

        if (boResult == null) {
            return BusinessResponse.success(voResult);
        }
        voResult.setBookId(id.getId());
        voResult.setFitAgeMax(boResult.getFitAgeMax());
        voResult.setFitAgeMin(boResult.getFitAgeMin());

        //适读对象
        if (boResult.getFitTargets() != null) {
            List<MetaBookRcmmdByBookIdResVO.FitTarget> fitTargets = boResult.getFitTargets().stream().map(item -> {
                MetaBookRcmmdByBookIdResVO.FitTarget fitTarget = new MetaBookRcmmdByBookIdResVO.FitTarget();
                fitTarget.setId(item.getId());
                fitTarget.setName(item.getName());
                return fitTarget;
            }).collect(Collectors.toList());
            voResult.setFitTargets(fitTargets);
        }
        //推荐信息
        for (MetaBookRcmmdByBookIdResBO.MetaBookRcmmdBO item : boResult.getBookRcmmds()) {
            MetaBookRcmmdByBookIdResVO.MetaBookRcmmdVO rcmmdVO = new MetaBookRcmmdByBookIdResVO.MetaBookRcmmdVO();
            rcmmdVO.setId(item.getId());
            rcmmdVO.setBizId(item.getBizId());
            rcmmdVO.setBizType(item.getBizType());
            rcmmdVO.setBizTime(item.getBizTime());
            rcmmdVO.setDescr(item.getDescr());
            rcmmdVO.setName(item.getName());
            rcmmdVO.setIsSelected(item.getIsSelected());
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
            } else if (BookRcmmdTypeEnum.MENTION.getCode().equals(item.getBizType())) {
                voResult.getMentionList().add(rcmmdVO);
            } else if (BookRcmmdTypeEnum.WENMIAO.getCode().equals(item.getBizType())) { //文喵推荐
                voResult.getWenMiao().setId(rcmmdVO.getId());
                voResult.getWenMiao().setBizType(rcmmdVO.getBizType());
                voResult.getWenMiao().setBizTime(rcmmdVO.getBizTime());
                voResult.getWenMiao().setDescr(rcmmdVO.getDescr());
                voResult.getWenMiao().setIsSelected(rcmmdVO.getIsSelected());
            } else if (BookRcmmdTypeEnum.XUANSHUREN.getCode().equals(item.getBizType())) { //选书人
                voResult.getWenMiao().setBizId(rcmmdVO.getBizId());
                voResult.getWenMiao().setName(rcmmdVO.getName());
                voResult.getWenMiao().setRecommend(rcmmdVO.getDescr());
            }
            else {
                log.error("书籍错误的推荐类型，type={}", item.getBizType());
            }
        }

        return BusinessResponse.success(voResult);
    }

    /**
     * 书籍推荐-编辑数据
     */
    @PostMapping("editByBookId")
    public BusinessResponse<Boolean> editByBookId(@RequestBody @Valid MetaBookRcmmdByBookIdReqVO editReqVO) {
        MetaBookRcmmdByBookIdReqBO editBO = new MetaBookRcmmdByBookIdReqBO();
        editBO.setBookId(editReqVO.getBookId());
        editBO.setBookRcmmds(new ArrayList<>());
        editBO.setFitAgeMax(editReqVO.getFitAgeMax());
        editBO.setFitAgeMin(editReqVO.getFitAgeMin());
        editBO.setFitTargetIds(editReqVO.getFitTargets().stream().map(MetaBookRcmmdByBookIdReqVO.FitTarget::getId).distinct().collect(Collectors.toList()));
        List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> boList = editBO.getBookRcmmds();
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.AWARD.getCode(), editReqVO.getAwardList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.EDITOR.getCode(), editReqVO.getEditorList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.MEDIA.getCode(), editReqVO.getMediaList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.ORGAN.getCode(), editReqVO.getOrganList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.EXPERT.getCode(), editReqVO.getExpertList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.QUOTE.getCode(), editReqVO.getQuoteList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.DRAFT.getCode(), editReqVO.getDraftList(), boList);
        wrapEditBO(editReqVO.getBookId(), BookRcmmdTypeEnum.MENTION.getCode(), editReqVO.getMentionList(), boList);
        //文喵 start
        if (editReqVO.getWenMiao() != null) {
            MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO bo = new MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO();
            BeanUtils.copyProperties(editReqVO.getWenMiao(), bo);
            bo.setBookId(editReqVO.getBookId());
            if (editReqVO.getWenMiao().getBizId() != null) {
                MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO boRec = new MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO();
                BeanUtils.copyProperties(bo, boRec);
                boRec.setDescr(editReqVO.getWenMiao().getRecommend());
                boRec.setBizType(BookRcmmdTypeEnum.XUANSHUREN.getCode());
                boList.add(boRec);
                bo.setBizId(null);
            }
            bo.setBizType(BookRcmmdTypeEnum.WENMIAO.getCode());
            boList.add(bo);
        }
        //文喵 end
        this.metaBookRcmmdProvider.editByBookId(editBO);
        return BusinessResponse.success(true);
    }

    private void wrapEditBO(Integer bookId, Integer type, List<MetaBookRcmmdByBookIdReqVO.MetaBookRcmmdVO> voList, List<MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO> boList) {
        if (CollectionUtils.isEmpty(voList)) {
            return;
        }
        for (MetaBookRcmmdByBookIdReqVO.MetaBookRcmmdVO vo : voList) {
            MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO bo = new MetaBookRcmmdByBookIdReqBO.MetaBookRcmmdBO();
            BeanUtils.copyProperties(vo, bo);
            bo.setBookId(bookId);
            bo.setBizId(vo.getBizId());
            bo.setBizType(type);
            bo.setBizTime(vo.getBizTime());
            bo.setIsSelected(vo.getIsSelected());
            boList.add(bo);
        }
    }
}

