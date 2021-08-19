package com.wanmi.sbc.goods.goodscatethirdcaterel.service;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelAddResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.cate.model.root.ContractCate;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.ContractCateRepository;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.ContractCateService;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.goods.goodscatethirdcaterel.repository.GoodsCateThirdCateRelRepository;
import com.wanmi.sbc.goods.goodscatethirdcaterel.model.root.GoodsCateThirdCateRel;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsCateThirdCateRelVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>平台类目和第三方平台类目映射业务逻辑</p>
 *
 * @author
 * @date 2020-08-18 19:51:55
 */
@Service
public class GoodsCateThirdCateRelService {
    @Autowired
    private GoodsCateThirdCateRelRepository goodsCateThirdCateRelRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private ContractCateRepository contractCateRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    /**
     * 新增平台类目和第三方平台类目映射
     *
     * @author
     */
    @Transactional
    public GoodsCateThirdCateRel add(GoodsCateThirdCateRel entity) {
        goodsCateThirdCateRelRepository.save(entity);
        return entity;
    }

    /**
     * 修改平台类目和第三方平台类目映射
     *
     * @author
     */
    @Transactional
    public GoodsCateThirdCateRel modify(GoodsCateThirdCateRel entity) {
        goodsCateThirdCateRelRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除平台类目和第三方平台类目映射
     *
     * @author
     */
    @Transactional
    public void deleteById(GoodsCateThirdCateRel entity) {
        goodsCateThirdCateRelRepository.save(entity);
    }

    /**
     * 批量删除平台类目和第三方平台类目映射
     *
     * @author
     */
    @Transactional
    public void deleteByIdList(List<GoodsCateThirdCateRel> infos) {
        goodsCateThirdCateRelRepository.saveAll(infos);
    }

    /**
     * 单个查询平台类目和第三方平台类目映射
     *
     * @author
     */
    public GoodsCateThirdCateRel getOne(Long id) {
        return goodsCateThirdCateRelRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "平台类目和第三方平台类目映射不存在"));
    }

    /**
     * 分页查询平台类目和第三方平台类目映射
     *
     * @author
     */
    public Page<GoodsCateThirdCateRel> page(GoodsCateThirdCateRelQueryRequest queryReq) {
        return goodsCateThirdCateRelRepository.findAll(
                GoodsCateThirdCateRelWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询平台类目和第三方平台类目映射
     *
     * @author
     */
    public List<GoodsCateThirdCateRel> list(GoodsCateThirdCateRelQueryRequest queryReq) {
        return goodsCateThirdCateRelRepository.findAll(GoodsCateThirdCateRelWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author
     */
    public GoodsCateThirdCateRelVO wrapperVo(GoodsCateThirdCateRel goodsCateThirdCateRel) {
        if (goodsCateThirdCateRel != null) {
            GoodsCateThirdCateRelVO goodsCateThirdCateRelVO = KsBeanUtil.convert(goodsCateThirdCateRel, GoodsCateThirdCateRelVO.class);
            return goodsCateThirdCateRelVO;
        }
        return null;
    }

    /**
     * 批量新增linkedmall类目映射
     *
     * @param goodsCateThirdCateRels
     */
    @Transactional
    public GoodsCateThirdCateRelAddResponse addBatch(List<GoodsCateThirdCateRel> goodsCateThirdCateRels) {
        ArrayList<String> updateEsGoodsIds = new ArrayList<>();
        ArrayList<String> delEsGoodsIds = new ArrayList<>();
        for (GoodsCateThirdCateRel goodsCateThirdCateRel : goodsCateThirdCateRels) {
            Long thirdCateId = goodsCateThirdCateRel.getThirdCateId();
            Long cateId = goodsCateThirdCateRel.getCateId();
            GoodsCate goodsCate = goodsCateService.findById(goodsCateThirdCateRel.getCateId());
            if (goodsCate.getCateGrade() != 3) {
                throw new SbcRuntimeException("K-030108");
            }
            goodsCateThirdCateRel.setDelFlag(DeleteFlag.NO);
            goodsCateThirdCateRel.setCreateTime(LocalDateTime.now());
            goodsCateThirdCateRel.setUpdateTime(LocalDateTime.now());
			//重新维护商品的平台类目和三方类目的映射关系
            goodsRepository.updateThirdCateMap(GoodsSource.LINKED_MALL.toValue(), thirdCateId, cateId);
            goodsInfoRepository.updateThirdCateMap(GoodsSource.LINKED_MALL.toValue(), thirdCateId, cateId);
//			重新维护商家商品的平台类目和三方类目的映射关系
//			List<Goods> goodsList = goodsRepository.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdCateId(thirdCateId).build().getWhereCriteria());
//			if (goodsList!=null&&goodsList.size()>0) {
//				List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdCateId(thirdCateId).build().getWhereCriteria());
//				List<Long> storeIds = goodsList.stream().map(v -> v.getStoreId()).distinct().collect(Collectors.toList());
//				List<ContractCate> contractCateList = contractCateRepository.findByStoreIds(storeIds);
//				if (contractCateList!=null&&contractCateList.size()>0) {
//					Map<Long, List<ContractCate>> map = contractCateList.stream().collect(Collectors.groupingBy(v -> v.getStoreId()));
//					for (Goods goods : goodsList) {
//						List<ContractCate> contractCates = map.get(goods.getStoreId());
//						Optional<ContractCate> optional = contractCates.stream().filter(v -> v.getGoodsCate().getCateId().equals(cateId)).findFirst();
//						if (optional.isPresent()) {
//							goods.setCateId(cateId);
//							updateEsGoodsIds.add(goods.getGoodsId());
//						}else {
//							goods.setDelFlag(DeleteFlag.YES);
//							delEsGoodsIds.add(goods.getGoodsId());
//							standardGoodsRelRepository.delByStoreIdAndGoodsId(goods.getStoreId(),goods.getGoodsId());
//						}
//					}
//					goodsRepository.saveAll(goodsList);
//					for (GoodsInfo goodsInfo : goodsInfos) {
//						List<ContractCate> contractCates = map.get(goodsInfo.getStoreId());
//						Optional<ContractCate> optional = contractCates.stream().filter(v -> v.getGoodsCate().getCateId().equals(cateId)).findFirst();
//						if (optional.isPresent()) {
//							goodsInfo.setCateId(cateId);
//						}else {
//							goodsInfo.setDelFlag(DeleteFlag.YES);
//						}
//					}
//					goodsInfoRepository.saveAll(goodsInfos);
//				}
//			}

            //重新维护商品库的平台类目和三方类目的映射关系
            standardGoodsRepository.updateThirdCateMap(GoodsSource.LINKED_MALL.toValue(), thirdCateId, cateId);
        }
        goodsCateThirdCateRelRepository.deleteInThirdCateIds(goodsCateThirdCateRels.stream().map(v -> v.getThirdCateId()).collect(Collectors.toList()), goodsCateThirdCateRels.get(0).getThirdPlatformType());
        goodsCateThirdCateRelRepository.saveAll(goodsCateThirdCateRels);
        return new GoodsCateThirdCateRelAddResponse(updateEsGoodsIds, delEsGoodsIds);
    }
}

