package com.wanmi.sbc.elastic.coupon.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoInitRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoPageRequest;
import com.wanmi.sbc.elastic.bean.constant.coupon.CouponInfoErrorCode;
import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponInfoDTO;
import com.wanmi.sbc.elastic.bean.vo.coupon.EsCouponInfoVO;
import com.wanmi.sbc.elastic.coupon.mapper.EsCouponInfoMapper;
import com.wanmi.sbc.elastic.coupon.model.root.EsCouponInfo;
import com.wanmi.sbc.elastic.coupon.repository.EsCouponInfoRepository;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListCouponDetailRequest;
import com.wanmi.sbc.goods.bean.dto.CouponInfoForScopeNamesDTO;
import com.wanmi.sbc.goods.bean.vo.CouponInfoForScopeNamesVO;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCateRelaQueryProvider;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateRelaListByCouponIdsRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoListByPageRequest;
import com.wanmi.sbc.marketing.bean.vo.CouponCateRelaVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 优惠券Service
 */
@Slf4j
@Service
public class EsCouponInfoService {

    @Autowired
    private EsCouponInfoRepository esCouponInfoRepository;

    @Autowired
    private EsCouponInfoMapper esCouponInfoMapper;

    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;

    @Autowired
    private CouponCateRelaQueryProvider couponCateRelaQueryProvider;

    @Autowired
    private CouponInfoQueryProvider couponInfoQueryProvider;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 初始化ES数据
     */
    public void init(EsCouponInfoInitRequest esCouponInfoInitRequest){
        Boolean initCouponInfo = Boolean.TRUE;
        int pageNum = esCouponInfoInitRequest.getPageNum();
        Integer pageSize = Objects.equals(10,esCouponInfoInitRequest.getPageSize().intValue()) ? 2000 : esCouponInfoInitRequest.getPageSize();
        CouponInfoListByPageRequest request = KsBeanUtil.convert(esCouponInfoInitRequest,CouponInfoListByPageRequest.class);
        try {
            while (initCouponInfo) {
                request.putSort("createTime", SortType.DESC.toValue());
                request.setPageNum(pageNum);
                request.setPageSize(pageSize);
                List<CouponInfoVO> couponInfos = couponInfoQueryProvider.listByPage(request).getContext().getCouponInfos();
                if (CollectionUtils.isEmpty(couponInfos)){
                    initCouponInfo = Boolean.FALSE;
                    log.info("==========ES初始化优惠券结束，结束pageNum:{}==============",pageNum);
                }else {
                    List<EsCouponInfo> esCouponInfoDTOList = esCouponInfoMapper.couponInfoToEsCouponInfo(couponInfos);
                    this.saveAll(esCouponInfoDTOList);
                    log.info("==========ES初始化优惠券成功，当前pageNum:{}==============",pageNum);
                    pageNum++;
                }
            }
        }catch (Exception e){
            log.info("==========ES初始化优惠券异常，异常pageNum:{}==============",pageNum);
            throw new SbcRuntimeException(CouponInfoErrorCode.INIT_COUPON_INFO_FAIL,new Object[]{pageNum});
        }

    }

    /**
     * 保存优惠券ES数据
     * @param esCouponInfoList
     * @return
     */
    public Iterable<EsCouponInfo> saveAll(List<EsCouponInfo> esCouponInfoList){
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsCouponInfo.class)){
            elasticsearchTemplate.createIndex(EsCouponInfo.class);
            elasticsearchTemplate.putMapping(EsCouponInfo.class);
        }
        return esCouponInfoRepository.saveAll(esCouponInfoList);
    }

    /**
     * 保存优惠券ES数据
     * @param esCouponInfoDTO
     * @return
     */
    public EsCouponInfo save(EsCouponInfoDTO esCouponInfoDTO){
        EsCouponInfo esCouponInfo = esCouponInfoMapper.couponInfoToEsCouponInfo(esCouponInfoDTO);
        //手动删除索引时，重新设置mapping
        if(!elasticsearchTemplate.indexExists(EsCouponInfo.class)){
            elasticsearchTemplate.createIndex(EsCouponInfo.class);
            elasticsearchTemplate.putMapping(EsCouponInfo.class);
        }
        return esCouponInfoRepository.save(esCouponInfo);
    }

    /**
     * 根据优惠券ID删除对应ES数据
     * @param couponId
     */
    public void deleteById(String couponId){
        esCouponInfoRepository.deleteById(couponId);
    }

    /**
     * 分页查询ES优惠券信息
     * @param request
     * @return
     */
    public Page<EsCouponInfo> page(EsCouponInfoPageRequest request){
        return esCouponInfoRepository.search(request.getSearchQuery());
    }

    /**
     * 包装分类名称和限制范围
     * @param esCouponInfoList
     */
    public void wrapperScopeNamesAndCateNames(List<EsCouponInfoVO> esCouponInfoList){
        if (CollectionUtils.isEmpty(esCouponInfoList)){
            return;
        }
        Map<String,List<String>> cateIdsMap = esCouponInfoList.stream().collect(Collectors.toMap(EsCouponInfoVO::getCouponId,EsCouponInfoVO::getCateIds));
        List<CouponCateRelaVO> cateRelaVOList = couponCateRelaQueryProvider.listByCateIdsMap(new CouponCateRelaListByCouponIdsRequest(cateIdsMap)).getContext().getCateRelaVOList();
        Map<String,CouponCateRelaVO> cateNamesMap =  cateRelaVOList.stream().collect(Collectors.toMap(CouponCateRelaVO::getCouponId, Function.identity()));
        Map<String,List<String>> scopeIdsMap = esCouponInfoList.stream().collect(Collectors.toMap(EsCouponInfoVO::getCouponId,EsCouponInfoVO::getScopeIds));

        List<CouponInfoForScopeNamesDTO> dtoList =  esCouponInfoList.stream().map(couponInfo -> {
            CouponInfoForScopeNamesDTO couponInfoForScopeNamesDTO = new CouponInfoForScopeNamesDTO();
            couponInfoForScopeNamesDTO.setCouponId(couponInfo.getCouponId());
            couponInfoForScopeNamesDTO.setCouponType(com.wanmi.sbc.goods.bean.enums.CouponType.fromValue(couponInfo.getCouponType().toValue()));
            couponInfoForScopeNamesDTO.setScopeType(com.wanmi.sbc.goods.bean.enums.ScopeType.fromValue(couponInfo.getScopeType().toValue()));
            couponInfoForScopeNamesDTO.setPlatformFlag(couponInfo.getPlatformFlag());
            couponInfoForScopeNamesDTO.setStoreId(couponInfo.getStoreId());
            return couponInfoForScopeNamesDTO;
        }).collect(Collectors.toList());

        List<CouponInfoForScopeNamesVO> couponInfoForScopeNamesVOS = goodsCateQueryProvider.couponDetail(new GoodsCateListCouponDetailRequest(dtoList,null,scopeIdsMap)).getContext().getVoList();
        Map<String,List<String>> scopeNamesMap = couponInfoForScopeNamesVOS.stream().collect(Collectors.toMap(CouponInfoForScopeNamesVO::getCouponId,CouponInfoForScopeNamesVO::getScopeNames));

        esCouponInfoList.stream().forEach(esCouponInfo -> {
            CouponCateRelaVO couponCateRelaVO = cateNamesMap.get(esCouponInfo.getCouponId());
            esCouponInfo.setCateNames(Objects.nonNull(couponCateRelaVO) ? couponCateRelaVO.getCouponCateName() : Lists.newArrayList());
            esCouponInfo.setIsFree(Objects.nonNull(couponCateRelaVO) ? couponCateRelaVO.getIsFree() : null);
            esCouponInfo.setScopeNames(scopeNamesMap.get(esCouponInfo.getCouponId()));
        });
    }

}
