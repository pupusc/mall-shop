package com.wanmi.sbc.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.grouponcenter.GrouponCenterQueryProvider;
import com.wanmi.sbc.marketing.api.request.grouponcenter.GrouponCenterListRequest;
import com.wanmi.sbc.marketing.api.response.grouponcenter.GrouponCenterListResponse;

import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 拼团活动首页Controller
 */
@RestController
@RequestMapping("/groupon/center")
@Api(tags = "GrouponCenterController", description = "S2B web公用-拼团营销")
public class GrouponCenterController {

    @Autowired
    private GrouponCenterQueryProvider grouponCenterQueryProvider;

    @Autowired
    private CommonUtil commonUtil;


    /**
     * 查询拼团活动spu列表信息
     * @return
     */
    @ApiModelProperty(value = "查询拼团活动spu列表信息")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public BaseResponse<GrouponCenterListResponse> list(@RequestBody GrouponCenterListRequest request){

        return grouponCenterQueryProvider.list(request);
    }

}
