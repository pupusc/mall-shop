package com.wanmi.sbc.paidcard;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/memberCall")
public class MemberCallController {

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;

    @GetMapping("/get-code/{type}")
    public void getCode(@PathVariable Integer type, HttpServletResponse response){
        BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
        String mobileWebsite = config.getMobileWebsite();
        // 生成小程序码或者二维码
        if(Objects.isNull(type)){
            throw new SbcRuntimeException("K-000009");
        }
        if(type == 1){
            //生成二维码

        }else{
            //生成小程序码
        }



    }

}
