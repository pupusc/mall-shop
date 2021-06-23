package com.wanmi.sbc.goods.bookingsalegoods.service;

import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.bookingsalegoods.BookingSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentGoodsInfoSimpleCriterIaBuilder;
import com.wanmi.sbc.goods.bean.dto.BookingGoodsInfoSimplePageDTO;
import com.wanmi.sbc.goods.bean.dto.BookingSaleGoodsDTO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsalegoods.model.root.BookingSaleGoods;
import com.wanmi.sbc.goods.bookingsalegoods.repository.BookingSaleGoodsRepository;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>预售商品信息业务逻辑</p>
 *
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@Service("BookingSaleGoodsService")
public class BookingSaleGoodsService {
    @Autowired
    private BookingSaleGoodsRepository bookingSaleGoodsRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 新增预售商品信息
     *
     * @author dany
     */
    @Transactional
    public BookingSaleGoods add(BookingSaleGoods entity) {
        bookingSaleGoodsRepository.save(entity);
        return entity;
    }

    /**
     * 修改预售商品信息
     *
     * @author dany
     */
    @Transactional
    public BookingSaleGoods modify(BookingSaleGoods entity) {
        bookingSaleGoodsRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除预售商品信息
     *
     * @author dany
     */
    @Transactional
    public void deleteById(Long id) {
        bookingSaleGoodsRepository.deleteById(id);
    }

    /**
     * 批量删除预售商品信息
     *
     * @author dany
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        bookingSaleGoodsRepository.deleteByIdIn(ids);
    }

    /**
     * 单个查询预售商品信息
     *
     * @author dany
     */
    public BookingSaleGoods getOne(Long id, Long storeId) {
        return bookingSaleGoodsRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预售商品信息不存在"));
    }

    /**
     * 分页查询预售商品信息
     *
     * @author dany
     */
    public Page<BookingSaleGoods> page(BookingSaleGoodsQueryRequest queryReq) {
        return bookingSaleGoodsRepository.findAll(
                BookingSaleGoodsWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询预售商品信息
     *
     * @author dany
     */
    public List<BookingSaleGoods> list(BookingSaleGoodsQueryRequest queryReq) {
        return bookingSaleGoodsRepository.findAll(BookingSaleGoodsWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author dany
     */
    public BookingSaleGoodsVO wrapperVo(BookingSaleGoods bookingSaleGoods) {
        if (bookingSaleGoods != null) {
            BookingSaleGoodsVO bookingSaleGoodsVO = KsBeanUtil.convert(bookingSaleGoods, BookingSaleGoodsVO.class);
            return bookingSaleGoodsVO;
        }
        return null;
    }

    public Page<BookingSaleGoods> build(BookingGoodsInfoSimplePageDTO queryRequest) {
        return bookingSaleGoodsRepository.findAll((root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<BookingSaleGoods, BookingSale> bookingSaleJoin = root.join(root.getModel().getSingularAttribute("bookingSale", BookingSale.class), JoinType.INNER);
            // 批量查询-goodsInfoIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsInfoIds())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsInfoIds()));
            }

            if (Objects.nonNull(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(bookingSaleJoin.get("storeId"), queryRequest.getStoreId()));
            }

            // 批量查询-storeIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            if (Objects.nonNull(queryRequest.getBookingType())) {
                predicates.add(cbuild.equal(bookingSaleJoin.get("bookingType"), queryRequest.getBookingType()));
            }

            if (Objects.nonNull(queryRequest.getQueryTab())) {
                if (queryRequest.getQueryTab().equals(AppointmentStatus.NO_START)) {
                    predicates.add(cbuild.greaterThan(bookingSaleJoin.get("startTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.RUNNING)) {
                    predicates.add(cbuild.lessThan(bookingSaleJoin.get("startTime"), LocalDateTime.now()));
                    predicates.add(cbuild.greaterThan(bookingSaleJoin.get("endTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.END)) {
                    predicates.add(cbuild.lessThan(bookingSaleJoin.get("endTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.SUSPENDED)) {
                    predicates.add(cbuild.equal(bookingSaleJoin.get("pauseFlag"), 1));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.NO_START_AND_RUNNING)) {
                    predicates.add(cbuild.greaterThan(bookingSaleJoin.get("endTime"), LocalDateTime.now()));
                }
            }
            Predicate[] pre = predicates.toArray(new Predicate[predicates.size()]);
            return cquery.where(cbuild.and(predicates.toArray(pre))).getRestriction();
        }, queryRequest.getPageable());
    }

    public BookingVO wrapperBookingVO(BookingSaleGoods entity, Map<Long, BookingSale> bookingSaleMap, Map<String, GoodsInfo> goodsInfoMap) {
        if (entity != null) {
            BookingSaleGoodsVO bookingSaleGoodsVO = KsBeanUtil.convert(entity, BookingSaleGoodsVO.class);
            bookingSaleGoodsVO.setGoodsInfoVO(KsBeanUtil.convert(goodsInfoMap.get(bookingSaleGoodsVO.getGoodsInfoId()), GoodsInfoVO.class));
            BookingSaleVO bookingSaleVO = KsBeanUtil.convert(bookingSaleMap.get(entity.getBookingSaleId()), BookingSaleVO.class);
            bookingSaleVO.buildStatus();
            return BookingVO.builder().
                    bookingSale(bookingSaleVO)
                    .bookingSaleGoods(bookingSaleGoodsVO).build();
        }
        return null;
    }

    public Page<BookingSaleGoodsVO> pageBookingGoodsInfo(BookingGoodsInfoSimpleCriterIaBuilder request) {
        Query query = entityManager.createNativeQuery(request.getQuerySql().concat(request.getQueryConditionSql()).concat(request.getQuerySort()));
        //组装查询参数
        this.wrapperQueryParam(query, request);
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 查询预售活动商品spu列表
        List<BookingSaleGoodsVO> saleGoodsVOS = BookingGoodsInfoSimpleCriterIaBuilder.converter(query.getResultList());

        //查询预售活动spu列表总数
        Query totalCountRes =
                entityManager.createNativeQuery(request.getQueryTotalCountSql().concat(request.getQueryConditionSql()).concat(request.getQueryTotalTemp()));
        //组装查询参数
        this.wrapperQueryParam(totalCountRes, request);
        long totalCount = Long.parseLong(totalCountRes.getSingleResult().toString());

        return new PageImpl<>(saleGoodsVOS, request.getPageable(), totalCount);
    }

    @Transactional(rollbackFor = Exception.class)
    public int subCanBookingCount(BookingSaleGoodsDTO bookingSaleGoodsDTO) {
        return bookingSaleGoodsRepository.subCanBookingCount(bookingSaleGoodsDTO.getBookingSaleId(), bookingSaleGoodsDTO.getGoodsInfoId(), bookingSaleGoodsDTO.getStock());
    }

    @Transactional(rollbackFor = Exception.class)
    public int addCanBookingCount(BookingSaleGoodsDTO bookingSaleGoodsDTO) {
        return bookingSaleGoodsRepository.addCanBookingCount(bookingSaleGoodsDTO.getBookingSaleId(), bookingSaleGoodsDTO.getGoodsInfoId(), bookingSaleGoodsDTO.getStock());
    }

    @Transactional(rollbackFor = Exception.class)
    public int addBookingPayCount(BookingSaleGoodsDTO bookingSaleGoodsDTO) {
        return bookingSaleGoodsRepository.addBookingPayCount(bookingSaleGoodsDTO.getBookingSaleId(), bookingSaleGoodsDTO.getGoodsInfoId(), bookingSaleGoodsDTO.getStock());
    }

    @Transactional(rollbackFor = Exception.class)
    public int addBookinghandSelCount(BookingSaleGoodsDTO bookingSaleGoodsDTO) {
        return bookingSaleGoodsRepository.addBookinghandSelCount(bookingSaleGoodsDTO.getBookingSaleId(), bookingSaleGoodsDTO.getGoodsInfoId(), bookingSaleGoodsDTO.getStock());
    }

    @Transactional(rollbackFor = Exception.class)
    public int addBookingTailCount(BookingSaleGoodsDTO bookingSaleGoodsDTO) {
        return bookingSaleGoodsRepository.addBookingTailCount(bookingSaleGoodsDTO.getBookingSaleId(), bookingSaleGoodsDTO.getGoodsInfoId(), bookingSaleGoodsDTO.getStock());
    }

    /**
     * 组装查询参数
     *
     * @param query
     * @param request
     */
    private void wrapperQueryParam(Query query, BookingGoodsInfoSimpleCriterIaBuilder request) {
        if (StringUtils.isNoneBlank(request.getGoodsName())) {
            query.setParameter("goodsName", request.getGoodsName());
        }
        if (CollectionUtils.isNotEmpty(request.getGoodsInfoIds())) {
            query.setParameter("goodsInfoIds", request.getGoodsInfoIds());
        }
    }
}

