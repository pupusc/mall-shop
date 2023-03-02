package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.entity.Authority;
import com.wanmi.sbc.bookmeta.mapper.AuthorityMapper;
import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:27
 * @Description:
 */
@Validated
@RestController
public class AuthorityProviderImpl implements AuthorityProvider {
    @Resource
    AuthorityMapper authorityMapper;

    @Override
    public BusinessResponse<Integer> addAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        int i = authorityMapper.insertAuthority(authority);
        if (i < 0) {
            return BusinessResponse.error("Invalid entity");
        }
        return BusinessResponse.success(Integer.parseInt(authority.getAuthorityId()));
    }

    @Override
    public BusinessResponse<Integer> updateAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        int i = authorityMapper.updateAuthority(authority);
        if (i < 0) {
            return BusinessResponse.error("Invalid");
        }
        return BusinessResponse.success(Integer.parseInt(authority.getAuthorityId()));
    }
}
