package com.wanmi.sbc.goods.provider.impl.spec;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.spec.GoodsSpecQueryProvider;
import com.wanmi.sbc.goods.api.request.spec.GoodsSpecListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecForExportResponse;
import com.wanmi.sbc.goods.api.response.spec.GoodsSpecListByGoodsIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecExportVO;
import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import com.wanmi.sbc.goods.spec.service.GoodsSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 商品规格查询服务
 *
 * @author daiyitian
 * @dateTime 2018/11/13 14:57
 */
@RestController
@Validated
public class GoodsSpecQueryController implements GoodsSpecQueryProvider {

    @Autowired
    private GoodsSpecService goodsSpecService;

    /**
     * 根据goodsIds批量查询商品规格列表
     *
     * @param request 包含goodsIds的查询请求结构 {@link GoodsSpecListByGoodsIdsRequest }
     * @return 商品规格列表 {@link GoodsSpecListByGoodsIdsResponse }
     */
    @Override
    
    public BaseResponse<GoodsSpecListByGoodsIdsResponse> listByGoodsIds(@RequestBody @Valid
                                                                                GoodsSpecListByGoodsIdsRequest
                                                                                request) {
        List<GoodsSpecVO> voList =
                KsBeanUtil.convertList(goodsSpecService.findByGoodsIds(request.getGoodsIds()), GoodsSpecVO.class);
        return BaseResponse.success(GoodsSpecListByGoodsIdsResponse.builder().goodsSpecVOList(voList).build());
    }

    @Override
    public BaseResponse<GoodsInfoSpecForExportResponse> listByGoodsInfoIdsForExport(@RequestBody @Valid GoodsSpecListByGoodsIdsRequest request) {
        Map<String, List<GoodsInfoSpecExportVO>> goodsInfoSpecForExport = goodsSpecService.findGoodsInfoSpecForExport(request.getGoodsIds());
        return BaseResponse.success(GoodsInfoSpecForExportResponse.builder().goodsInfoSpecMap(goodsInfoSpecForExport).build());
    }
}
