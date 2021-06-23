package com.wanmi.sbc.goods.appointmentsalegoods.service;

import com.wanmi.sbc.common.enums.AppointmentStatus;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.AppointmentSaleGoodsQueryRequest;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.repository.AppointmentSaleGoodsRepository;
import com.wanmi.sbc.goods.bean.dto.AppointmentGoodsInfoSimplePageDTO;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsDTO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.AppointmentVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
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
 * <p>预约抢购业务逻辑</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@Service("AppointmentSaleGoodsService")
public class AppointmentSaleGoodsService {
    @Autowired
    private AppointmentSaleGoodsRepository appointmentSaleGoodsRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 新增预约抢购
     *
     * @author zxd
     */
    @Transactional
    public AppointmentSaleGoods add(AppointmentSaleGoods entity) {
        appointmentSaleGoodsRepository.save(entity);
        return entity;
    }

    /**
     * 修改预约抢购
     *
     * @author zxd
     */
    @Transactional
    public AppointmentSaleGoods modify(AppointmentSaleGoods entity) {
        appointmentSaleGoodsRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除预约抢购
     *
     * @author zxd
     */
    @Transactional
    public void deleteById(Long id) {
        appointmentSaleGoodsRepository.deleteById(id);
    }

    /**
     * 批量删除预约抢购
     *
     * @author zxd
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        appointmentSaleGoodsRepository.deleteByIdIn(ids);
    }

    /**
     * 单个查询预约抢购
     *
     * @author zxd
     */
    public AppointmentSaleGoods getOne(Long id, Long storeId) {
        return appointmentSaleGoodsRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "预约抢购不存在"));
    }

    /**
     * 分页查询预约抢购
     *
     * @author zxd
     */
    public Page<AppointmentSaleGoods> page(AppointmentSaleGoodsQueryRequest queryReq) {
        return appointmentSaleGoodsRepository.findAll(
                AppointmentSaleGoodsWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询预约抢购
     *
     * @author zxd
     */
    public List<AppointmentSaleGoods> list(AppointmentSaleGoodsQueryRequest queryReq) {
        return appointmentSaleGoodsRepository.findAll(AppointmentSaleGoodsWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author zxd
     */
    public AppointmentSaleGoodsVO wrapperVo(AppointmentSaleGoods appointmentSaleGoods) {
        if (appointmentSaleGoods != null) {
            AppointmentSaleGoodsVO appointmentSaleGoodsVO = KsBeanUtil.convert(appointmentSaleGoods, AppointmentSaleGoodsVO.class);
            return appointmentSaleGoodsVO;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateAppointmentCount(AppointmentSaleGoodsDTO appointmentSaleGoodsDTO) {
        return appointmentSaleGoodsRepository.updateAppointmentCount(appointmentSaleGoodsDTO.getAppointmentSaleId(), appointmentSaleGoodsDTO.getGoodsInfoId());
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateBuyCount(AppointmentSaleGoodsDTO appointmentSaleGoodsDTO) {
        return appointmentSaleGoodsRepository.updateBuyCount(appointmentSaleGoodsDTO.getAppointmentSaleId(), appointmentSaleGoodsDTO.getGoodsInfoId(), appointmentSaleGoodsDTO.getStock());
    }

    public Page<AppointmentSaleGoods> build(AppointmentGoodsInfoSimplePageDTO queryRequest) {
        return appointmentSaleGoodsRepository.findAll((root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<AppointmentSaleGoods, AppointmentSale> appointmentSaleJoin = root.join(root.getModel().getSingularAttribute("appointmentSale", AppointmentSale.class), JoinType.INNER);
            // 批量查询-goodsInfoIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getGoodsInfoIds())) {
                predicates.add(root.get("goodsInfoId").in(queryRequest.getGoodsInfoIds()));
            }

            // 查询-storeId
            if (Objects.nonNull(queryRequest.getStoreId())) {
                predicates.add(cbuild.equal(root.get("storeId"), queryRequest.getStoreId()));
            }
            // 批量查询-storeIdList
            if (CollectionUtils.isNotEmpty(queryRequest.getStoreIds())) {
                predicates.add(root.get("storeId").in(queryRequest.getStoreIds()));
            }

            if (Objects.nonNull(queryRequest.getAppointmentType())) {
                predicates.add(cbuild.equal(appointmentSaleJoin.get("appointmentType"), queryRequest.getAppointmentType()));
            }

            if (Objects.nonNull(queryRequest.getQueryTab())) {
                if (queryRequest.getQueryTab().equals(AppointmentStatus.NO_START)) {
                    predicates.add(cbuild.greaterThan(appointmentSaleJoin.get("appointmentStartTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.RUNNING)) {
                    predicates.add(cbuild.lessThan(appointmentSaleJoin.get("appointmentStartTime"), LocalDateTime.now()));
                    predicates.add(cbuild.greaterThan(appointmentSaleJoin.get("appointmentEndTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.END)) {
                    predicates.add(cbuild.lessThan(appointmentSaleJoin.get("snapUpEndTime"), LocalDateTime.now()));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.SUSPENDED)) {
                    predicates.add(cbuild.equal(appointmentSaleJoin.get("pauseFlag"), 1));
                } else if (queryRequest.getQueryTab().equals(AppointmentStatus.NO_START_AND_RUNNING)) {
                    predicates.add(cbuild.greaterThan(appointmentSaleJoin.get("snapUpEndTime"), LocalDateTime.now()));
                }
            }
            Predicate[] pre = predicates.toArray(new Predicate[predicates.size()]);
            return cquery.where(cbuild.and(predicates.toArray(pre))).getRestriction();
        }, queryRequest.getPageable());
    }

    public AppointmentVO wrapperAppointmentVO(AppointmentSaleGoods appointmentSaleGoods, Map<Long, AppointmentSale> appointmentSaleMap, Map<String, GoodsInfo> goodsInfoMap) {
        if (appointmentSaleGoods != null) {
            AppointmentSaleGoodsVO appointmentSaleGoodsVO = KsBeanUtil.convert(appointmentSaleGoods, AppointmentSaleGoodsVO.class);
            appointmentSaleGoodsVO.setGoodsInfoVO(KsBeanUtil.convert(goodsInfoMap.get(appointmentSaleGoodsVO.getGoodsInfoId()), GoodsInfoVO.class));
            AppointmentSaleVO appointmentSaleVO = KsBeanUtil.convert(appointmentSaleMap.get(appointmentSaleGoods.getAppointmentSaleId()), AppointmentSaleVO.class);
            appointmentSaleVO.buildStatus();
            return AppointmentVO.builder().
                    appointmentSale(appointmentSaleVO)
                    .appointmentSaleGoods(appointmentSaleGoodsVO).build();
        }
        return null;
    }


    public Page<AppointmentSaleGoodsVO> pageAppointmentGoodsInfo(AppointmentGoodsInfoSimpleCriterIaBuilder request) {
        Query query = entityManager.createNativeQuery(request.getQuerySql().concat(request.getQueryConditionSql()).concat(request.getQuerySort()));
        //组装查询参数
        wrapperQueryParam(query, request);
        query.setFirstResult(request.getPageNum() * request.getPageSize());
        query.setMaxResults(request.getPageSize());
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 查询预约活动商品spu列表
        List<AppointmentSaleGoodsVO> saleGoodsVOS = AppointmentGoodsInfoSimpleCriterIaBuilder.converter(query.getResultList());

        //查询预约活动spu列表总数
        Query totalCountRes =
                entityManager.createNativeQuery(request.getQueryTotalCountSql().concat(request.getQueryConditionSql()).concat(request.getQueryTotalTemp()));
        //组装查询参数
        wrapperQueryParam(totalCountRes, request);
        long totalCount = Long.parseLong(totalCountRes.getSingleResult().toString());

        return new PageImpl<>(saleGoodsVOS, request.getPageable(), totalCount);
    }

    /**
     * 组装查询参数
     *
     * @param query
     * @param request
     */
    private void wrapperQueryParam(Query query, AppointmentGoodsInfoSimpleCriterIaBuilder request) {
        if (StringUtils.isNoneBlank(request.getGoodsName())) {
            query.setParameter("goodsName", request.getGoodsName());
        }
        if (CollectionUtils.isNotEmpty(request.getGoodsInfoIds())) {
            query.setParameter("goodsInfoIds", request.getGoodsInfoIds());
        }
    }
}

