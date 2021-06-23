package com.wanmi.sbc.goods.distributor.goods.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoAddRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoModifySequenceRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoVerifyRequest;
import com.wanmi.sbc.goods.bean.dto.DistributorGoodsInfoModifySequenceDTO;
import com.wanmi.sbc.goods.distributor.goods.model.root.DistributorGoodsInfo;
import com.wanmi.sbc.goods.distributor.goods.repository.DistributiorGoodsInfoRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分销员商品-服务层
 *
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:13
 * @version: 1.0
 */
@Service
public class DistributorGoodsInfoService {

    @Autowired
    private DistributiorGoodsInfoRepository distributiorGoodsInfoRepository;

    /**
     * 根据分销员-会员ID查询分销员商品列表
     *
     * @param customerId
     * @return
     */
    public List<DistributorGoodsInfo> findByCustomerId(String customerId) {
        return distributiorGoodsInfoRepository.findByCustomerIdOrderBySequence(customerId);
    }

    public List<DistributorGoodsInfo> findByCustomerIdAndStoreId(String customerId, Long storeId) {
        return distributiorGoodsInfoRepository.findByCustomerIdAndStoreIdOrderBySequence(customerId, storeId);
    }


    /**
     * 根据分销员-会员ID和SPU编号查询分销员商品列表
     *
     * @param customerId 会员ID
     * @param goodsId    SPU编号
     * @return
     */
    public List<DistributorGoodsInfo> findByCustomerIdAndGoodsId(String customerId, String goodsId) {
        return distributiorGoodsInfoRepository.findByCustomerIdAndGoodsId(customerId, goodsId);
    }

    /**
     * 根据分销员，过滤非精选的单品
     */
    public List<String> verifyDistributorGoodsInfo(DistributorGoodsInfoVerifyRequest request) {
        // 小店下所有精选单品
        List<String> validIds =
                distributiorGoodsInfoRepository.findByCustomerIdOrderBySequence(request.getDistributorId())
                .stream().map(DistributorGoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        // 过滤非精选的单品
        return request.getGoodsInfoIds().stream().filter(
                goodsInfoId -> !validIds.contains(goodsInfoId)).collect(Collectors.toList());
    }


    /**
     * 新增分销员商品
     *
     * @param distributorGoodsInfoAddRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public DistributorGoodsInfo add(DistributorGoodsInfoAddRequest distributorGoodsInfoAddRequest) {
        DistributorGoodsInfo distributorGoodsInfo =
                findByCustomerIdAndGoodsInfoId(distributorGoodsInfoAddRequest.getCustomerId(),
                        distributorGoodsInfoAddRequest.getGoodsInfoId());
        if (Objects.nonNull(distributorGoodsInfo)) {
            return distributorGoodsInfo;
        }
        Integer sequence = findMaxSequenceByCustomerId(distributorGoodsInfoAddRequest.getCustomerId());
        distributorGoodsInfo = new DistributorGoodsInfo();
        KsBeanUtil.copyPropertiesThird(distributorGoodsInfoAddRequest, distributorGoodsInfo);
        distributorGoodsInfo.setSequence(sequence == null ? 0 : sequence + 1);
        distributorGoodsInfo.setStatus(NumberUtils.INTEGER_ZERO);
        distributorGoodsInfo.setCreateTime(LocalDateTime.now());
        distributorGoodsInfo.setUpdateTime(LocalDateTime.now());
        return distributiorGoodsInfoRepository.save(distributorGoodsInfo);
    }

    /**
     * 根据分销员-会员ID获取管理的分销商品排序最大值
     *
     * @param customerId
     * @return
     */
    public Integer findMaxSequenceByCustomerId(String customerId) {
        return distributiorGoodsInfoRepository.findMaxSequenceByCustomerId(customerId);
    }

    /**
     * 根据分销员-会员ID和SkuId删除分销员商品信息
     *
     * @param customerId
     * @param goodsInfoId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByCustomerIdAndGoodsInfoId(String customerId, String goodsInfoId) {
        return distributiorGoodsInfoRepository.deleteByCustomerIdAndGoodsInfoId(customerId, goodsInfoId);
    }

    /**
     * 修改分销员-分销商品排序
     *
     * @param request
     * @return true : 成功，false:失败
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifySequence(DistributorGoodsInfoModifySequenceRequest request) {
        List<DistributorGoodsInfoModifySequenceDTO> distributorGoodsInfoDTOList =
                request.getDistributorGoodsInfoDTOList();
        List<DistributorGoodsInfo> distributorGoodsInfoList = KsBeanUtil.convert(distributorGoodsInfoDTOList,
                DistributorGoodsInfo.class);
        distributorGoodsInfoList = distributiorGoodsInfoRepository.saveAll(distributorGoodsInfoList);
        return distributorGoodsInfoDTOList.size() == distributorGoodsInfoList.size();
    }

    /**
     * 根据分销员-会员ID查询分销员商品列表(分页接口)
     *
     * @param customerId
     * @return
     */
    public Page<DistributorGoodsInfo> findByCustomerIdAndStatusOrderBySequence(String customerId, Integer status,
                                                                               Pageable pageable) {
        return distributiorGoodsInfoRepository.findByCustomerIdAndStatusOrderBySequence(customerId, status, pageable);
    }

    /**
     * 根据分销员-会员ID和skuID查询分销员商品信息
     *
     * @param customerId
     * @param goodsInfoId
     * @return
     */
    public DistributorGoodsInfo findByCustomerIdAndGoodsInfoId(String customerId, String goodsInfoId) {
        return distributiorGoodsInfoRepository.findByCustomerIdAndGoodsInfoId(customerId, goodsInfoId);
    }


    /**
     * 商家-社交分销开关，更新对应的分销员商品状态
     *
     * @param storeId 店铺ID
     * @param status
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int modifyByStoreIdAndStatus(Long storeId, Integer status) {
        return distributiorGoodsInfoRepository.modifyByStoreIdAndStatus(storeId, status, LocalDateTime.now());
    }

    /**
     * 根据SkuId删除分销员商品信息
     *
     * @param goodsInfoId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByGoodsInfoId(String goodsInfoId) {
        return distributiorGoodsInfoRepository.deleteByGoodsInfoId(goodsInfoId);
    }

    /**
     * 根据SpuId删除分销员商品信息
     *
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByGoodsId(String goodsId) {
        return distributiorGoodsInfoRepository.deleteByGoodsId(goodsId);
    }

    /**
     * 根据店铺ID集合删除分销员商品表数据
     *
     * @param storeId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByStoreId(Long storeId) {
        return distributiorGoodsInfoRepository.deleteByStoreId(storeId);
    }

    /**
     * 根据店铺ID集合批量删除分销员商品表数据
     *
     * @param storeIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteByStoreIdsIn(List<Long> storeIds) {
        return distributiorGoodsInfoRepository.deleteByStoreIdsIn(storeIds);
    }

    /**
     * 查询分销员商品表-店铺ID集合数据
     *
     * @return
     */
    public List<Long> findAllStoreId() {
        return distributiorGoodsInfoRepository.findAllStoreId();
    }


    /**
     * 根据会员id查询这个店铺下的分销商品数
     *
     * @param customerId
     * @return
     */
    public Long getCountsByCustomerId(String customerId) {
        return distributiorGoodsInfoRepository.getCountsByCustomerId(customerId);
    }

    /**
     * 销量排序。前10分销商品
     * @return
     */
    public List<String> salesNumDescLimit10(){
        return distributiorGoodsInfoRepository.salesNumDescLimit10();
    }
}
