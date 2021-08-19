package com.wanmi.sbc.goods;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.request.SearchHistoryRequest;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 用户商品搜索记录相关
 * Created by CHENLI on 2017/4/21.
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "SearchHistoryBaseController", description = "S2B web公用-用户商品搜索历史记录信息API")
public class SearchHistoryBaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonUtil commonUtil;

    private final String REDIS_KEY = CacheKeyConstant.SEARCH_HISTORY_KEY;

    private final String DISTRIBUTE_SEARCH_HISTORY_KEY = CacheKeyConstant.DISTRIBUTE_SEARCH_HISTORY_KEY;

    /**
     * 搜索拼团商品历史的key
     */
    private final String GROUPON_SEARCH_HISTORY_KEY = CacheKeyConstant.GROUPON_SEARCH_HISTORY_KEY;

    /**
     * 搜索分销推广商品历史的key
     */
    private final String DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY = CacheKeyConstant.DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY;

    /**
     * 查询用户相关的搜索记录
     *
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "查询用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public BaseResponse<List<String>> query() {
        return BaseResponse.success(this.queryHistories(REDIS_KEY));
    }

    /**
     * 新增用户相关的搜索记录
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "新增用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid SearchHistoryRequest request) {
        // 将搜索记录存储到Redis
        List<String> histories = this.makeHistories(REDIS_KEY, request.getKeyword());

        redisService.hset(REDIS_KEY, commonUtil.getOperatorId(), JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 清空用户相关的搜索记录
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "清空用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.DELETE)
    public BaseResponse delete() {
        redisService.hdelete(REDIS_KEY, commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询指定店铺内商品的历史记录
     *
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "查询指定店铺内商品的历史记录")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "店铺id", required = true)
    @RequestMapping(value = "/store/history/{id}", method = RequestMethod.GET)
    public BaseResponse<List<String>> queryStore(@PathVariable Long id) {
        return BaseResponse.success(this.queryHistories(REDIS_KEY.concat(id.toString())));
    }

    /**
     * 新增指定店铺内商品的历史记录
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "新增指定店铺内商品的历史记录")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "店铺id", required = true)
    @RequestMapping(value = "/store/history/{id}", method = RequestMethod.POST)
    public BaseResponse addStore(@PathVariable Long id, @RequestBody @Valid SearchHistoryRequest request) {
        String redisKey = REDIS_KEY.concat(id.toString());
        // 将搜索记录存储到Redis
        List<String> histories = this.makeHistories(redisKey, request.getKeyword());

        redisService.hset(redisKey, commonUtil.getOperatorId(), JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 清空指定店铺内商品的历史记录
     *
     * @return BaseResponse
     */
    @ApiOperation(value = "清空指定店铺内商品的历史记录")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "id", value = "店铺id", required = true)
    @RequestMapping(value = "/store/history/{id}", method = RequestMethod.DELETE)
    public BaseResponse deleteStore(@PathVariable Long id) {
        redisService.hdelete(REDIS_KEY.concat(id.toString()), commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "查询分销员选品商品的历史记录")
    @RequestMapping(value = "/distribute/history", method = RequestMethod.GET)
    public BaseResponse<List<String>> queryDistribute() {
        return BaseResponse.success(this.queryHistories(DISTRIBUTE_SEARCH_HISTORY_KEY));
    }

    @ApiOperation(value = "新增分销员选品商品的历史记录")
    @RequestMapping(value = "/distribute/history", method = RequestMethod.POST)
    public BaseResponse addDistribute(@RequestBody @Valid SearchHistoryRequest request) {
        // 将搜索记录存储到Redis
        List<String> histories = this.makeHistories(DISTRIBUTE_SEARCH_HISTORY_KEY, request.getKeyword());

        redisService.hset(DISTRIBUTE_SEARCH_HISTORY_KEY, commonUtil.getOperatorId(), JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "清空分销员选品商品的历史记录")
    @RequestMapping(value = "/distribute/history", method = RequestMethod.DELETE)
    public BaseResponse deleteDistribute() {
        redisService.hdelete(DISTRIBUTE_SEARCH_HISTORY_KEY, commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "查询拼团活动商品的历史记录")
    @RequestMapping(value = "/groupon/history", method = RequestMethod.GET)
    public BaseResponse<List<String>> queryGrouponHistory() {

        return BaseResponse.success(this.queryHistories(GROUPON_SEARCH_HISTORY_KEY));
    }

    @ApiOperation(value = "新增拼团活动商品的历史记录")
    @RequestMapping(value = "/groupon/history", method = RequestMethod.POST)
    public BaseResponse addGrouponHistory(@RequestBody @Valid SearchHistoryRequest request) {
        // 将搜索记录存储到Redis
        List<String> histories = this.makeHistories(GROUPON_SEARCH_HISTORY_KEY, request.getKeyword());

        redisService.hset(GROUPON_SEARCH_HISTORY_KEY, commonUtil.getOperatorId(), JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "清空拼团活动商品的历史记录")
    @RequestMapping(value = "/groupon/history", method = RequestMethod.DELETE)
    public BaseResponse deleteGrouponHistory() {
        redisService.hdelete(GROUPON_SEARCH_HISTORY_KEY, commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }


    @ApiOperation(value = "查询分销推广商品的历史记录")
    @RequestMapping(value = "/distribute-goods/history", method = RequestMethod.GET)
    public BaseResponse<List<String>> queryDistributeGoodsHistory() {

        return BaseResponse.success(this.queryHistories(DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY));
    }

    @ApiOperation(value = "新增分销推广商品的历史记录")
    @RequestMapping(value = "/distribute-goods/history", method = RequestMethod.POST)
    public BaseResponse addDistributeGoodsHistory(@RequestBody @Valid SearchHistoryRequest request) {
        // 将搜索记录存储到Redis
        List<String> histories = this.makeHistories(DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY, request.getKeyword());

        redisService.hset(DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY, commonUtil.getOperatorId(), JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "清空分销推广商品的历史记录")
    @RequestMapping(value = "/distribute-goods/history", method = RequestMethod.DELETE)
    public BaseResponse clearDistributeGoodsHistory() {
        redisService.hdelete(DISTRIBUTE_GOODS_SEARCH_HISTORY_KEY, commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询历史记录
     *
     * @param redisKey
     * @return
     */
    private List<String> queryHistories(String redisKey) {
        String operatorId = commonUtil.getOperatorId();
        List<String> histories = JSON.parseArray(StringUtils.defaultString(redisService.hget(redisKey, operatorId)), String.class);
        if (CollectionUtils.isEmpty(histories)) {
            histories = new ArrayList<>();
        }
        return histories;
    }

    /**
     * 新增搜索历史记录
     *
     * @param redisKey
     * @param keyWord
     * @return
     */
    private List<String> makeHistories(String redisKey, String keyWord) {
        List<String> histories = this.queryHistories(redisKey);
        Iterator<String> it = histories.iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.equals(keyWord)) {
                it.remove();
                break;
            }
        }

        histories.add(keyWord);

        //删除多余的搜索记录，只保留最新10条搜索记录
        int surplusNum = histories.size() - 10;
        if (surplusNum > 0) {
            for (int i = 0; i < surplusNum; i++) {
                histories.remove(i);
            }
        }

        return histories;
    }
}
