package com.wanmi.sbc.goods.provider.impl.freight;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateStoreQueryProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreAreaIdByIdAndStoreIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreByIdRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStoreListByStoreIdAndDeleteFlagRequest;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplateStorePageRequest;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateStoreAreaIdByIdAndStoreIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateStoreByIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateStoreListByStoreIdAndDeleteFlagResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateStorePageResponse;
import com.wanmi.sbc.goods.bean.vo.FreightTemplateStoreVO;
import com.wanmi.sbc.goods.freight.service.FreightTemplateStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>对店铺运费模板查询接口</p>
 * Created by daiyitian on 2018-11-1-下午6:23.
 */
@RestController
@Validated
public class FreightTemplateStoreQueryController implements FreightTemplateStoreQueryProvider {

    @Autowired
    private FreightTemplateStoreService freightTemplateStoreService;

    /**
     * 根据id查询店铺运费模板
     *
     * @param request 包含id的查询请求结构 {@link FreightTemplateStoreByIdRequest}
     * @return 店铺运费模板 {@link FreightTemplateStoreByIdResponse}
     */
    @Override

    public BaseResponse<FreightTemplateStoreByIdResponse> getById(@RequestBody @Valid
                                                                   FreightTemplateStoreByIdRequest request){
        FreightTemplateStoreByIdResponse response = new FreightTemplateStoreByIdResponse();
        KsBeanUtil.copyPropertiesThird(freightTemplateStoreService.queryById(request.getFreightTempId()), response);
        return BaseResponse.success(response);
    }

    /**
     * 查询店铺运费模板分页列表
     *
     * @param request 分页查询请求结构 {@link FreightTemplateStorePageRequest}
     * @return 店铺运费模板分页列表 {@link FreightTemplateStorePageResponse}
     */
    @Override

    public BaseResponse<FreightTemplateStorePageResponse> page(@RequestBody @Valid
                                                                FreightTemplateStorePageRequest request){
        MicroServicePage<FreightTemplateStoreVO> page =
                KsBeanUtil.convertPage(freightTemplateStoreService.queryByAll(request), FreightTemplateStoreVO.class);
        return BaseResponse.success(FreightTemplateStorePageResponse.builder().freightTemplateStorePage(page).build());
    }

    /**
     * 根据店铺id和删除状态查询店铺运费模板列表
     *
     * @param request 包含店铺id和删除状态的查询请求结构 {@link FreightTemplateStoreListByStoreIdAndDeleteFlagRequest}
     * @return 店铺运费模板列表 {@link FreightTemplateStoreListByStoreIdAndDeleteFlagResponse}
     */
    @Override

    public BaseResponse<FreightTemplateStoreListByStoreIdAndDeleteFlagResponse> listByStoreIdAndDeleteFlag(@RequestBody @Valid
                                                                                                                   FreightTemplateStoreListByStoreIdAndDeleteFlagRequest request) {
        List<FreightTemplateStoreVO> voList = KsBeanUtil.convertList(freightTemplateStoreService
                .findByStoreIdAndDeleteFlag(request.getStoreId(), request.getDeleteFlag()), FreightTemplateStoreVO.class);
        return BaseResponse.success(FreightTemplateStoreListByStoreIdAndDeleteFlagResponse.builder()
                .freightTemplateStoreVOList(voList).build());
    }

    /**
     * 根据运费模板id和店铺id查询区域id
     *
     * @param request 包含运费模板id和店铺id查询请求结构 {@link FreightTemplateStoreAreaIdByIdAndStoreIdRequest}
     * @return 多个区域id数据 {@link FreightTemplateStoreAreaIdByIdAndStoreIdResponse}
     */
    @Override

    public BaseResponse<FreightTemplateStoreAreaIdByIdAndStoreIdResponse> queryAreaIdsByIdAndStoreId(@RequestBody @Valid
                                                                                                      FreightTemplateStoreAreaIdByIdAndStoreIdRequest
                                                                                                      request) {
        return BaseResponse.success(
                FreightTemplateStoreAreaIdByIdAndStoreIdResponse.builder()
                        .areaIds(freightTemplateStoreService.querySelectedArea(request.getStoreId(),
                                request.getFreightTempId()))
                        .build()
        );
    }

}
