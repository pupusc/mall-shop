package com.wanmi.sbc.store;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.request.SearchHistoryRequest;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 用户店铺搜索记录相关
 * Created by CHENLI on 2017/4/21.
 */
@Api(tags = "StoreHistoryBaseController", description = " 用户店铺搜索记录 API")
@RestController
@RequestMapping("/store")
public class StoreHistoryBaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonUtil commonUtil;

    private final String REDIS_KEY = CacheKeyConstant.STORE_SEARCH_HISTORY_KEY;

    /**
     * 查询用户相关的搜索记录
     * @return BaseResponse<String>
     */
    @ApiOperation(value = "查询用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public BaseResponse<List<String>> query() {
        List<String> histories = JSON.parseArray( StringUtils.defaultString(redisService.hget(REDIS_KEY, commonUtil.getOperatorId())),String.class);
        return BaseResponse.success(histories != null? histories : Collections.EMPTY_LIST);
    }

    /**
     * 新增用户相关的搜索记录
     * @return BaseResponse
     */
    @ApiOperation(value = "新增用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Valid SearchHistoryRequest request) {
        String operatorId = commonUtil.getOperatorId();
        List<String> histories = JSON.parseArray(StringUtils.defaultString(redisService.hget(REDIS_KEY, operatorId)),String.class);
        if(histories==null){
            histories = new ArrayList<>();
        }

        Iterator<String> it = histories.iterator();
        while(it.hasNext()){
            String x = it.next();
            if(x.equals(request.getKeyword())){
                it.remove();
                break;
            }
        }

        histories.add(request.getKeyword());

        //删除多余的搜索记录，只保留最新10条搜索记录
        int surplusNum = histories.size() - 10;
        if(surplusNum>0){
            for(int i = 0;i<surplusNum;i++){
                histories.remove(i);
            }
        }
        redisService.hset(REDIS_KEY, operatorId, JSON.toJSONString(histories));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 清空用户相关的搜索记录
     * @return BaseResponse
     */
    @ApiOperation(value = "清空用户相关的搜索记录")
    @RequestMapping(value = "/history", method = RequestMethod.DELETE)
    public BaseResponse delete() {
        redisService.hdelete(REDIS_KEY, commonUtil.getOperatorId());
        return BaseResponse.SUCCESSFUL();
    }
}
