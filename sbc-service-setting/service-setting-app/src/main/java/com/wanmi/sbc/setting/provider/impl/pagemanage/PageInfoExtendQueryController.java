package com.wanmi.sbc.setting.provider.impl.pagemanage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.pagemanage.PageInfoExtendQueryProvider;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigQueryRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByPageIdRequest;
import com.wanmi.sbc.setting.api.response.pagemanage.PageInfoExtendByIdResponse;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import com.wanmi.sbc.setting.baseconfig.service.BaseConfigService;
import com.wanmi.sbc.setting.bean.vo.PageInfoExtendVO;
import com.wanmi.sbc.setting.pagemanage.service.PageInfoExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>页面投放查询接口</p>
 * @author dyt
 * @date 2020-04-16
 */
@RestController
public class PageInfoExtendQueryController implements PageInfoExtendQueryProvider {


    @Autowired
    private PageInfoExtendService pageInfoExtendService;

    @Autowired
    private BaseConfigService baseConfigService;

    @Override
    public BaseResponse<PageInfoExtendByIdResponse> findById(@RequestBody PageInfoExtendByIdRequest request) {
        PageInfoExtendVO vo = KsBeanUtil.convert(pageInfoExtendService.findById(request), PageInfoExtendVO.class);
        BaseConfig baseConfig = baseConfigService.list(BaseConfigQueryRequest.builder().build()).get(0);
        String web;
        if("pc".equals(request.getPlatform())){
            web = baseConfig.getPcWebsite();
        }else {
            web = baseConfig.getMobileWebsite();
        }
        if((!web.endsWith("\\")) && (!web.endsWith("/"))){
            web = web.concat("/");
        }
        vo.setUrl(web.concat(Objects.toString(vo.getUrl(), "")));
        return BaseResponse.success(new PageInfoExtendByIdResponse(vo));
    }

    @Override
    public BaseResponse<PageInfoExtendByIdResponse> findRecruitingCustomersById(PageInfoExtendByPageIdRequest request) {
        PageInfoExtendVO vo = KsBeanUtil.convert(pageInfoExtendService.findRecruitingCustomersById(request), PageInfoExtendVO.class);
        BaseConfig baseConfig = baseConfigService.list(BaseConfigQueryRequest.builder().build()).get(0);
        String web;
        if("pc".equals(request.getPlatform())){
            web = baseConfig.getPcWebsite();
        }else {
            web = baseConfig.getMobileWebsite();
        }
        if((!web.endsWith("\\")) && (!web.endsWith("/"))){
            web = web.concat("/");
        }
        vo.setUrl(web.concat(Objects.toString(vo.getUrl(), "")));
        return BaseResponse.success(new PageInfoExtendByIdResponse(vo));
    }
}
