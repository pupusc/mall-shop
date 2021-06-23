package com.wanmi.sbc.goods.pointsgoodscate.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.PointsGoodsCateErrorCode;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateQueryRequest;
import com.wanmi.sbc.goods.api.request.pointsgoodscate.PointsGoodsCateSortRequest;
import com.wanmi.sbc.goods.bean.vo.PointsGoodsCateVO;
import com.wanmi.sbc.goods.pointsgoodscate.model.root.PointsGoodsCate;
import com.wanmi.sbc.goods.pointsgoodscate.repository.PointsGoodsCateRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * <p>积分商品分类表业务逻辑</p>
 *
 * @author yang
 * @date 2019-05-13 09:50:07
 */
@Service("PointsGoodsCateService")
public class PointsGoodsCateService {
    @Autowired
    private PointsGoodsCateRepository pointsGoodsCateRepository;

    /**
     * 新增积分商品分类表
     *
     * @author yang
     */
    @Transactional
    public PointsGoodsCate add(PointsGoodsCate entity) {
        PointsGoodsCateQueryRequest cateQueryRequest = PointsGoodsCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO)
                .build();
        List<PointsGoodsCate> pointsGoodsCates = list(cateQueryRequest);
        if (pointsGoodsCates.size() >= 30) {
            throw new SbcRuntimeException(PointsGoodsCateErrorCode.CATE_OVERSTEP);
        }
        if (pointsGoodsCates.stream()
                .anyMatch(cate ->
                        StringUtils.equals(entity.getCateName(), cate.getCateName()))) {
            throw new SbcRuntimeException(PointsGoodsCateErrorCode.NAME_ALREADY_EXIST);
        }
        pointsGoodsCateRepository.save(entity);
        return entity;
    }

    /**
     * 修改积分商品分类表
     *
     * @author yang
     */
    @Transactional
    public PointsGoodsCate modify(PointsGoodsCate entity) {
        PointsGoodsCateQueryRequest cateQueryRequest = PointsGoodsCateQueryRequest.builder()
                .delFlag(DeleteFlag.NO)
                .build();
        List<PointsGoodsCate> pointsGoodsCates = list(cateQueryRequest);
        if (pointsGoodsCates.stream()
                .anyMatch(cate ->
                        StringUtils.equals(entity.getCateName(), cate.getCateName()) &&
                                !Objects.equals(entity.getCateId(), cate.getCateId()))) {
            throw new SbcRuntimeException(PointsGoodsCateErrorCode.NAME_ALREADY_EXIST);
        }
        pointsGoodsCateRepository.save(entity);
        return entity;
    }

    /**
     * 拖拽排序
     *
     * @param request
     */
    @Transactional
    public void editSort(PointsGoodsCateSortRequest request) {
        List<Integer> cateIdList = request.getCateIdList();
        for (int i = 0; i < cateIdList.size(); i++) {
            PointsGoodsCate pointsGoodsCate = pointsGoodsCateRepository.findById(cateIdList.get(i)).get();
            pointsGoodsCate.setUpdatePerson(request.getUpdatePerson());
            pointsGoodsCate.setUpdateTime(request.getUpdateTime());
            pointsGoodsCate.setSort(i + 1);
            pointsGoodsCateRepository.save(pointsGoodsCate);
        }
    }

    /**
     * 单个删除积分商品分类表
     *
     * @author yang
     */
    @Transactional
    public void deleteById(Integer id) {
        pointsGoodsCateRepository.modifyDelFlagById(id);
    }

    /**
     * 单个查询积分商品分类表
     *
     * @author yang
     */
    public PointsGoodsCate getById(Integer id) {
        return pointsGoodsCateRepository.findById(id).get();
    }

    /**
     * 分页查询积分商品分类表
     *
     * @author yang
     */
    public Page<PointsGoodsCate> page(PointsGoodsCateQueryRequest queryReq) {
        return pointsGoodsCateRepository.findAll(
                PointsGoodsCateWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询积分商品分类表
     *
     * @author yang
     */
    public List<PointsGoodsCate> list(PointsGoodsCateQueryRequest queryReq) {
        return pointsGoodsCateRepository.findAll(
                PointsGoodsCateWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author yang
     */
    public PointsGoodsCateVO wrapperVo(PointsGoodsCate pointsGoodsCate) {
        if (pointsGoodsCate != null) {
            PointsGoodsCateVO pointsGoodsCateVO = new PointsGoodsCateVO();
            KsBeanUtil.copyPropertiesThird(pointsGoodsCate, pointsGoodsCateVO);
            return pointsGoodsCateVO;
        }
        return null;
    }
}
