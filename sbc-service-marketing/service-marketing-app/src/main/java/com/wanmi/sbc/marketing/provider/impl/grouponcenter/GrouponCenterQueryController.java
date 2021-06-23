package com.wanmi.sbc.marketing.provider.impl.grouponcenter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsPageResponse;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import com.wanmi.sbc.marketing.api.provider.grouponcenter.GrouponCenterQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.*;
import com.wanmi.sbc.marketing.api.request.grouponcenter.GrouponCenterListRequest;
import com.wanmi.sbc.marketing.api.response.grouponcenter.GrouponCenterListResponse;
import com.wanmi.sbc.marketing.bean.vo.GrouponCenterVO;
import com.wanmi.sbc.marketing.grouponactivity.model.root.GrouponActivity;
import com.wanmi.sbc.marketing.grouponactivity.service.GrouponActivityService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>H5-拼团活动首页列表查询接口实现</p>
 *
 * @author chenli
 * @date 2019-05-21 14:02:38
 */
@RestController
@Validated
public class GrouponCenterQueryController implements GrouponCenterQueryProvider {

    @Autowired
    private GrouponActivityService grouponActivityService;

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Override
    public BaseResponse<GrouponCenterListResponse> list(@RequestBody @Valid GrouponCenterListRequest listRequest) {
        // 返回对象
        GrouponCenterListResponse listResponse = new GrouponCenterListResponse();

        GrouponGoodsPageRequest grouponGoodsPageRequest  =KsBeanUtil.convert(listRequest, GrouponGoodsPageRequest
                .class);

        // 1.1 分页查询拼团商品信息
        BaseResponse<GrouponGoodsPageResponse> grouponGoodsPageResponseBaseResponse = grouponGoodsInfoQueryProvider.
                pageGrouponGoods(grouponGoodsPageRequest);
        if (Objects.nonNull(grouponGoodsPageResponseBaseResponse.getContext())) {
            MicroServicePage<GrouponGoodsVO> grouponGoodsVOS = grouponGoodsPageResponseBaseResponse.getContext().getGrouponGoodsVOS();

            listResponse.setGrouponCenterVOList(new MicroServicePage<>(Collections.emptyList(), listRequest.getPageable(),
                    grouponGoodsVOS.getTotalElements()));

            if (CollectionUtils.isNotEmpty(grouponGoodsVOS.getContent())) {

                List<GrouponGoodsVO> grouponGoodsVOList = grouponGoodsVOS.getContent();

                // 1.2 将拼团商品vo包装成拼团中心需要的数据VO
                List<GrouponCenterVO> newList = grouponGoodsVOList.stream().map(entity -> grouponActivityService
                        .wrapperGrouponCenterVo(entity)).collect(Collectors.toList());

                // 2.1 聚合出正在进行的活动的spuIds
                List<String> spuIds = grouponGoodsVOList.stream().map(GrouponGoodsVO::getGrouponActivityId).collect(Collectors.toList());

                // 封装查询拼团活动参数
                GrouponActivityQueryRequest queryReq = new GrouponActivityQueryRequest();
                queryReq.setGrouponActivityIdList(spuIds);
                // 2.2 查询拼团活动信息
                List<GrouponActivity> grouponActivities = grouponActivityService.list(queryReq);

                // 2.3 对两个集合压缩，返回新的对象的集合
                IteratorUtils.zip(newList, grouponActivities,
                        (collect1, activity1) -> collect1.getGrouponActivityId().equals(activity1.getGrouponActivityId()),
                        (collect2, activity2) -> {
                            collect2.setGrouponNum(activity2.getGrouponNum());
                        }
                );

                // 3、组装返回对象
                Page<GrouponCenterVO> grouponCenterVOPage = new PageImpl<>(newList, listRequest.getPageable(),
                        grouponGoodsVOS.getTotalElements());

                MicroServicePage<GrouponCenterVO> grouponCenterVOList = new MicroServicePage<>(grouponCenterVOPage, listRequest
                        .getPageable());
                listResponse.setGrouponCenterVOList(grouponCenterVOList);
            }
        }

        return BaseResponse.success(listResponse);
    }
}

