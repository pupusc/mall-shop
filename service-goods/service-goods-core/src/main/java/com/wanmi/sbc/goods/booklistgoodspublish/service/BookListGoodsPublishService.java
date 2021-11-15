package com.wanmi.sbc.goods.booklistgoodspublish.service;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.goods.api.enums.CategoryEnum;
import com.wanmi.sbc.goods.api.enums.DeleteFlagEnum;
import com.wanmi.sbc.goods.api.request.booklistgoodspublish.CountBookListModelGroupProviderRequest;
import com.wanmi.sbc.goods.booklistgoods.model.root.BookListGoodsDTO;
import com.wanmi.sbc.goods.booklistgoods.service.BookListGoodsService;
import com.wanmi.sbc.goods.booklistgoodspublish.model.root.BookListGoodsPublishDTO;
import com.wanmi.sbc.goods.booklistgoodspublish.repository.BookListGoodsPublishRepository;
import com.wanmi.sbc.goods.booklistgoodspublish.response.BookListGoodsPublishLinkModelResponse;
import com.wanmi.sbc.goods.booklistgoodspublish.response.CountBookListModelGroupResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private EntityManager entityManager;


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
     * 获取书单 商品数量
     * @return
     */
    public List<CountBookListModelGroupResponse> countByBookListModelList(CountBookListModelGroupProviderRequest countBookListModelGroupProviderRequest) {
        String sql = "select count(1) goodsCount, m.id bookListModelId from t_book_list_goods_publish publish left join t_book_list_model m on publish.book_list_id = m.id " +
                "where publish.del_flag = 0 and m.del_flag = 0 ";
        String businessTypeSql = "";
        for (Integer businessTypeId : countBookListModelGroupProviderRequest.getBusinessTypeColl()) {
            if (StringUtils.isEmpty(businessTypeSql)) {
                businessTypeSql += businessTypeId.toString();
            } else {
                businessTypeSql += "," + businessTypeId;
            }
        }
        sql += " and m.business_type in (" + businessTypeSql + ") and publish.category = " + countBookListModelGroupProviderRequest.getCategoryId();
        String bookListModelIdSql = "";
        for (Integer bookListModelId : countBookListModelGroupProviderRequest.getBookListIdCollection()) {
            if (StringUtils.isEmpty(bookListModelIdSql)) {
                bookListModelIdSql += bookListModelId.toString();
            } else {
                bookListModelIdSql += "," + bookListModelId.toString();
            }
        }
        sql += " and m.id in (" + bookListModelIdSql + ") group by m.id ";

        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> sqlResult = nativeQuery.getResultList();
        List<CountBookListModelGroupResponse>  result = new ArrayList<>();
        for (Map<String, Object> stringObjectMap : sqlResult) {
            if (stringObjectMap.get("bookListModelId") == null) {
                continue;
            }
            CountBookListModelGroupResponse param = new CountBookListModelGroupResponse();
            param.setGoodsCount(stringObjectMap.get("goodsCount") == null ? 0 : Integer.parseInt(stringObjectMap.get("goodsCount").toString()));
            param.setBookListModelId(Integer.parseInt(stringObjectMap.get("bookListModelId").toString()));
            result.add(param);
        }
        return result;
    }

    /**
     * 获取书单 书单 发布商品 列表信息
     * @param businessTypeList 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     * @param spuIdColl 商品 spuId
     * @return
     */
    public List<BookListGoodsPublishLinkModelResponse> listPublishGoodsAndBookListModelBySpuId(List<Integer> businessTypeList, Collection<String> spuIdColl){
        if (CollectionUtils.isEmpty(businessTypeList) || CollectionUtils.isEmpty(spuIdColl)) {
            log.error("--->> BookListGoodsPublishService.listPublishGoodsAndBookListModel param businessType:{} spuId:{} one of these is null",
                    JSON.toJSONString(businessTypeList), JSON.toJSONString(spuIdColl));
        }
        return bookListGoodsPublishRepository.listGoodsPublishLinkModel(businessTypeList, CategoryEnum.BOOK_LIST_MODEL.getCode(), spuIdColl);
    }



    /**
     * 获取书单 类目 发布商品 列表信息
     * @param businessTypeList 书单模板类型 1 排行榜 2 书单 3 编辑推荐 4 专题
     * @return
     */
    public List<BookListGoodsPublishLinkModelResponse> listPublishGoodsAndBookListModelByClassifyAndSupId(List<Integer> businessTypeList, List<Integer> notInBookListIdList, Collection<Integer> classifyIdColl){
        if (CollectionUtils.isEmpty(businessTypeList) || CollectionUtils.isEmpty(classifyIdColl)) {
            log.error("--->> BookListGoodsPublishService.listPublishGoodsAndBookListModel param businessType:{} spuId:{} not in: {} one of these is null",
                    JSON.toJSONString(businessTypeList), JSON.toJSONString(classifyIdColl), JSON.toJSONString(notInBookListIdList));
        }

        return bookListGoodsPublishRepository.listGoodsPublishLinkClassify(businessTypeList, notInBookListIdList, CategoryEnum.BOOK_LIST_MODEL.getCode(), classifyIdColl);
    }


    private Specification<BookListGoodsPublishDTO> packageWhere(Collection<Integer> bookListIdCollection, Integer bookListId, Integer categoryId, String spuId) {
        return new Specification<BookListGoodsPublishDTO>() {
            final List<Predicate> predicateList = new ArrayList<>();
            @Override
            public Predicate toPredicate(Root<BookListGoodsPublishDTO> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"), DeleteFlagEnum.NORMAL.getCode()));
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
