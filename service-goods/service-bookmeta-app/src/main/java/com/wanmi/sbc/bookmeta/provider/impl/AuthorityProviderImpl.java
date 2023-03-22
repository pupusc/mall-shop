package com.wanmi.sbc.bookmeta.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.bo.AuthorityQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.Authority;
import com.wanmi.sbc.bookmeta.mapper.AuthorityMapper;
import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import com.wanmi.sbc.common.base.BusinessResponse;
import com.wanmi.sbc.common.base.Page;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
public class AuthorityProviderImpl implements AuthorityProvider {
    @Resource
    AuthorityMapper authorityMapper;

    @Override
    public BusinessResponse<String> addAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        try {
            authority.setCreateTime(new Date());
            int i = authorityMapper.insertAuthority(authority);
            if (i < 0) {
                return BusinessResponse.error("Invalid entity");
            }
        } catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "addAuthority",
                    Objects.isNull(pageReqBO) ? "" : JSON.toJSONString(pageReqBO),
                    e);
        }

        return BusinessResponse.success(authority.getAuthorityId());
    }

    @Override
    public BusinessResponse<String> updateAuthority(AuthorityAddReqBO pageReqBO) {
        Authority authority = KsBeanUtil.convert(pageReqBO, Authority.class);
        try {
            int i = authorityMapper.updateAuthority(authority);
            if (i < 0) {
                return BusinessResponse.error("Invalid");
            }
        }catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "updateAuthority",
                    Objects.isNull(pageReqBO) ? "" : JSON.toJSONString(pageReqBO),
                    e);
        }
        return BusinessResponse.success(authority.getAuthorityId());
    }

    @Override
    public BusinessResponse<Integer> deleteAuthority(String authorityId) {
        int i  = 0;
        try {
            i = authorityMapper.deleteAuthority(authorityId);
        }catch (Exception e) {
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "deleteAuthority",
                    Objects.isNull(authorityId) ? "" : JSON.toJSONString(authorityId),
                    e);
        }
        return BusinessResponse.success(i);
    }

    @Override
    public BusinessResponse<List<AuthorityBO>> getAuthorityByUrl(AuthorityQueryByPageReqBO pageReqBO) {
        Page page = pageReqBO.getPage();
        List<AuthorityBO> authorityByUrl = new ArrayList<>();
        try {
            page.setTotalCount((int) authorityMapper.getAuthorityByUrlCount(pageReqBO.getAuthorityUrl()));
            if (page.getTotalCount() <= 0) {
                return BusinessResponse.success(Collections.EMPTY_LIST, page);
            }
            authorityByUrl = authorityMapper.getAuthorityByUrl(pageReqBO.getAuthorityUrl(), page.getOffset(), page.getPageSize());
        }catch (Exception e){
            log.error("时间:{},方法:{},入口参数:{},执行异常,Cause:{}",
                    DateUtil.format(new Date(), DateUtil.FMT_TIME_1),
                    "getAuthorityByUrl",
                    Objects.isNull(pageReqBO) ? "" : JSON.toJSONString(pageReqBO),
                    e);
        }
        return BusinessResponse.success(authorityByUrl, page);
    }

}
