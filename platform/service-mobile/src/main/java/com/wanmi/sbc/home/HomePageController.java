package com.wanmi.sbc.home;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.home.request.HomeBookListModelRecommendRequest;
import com.wanmi.sbc.home.request.HomeNewBookRequest;
import com.wanmi.sbc.home.response.HomeGoodsListResponse;
import com.wanmi.sbc.home.response.HomeImageResponse;
import com.wanmi.sbc.home.response.HomeBookListRecommendResponse;
import com.wanmi.sbc.home.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/18 1:26 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@RequestMapping("/mobile/home")
@RestController
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    /**
     * 首页图片
     * @return
     */
    @GetMapping("/homeBanner")
    public BaseResponse<HomeImageResponse> homeBanner() {

        return BaseResponse.success(homePageService.banner());
    }

    /**
     * 编辑推荐和名家推荐
     * @param homeBookListModelRecommendRequest
     * @return
     */
    @PostMapping("/home-recommend")
    public BaseResponse<HomeBookListRecommendResponse> homeRecommend(@RequestBody HomeBookListModelRecommendRequest homeBookListModelRecommendRequest) {

        return BaseResponse.success(homePageService.homeRecommend());
    }

    /**
     * 获取首页图书专区
     * @param homeNewBookRequest
     * @return
     */
    @PostMapping("/homeGoodsList")
    public BaseResponse<HomeGoodsListResponse> homeGoodsList(@RequestBody HomeNewBookRequest homeNewBookRequest){
        int pageSize = 15;
        HomeGoodsListResponse homeGoodsListResponse = new HomeGoodsListResponse();
        homeGoodsListResponse.setNewBookGoodsList(homePageService.newBookList(pageSize));
        homeGoodsListResponse.setSellWellGoodsList(homePageService.sellWellBookList(pageSize));
        homeGoodsListResponse.setSpecialOfferBookList(homePageService.specialOfferBookList(pageSize));
        return BaseResponse.success(homeGoodsListResponse);
    }



}
