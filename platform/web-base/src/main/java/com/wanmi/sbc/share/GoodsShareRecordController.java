package com.wanmi.sbc.share;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goodssharerecord.GoodsShareRecordSaveProvider;
import com.wanmi.sbc.goods.api.request.goodssharerecord.GoodsShareRecordAddRequest;
import com.wanmi.sbc.goods.api.response.goodssharerecord.GoodsShareRecordAddResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;


/**
 * @author zhangwenchang
 */
@Api(description = "商品分享管理API", tags = "GoodsShareRecordController")
@RestController
@RequestMapping(value = "/goods/share/record")
public class GoodsShareRecordController {

    @Autowired
    private GoodsShareRecordSaveProvider goodsShareRecordSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "新增商品分享")
    @PostMapping("/add")
    public BaseResponse<GoodsShareRecordAddResponse> add(@RequestBody @Valid GoodsShareRecordAddRequest addReq) {
        addReq.setCreateTime(LocalDateTime.now());
        addReq.setTerminalSource(commonUtil.getTerminal());
        addReq.setCustomerId(commonUtil.getOperatorId());
        if (addReq.getGoodsInfoId() == null) {
            return BaseResponse.SUCCESSFUL();
        }
        return goodsShareRecordSaveProvider.add(addReq);
    }

}
