package com.wanmi.sbc.liveroom;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyQueryProvider;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomQueryProvider;
import com.wanmi.sbc.customer.api.provider.liveroomreplay.LiveRoomReplayQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomListRequest;
import com.wanmi.sbc.customer.api.request.liveroom.LiveRoomPageRequest;
import com.wanmi.sbc.customer.api.request.liveroomreplay.LiveRoomReplayByRoomIdRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyReponse;
import com.wanmi.sbc.customer.api.response.liveroom.*;
import com.wanmi.sbc.customer.bean.enums.LiveRoomStatus;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel.LiveRoomLiveGoodsRelQueryProvider;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelByRoomIdRequest;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListResponse;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Api(description = "直播间管理API", tags = "LiveRoomController")
@RestController
@RequestMapping(value = "/liveroomweb")
public class LiveRoomMobileController {

    @Autowired
    private LiveRoomQueryProvider liveRoomQueryProvider;

    @Autowired
    private LiveGoodsQueryProvider liveGoodsQueryProvider;
    @Autowired
    private LiveRoomLiveGoodsRelQueryProvider liveRoomLiveGoodsRelQueryProvider;
    @Autowired
    private StoreQueryProvider storeQueryProvider;
    @Autowired
    private LiveCompanyQueryProvider liveCompanyQueryProvider;
    @Autowired
    private LiveRoomReplayQueryProvider liveRoomReplayQueryProvider;
    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;
    @Autowired
    private DateUtils dateUtils;


    @ApiOperation(value = "分页查询直播间")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<LiveRoomPageMobileResponse> getPage(@RequestBody @Valid LiveRoomPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        int count = 0;
        if (pageReq.getLiveStatus() != null && pageReq.getLiveStatus() == LiveRoomStatus.ZERO) {
            pageReq.setLiveStatus(null);
            count = 1;
        }
        BaseResponse<LiveRoomPageResponse> add = liveRoomQueryProvider.page(pageReq);
        //获取分页对象数据
        List<LiveRoomVO> collect = add.getContext().getLiveRoomVOPage().getContent().stream().filter(liveRoomVO -> Objects.nonNull(liveRoomVO.getStoreId())).collect(Collectors.toList());
        //过滤已过期，禁播的商家直播
        LiveCompanyByIdRequest liveCompanyByIdRequest = new LiveCompanyByIdRequest();
        //查询店铺状态是否已过期/是否禁用
        List<LiveRoomVO> liveRoomList = collect.stream().map(liveRoomVO -> {
            StoreVO storeVO = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(liveRoomVO.getStoreId())).getContext().getStoreVO();
            if (storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING) {
                //过滤已禁用的商家
                liveCompanyByIdRequest.setStoreId(liveRoomVO.getStoreId());
                LiveCompanyVO liveCompanyVO = liveCompanyQueryProvider.getById(liveCompanyByIdRequest).getContext().getLiveCompanyVO();
                if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
                    return liveRoomVO;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //过滤掉已过期
        List<LiveRoomVO> content = liveRoomList.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() != LiveRoomStatus.SIX && liveRoomVO.getLiveStatus() != LiveRoomStatus.FIVE).collect(Collectors.toList());
        if (count == 1) {
            content = content.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() != LiveRoomStatus.THREE && liveRoomVO.getLiveStatus() != LiveRoomStatus.FOUR).collect(Collectors.toList());
        }
       /* //获取直播中的数量
        Long liveCount = content.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() == 0).count();
        //获取未开始的数量
        Long foreShowCount = content.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() == 1).count();
        //获取直播回放的数量
        Long playbackCount = content.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() == 2).count();*/
        //封装时间格式
        content.stream()
                .filter(liveRoomVO -> liveRoomVO.getLiveStatus() == LiveRoomStatus.THREE).forEach(liveRoomVO -> {
            //localDate转化成date
            Date date = Date.from(liveRoomVO.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
            liveRoomVO.setStartTimeSting(dateUtils.getTime(date));
        });
        //根据直播状态为已结束和roomId查询直播回放
        Map<Long, List<LiveRoomReplayVO>> replayList = content.stream()
                .filter(liveRoomVO -> liveRoomVO.getLiveStatus() == LiveRoomStatus.FOUR)
                .collect(Collectors.toMap(LiveRoomVO::getRoomId, c ->
                      liveRoomReplayQueryProvider.getByRoomId(new LiveRoomReplayByRoomIdRequest(c.getRoomId()))
                              .getContext().getLiveRoomReplayVOList()
        ));
        //根据storeId查询店铺信息（名称和logo）
        Map<Long, StoreVO> storeVOMap = content.stream()
                .collect(Collectors.toMap(LiveRoomVO::getStoreId, c ->
                        storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(c.getStoreId())).getContext().getStoreVO()
                , (oldValue, newValue) -> newValue)
        );
        //根据roomId查询直播间商品
        Map<Long, List<LiveGoodsByWeChatVO>> liveGoodsList = content.stream()
                .collect(Collectors.toMap(LiveRoomVO::getRoomId, c -> {
                    //根据roomId查询中间表 查到对应的goodsId集合
                    BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider
                            .getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(c.getRoomId()));
                    //根据goodId 去查询商品信息 返回商品的图片
                    //返回图片集合 作为map的值
                    return list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
                                LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(),
                                        c.getStoreId())).getContext().getLiveGoodsVO();
                                return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
                            }
                    ).collect(Collectors.toList());
                }
        ));
        LiveRoomPageMobileResponse result = new LiveRoomPageMobileResponse();

        PageImpl<LiveRoomVO> newPage = new PageImpl<>(content, pageReq.getPageable(), content.size());
        MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
        result.setLiveRoomVOPage(microPage);
        result.setLiveGoodsList(liveGoodsList);
        result.setStoreVO(storeVOMap);
       /* result.setLiveCount(liveCount);
        result.setForeShowCount(foreShowCount);*/
        result.setLiveRoomReplayVOList(replayList);
        // result.setPlaybackCount(playbackCount);
        return BaseResponse.success(result);
    }


    @ApiOperation(value = "列表查询直播间")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public BaseResponse<LiveRoomListResponse> getList(@RequestBody @Valid LiveRoomListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        BaseResponse<LiveRoomListResponse> list = liveRoomQueryProvider.list(listReq);
        List<LiveRoomVO> liveRoomVOList = list.getContext().getLiveRoomVOList();

        LiveCompanyByIdRequest liveCompanyByIdRequest = new LiveCompanyByIdRequest();
        //查询店铺状态是否已过期/是否禁用
        List<LiveRoomVO> liveRoomList = liveRoomVOList.stream().map(liveRoomVO -> {
            StoreVO storeVO = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(liveRoomVO.getStoreId())).getContext().getStoreVO();
            if (storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING) {
                //过滤已禁用的商家
                liveCompanyByIdRequest.setStoreId(liveRoomVO.getStoreId());
                LiveCompanyVO liveCompanyVO = liveCompanyQueryProvider.getById(liveCompanyByIdRequest).getContext().getLiveCompanyVO();
                if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
                    return liveRoomVO;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //过滤掉已过期
        List<LiveRoomVO> content = liveRoomList.stream().filter(liveRoomVO -> liveRoomVO.getLiveStatus() != LiveRoomStatus.SIX).collect(Collectors.toList());
        //封装时间
        content.stream().forEach(liveRoomVO -> {
            //localDate转化成date
            Date date = Date.from(liveRoomVO.getStartTime().atZone(ZoneId.systemDefault()).toInstant());
            liveRoomVO.setStartTimeSting(dateUtils.getTime(date));
        });
        return BaseResponse.success(new LiveRoomListResponse(content));
    }

    @ApiOperation(value = "根据id查询直播间")
    @GetMapping("/{id}")
    public BaseResponse<LiveRoomByIdResponse> getById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveRoomByIdRequest idReq = new LiveRoomByIdRequest();
        idReq.setId(id);
        return liveRoomQueryProvider.getById(idReq);
    }


    /**
     * 查询直播是否开启
     *
     * @return
     */
    @ApiOperation(value = "查询直播是否开启")
    @RequestMapping(value = "/isOpen", method = RequestMethod.GET)
    public BaseResponse<SystemConfigResponse> isLiveOpen() {
        ConfigQueryRequest request = new ConfigQueryRequest();
        request.setDelFlag(0);
        request.setConfigKey("liveSwitch");
        request.setConfigType("liveSwitch");
        return systemConfigQueryProvider.findByConfigKeyAndDelFlag(request);
    }
}
