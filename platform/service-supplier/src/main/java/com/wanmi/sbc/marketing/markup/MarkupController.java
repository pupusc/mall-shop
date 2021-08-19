package com.wanmi.sbc.marketing.markup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupSaveProvider;
import com.wanmi.sbc.marketing.api.request.markup.*;
import com.wanmi.sbc.marketing.api.response.markup.*;

import javax.validation.Valid;

import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(description = "加价购活动管理API", tags = "MarkupController")
@RestController
@RequestMapping(value = "/marketing/markup")
public class MarkupController {

    @Autowired
    private MarkupQueryProvider markupQueryProvider;

    @Autowired
    private MarkupSaveProvider markupSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @ApiOperation(value = "根据id查询加价购活动")
    @GetMapping("/markupLevelList")
    public BaseResponse<MarkupLevelByIdResponse> getById(@Valid @RequestBody MarkupLevelByIdRequest request) {
        CustomerGetByIdResponse customer = null;
        if(StringUtils.isNotBlank(request.getCustomerId())){
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(request.getCustomerId())).getContext();
        }
        request.setCustomer(KsBeanUtil.convert(customer, CustomerDTO.class));
        return markupQueryProvider.getLevelById(request);
    }

    @ApiOperation(value = "新增加价购活动")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid MarkupAddRequest addReq) {
        addReq.valid();

        addReq.setIsBoss(BoolFlag.NO);
        addReq.setStoreId(commonUtil.getStoreId());
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setMarketingType(MarketingType.MARKUP);
        addReq.setSubType(MarketingSubType.MARKUP);
         markupSaveProvider.add(addReq);

        operateLogMQUtil.convertAndSend("营销","创建满赠活动","创建满赠活动："+addReq.getMarketingName());
        return  BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "修改加价购活动")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse modify(@RequestBody @Valid MarkupModifyRequest modifyReq) {
        modifyReq.valid();

        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        markupSaveProvider.modify(modifyReq);

        operateLogMQUtil.convertAndSend("营销","编辑促销活动","编辑促销活动："+modifyReq.getMarketingName());

        return  BaseResponse.SUCCESSFUL();
    }


}
