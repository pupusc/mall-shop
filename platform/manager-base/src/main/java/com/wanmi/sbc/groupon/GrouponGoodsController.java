package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.groupongoodsinfo.GrouponGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoSimplePageRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.groupongoodsinfo.GrouponGoodsInfoSimplePageResponse;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsVO;
import com.wanmi.sbc.marketing.api.provider.grouponactivity.GrouponActivityQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponactivity.GrouponActivityListRequest;
import com.wanmi.sbc.marketing.bean.vo.GrouponActivityVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * S2B的拼团活动服务
 */
@RestController
@RequestMapping("/groupon/goods")
@Api(description = "S2B的拼团商品服务", tags = "GrouponGoodsController")
public class GrouponGoodsController {

    @Autowired
    private GrouponGoodsInfoQueryProvider grouponGoodsInfoQueryProvider;

    @Autowired
    private GrouponActivityQueryProvider grouponActivityQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 分页查询拼团活动
     *
     * @param pageRequest 商品 {@link DistributionGoodsPageRequest}
     * @return 拼团活动分页
     */
    @ApiOperation(value = "分页查询拼团商品")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<GrouponGoodsInfoSimplePageResponse> page(@RequestBody @Valid GrouponGoodsInfoSimplePageRequest
                                                                         pageRequest) {
        Long storeId = commonUtil.getStoreId();
        if(Objects.isNull(storeId)){
            storeId = pageRequest.getStoreId();
        }
        pageRequest.setStoreId(storeId);
        pageRequest.setHavSpecTextFlag(Boolean.TRUE);
        GrouponGoodsInfoSimplePageResponse response = grouponGoodsInfoQueryProvider.pageGrouponGoodsInfo(pageRequest).getContext();
        if(CollectionUtils.isNotEmpty(response.getGrouponGoodsVOS().getContent())) {
            List<String> grouponActivityId = response.getGrouponGoodsVOS().getContent().stream()
                    .map(GrouponGoodsVO::getGrouponActivityId).collect(Collectors.toList());
            List<GrouponActivityVO> activityVOList = grouponActivityQueryProvider.list(
                    GrouponActivityListRequest.builder().grouponActivityIdList(grouponActivityId).build())
                    .getContext().getGrouponActivityVOList();
            if(CollectionUtils.isNotEmpty(activityVOList)) {
                Map<String, Integer> activityMap = activityVOList.stream()
                        .collect(Collectors.toMap(GrouponActivityVO::getGrouponActivityId, GrouponActivityVO::getGrouponNum));
                response.getGrouponGoodsVOS().getContent().forEach(g -> {
                    g.setGrouponNum(activityMap.getOrDefault(g.getGrouponActivityId(), 2));
                });
            }
        }
        return BaseResponse.success(response);
    }
}
