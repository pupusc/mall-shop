package com.wanmi.sbc.flashsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.flashsalecate.FlashSaleCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalecate.FlashSaleCateSaveProvider;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateListRequest;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateListResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Api(description = "秒杀分类管理API", tags = "FlashSaleCateController")
@RestController
@RequestMapping(value = "/flashsalecate")
public class FlashSaleCateController {

    @Autowired
    private FlashSaleCateQueryProvider flashSaleCateQueryProvider;

    @Autowired
    private FlashSaleCateSaveProvider flashSaleCateSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "列表查询秒杀分类")
    @PostMapping("/list")
    public BaseResponse<FlashSaleCateListResponse> getList(@RequestBody @Valid FlashSaleCateListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("sort", "desc");
        return flashSaleCateQueryProvider.list(listReq);
    }
}
