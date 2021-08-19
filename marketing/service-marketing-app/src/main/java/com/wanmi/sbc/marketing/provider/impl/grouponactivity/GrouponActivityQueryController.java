package com.wanmi.sbc.marketing.provider.impl.grouponactivity;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreNameListByStoreIdsResquest;
import com.wanmi.sbc.customer.bean.vo.StoreNameVO;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoBatchByActivityIdAndGoodsIdRequest;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsInfoBatchByActivityIdAndGoodsIdResponse;
import com.wanmi.sbc.goods.bean.dto.GrouponGoodsInfoByActivityIdAndGoodsIdDTO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoByActivityIdAndGoodsIdVO;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.response.grouponactivity.*;
import com.wanmi.sbc.marketing.bean.enums.AuditStatus;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityForManagerVO;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityService;
import com.wanmi.sbc.marketing.grouponcate.model.root.GrouponCate;
import com.wanmi.sbc.marketing.grouponcate.service.GrouponCateService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * <p>拼团活动信息表查询服务接口实现</p>
 *
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@RestController
@Validated
public class GrouponActivityQueryController implements GrouponActivityQueryProvider {
    @Autowired
    private GrouponActivityService grouponActivityService;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;


    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GrouponCateService grouponCateService;

    @Override
    public BaseResponse<GrouponActivityListSpuIdResponse> listActivityingSpuIds(
            @RequestBody @Valid GrouponActivityListSpuIdRequest request) {
        List<String> result = grouponActivityService.listActivityingSpuIds(
                request.getGoodsIds(), request.getStartTime(), request.getEndTime());
        return BaseResponse.success(new GrouponActivityListSpuIdResponse(result));
    }

    @Override
    public BaseResponse<GrouponActivityPage4MangerResponse> page4Manager(@RequestBody @Valid
                                                                                 GrouponActivityPageRequest
                                                                                 grouponActivityPageReq) {
        GrouponActivityQueryRequest queryReq = new GrouponActivityQueryRequest();
        KsBeanUtil.copyPropertiesThird(grouponActivityPageReq, queryReq);
        Page<GrouponActivity> grouponActivityPage = grouponActivityService.page(queryReq);
        //设置最低拼团价、店铺名称-转换vo
        Page<GrouponActivityForManagerVO> newPage = wraperGrouponForManagerVO(grouponActivityPage);
        MicroServicePage<GrouponActivityForManagerVO> microPage = new MicroServicePage<>(newPage,
                grouponActivityPageReq.getPageable());
        GrouponActivityPage4MangerResponse finalRes = new GrouponActivityPage4MangerResponse(microPage);
        return BaseResponse.success(finalRes);
    }


    @Override
    public BaseResponse<GrouponActivityPageResponse> page(@RequestBody @Valid GrouponActivityPageRequest
                                                                  grouponActivityPageReq) {
        GrouponActivityQueryRequest queryReq = new GrouponActivityQueryRequest();
        KsBeanUtil.copyPropertiesThird(grouponActivityPageReq, queryReq);
        Page<GrouponActivity> grouponActivityPage = grouponActivityService.page(queryReq);
        Page<GrouponActivityVO> newPage = grouponActivityPage.map(entity -> grouponActivityService.wrapperVo
                (entity));
        MicroServicePage<GrouponActivityVO> microPage = new MicroServicePage<>(newPage, grouponActivityPageReq
                .getPageable());
        GrouponActivityPageResponse finalRes = new GrouponActivityPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<GrouponActivityListResponse> list(@RequestBody @Valid GrouponActivityListRequest
                                                                  grouponActivityListReq) {
        GrouponActivityQueryRequest queryReq = new GrouponActivityQueryRequest();
        KsBeanUtil.copyPropertiesThird(grouponActivityListReq, queryReq);
        List<GrouponActivity> grouponActivityList = grouponActivityService.list(queryReq);
        List<GrouponActivityVO> newList = grouponActivityList.stream().map(entity -> grouponActivityService
                .wrapperVo
                        (entity)).collect(Collectors.toList());
        return BaseResponse.success(new GrouponActivityListResponse(newList));
    }

    @Override
    public BaseResponse<GrouponActivityByIdResponse> getById(@RequestBody @Valid GrouponActivityByIdRequest request) {

        // 1.查询拼团活动信息
        GrouponActivity grouponActivity = grouponActivityService.getById(request.getGrouponActivityId());
        GrouponActivityByIdResponse response = new GrouponActivityByIdResponse(
                grouponActivityService.wrapperVo(grouponActivity), null);

        // 2.查询店铺名称
        List<StoreNameVO> storeNames = storeQueryProvider.listStoreNameByStoreIds(
                new StoreNameListByStoreIdsResquest(Arrays.asList(Long.parseLong(grouponActivity.getStoreId())))
        ).getContext().getStoreNameList();
        if (CollectionUtils.isNotEmpty(storeNames)) {
            response.getGrouponActivity().setStoreName(storeNames.get(0).getStoreName());
        }

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GrouponActivityFreeDeliveryByIdResponse> getFreeDeliveryById(
            @RequestBody @Valid GrouponActivityFreeDeliveryByIdRequest request) {
        boolean result =  grouponActivityService.getFreeDeliveryById(request.getGrouponActivityId());
        return BaseResponse.success(new GrouponActivityFreeDeliveryByIdResponse(result));
    }

    @Override
    public BaseResponse<GrouponActivityingByGoodsInfoIdsResponse> listActivityingByGoodsInfoIds(
            @RequestBody @Valid GrouponActivityingByGoodsInfoIdsRequest request) {
        Map<String, GrouponGoodsInfoVO> map = grouponActivityService.listActivityingWithGoodsInfo(request.getGoodsInfoIds());
        return BaseResponse.success(new GrouponActivityingByGoodsInfoIdsResponse(map));
    }

    /**
     * 团活动设置拼团价
     *
     * @param grouponActivityPage
     * @return
     */
    private Page<GrouponActivityForManagerVO> wraperGrouponForManagerVO(Page<GrouponActivity> grouponActivityPage) {
        List<GrouponGoodsInfoByActivityIdAndGoodsIdDTO> list = new ArrayList<>();
        List<GrouponActivity> grouponActivityList = grouponActivityPage.getContent();
        if (CollectionUtils.isEmpty(grouponActivityList)){
            return new PageImpl<>(Collections.EMPTY_LIST);
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
        List<Long> storeIds = grouponActivityList.stream().filter(d -> d.getStoreId() != null).map(v -> Long.valueOf
                (v.getStoreId())).collect(toList());
        List<StoreNameVO> storeVOS = storeQueryProvider.listStoreNameByStoreIds(new StoreNameListByStoreIdsResquest(storeIds)).getContext()
                .getStoreNameList();

        List<String> grouponCateIds = grouponActivityList.stream().map(GrouponActivity::getGrouponCateId).collect(Collectors.toList());
        Map<String,String> map = new HashMap<>(grouponCateIds.size());
        if (CollectionUtils.isNotEmpty(grouponCateIds)){
            List<GrouponCate> grouponCateList = grouponCateService.findByGrouponCateIdIn(grouponCateIds);
            map = grouponCateList.stream().collect(Collectors.toMap(GrouponCate::getGrouponCateId,GrouponCate::getGrouponCateName));
        }
        final Map<String,String> mapResult = map;
        //设置最低拼团价\商家名称
        Page<GrouponActivityForManagerVO> newPage = grouponActivityPage.map(entity -> {
                    GrouponActivityForManagerVO vo = grouponActivityService.wrapperMangerVo(entity);
                    vo.setGrouponPrice(BigDecimal.ZERO);
                    if (CollectionUtils.isNotEmpty(grouponGoodsInfos)) {
                        Optional<GrouponGoodsInfoByActivityIdAndGoodsIdVO> grouponGoodsInfoOptional = grouponGoodsInfos
                                .stream().filter(g ->
                                        entity.getGrouponActivityId().equals(g.getGrouponActivityId()))
                                .filter(g ->
                                        entity.getGoodsId().equals(g.getGoodsId())).findFirst();

                        if (grouponGoodsInfoOptional.isPresent()) {
                            vo.setGrouponPrice(grouponGoodsInfoOptional.get().getGrouponPrice());
                            vo.setGoodsInfoId(grouponGoodsInfoOptional.get().getGoosInfoId());
                        }
                    }

                    if (CollectionUtils.isNotEmpty(storeVOS)) {
                        Optional<StoreNameVO> storeVoOptional = storeVOS
                                .stream().filter(g ->
                                        entity.getStoreId().equals(String.valueOf(g.getStoreId()))).findFirst();

                        if (storeVoOptional.isPresent()) {
                            vo.setSupplierName(storeVoOptional.get().getStoreName());
                        }
                    }

                    if (MapUtils.isNotEmpty(mapResult)){
                        vo.setGrouponCateName(mapResult.get(vo.getGrouponCateId()));
                    }
                    return vo;
                }
        );
        return newPage;
    }


    @Override
    public BaseResponse<GrouponActivityByUniqueStoreIdResponse> querySupplierNum(@RequestBody @Valid GrouponActivityListRequest
                                                                  grouponActivityListReq) {
        int supplierNum = grouponActivityService.querySupplierNum(AuditStatus.CHECKED);
        GrouponActivityByUniqueStoreIdResponse response = new GrouponActivityByUniqueStoreIdResponse();
        response.setSupplierNum(supplierNum);
        return BaseResponse.success(response);
    }
}

