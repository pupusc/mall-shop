package com.wanmi.sbc.index;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.index.response.IndexConfigResponse;
import com.wanmi.sbc.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    private RedisService redisService;


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
     * 热销榜
     * @return BaseResponse
     */
    @PostMapping(value = "/hot")
    public BaseResponse<IndexConfigResponse> hot() {
        //redisService.getList("")
        IndexConfigResponse indexConfigResponse = JSON.parseObject(indexConfig, IndexConfigResponse.class);
        return BaseResponse.success(indexConfigResponse);
    }
}
