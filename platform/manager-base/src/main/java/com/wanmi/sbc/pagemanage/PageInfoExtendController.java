package com.wanmi.sbc.pagemanage;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.setting.api.provider.pagemanage.PageInfoExtendQueryProvider;
import com.wanmi.sbc.setting.api.provider.pagemanage.PageInfoExtendSaveProvider;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByPageIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendModifyRequest;
import com.wanmi.sbc.setting.api.response.pagemanage.PageInfoExtendByIdResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 页面投放服务
 * Created by daiyitian on 17/4/12.
 */
@Api(tags = "PageInfoExtendController", description = "页面投放服务")
@RestController
@RequestMapping("/pageInfoExtend")
public class PageInfoExtendController {

    @Autowired
    private PageInfoExtendQueryProvider pageInfoExtendQueryProvider;

    @Autowired
    private PageInfoExtendSaveProvider pageInfoExtendSaveProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 页面投放详情
     *
     * @param request 查询参数
     * @return 页面投放详情
     */
    @ApiOperation(value = "页面投放详情")
    @PostMapping("/query")
    public BaseResponse<PageInfoExtendByIdResponse> query(@Valid @RequestBody PageInfoExtendByIdRequest request) {
        request.setStoreId(commonUtil.getStoreId());
        return pageInfoExtendQueryProvider.findById(request);
    }

    /**
     * 编辑页面投放
     */
    @ApiOperation(value = "编辑页面投放")
    @PutMapping(value = "/modify")
    public BaseResponse edit(@Valid @RequestBody PageInfoExtendModifyRequest request) {
        return pageInfoExtendSaveProvider.modify(request);
    }

    /**
     * 页面投放详情（招募会员）
     *
     * @param request 查询参数
     * @return 页面投放详情
     */
    @ApiOperation(value = "页面投放详情")
    @PostMapping("/queryRecruitingCustomers")
    public BaseResponse<PageInfoExtendByIdResponse> queryRecruitingCustomers(@Valid @RequestBody PageInfoExtendByPageIdRequest request) {
        return pageInfoExtendQueryProvider.findRecruitingCustomersById(request);
    }
}
