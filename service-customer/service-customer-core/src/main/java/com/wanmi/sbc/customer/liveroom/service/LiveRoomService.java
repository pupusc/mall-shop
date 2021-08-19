package com.wanmi.sbc.customer.liveroom.service;


import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.LiveErrCodeUtil;
import com.wanmi.sbc.common.util.MediaIdUtil;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomListRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomUpdateRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomCreateResponse;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomPageAllResponse;
import com.wanmi.sbc.customer.api.response.liveroom.LiveRoomPageResponse;
import com.wanmi.sbc.customer.bean.enums.LiveRoomStatus;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.livecompany.model.root.LiveCompany;
import com.wanmi.sbc.customer.livecompany.service.LiveCompanyService;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.liveroom.repository.LiveRoomRepository;
import com.wanmi.sbc.customer.liveroom.model.root.LiveRoom;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomQueryRequest;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>直播间业务逻辑</p>
 *
 * @author zwb
 * @date 2020-06-06 18:28:57
 */
@Service("LiveRoomService")
public class LiveRoomService {
    @Autowired
    private LiveRoomRepository liveRoomRepository;

    private static final Logger log = LoggerFactory.getLogger(LiveRoomService.class);

    private String createLiveRoomListUrl = "https://api.weixin.qq.com/wxaapi/broadcast/room/create?access_token=";

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private StoreService storeService;

    @Autowired
    private LiveCompanyService liveCompanyService;


    /**
     * 创建直播间
     *
     * @author zwb
     */
    @Transactional
    public LiveRoom add(LiveRoom entity, String accessToken) {
        //拼接Url
        String url = createLiveRoomListUrl+accessToken;
        Map<String, Object> map = new HashMap<>();
        map.put("name", entity.getName());
        //时间转换成秒
        map.put("startTime", entity.getStartTime().toEpochSecond(ZoneOffset.of("+8")));
        map.put("endTime", entity.getEndTime().toEpochSecond(ZoneOffset.of("+8")));
        map.put("anchorName", entity.getAnchorName());
        map.put("anchorWechat", entity.getAnchorWechat());
        map.put("screenType", entity.getScreenType());
        map.put("type", entity.getType());
        map.put("closeLike", entity.getCloseLike());
        map.put("closeGoods", entity.getCloseGoods());
        map.put("closeComment", entity.getCloseComment());
        //调用微信接口上传文件，查询mediaId
        String coverImg = null;
        String shareImg = null;
        try {
            //将图片保存到本地，再根据图片的路径去获取media_id
            String coverImgUrl = MediaIdUtil.uploadURL(entity.getCoverImg());
            String shareImgUrl = MediaIdUtil.uploadURL(entity.getShareImg());
            coverImg = MediaIdUtil.uploadFile(coverImgUrl, accessToken, "image");
            shareImg = MediaIdUtil.uploadFile(shareImgUrl, accessToken, "image");
            //删除本地图片
           File file = new File(coverImgUrl);
            if (file.exists()){
                file.delete();
            }
            File shareImgFile = new File(shareImgUrl);
            if (shareImgFile.exists()){
                shareImgFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("shareImg", shareImg);
        map.put("coverImg", coverImg);

        //请求微信创建直播间接口
        String result = restTemplate.postForObject(url, map, String.class);
        LiveRoomCreateResponse resp = JSONObject.parseObject(result, LiveRoomCreateResponse.class);
        if (resp.getErrcode() != 0) {
            log.error("微信创建直播间异常，返回信息：" + resp.toString());
            throw new SbcRuntimeException(resp.getErrcode().toString(), LiveErrCodeUtil.getErrCodeMessage(resp.getErrcode()));
        }
        entity.setLiveStatus(LiveRoomStatus.THREE);
        entity.setRoomId(resp.getRoomId());
        entity.setRecommend(0);
        liveRoomRepository.save(entity);
        return entity;
    }

    /**
     * 修改直播间
     *
     * @author zwb
     */
    @Transactional
    public LiveRoom modify(LiveRoom entity) {
        liveRoomRepository.save(entity);
        return entity;
    }

    /**
     * 定时任务修改直播间状态
     *
     * @author zwb
     */
    @Transactional
    public void update(LiveRoomUpdateRequest request) {
        Map<LiveRoomStatus, List<LiveRoomUpdateRequest>> liveRoomList = request.getLiveRoomList();
        for (LiveRoomStatus liveRoomStatus : liveRoomList.keySet()) {
            List<Long> roomsIdList = liveRoomList.get(liveRoomStatus).stream()
                    .map(LiveRoomUpdateRequest::getRoomId)
                    .collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(roomsIdList)) {
                liveRoomRepository.updateStatusByRoomIdList(liveRoomStatus, roomsIdList);
            }
        }


    }


    /**
     * 单个删除直播间
     *
     * @author zwb
     */
    @Transactional
    public void deleteById(LiveRoom entity) {
        liveRoomRepository.save(entity);
    }

    /**
     * 批量删除直播间
     *
     * @author zwb
     */
    @Transactional
    public void deleteByIdList(List<LiveRoom> infos) {
        liveRoomRepository.saveAll(infos);
    }

    /**
     * 单个查询直播间
     *
     * @author zwb
     */
    public LiveRoom getOne(Long id) {
        return liveRoomRepository.findById(id)
                .orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "直播间不存在"));
    }

    /**
     * 分页查询直播间
     *
     * @author zwb
     */
    public Page<LiveRoom> page(LiveRoomQueryRequest queryReq) {
        return liveRoomRepository.findAll(
                LiveRoomWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询直播间
     *
     * @author zwb
     */
    public List<LiveRoom> list(LiveRoomQueryRequest queryReq) {
        return liveRoomRepository.findAll(LiveRoomWhereCriteriaBuilder.build(queryReq));
    }

    /**
     * 将实体包装成VO
     *
     * @author zwb
     */
    public LiveRoomVO wrapperVo(LiveRoom liveRoom) {
        if (liveRoom != null) {
            LiveRoomVO liveRoomVO = KsBeanUtil.convert(liveRoom, LiveRoomVO.class);
            return liveRoomVO;
        }
        return null;
    }

    /**
     * 修改直播间是否推荐
     * @param recommend
     * @param roomId
     */
    @Transactional
    public void recommend(Integer recommend, Long roomId) {
        liveRoomRepository.updateRecommendByRoomId(recommend,roomId);
    }

    public LiveRoomPageAllResponse pageNew(LiveRoomQueryRequest pageReq){
        List<LiveRoom> liveRoomVOList;
        Map<Long, String> storeName = new HashMap<>();
        LiveRoomPageAllResponse result = new LiveRoomPageAllResponse();
        if (StringUtils.isNotBlank(pageReq.getStoreName())) {
            //查询所有匹配的storeId
            List<Store> storeSimpleInfos = storeService.queryStoreByName(pageReq.getStoreName());
            storeSimpleInfos = storeSimpleInfos.stream()
                    .filter(liveRoomVO -> {
                        if(Objects.isNull(liveRoomVO.getStoreId())){
                            return  false;
                        }
                        //过滤已关店和已过期的店铺
                        Store storeVO = storeService.findOne(liveRoomVO.getStoreId());
                        if (Objects.nonNull(storeVO)&&Objects.nonNull(storeVO.getContractEndDate())&& Objects.nonNull(storeVO.getStoreState())) {
                            if(storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING){
                                return true;
                            }
                        }
                        return false;
                    }).collect(Collectors.toList());
            Map<Long, String> storeIdAndNameMap = storeSimpleInfos.stream()
                    .filter(c -> c.getStoreId() != null)
                    .collect(Collectors.toMap(Store::getStoreId, c -> {
                        String name = c.getStoreName();
                        if (StringUtils.isEmpty(name)) {
                            name = "-";
                        }
                        return name;
                    }, (oldValue, newValue) -> newValue));

            //查询所有结果
            liveRoomVOList = this.list(pageReq);

            List<LiveRoom> collect = new ArrayList<>();
            for (LiveRoom liveRoomVO : liveRoomVOList) {
                if (storeIdAndNameMap.keySet().contains(liveRoomVO.getStoreId())) {
                    //过滤已禁用的商家
                    LiveCompany liveCompanyVO = liveCompanyService.getOne(liveRoomVO.getStoreId());
                    if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
                        collect.add(liveRoomVO);
                        //封装所属店铺名称
                        storeName.put(liveRoomVO.getStoreId(), storeIdAndNameMap.get(liveRoomVO.getStoreId()));
                    }

                }
            }


            PageImpl<LiveRoomVO> newPage = new PageImpl(collect, pageReq.getPageable(), collect.size());
            MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
            result.setLiveRoomVOPage(microPage);
            result.setStoreName(storeName);
        } else {
//            pageReq.setStoreId(commonUtil.getStoreId());
            Page<LiveRoom> add = this.page(pageReq);
            //获取分页对象数据
            List<LiveRoom> content = add.getContent().stream().filter(liveRoomVO -> Objects.nonNull(liveRoomVO.getStoreId())).collect(Collectors.toList());


           // LiveCompanyByIdRequest liveCompanyByIdRequest = new LiveCompanyByIdRequest();
            //查询店铺状态是否已过期/是否禁用
             liveRoomVOList = content.stream().map(liveRoomVO -> {
                Store storeVO = storeService.findOne(liveRoomVO.getStoreId());
                if (storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING) {
                    //过滤已禁用的商家
                  //  liveCompanyByIdRequest.setStoreId(liveRoomVO.getStoreId());
                    LiveCompany liveCompanyVO = liveCompanyService.getOne(liveRoomVO.getStoreId());
                    if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
                        //获取所属店铺名称
                        if (StringUtils.isEmpty(storeVO.getStoreName())) {
                            storeName.put(liveRoomVO.getStoreId(), "-");
                        } else {
                            storeName.put(liveRoomVO.getStoreId(), storeVO.getStoreName());
                        }
                        return liveRoomVO;
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
            PageImpl<LiveRoomVO> newPage = new PageImpl(liveRoomVOList, pageReq.getPageable(), liveRoomVOList.size());
            MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
            result.setLiveRoomVOPage(microPage);
            result.setStoreName(storeName);
        }
        return result;
    }
}

