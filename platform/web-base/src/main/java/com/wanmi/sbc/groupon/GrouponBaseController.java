package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import com.wanmi.sbc.marketing.bean.enums.GrouponOrderStatus;
import com.wanmi.sbc.order.api.provider.groupon.GrouponProvider;
import com.wanmi.sbc.order.api.provider.trade.GrouponInstanceQueryProvider;
import com.wanmi.sbc.order.api.request.groupon.GrouponOrderValidRequest;
import com.wanmi.sbc.order.api.request.trade.GrouponInstancePageRequest;
import com.wanmi.sbc.order.api.response.trade.GrouponInstancePageWithCustomerInfoResponse;
import com.wanmi.sbc.order.api.response.trade.GrouponInstanceByActivityIdResponse;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceWithCustomerInfoVO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.setting.api.provider.WechatAuthProvider;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 拼团Controller
 */
@RestController
@RequestMapping("/groupon")
@Api(tags = "GrouponBaseController", description = "S2B web公用-拼团营销")
public class GrouponBaseController {

    @Autowired
    private GrouponInstanceQueryProvider grouponInstanceQueryProvider;

    @Autowired
    private GrouponProvider grouponProvider;

    @Autowired
    private WechatAuthProvider wechatAuthProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;

    /**
     * 分页查询进行中团实例
     */
    @ApiOperation(value = "分页查询进行中团实例")
    @RequestMapping(value = "/instance/page", method = RequestMethod.POST)
    public BaseResponse<GrouponInstancePageWithCustomerInfoResponse> page(@RequestBody GrouponInstancePageRequest
                                                                                      grouponInstancePageRequest) {
        grouponInstancePageRequest.setGrouponStatus(GrouponOrderStatus.WAIT);
        grouponInstancePageRequest.setSortColumn("createTime");
        grouponInstancePageRequest.setSortRole(SortType.DESC.toValue());
        return grouponInstanceQueryProvider.pageCriteriaWithCustomerInfoResponse(grouponInstancePageRequest);
    }

    /**
     * 根据团实例编号查询团信息带人员信息
     */
    @RequestMapping(value = "/grouponInstanceInfo/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<GrouponInstanceWithCustomerInfoVO> getGrouponInstanceInfo(@PathVariable String grouponNo) {
        GrouponInstancePageRequest grouponInstancePageRequest = new GrouponInstancePageRequest();
        grouponInstancePageRequest.setGrouponStatus(GrouponOrderStatus.WAIT);
        grouponInstancePageRequest.setSortColumn("createTime");
        grouponInstancePageRequest.setSortRole(SortType.DESC.toValue());
        grouponInstancePageRequest.setGrouponNo(grouponNo);
        List<GrouponInstanceWithCustomerInfoVO> response = grouponInstanceQueryProvider
                .pageCriteriaWithCustomerInfoResponse
                        (grouponInstancePageRequest).getContext().getGrouponInstanceVOS().getContent();
        if (CollectionUtils.isNotEmpty(response)) {
            return BaseResponse.success(response.get(0));
        }
        return BaseResponse.error(null);
    }


    @ApiOperation(value = "邀请参团小程序码")
    @RequestMapping(value = "/invite/add/group/{grouponNo}", method = RequestMethod.GET)
    public BaseResponse<String> inviteAddGroup(@PathVariable String grouponNo) {
        //生成小程序码
        MiniProgramQrCodeRequest request = new MiniProgramQrCodeRequest();
        String url = String.format("/groupon/%s/%s/%s", grouponNo, commonUtil.getOperatorId(), commonUtil.getShareId(""));
        String redisKey = (MD5Util.md5Hex(url, "utf-8")).toUpperCase().substring(16);
        if (StringUtils.isNotBlank(redisKey)) {
            redisService.setString(redisKey, url, 15000000L);
        }
        request.setPage("pages/sharepage/sharepage");
        request.setScene("G" + redisKey);
        return wechatAuthProvider.getWxaCodeUnlimit(request);
    }


    /**
     * 商品详情页、拼团页验证拼团信息
     */
    @RequestMapping(value = "/vaildateGrouponStatusForOpenOrJoin", method = RequestMethod.POST)
    public BaseResponse<GrouponGoodsInfoVO> vaildateGrouponStatusForOpenOrJoin(@RequestBody GrouponOrderValidRequest
                                                                                           request) {
        request.setCustomerId(commonUtil.getOperatorId());
        // 1.校验拼团商品
        GrouponGoodsInfoVO grouponGoodsInfo = grouponProvider.validGrouponOrderBeforeCommit(request).getContext().getGrouponGoodsInfo();
        return BaseResponse.success(grouponGoodsInfo);
    }


    @ApiOperation(value = "查询最近的待成团实例")
    @RequestMapping(value = "/latest/instance", method = RequestMethod.POST)
    public BaseResponse<GrouponInstanceByActivityIdResponse> getGrouponLatestInstanceByActivityId(@RequestBody GrouponInstancePageRequest
                                                                                  grouponInstancePageRequest) {

        return grouponInstanceQueryProvider.getGrouponLatestInstanceByActivityId(grouponInstancePageRequest);
    }
}
