package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.AuthorityAddReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/13:57
 * @Description:
 */
@FeignClient(value = "${application.goods.name}", contextId = "AuthorityProvider")
public interface AuthorityProvider {

    @PostMapping("/goods/${application.goods.version}/authority/addAuthority")
    BusinessResponse<Integer> addAuthority(@RequestBody @Valid AuthorityAddReqBO pageReqBO);

    @PostMapping("/goods/${application.goods.version}/authority/updateAuthority")
    BusinessResponse<Integer> updateAuthority(@RequestBody @Valid AuthorityAddReqBO pageReqBO);

}
