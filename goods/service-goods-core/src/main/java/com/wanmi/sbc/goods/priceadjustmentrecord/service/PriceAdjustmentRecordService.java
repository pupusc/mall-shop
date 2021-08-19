package com.wanmi.sbc.goods.priceadjustmentrecord.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.priceadjustmentrecord.PriceAdjustmentRecordQueryRequest;
import com.wanmi.sbc.goods.bean.vo.PriceAdjustmentRecordVO;
import com.wanmi.sbc.goods.priceadjustmentrecord.model.root.PriceAdjustmentRecord;
import com.wanmi.sbc.goods.priceadjustmentrecord.repository.PriceAdjustmentRecordRepository;
import com.wanmi.sbc.goods.priceadjustmentrecorddetail.repository.PriceAdjustmentRecordDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>调价记录表业务逻辑</p>
 *
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@Service("PriceAdjustmentRecordService")
public class PriceAdjustmentRecordService {
    @Autowired
    private PriceAdjustmentRecordRepository priceAdjustmentRecordRepository;

    @Autowired
    private PriceAdjustmentRecordDetailRepository priceAdjustmentRecordDetailRepository;

    /**
     * 新增调价记录表
     *
     * @author chenli
     */
    @Transactional
    public PriceAdjustmentRecord add(PriceAdjustmentRecord entity) {
        entity.setCreateTime(LocalDateTime.now());
        priceAdjustmentRecordRepository.save(entity);
        return entity;
    }

    /**
     * 修改调价记录表
     *
     * @author chenli
     */
    @Transactional
    public PriceAdjustmentRecord modify(PriceAdjustmentRecord entity) {
        priceAdjustmentRecordRepository.save(entity);
        return entity;
    }

    /**
     * 单个查询调价记录表
     *
     * @author chenli
     */
    public PriceAdjustmentRecord getOne(String id, Long storeId) {
        return priceAdjustmentRecordRepository.findByIdAndStoreId(id, storeId);
    }

    /**
     * 单个查询调价记录表
     *
     * @param id
     * @return
     */
    public PriceAdjustmentRecord findById(String id) {
        return priceAdjustmentRecordRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询调价记录表
     *
     * @author chenli
     */
    public Page<PriceAdjustmentRecord> page(PriceAdjustmentRecordQueryRequest queryReq) {
        return priceAdjustmentRecordRepository.findAll(
                PriceAdjustmentRecordWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询调价记录表
     *
     * @author chenli
     */
    public List<PriceAdjustmentRecord> list(PriceAdjustmentRecordQueryRequest queryReq) {
        return priceAdjustmentRecordRepository.findAll(PriceAdjustmentRecordWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 根据设定时间删除未确认的调价记录
     *
     * @param time
     */
    @Transactional
    public void deleteUnconfirmRecord(LocalDateTime time) {
        List<String> ids = priceAdjustmentRecordRepository.findByTime(time);
        priceAdjustmentRecordRepository.deleteByIds(ids);
        priceAdjustmentRecordDetailRepository.deleteByAdjustNos(ids);
    }

    /**
     * 将实体包装成VO
     *
     * @author chenli
     */
    public PriceAdjustmentRecordVO wrapperVo(PriceAdjustmentRecord priceAdjustmentRecord) {
        if (priceAdjustmentRecord != null) {
            PriceAdjustmentRecordVO priceAdjustmentRecordVO = KsBeanUtil.convert(priceAdjustmentRecord, PriceAdjustmentRecordVO.class);
            return priceAdjustmentRecordVO;
        }
        return null;
    }

    /**
     * 将实体包装成VO
     *
     * @author chenli
     */
    public PriceAdjustmentRecordVO wrapperVoForPage(PriceAdjustmentRecord priceAdjustmentRecord) {
        if (priceAdjustmentRecord != null) {
            PriceAdjustmentRecordVO priceAdjustmentRecordVO = KsBeanUtil.convert(priceAdjustmentRecord, PriceAdjustmentRecordVO.class);
            priceAdjustmentRecordVO.setCreatorAccount(priceAdjustmentRecordVO.getCreatorAccount() == null ? "" : String.format("%s%s%s",
                    priceAdjustmentRecordVO.getCreatorAccount().substring(0, 3), "****", priceAdjustmentRecordVO.getCreatorAccount().substring(7)));
            return priceAdjustmentRecordVO;
        }
        return null;
    }
}

