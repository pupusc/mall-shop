package com.wanmi.sbc.bookmeta.provider.impl;

import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.Authority;
import com.wanmi.sbc.bookmeta.mapper.AuthorityMapper;
import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

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
    public BusinessResponse<String> addAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        authority.setCreateTime(new Date());
        int i = authorityMapper.insertAuthority(authority);
        if (i < 0) {
            return BusinessResponse.error("Invalid entity");
        }
        return BusinessResponse.success(authority.getAuthorityId());
    }

    @Override
    public BusinessResponse<String> updateAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        int i = authorityMapper.updateAuthority(authority);
        if (i < 0) {
            return BusinessResponse.error("Invalid");
        }
        return BusinessResponse.success(authority.getAuthorityId());
    }

    @Override
    public BusinessResponse<Integer> deleteAuthority(String authorityId) {
        return BusinessResponse.success(authorityMapper.deleteAuthority(authorityId));
    }

    @Override
    public BusinessResponse<List<AuthorityBO>> getAuthorityByUrl(AuthorityQueryByPageReqBO pageReqBO) {
        Page page = pageReqBO.getPage();
        page.setTotalCount((int) authorityMapper.getAuthorityByUrlCount(pageReqBO.getAuthorityUrl()));
        if (page.getTotalCount() <= 0) {
            return BusinessResponse.success(Collections.EMPTY_LIST, page);
        }
        List<AuthorityBO> authorityByUrl = authorityMapper.getAuthorityByUrl(pageReqBO.getAuthorityUrl(), page.getOffset(),page.getPageSize());
        System.out.println(authorityByUrl);
        return BusinessResponse.success(authorityByUrl, page);
    }

}
