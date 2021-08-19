package com.wanmi.sbc.crm.customertagrel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.provider.customertagrel.CustomerTagRelQueryProvider;
import com.wanmi.sbc.crm.api.provider.customertagrel.CustomerTagRelSaveProvider;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelAddRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelDelByIdRequest;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelListRequest;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelAddResponse;
import com.wanmi.sbc.crm.api.response.customertagrel.CustomerTagRelListResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;


@Api(description = "会员标签关联管理API", tags = "CustomerTagRelController")
@RestController
@RequestMapping(value = "/customer/tag-rel")
public class CustomerTagRelController {

    @Autowired
    private CustomerTagRelQueryProvider customerTagRelQueryProvider;

    @Autowired
    private CustomerTagRelSaveProvider customerTagRelSaveProvider;

    @Autowired
    private CommonUtil commonUtil;


    @ApiOperation(value = "列表查询会员标签关联")
    @PostMapping("/list")
    public BaseResponse<CustomerTagRelListResponse> getList(@RequestBody @Valid CustomerTagRelListRequest listReq) {
        listReq.setShowTagName(Boolean.TRUE);
        listReq.putSort("id", "desc");
        return customerTagRelQueryProvider.list(listReq);
    }

    @ApiOperation(value = "新增会员标签关联")
    @PostMapping("/add")
    public BaseResponse add(@RequestBody @Valid CustomerTagRelAddRequest addReq) {
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        return customerTagRelSaveProvider.add(addReq);
    }

    @ApiOperation(value = "根据id删除会员标签关联")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "会员标签ID", required = true)
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        CustomerTagRelDelByIdRequest delByIdReq = new CustomerTagRelDelByIdRequest();
        delByIdReq.setId(id);
        return customerTagRelSaveProvider.deleteById(delByIdReq);
    }
}
