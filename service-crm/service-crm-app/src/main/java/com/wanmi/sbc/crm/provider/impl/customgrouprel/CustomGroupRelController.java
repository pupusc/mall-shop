package com.wanmi.sbc.crm.provider.impl.customgrouprel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.provider.customgrouprel.CustomGroupRelProvide;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customgrouprel.CustomGroupRelRequest;
import com.wanmi.sbc.crm.bean.vo.CustomGroupRelVo;
import com.wanmi.sbc.crm.customgroup.model.CustomGroupRel;
import com.wanmi.sbc.crm.customgroup.service.CustomGroupRelService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-15
 * \* Time: 14:50
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@RestController
public class CustomGroupRelController implements CustomGroupRelProvide {

    @Autowired
    private CustomGroupRelService customGroupRelService;

    @Override
    public BaseResponse<List<CustomGroupRelVo>> queryListByCustomerId(@RequestBody CustomGroupRelRequest request) {
        List<CustomGroupRel> list = this.customGroupRelService.queryListByCustomerId(request.getCustomerId());
        List<CustomGroupRelVo> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(list)){
            voList = KsBeanUtil.convertList(list,CustomGroupRelVo.class);
        }
        return BaseResponse.success(voList);
    }

    @Override
    public BaseResponse<List<String>> queryListByGroupIds(@RequestBody @Valid CrmGroupRequest request) {
        return BaseResponse.success(customGroupRelService.queryListByCustomerId(request));
    }
}
