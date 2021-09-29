package com.wanmi.sbc.index;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.booklistmodel.response.SortGoodsCustomResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.index.requst.VersionRequest;
import com.wanmi.sbc.index.response.IndexConfigResponse;
import com.wanmi.sbc.redis.RedisListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @menu 商城首页
 * @tag feature_d_1111_index
 * @status undone
 */
@RestController
@RequestMapping("/index")
public class IndexHomeController {


    @Autowired
    private RefreshConfig refreshConfig;

    @Autowired
    private RedisListService redisService;

    @Autowired
    private RedisTemplate redisTemplate;
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
        IndexConfigResponse indexConfigResponse = JSON.parseObject(refreshConfig.getIndexConfig(), IndexConfigResponse.class);
        return BaseResponse.success(indexConfigResponse);
    }


    /**
     * @description 热销榜
     * @menu 商城首页
     * @tag feature_d_1111_index
     * @status done
     */
    @PostMapping(value = "/hot")
    public BaseResponse<MicroServicePage<SortGoodsCustomResponse>> hot(@RequestBody VersionRequest versionRequest) {
        MicroServicePage<SortGoodsCustomResponse> page = new MicroServicePage();
        if (versionRequest.getPageNum() == 0) {
            versionRequest.setPageNum(1);
        }
        Long refreshHotCount;
        String ip = HttpUtil.getIpAddr();
        if (!versionRequest.getFalshFlag()) {
            refreshHotCount = Long.valueOf(redisTemplate.opsForValue().get("ip" + ip).toString());
            if (redisTemplate.hasKey("hotGoods" + refreshHotCount) && redisTemplate.hasKey("hotBooks" + refreshHotCount)) {
                refreshHotCount = Long.valueOf(redisTemplate.opsForValue().get("refreshHotCount").toString());
                redisTemplate.opsForValue().set("ip:" + ip, refreshHotCount, 30, TimeUnit.MINUTES);
            }
        } else {
            refreshHotCount = Long.valueOf(redisTemplate.opsForValue().get("refreshHotCount").toString());
            redisTemplate.opsForValue().set("ip:" + ip, refreshHotCount, 30, TimeUnit.MINUTES);
        }
        if (redisTemplate.hasKey("hotGoods" + refreshHotCount) && redisTemplate.hasKey("hotBooks" + refreshHotCount)) {
            refreshHotCount = refreshHotCount - 1;
        }

        List<SortGoodsCustomResponse> goodsCustomResponseList = redisService.findByRange("hotGoods" + refreshHotCount, (versionRequest.getPageNum() - 1) * GOODS_SIZE, versionRequest.getPageNum() * GOODS_SIZE - 1);
        goodsCustomResponseList.addAll(redisService.findByRange("hotBooks" + refreshHotCount, (versionRequest.getPageNum() - 1) * versionRequest.getPageNum() * BOOKS_SIZE, BOOKS_SIZE - 1));
        page.setContent(goodsCustomResponseList);

        page.setNumber(versionRequest.getPageNum());
        return BaseResponse.success(page);
    }
}
