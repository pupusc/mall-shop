package com.wanmi.sbc.bookmeta.controller;

import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import com.wanmi.sbc.bookmeta.vo.AuthorityAddReqVO;
import com.wanmi.sbc.bookmeta.vo.AuthorityQueryByPageReqVO;
import com.wanmi.sbc.bookmeta.vo.AuthorityQueryByPageRespVO;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:40
 * @Description:
 */

@RestController
@RequestMapping("authorityV2")
public class AuthorityController {
    @Resource
    AuthorityProvider authorityProvider;
    @PostMapping("addAuthority")
    public BusinessResponse<String> insert(@RequestBody AuthorityAddReqVO requestVO){
        AuthorityAddReqBO authority = KsBeanUtil.convert(requestVO, AuthorityAddReqBO.class);
        BusinessResponse<String> integerBusinessResponse = this.authorityProvider.addAuthority(authority);
        return  integerBusinessResponse;
    }

    @PostMapping("updateAuthority")
    public BusinessResponse<String> update(@RequestBody AuthorityAddReqVO requestVO){
        AuthorityAddReqBO authority = KsBeanUtil.convert(requestVO, AuthorityAddReqBO.class);
        BusinessResponse<String> integerBusinessResponse = authorityProvider.updateAuthority(authority);
        return  integerBusinessResponse;
    }

    @PostMapping("deleteAuthority")
    public BusinessResponse<Integer> delete(@RequestBody AuthorityAddReqVO requestVO){
        AuthorityAddReqBO authority = KsBeanUtil.convert(requestVO, AuthorityAddReqBO.class);
        BusinessResponse<Integer> integerBusinessResponse = authorityProvider.deleteAuthority(authority.getAuthorityId());
        return  integerBusinessResponse;
    }


    @PostMapping("queryAuthority")
    public BusinessResponse<List<AuthorityQueryByPageRespVO>> queryAuthority(@RequestBody AuthorityQueryByPageReqVO pageRequest) {
        AuthorityQueryByPageReqBO pageReqBO = KsBeanUtil.convert(pageRequest,AuthorityQueryByPageReqBO.class);
        BusinessResponse<List<AuthorityBO>> boResult = authorityProvider.getAuthorityByUrl(pageReqBO);
        if (!CommonErrorCode.SUCCESSFUL.equals(boResult.getCode())) {
            return BusinessResponse.error(boResult.getCode(), boResult.getMessage());
        }
        List<AuthorityQueryByPageRespVO> authorityQueryByPageRespVOS = KsBeanUtil.convertList(boResult.getContext(), AuthorityQueryByPageRespVO.class);
        return BusinessResponse.success(authorityQueryByPageRespVOS,boResult.getPage());
    }


}
