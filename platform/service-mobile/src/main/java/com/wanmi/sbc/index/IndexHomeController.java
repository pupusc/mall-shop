package com.wanmi.sbc.index;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.index.requst.VersionRequest;
import com.wanmi.sbc.index.response.IndexConfigResponse;
import com.wanmi.sbc.redis.RedisListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @menu 商城首页
 * @tag feature_d_1111_index
 * @status undone
 */
@RestController
@RequestMapping("/index")
public class IndexHomeController {

    @Value("${index.config}")
    private String indexConfig;

    @Autowired
    private RedisListService redisService;


    public static final Integer GOODS_SIZE = 5;
    public static final Integer BOOKS_SIZE = 1;
    /**
     * @description 获取首页配置数据
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/config")
    public BaseResponse<IndexConfigResponse> config() {
        IndexConfigResponse indexConfigResponse = JSON.parseObject(indexConfig, IndexConfigResponse.class);
        return BaseResponse.success(indexConfigResponse);
    }


    /**
     * @description 热销榜
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/hot")
    public BaseResponse<List<SortGoodsCustomResponse>> hot(@RequestBody VersionRequest versionRequest) {
        List<SortGoodsCustomResponse> goodsCustomResponseList = redisService.findByRange("hotGoods", versionRequest.getPageNum() * GOODS_SIZE, GOODS_SIZE);
        goodsCustomResponseList.addAll(redisService.findByRange("hotBooks", versionRequest.getPageNum() * BOOKS_SIZE, BOOKS_SIZE));
        return BaseResponse.success(goodsCustomResponseList);
    }
}
