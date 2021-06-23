package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomProvider;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomModifyRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomUpdateRequest;
import com.wanmi.sbc.customer.bean.enums.LiveRoomStatus;
import com.wanmi.sbc.customer.bean.vo.LiveRoomByWeChatVO;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsUpdateRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.MiniProgramUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 直播列表定时任务
 */
@Component
@Slf4j
@JobHandler(value = "LiveRoomJobHandler")
public class LiveRoomJobHandler extends IJobHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MiniProgramUtil miniProgramUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LiveRoomProvider liveRoomProvider;


    private Integer start = 0;

    private Integer limit = 100;


    private String liveRoomListUrl = "https://api.weixin.qq.com/wxa/business/getliveinfo?access_token=";

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //查询直播房间列表
        getLiveInfo(start, limit);

        return SUCCESS;
    }


    /**
     * 获取小程序直播房间列表
     *
     * @return
     */
    public void getLiveInfo(Integer start, Integer limit) {

        JSONObject jsonObject = getLiveRoomList(start);

        //获取roomInfo信息
        String roomInfo = jsonObject.getString("room_info");
        //转换成集合
        List<LiveRoomByWeChatVO> roomInfoList = JSON.parseArray(roomInfo, LiveRoomByWeChatVO.class);
        //存入数据库
        List<LiveRoomUpdateRequest> liveRoomUpdateRequestList = roomInfoList.stream().map(liveRoom -> {
            LiveRoomUpdateRequest request = new LiveRoomUpdateRequest();
            //修改状态
            switch (liveRoom.getLiveStatus()) {
                case 101:
                    request.setLiveStatus(LiveRoomStatus.ZERO);
                    break;
                case 102:
                    request.setLiveStatus(LiveRoomStatus.THREE);
                    break;
                case 103:
                    request.setLiveStatus(LiveRoomStatus.FOUR);
                    break;
                case 104:
                    request.setLiveStatus(LiveRoomStatus.FIVE);
                    break;
                case 105:
                    request.setLiveStatus(LiveRoomStatus.ONE);
                    break;
                case 106:
                    request.setLiveStatus(LiveRoomStatus.TOW);
                    break;
                case 107:
                    request.setLiveStatus(LiveRoomStatus.SIX);
                    break;
                default:
                    System.out.println("无状态");

            }
            request.setRoomId(liveRoom.getRoomId());
            request.setDelFlag(DeleteFlag.NO);
            return request;
        }).collect(Collectors.toList());

       //获取总条数
        Integer total = (Integer) jsonObject.get("total");
        //循环获取数据
        if (limit < total) {
            while (start < total) {
                start = start+limit + 1;
                //分页循环获取数据，存入数据库
                JSON.parseArray(getLiveRoomList(start).getString("room_info"), LiveRoomByWeChatVO.class).stream().forEach(liveRoom -> {
                    LiveRoomUpdateRequest request = KsBeanUtil.convert(liveRoom, LiveRoomUpdateRequest.class);
                    //修改状态
                    switch(liveRoom.getLiveStatus()) {
                        case 101:
                            request.setLiveStatus(LiveRoomStatus.ZERO);
                            break;
                        case 102:
                            request.setLiveStatus(LiveRoomStatus.THREE);
                            break;
                        case 103:
                            request.setLiveStatus(LiveRoomStatus.FOUR);
                            break;
                        case 104:
                            request.setLiveStatus(LiveRoomStatus.FIVE);
                            break;
                        case 105:
                            request.setLiveStatus(LiveRoomStatus.ONE);
                            break;
                        case 106:
                            request.setLiveStatus(LiveRoomStatus.TOW);
                            break;
                        case 107:
                            request.setLiveStatus(LiveRoomStatus.SIX);
                            break;
                        default:
                            System.out.println("无状态");
                    }
                    request.setRoomId(liveRoom.getRoomId());
                    request.setDelFlag(DeleteFlag.NO);
                    liveRoomUpdateRequestList.add(request);
                });
            }
        }
        //将所查询出所有的直播间按照状态分组
        Map<LiveRoomStatus, List<LiveRoomUpdateRequest>> liveRoomList = liveRoomUpdateRequestList.stream()
                .collect(Collectors.groupingBy(LiveRoomUpdateRequest::getLiveStatus));
        //批量修改
        liveRoomProvider.update(LiveRoomUpdateRequest.builder().liveRoomList(liveRoomList).build());
    }

      public JSONObject getLiveRoomList(Integer start){
          String accessToken = miniProgramUtil.getToken();
          String url = liveRoomListUrl + accessToken;
          Map<String, Integer> map = new HashMap<>();
          map.put("start", start);
          map.put("limit", limit);
          String result = restTemplate.postForObject(url, map, String.class);
          JSONObject jsonObject = JSON.parseObject(result);
          if (0 != (Integer) jsonObject.get("errcode")) {
              log.error("查询直播列表异常，返回信息：" + result);
              throw new RuntimeException("查询直播列表异常");
          }
          return jsonObject;
      }

}
