package com.wanmi.sbc.home;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.home.response.HomeNewBookAndSellWellGoodsListResponse;
import com.wanmi.sbc.home.response.HomeImageResponse;
import com.wanmi.sbc.home.response.HomeBookListRecommendResponse;
import com.wanmi.sbc.home.response.HomeSpecialOfferAndUnSellWellGoodsListResponse;
import com.wanmi.sbc.home.response.HomeSpecialTopicResponse;
import com.wanmi.sbc.home.response.NoticeResponse;
import com.wanmi.sbc.home.service.HomePageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
     * 首页 - 公告
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/homeNotice")
    public BaseResponse<NoticeResponse> homeNotice(){
        return BaseResponse.success(homePageService.notice());
    }

    /**
     *
     * 首页 - banner 广告图和 卖点图
     *
     * @menu 新版首页
     * 首页图片
     * @return
     */
    @GetMapping("/homeBanner")
    public BaseResponse<HomeImageResponse> homeBanner() {

        return BaseResponse.success(homePageService.banner());
    }

    /**
     * 首页 - 栏目列表
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/homeSpecialTopic")
    public BaseResponse<List<HomeSpecialTopicResponse>> homeSpecialTopic() {
        return BaseResponse.success(homePageService.homeSpecialTopic());
    }

    /**
     *
     * 首页 - 编辑推荐和名家推荐
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/homeRecommend")
    public BaseResponse<HomeBookListRecommendResponse> homeRecommend() {
        return BaseResponse.success(homePageService.homeRecommend());
    }

    /**
     *
     * 首页 - 获取首页新书榜 畅销榜
     *
     * @menu 新版首页
     *
     * @return
     */
    @GetMapping("/homeWellGoodsList")
    public BaseResponse<HomeNewBookAndSellWellGoodsListResponse> homeWellGoodsList(){

        return BaseResponse.success(homePageService.homeNewBookAndSellWellGoodsList());
    }

    /**
     * 首页 特价 不畅销畅销
     *
     * @menu 新版首页
     * @return
     */
    @GetMapping("/homeSpecialGoodsList")
    public BaseResponse<HomeSpecialOfferAndUnSellWellGoodsListResponse> homeSpecialGoodsList() {
        return BaseResponse.success(homePageService.homeSpecialOfferAndUnSellWellGoodsList());
    }


}
