package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsProvider;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsUpdateRequest;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsListByWeChatResponse;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.util.MiniProgramUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 商品列表定时任务
 */
@Component
@Slf4j
@JobHandler(value="LiveGoodsJobHandler")
public class LiveGoodsJobHandler extends IJobHandler {

    private String liveGoodsListUrl = "https://api.weixin.qq.com/wxaapi/broadcast/goods/getapproved?access_token=";
    @Autowired
    private MiniProgramUtil miniProgramUtil;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LiveGoodsProvider liveGoodsProvider;


    private Integer offset = 0;
    private Integer limit = 100;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("LiveGoodsGoodsTask定时任务执行开始： " + LocalDateTime.now());
        //获取accessToken
        String accessToken = miniProgramUtil.getToken();
        //拼接Url
        String url = liveGoodsListUrl+accessToken+"&offset={offset}&limit={limit}&status={status}";
        for (int i = 0; i < 4; i++) {
            offset=0;
            //请求参数
            Map<String, Integer> map = new HashMap<>();
            map.put("offset",offset);
            map.put("limit",limit);
            map.put("status",i);
            //调用微信接口查询商品列表
            LiveGoodsListByWeChatResponse resp = getLiveGoodsList(url, map);
            List<Long> goodsIdList = resp.getGoods().stream()
                    .map(LiveGoodsVO::getGoodsId)
                    .collect(Collectors.toList());
            //批量修改数据库状态
            liveGoodsProvider.update(LiveGoodsUpdateRequest.builder()
                    .goodsIdList(goodsIdList)
                    .auditStatus(i)
                    .build());
            //获取总条数
            Integer total = resp.getTotal();

            //循环获取数据
            if (limit<total) {
                while (offset < total) {
                    offset = offset+ limit + 1;
                    map.put("offset", offset);
                    liveGoodsProvider.update(LiveGoodsUpdateRequest.builder()
                            .goodsIdList(getLiveGoodsList(url, map).getGoods().stream()
                                    .map(LiveGoodsVO::getGoodsId)
                                    .collect(Collectors.toList()))
                            .auditStatus(i)
                            .build());
                }
            }
        }
        XxlJobLogger.log("LiveGoodsGoodsTask定时任务执行结束： " + LocalDateTime.now()  );
        return SUCCESS;
    }




    /**
     * 请求微信直播商品列表接口
     */
        private  LiveGoodsListByWeChatResponse  getLiveGoodsList(String url,Map<String,Integer> map){
            //调用微信接口查询商品列表
            String result = restTemplate.getForObject(url,String.class, map);
            LiveGoodsListByWeChatResponse resp = JSONObject.parseObject(result, LiveGoodsListByWeChatResponse.class);
            if (resp.getErrcode() != 0) {
                log.error("查询直播商品列表异常，返回信息：" + resp.toString());
                throw new RuntimeException("查询直播商品列表异常");
            }
          return resp;
        }


}
