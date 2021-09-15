package com.wanmi.sbc.goods.booklistgoodspublish.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/5 2:27 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
@Slf4j
public class BookListGoodsPublishService {

    @Resource
    private BookListGoodsPublishRepository bookListGoodsPublishRepository;

    @Resource
    private BookListGoodsService bookListGoodsService;


    @Transactional
    public void publish(Integer bookListId, Integer categoryId, String operator) {
        //获取书单模版对应的 商品列表,即待发布的列表
        List<BookListGoodsDTO> bookListGoodsDTOList = bookListGoodsService.list(null, bookListId, categoryId);
        if (CollectionUtils.isEmpty(bookListGoodsDTOList)) {
            log.error("-------->>> BookListGoodsPublishService.publish bookListId:{}, categoryId: {} is empty return", bookListId, CategoryEnum.BOOK_LIST_MODEL.getCode());
            return;
        }

        //删除已经发布的
        List<BookListGoodsPublishDTO> rawBookListGoodsPublishDTOList = this.list(null, bookListId, categoryId, null, operator);
        Date now = new Date();
        for (BookListGoodsPublishDTO bookListGoodsPublishParam : rawBookListGoodsPublishDTOList) {
            bookListGoodsPublishParam.setUpdateTime(now);
            bookListGoodsPublishParam.setDelFlag(DeleteFlagEnum.DELETE.getCode());
        }
        bookListGoodsPublishRepository.saveAll(rawBookListGoodsPublishDTOList);

        //新增已经发布的
        List<BookListGoodsPublishDTO> bookListGoodsPublishList = new ArrayList<>();
        for (BookListGoodsDTO bookListGoodsParam : bookListGoodsDTOList) {
            BookListGoodsPublishDTO bookListGoodsPublishModel = new BookListGoodsPublishDTO();
            BeanUtils.copyProperties(bookListGoodsParam, bookListGoodsPublishModel);
            bookListGoodsPublishList.add(bookListGoodsPublishModel);
        }
        bookListGoodsPublishRepository.saveAll(bookListGoodsPublishList);
    }

    /**
     * 发布商品列表 档期参数都是按照个数传递，后续更改为对象，
     * @param bookListId
     * @return
     */
    public List<BookListGoodsPublishDTO> list(Collection<Integer> bookListIdCollection, Integer bookListId, Integer categoryId, String spuId, String operator) {
        log.info("---->> BookListGoodsPublishService.list operator:{} bookListId:{} categoryId: {}",
                operator, bookListId, categoryId);
        return bookListGoodsPublishRepository.findAll(this.packageWhere(bookListIdCollection, bookListId, categoryId, spuId));
    }

    /**
     * 获取书单 书单 发布商品 列表信息
     * @param businessTypeList 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     * @param spuId 商品 spuId
     * @return
     */
    public List<BookListGoodsPublishLinkModelResponse> listPublishGoodsAndBookListModelBySpuId(List<Integer> businessTypeList, String spuId){
        if (CollectionUtils.isEmpty(businessTypeList) || StringUtils.isEmpty(spuId)) {
            log.error("--->> BookListGoodsPublishService.listPublishGoodsAndBookListModel param businessType:{} spuId:{} one of these is null",
                    JSON.toJSONString(businessTypeList), spuId);
        }
        return bookListGoodsPublishRepository.listGoodsPublishLinkModel(businessTypeList, CategoryEnum.BOOK_LIST_MODEL.getCode(), spuId);
    }


    /**
     * 获取书单 类目 发布商品 列表信息
     * @param businessTypeList 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     * @param spuId 商品 spuId
     * @return
     */
    public List<BookListGoodsPublishLinkModelResponse> listPublishGoodsAndBookListModelByClassifyAndSupId(List<Integer> businessTypeList, List<Integer> notInBookListIdList, String spuId){
        if (CollectionUtils.isEmpty(businessTypeList) || StringUtils.isEmpty(spuId)) {
            log.error("--->> BookListGoodsPublishService.listPublishGoodsAndBookListModel param businessType:{} spuId:{} not in: {} one of these is null",
                    JSON.toJSONString(businessTypeList), spuId, JSON.toJSONString(notInBookListIdList));
        }
        return bookListGoodsPublishRepository.listGoodsPublishLinkClassify(businessTypeList, notInBookListIdList, CategoryEnum.BOOK_LIST_MODEL.getCode(), spuId);
    }


    private Specification<BookListGoodsPublishDTO> packageWhere(Collection<Integer> bookListIdCollection, Integer bookListId, Integer categoryId, String spuId) {
        return new Specification<BookListGoodsPublishDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<BookListGoodsPublishDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL));
                if (!CollectionUtils.isEmpty(bookListIdCollection)) {
                    predicateList.add(root.get("bookListId").in(bookListIdCollection));
                }
                if (bookListId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("bookListId"), bookListId));
                }
                if (categoryId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("category"), categoryId));
                }
                if (!StringUtils.isEmpty(spuId)) {
                    predicateList.add(criteriaBuilder.equal(root.get("spuId"), spuId));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

}
