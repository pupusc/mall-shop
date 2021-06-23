package com.wanmi.sbc.elastic.groupon.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreNameListByStoreIdsResquest;
import com.wanmi.sbc.customer.bean.vo.StoreNameVO;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.response.groupon.EsGrouponActivityPageResponse;
import com.wanmi.sbc.elastic.groupon.model.root.EsGrouponActivity;
import com.wanmi.sbc.elastic.groupon.repository.EsGrouponActivityRepository;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsInfoBatchByActivityIdAndGoodsIdResponse;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoByActivityIdAndGoodsIdDTO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoByActivityIdAndGoodsIdVO;
import com.wanmi.sbc.marketing.api.provider.grouponcate.GrouponCateQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponcate.GrouponCateByIdsRequest;
import com.wanmi.sbc.marketing.api.response.grouponcate.GrouponCateListResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityForManagerVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponCateVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author: HouShuai
 * @date: 2020/12/8 11:18
 * @description:
 */
@Service
public class EsGrouponActivityQueryService {

    @Autowired
    private EsGrouponActivityRepository esGrouponActivityRepository;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GrouponCateQueryProvider grouponCateQueryProvider;

    public BaseResponse<EsGrouponActivityPageResponse> page(EsGrouponActivityPageRequest request) {
        //es语法
        BoolQueryBuilder boolQuery = request.esCriteria();
        Page<EsGrouponActivity> esGrouponActivityPage = esGrouponActivityRepository.search(boolQuery, request.getPageable());
        //设置最低拼团价、店铺名称-转换vo
        Page<GrouponActivityForManagerVO> newPage = this.wraperGrouponForManagerVO(esGrouponActivityPage);
        MicroServicePage<GrouponActivityForManagerVO> microPage = new MicroServicePage<>(newPage,
                request.getPageable());
        EsGrouponActivityPageResponse finalRes = new EsGrouponActivityPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }


    private Page<GrouponActivityForManagerVO> wraperGrouponForManagerVO(Page<EsGrouponActivity> grouponActivityPage) {
        List<GrouponGoodsInfoByActivityIdAndGoodsIdDTO> list = new ArrayList<>();
        List<EsGrouponActivity> grouponActivityList = grouponActivityPage.getContent();
        if (CollectionUtils.isEmpty(grouponActivityList)) {
            return new PageImpl<>(Collections.emptyList());
        }
        grouponActivityList.forEach(grouponActivity -> {
            list.add(GrouponGoodsInfoByActivityIdAndGoodsIdDTO.builder()
                    .grouponActivityId(grouponActivity.getGrouponActivityId())
                    .goodsId(grouponActivity.getGoodsId()).build());
        });
        //拼团商品构造查询条件
        GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest request =
                GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest.builder().list(list).build();
        BaseResponse<GrouponGoodsInfoBatchByActivityIdAndGoodsIdResponse>
                grouponGoodsInfoResponse = grouponGoodsInfoQueryProvider
                .batchByActivityIdAndGoodsId(request);
        List<GrouponGoodsInfoByActivityIdAndGoodsIdVO> grouponGoodsInfos = grouponGoodsInfoResponse.getContext()
                .getList();

        //根据storeIds查询店铺信息，并塞入值
        List<Long> storeIds = grouponActivityList.stream()
                .filter(d -> d.getStoreId() != null)
                .map(v -> Long.valueOf(v.getStoreId()))
                .collect(Collectors.toList());
        List<StoreNameVO> storeNameList = storeQueryProvider.listStoreNameByStoreIds(new StoreNameListByStoreIdsResquest(storeIds)).getContext()
                .getStoreNameList();

        List<String> grouponCateIds = grouponActivityList.stream()
                .map(EsGrouponActivity::getGrouponCateId)
                .collect(Collectors.toList());

        //获取分类信息
        Map<String, String> mapResult = this.getGrouponCateMap(grouponCateIds);
        //设置最低拼团价\商家名称
        return grouponActivityPage.map(entity -> {
            GrouponActivityForManagerVO vo = this.copyBean(entity);

            this.setPrice(grouponGoodsInfos, entity, vo);

            this.setStoreName(storeNameList, entity, vo);

            if (MapUtils.isNotEmpty(mapResult)) {
                vo.setGrouponCateName(mapResult.get(vo.getGrouponCateId()));
            }
            return vo;
        });
    }

    /**
     * 查询拼团分类信息表
     *
     * @param grouponCateIds
     * @return
     */
    private Map<String, String> getGrouponCateMap(List<String> grouponCateIds) {
        if (CollectionUtils.isEmpty(grouponCateIds)) {
            return Collections.emptyMap();
        }
        GrouponCateByIdsRequest idsRequest = GrouponCateByIdsRequest.builder().grouponCateIds(grouponCateIds).build();
        BaseResponse<GrouponCateListResponse> result = grouponCateQueryProvider.getByIds(idsRequest);
        List<GrouponCateVO> grouponCateVOList = result.getContext().getGrouponCateVOList();
        return grouponCateVOList.stream().collect(Collectors.toMap(GrouponCateVO::getGrouponCateId, GrouponCateVO::getGrouponCateName));
    }

    /**
     * copyBean
     *
     * @param esGrouponActivity
     * @return
     */
    private GrouponActivityForManagerVO copyBean(EsGrouponActivity esGrouponActivity) {
        EsGrouponActivity grouponActivity = Objects.requireNonNull(esGrouponActivity);
        GrouponActivityForManagerVO forManagerVO = new GrouponActivityForManagerVO();
        BeanUtils.copyProperties(grouponActivity, forManagerVO);
        return forManagerVO;
    }


    /**
     * 设置拼团价格
     *
     * @param grouponGoodsInfos
     * @param entity
     * @param vo
     */
    private void setPrice(List<GrouponGoodsInfoByActivityIdAndGoodsIdVO> grouponGoodsInfos,
                          EsGrouponActivity entity, GrouponActivityForManagerVO vo) {
        vo.setGrouponPrice(BigDecimal.ZERO);
        if (CollectionUtils.isNotEmpty(grouponGoodsInfos)) {
            Optional<GrouponGoodsInfoByActivityIdAndGoodsIdVO> grouponGoodsInfoOptional = grouponGoodsInfos.stream()
                    .filter(g -> entity.getGrouponActivityId().equals(g.getGrouponActivityId()))
                    .filter(g -> entity.getGoodsId().equals(g.getGoodsId()))
                    .findFirst();

            grouponGoodsInfoOptional.ifPresent(grouponGoodsInfo -> {
                vo.setGrouponPrice(grouponGoodsInfo.getGrouponPrice());
                vo.setGoodsInfoId(grouponGoodsInfo.getGoosInfoId());
            });
        }
    }

    /**
     * 设置商家名称
     *
     * @param storeNameList
     * @param entity
     * @param vo
     */
    private void setStoreName(List<StoreNameVO> storeNameList, EsGrouponActivity entity, GrouponActivityForManagerVO vo) {
        if (CollectionUtils.isNotEmpty(storeNameList)) {
            Optional<StoreNameVO> storeVoOptional = storeNameList.stream()
                    .filter(g -> entity.getStoreId().equals(String.valueOf(g.getStoreId())))
                    .findFirst();

            storeVoOptional.ifPresent(storeNameVO -> vo.setSupplierName(storeNameVO.getStoreName()));
        }
    }

}
