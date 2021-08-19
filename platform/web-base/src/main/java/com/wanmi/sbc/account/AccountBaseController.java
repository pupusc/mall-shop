package com.wanmi.sbc.account;

import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import com.wanmi.sbc.common.base.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>商家账户Controller</p>
 * Created by of628-wenzhi on 2017-07-17-下午3:56.
 */
@RestController
@RequestMapping("/account")
@Api(tags = "AccountBaseController", description = "S2B web公用-供应商线下账户API")
public class AccountBaseController {

    @Resource
    private OfflineQueryProvider offlineQueryProvider;

    /**
     * 查询所有有效线下账户
     *
     * @return 查询所有有效线下账户
     */
    @ApiOperation(value = "查询所有有效线下账户")
    @RequestMapping(value = "/offlineValidAccounts", method = RequestMethod.GET)
    public BaseResponse<List<OfflineAccountVO>> findValidOfflineAccounts() {

        List<OfflineAccountVO> offlineAccountVOList = offlineQueryProvider.listValidAccount().getContext().getVoList();

//        TODO 账号脱敏
//        if(!offlineAccountVOList.isEmpty()) {
//            offlineAccountVOList.forEach(offlineAccountVO -> {
//                if (offlineAccountVO.getBankNo().length() > 4) {
//                    offlineAccountVO.setBankNo(
//                            offlineAccountVO.getBankNo().substring(0, 4)
//                                    + "***********" +
//                                    offlineAccountVO.getBankNo().substring(offlineAccountVO.getBankNo().length() - 4));
//                }
//            });
//        }
        return BaseResponse.success(offlineAccountVOList);
    }

}
