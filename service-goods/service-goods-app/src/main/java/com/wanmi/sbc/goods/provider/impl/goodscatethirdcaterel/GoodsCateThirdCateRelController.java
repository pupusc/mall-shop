package com.wanmi.sbc.goods.provider.impl.goodscatethirdcaterel;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.provider.goodscatethirdcaterel.GoodsCateThirdCateRelProvider;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelAddRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelAddResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelModifyRequest;
import com.wanmi.sbc.goods.api.response.goodscatethirdcaterel.GoodsCateThirdCateRelModifyResponse;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelDelByIdRequest;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelDelByIdListRequest;
import com.wanmi.sbc.goods.goodscatethirdcaterel.service.GoodsCateThirdCateRelService;
import com.wanmi.sbc.goods.goodscatethirdcaterel.model.root.GoodsCateThirdCateRel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * <p>平台类目和第三方平台类目映射保存服务接口实现</p>
 *
 * @author
 * @date 2020-08-18 19:51:55
 */
@RestController
@Validated
public class GoodsCateThirdCateRelController implements GoodsCateThirdCateRelProvider {
    @Autowired
    private GoodsCateThirdCateRelService goodsCateThirdCateRelService;

    @Override
    public BaseResponse<GoodsCateThirdCateRelAddResponse> addBatch(@RequestBody @Valid GoodsCateThirdCateRelAddRequest goodsCateThirdCateRelAddRequest) {
        List<GoodsCateThirdCateRel> goodsCateThirdCateRels = KsBeanUtil.convert(goodsCateThirdCateRelAddRequest.getGoodsCateThirdCateRels(), GoodsCateThirdCateRel.class);
        if (ThirdPlatformType.LINKED_MALL.equals(goodsCateThirdCateRels.get(0).getThirdPlatformType())) {
            return BaseResponse.success(goodsCateThirdCateRelService.addBatch(goodsCateThirdCateRels));
        }
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<GoodsCateThirdCateRelModifyResponse> modify(@RequestBody @Valid GoodsCateThirdCateRelModifyRequest goodsCateThirdCateRelModifyRequest) {
        GoodsCateThirdCateRel goodsCateThirdCateRel = KsBeanUtil.convert(goodsCateThirdCateRelModifyRequest, GoodsCateThirdCateRel.class);
        return BaseResponse.success(new GoodsCateThirdCateRelModifyResponse(
                goodsCateThirdCateRelService.wrapperVo(goodsCateThirdCateRelService.modify(goodsCateThirdCateRel))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid GoodsCateThirdCateRelDelByIdRequest goodsCateThirdCateRelDelByIdRequest) {
        GoodsCateThirdCateRel goodsCateThirdCateRel = KsBeanUtil.convert(goodsCateThirdCateRelDelByIdRequest, GoodsCateThirdCateRel.class);
        goodsCateThirdCateRel.setDelFlag(DeleteFlag.YES);
        goodsCateThirdCateRelService.deleteById(goodsCateThirdCateRel);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid GoodsCateThirdCateRelDelByIdListRequest goodsCateThirdCateRelDelByIdListRequest) {
        List<GoodsCateThirdCateRel> goodsCateThirdCateRelList = goodsCateThirdCateRelDelByIdListRequest.getIdList().stream()
                .map(Id -> {
                    GoodsCateThirdCateRel goodsCateThirdCateRel = KsBeanUtil.convert(Id, GoodsCateThirdCateRel.class);
                    goodsCateThirdCateRel.setDelFlag(DeleteFlag.YES);
                    return goodsCateThirdCateRel;
                }).collect(Collectors.toList());
        goodsCateThirdCateRelService.deleteByIdList(goodsCateThirdCateRelList);
        return BaseResponse.SUCCESSFUL();
    }

}

