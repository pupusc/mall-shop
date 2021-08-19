package com.wanmi.sbc.goods.goodssharerecord.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsShareRecordVO;
import com.wanmi.sbc.goods.goodssharerecord.model.root.GoodsShareRecord;
import com.wanmi.sbc.goods.goodssharerecord.repository.GoodsShareRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>商品分享业务逻辑</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:46:24
 */
@Service("GoodsShareRecordService")
public class GoodsShareRecordService {
    @Autowired
    private GoodsShareRecordRepository goodsShareRecordRepository;

    /**
     * 新增商品分享
     *
     * @author zhangwenchang
     */
    @Transactional
    public GoodsShareRecord add(GoodsShareRecord entity) {
        goodsShareRecordRepository.save(entity);
        return entity;
    }

    /**
     * 修改商品分享
     *
     * @author zhangwenchang
     */
    @Transactional
    public GoodsShareRecord modify(GoodsShareRecord entity) {
        goodsShareRecordRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除商品分享
     *
     * @author zhangwenchang
     */
    @Transactional
    public void deleteById(Long id) {
        goodsShareRecordRepository.deleteById(id);
    }

    /**
     * 批量删除商品分享
     *
     * @author zhangwenchang
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        ids.forEach(id -> goodsShareRecordRepository.deleteById(id));
    }

    /**
     * 单个查询商品分享
     *
     * @author zhangwenchang
     */
    public GoodsShareRecord getById(Long id) {
        return goodsShareRecordRepository.getOne(id);
    }

    /**
     * 分页查询商品分享
     *
     * @author zhangwenchang
     */
    public Page<GoodsShareRecord> page(GoodsShareRecordQueryRequest queryReq) {
        return goodsShareRecordRepository.findAll(
                GoodsShareRecordWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询商品分享
     *
     * @author zhangwenchang
     */
    public List<GoodsShareRecord> list(GoodsShareRecordQueryRequest queryReq) {
        return goodsShareRecordRepository.findAll(
                GoodsShareRecordWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 将实体包装成VO
     *
     * @author zhangwenchang
     */
    public GoodsShareRecordVO wrapperVo(GoodsShareRecord goodsShareRecord) {
        if (goodsShareRecord != null) {
            GoodsShareRecordVO goodsShareRecordVO = new GoodsShareRecordVO();
            KsBeanUtil.copyPropertiesThird(goodsShareRecord, goodsShareRecordVO);
            return goodsShareRecordVO;
        }
        return null;
    }
}
