package com.wanmi.sbc.bookmeta.service;

import com.wanmi.sbc.bookmeta.provider.AuthorityProvider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:26
 * @Description:
 */
@Service
public class AuthorityService {

    @Resource
    AuthorityProvider authorityProvider;


}
