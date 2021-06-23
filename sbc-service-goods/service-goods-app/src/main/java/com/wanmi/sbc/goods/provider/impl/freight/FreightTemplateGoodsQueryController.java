package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.freight.*;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdsResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsDefaultByStoreIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsListByStoreIdResponse;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateGoodsVO;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoods;
import com.wanmi.sbc.goods.freight.service.FreightTemplateGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>对单品运费模板查询接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@RestController
@Validated
public class FreightTemplateGoodsQueryController implements FreightTemplateGoodsQueryProvider {

    @Autowired
    private FreightTemplateGoodsService freightTemplateGoodsService;

    /**
     * 根据店铺id查询单品运费模板
     *
     * @param request 包含店铺id的查询请求结构 {@link FreightTemplateGoodsListByStoreIdRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsListByStoreIdResponse}
     */
    @Override
    public BaseResponse<FreightTemplateGoodsListByStoreIdResponse> listByStoreId(@RequestBody @Valid
                                                                                  FreightTemplateGoodsListByStoreIdRequest request){
        List<FreightTemplateGoods> templateGoods = freightTemplateGoodsService.queryAll(request.getStoreId());
        List<FreightTemplateGoodsVO> voList = FreightTemplateGoodsConvert.convertFreightTemplateGoodsListToVoList(templateGoods);
        return BaseResponse.success(FreightTemplateGoodsListByStoreIdResponse.builder()
                .freightTemplateGoodsVOList(voList).build());
    }

    /**
     * 根据批量单品运费模板id查询单品运费模板列表
     *
     * @param request 包含批量ids的查询请求结构 {@link FreightTemplateGoodsListByIdsRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsByIdsResponse}
     */
    @Override
    public BaseResponse<FreightTemplateGoodsByIdsResponse> listByIds(@RequestBody @Valid
                                                                      FreightTemplateGoodsListByIdsRequest request){
        List<FreightTemplateGoods> templateGoods = freightTemplateGoodsService.queryAllByIds(request.getFreightTempIds());
        List<FreightTemplateGoodsVO> voList = FreightTemplateGoodsConvert.convertFreightTemplateGoodsListToVoList(templateGoods);
        return BaseResponse.success(FreightTemplateGoodsByIdsResponse.builder()
                .freightTemplateGoodsVOList(voList).build());
    }

    /**
     * 根据单品运费模板id查询单品运费模板
     *
     * @param request 包含id的查询请求结构 {@link FreightTemplateGoodsByIdRequest}
     * @return 单品运费模板 {@link FreightTemplateGoodsByIdResponse}
     */
    @Override
    public BaseResponse<FreightTemplateGoodsByIdResponse> getById(@RequestBody @Valid
                                                                   FreightTemplateGoodsByIdRequest request){
        FreightTemplateGoods templateGoods = freightTemplateGoodsService.queryById(request.getFreightTempId());
        return BaseResponse.success(FreightTemplateGoodsConvert.convertFreightTemplateGoodsToResponse(templateGoods));
    }


    /**
     * 根据单品运费模板id验证单品运费模板
     *
     * @param request 包含id的验证请求结构 {@link FreightTemplateGoodsExistsByIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse existsById(@RequestBody @Valid FreightTemplateGoodsExistsByIdRequest request){
        freightTemplateGoodsService.hasFreightTemp(request.getFreightTempId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据店铺id查询默认单品运费模板请求
     *
     * @param request 包含店铺id的查询请求结构 {@link FreightTemplateGoodsDefaultByStoreIdRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsDefaultByStoreIdResponse}
     */
    @Override
    public BaseResponse<FreightTemplateGoodsDefaultByStoreIdResponse> getDefaultByStoreId(@RequestBody @Valid
                                                                                                  FreightTemplateGoodsDefaultByStoreIdRequest request) {
        FreightTemplateGoods templateGoods = freightTemplateGoodsService.queryByDefaultByStoreId(request.getStoreId());
        if(Objects.nonNull(templateGoods)){
            return BaseResponse.success(KsBeanUtil.convert(templateGoods, FreightTemplateGoodsDefaultByStoreIdResponse.class));
        }
        return BaseResponse.SUCCESSFUL();
    }
}
