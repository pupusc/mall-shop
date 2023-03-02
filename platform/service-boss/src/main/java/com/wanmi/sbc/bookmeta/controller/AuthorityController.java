package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import com.wanmi.sbc.bookmeta.vo.AuthorityAddReqVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:40
 * @Description:
 */

@RestController
@RequestMapping("authority")
public class AuthorityController {
    @Resource
    AuthorityProvider authorityProvider;
    @PostMapping("addAuthority")
    public BusinessResponse<Integer> insert(AuthorityAddReqVO requestVO){
        AuthorityAddReqBO authority = KsBeanUtil.convert(requestVO, AuthorityAddReqBO.class);
        BusinessResponse<Integer> integerBusinessResponse = this.authorityProvider.addAuthority(authority);
        return  integerBusinessResponse;
    }

    @PostMapping("updateAuthority")
    public BusinessResponse<Integer> update(AuthorityAddReqVO requestVO){
        AuthorityAddReqBO authority = KsBeanUtil.convert(requestVO, AuthorityAddReqBO.class);
        BusinessResponse<Integer> integerBusinessResponse = authorityProvider.updateAuthority(authority);
        return  integerBusinessResponse;
    }

}
