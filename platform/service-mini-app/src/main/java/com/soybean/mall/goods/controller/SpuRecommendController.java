//package com.soybean.mall.goods.controller;
//
//import com.soybean.common.resp.CommonPageResp;
//import com.soybean.elastic.api.resp.EsSpuNewResp;
//import com.soybean.mall.goods.response.SpuNewBookListResp;
//import com.soybean.mall.goods.service.SpuNewSearchService;
//import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.goods.api.provider.classify.ClassifyProvider;
//import com.wanmi.sbc.goods.api.response.classify.ClassifyGoodsProviderResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Description: 商品推荐 h5 小程序公用
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2022/7/8 1:25 上午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//@RequestMapping("/spu/recommend")
//@RestController
//@Slf4j
//public class SpuRecommendController {
//
//    @Autowired
//    private ClassifyProvider classifyProvider;
//
//    @Autowired
//    private SpuNewSearchService spuNewSearchService;
//
//    /**
//     * 看了又看
//     * @return
//     */
//    @PostMapping("/list-see")
//    public BaseResponse<CommonPageResp<List<SpuNewBookListResp>>> listSee() {
//        BaseResponse<List<ClassifyGoodsProviderResponse>> listClassifyGoodsAllChildOfParentResponse = classifyProvider.listGoodsIdOfChildOfParentByGoodsId("seePageRequest.getSpuId()");
//        List<ClassifyGoodsProviderResponse> listClassifyGoodsAllChildOfParent = listClassifyGoodsAllChildOfParentResponse.getContext();
//        if (CollectionUtils.isEmpty(listClassifyGoodsAllChildOfParent)) {
//            return BaseResponse.success(new CommonPageResp<>(0L, new ArrayList<>()));
//        }
//
//        CommonPageResp<List<EsSpuNewResp>> context = new CommonPageResp<>();
//        List<SpuNewBookListResp> spuNewBookListResps = spuNewSearchService.listSpuNewSearch(context.getContent());
//        return BaseResponse.success(new CommonPageResp<>(context.getTotal(), spuNewBookListResps));
//    }
//
//}
