package com.wanmi.sbc.liveroom;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.annotation.MultiSubmit;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.LiveErrCodeUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.excel.Column;
import com.wanmi.sbc.common.util.excel.ExcelHelper;
import com.wanmi.sbc.common.util.excel.impl.SpelColumnRender;
import com.wanmi.sbc.customer.api.provider.livecompany.LiveCompanyQueryProvider;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomQueryProvider;
import com.wanmi.sbc.customer.api.provider.liveroom.LiveRoomProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.livecompany.LiveCompanyByIdRequest;
import com.wanmi.sbc.customer.api.request.liveroom.*;
import com.wanmi.sbc.customer.api.request.store.ListStoreByNameRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyReponse;
import com.wanmi.sbc.customer.api.response.liveroom.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.wanmi.sbc.customer.api.response.store.NoDeleteStoreByIdResponse;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsProvider;
import com.wanmi.sbc.goods.api.provider.livegoods.LiveGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.liveroomlivegoodsrel.LiveRoomLiveGoodsRelQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsAddRequest;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.livegoods.LiveGoodsListRequest;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelByRoomIdRequest;
import com.wanmi.sbc.goods.api.request.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListByRoomIdRequest;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.livegoods.LiveGoodsPageResponse;
import com.wanmi.sbc.goods.api.response.liveroomlivegoodsrel.LiveRoomLiveGoodsRelListResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.LiveGoodsVO;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.MiniProgramUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;


@Api(description = "直播间管理API", tags = "LiveRoomController")
@RestController
@RequestMapping(value = "/liveroom")
public class LiveRoomController {

    @Autowired
    private LiveRoomQueryProvider liveRoomQueryProvider;
    @Autowired
    private SystemConfigQueryProvider systemConfigQueryProvider;

    @Autowired
    private LiveRoomProvider liveRoomProvider;

    @Autowired
    private LiveGoodsProvider liveGoodsProvider;

    @Autowired
    private CommonUtil commonUtil;
    @Autowired
    private MiniProgramUtil miniProgramUtil;
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private LiveGoodsQueryProvider liveGoodsQueryProvider;
    @Autowired
    private LiveRoomLiveGoodsRelQueryProvider liveRoomLiveGoodsRelQueryProvider;
    @Autowired
    private StoreQueryProvider storeQueryProvider;


    @ApiOperation(value = "分页查询直播间")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<LiveRoomPageAllResponse> getPage(@RequestBody @Valid LiveRoomPageRequest pageReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        pageReq.setDelFlag(DeleteFlag.NO);
        pageReq.putSort("startTime", "desc");
        pageReq.putSort("id", "desc");
        if (StringUtils.isBlank(pageReq.getStoreName())) {
            pageReq.setStoreId(commonUtil.getStoreId());
        }
        LiveRoomPageAllResponse allResponse = liveRoomQueryProvider.pageNew(pageReq).getContext();
        List<LiveRoomVO> liveRoomVOList = allResponse.getLiveRoomVOPage().getContent();
        Map<Long, Long> roomIdAndStoreIdMap = liveRoomVOList.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, LiveRoomVO::getStoreId));
        Map<Long, List<LiveGoodsByWeChatVO>> liveGoodsList = liveRoomLiveGoodsRelQueryProvider.listByRoomId(new LiveRoomLiveGoodsRelListByRoomIdRequest(roomIdAndStoreIdMap))
                                                                .getContext().getResult();
//        if (StringUtils.isNotBlank(pageReq.getStoreName())){
//            //根据roomId查询直播间商品
//            liveGoodsList = liveRoomVOList.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, c -> {
//                        //根据roomId查询中间表 查到对应的goodsId集合
//                        BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider.getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(c.getRoomId()));
//                        //根据goodId 去查询商品信息 返回商品的图片
//                        List<LiveGoodsByWeChatVO> goodsVOList = list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
//                                    LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(), c.getStoreId())).getContext().getLiveGoodsVO();
//                                    return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
//                                }
//                        ).collect(Collectors.toList());
//                        //返回图片集合 作为map的值
//                        return goodsVOList;
//                    }
//            ));
//
//            liveGoodsList = liveRoomVOList.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, LiveRoomVO::getStoreId);
//        }else{
//            //根据roomId查询直播间商品
//           liveGoodsList = liveRoomVOList.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, c -> {
//                        //根据roomId查询中间表 查到对应的goodsId集合
//                        BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider.getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(c.getRoomId()));
//                        //根据goodId 去查询商品信息 返回商品的图片
//                        List<LiveGoodsByWeChatVO> goodsVOList = list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
//                                    LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(), c.getStoreId())).getContext().getLiveGoodsVO();
//                                    return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
//                                }
//                        ).collect(Collectors.toList());
//                        //返回图片集合 作为map的值
//                        return goodsVOList;
//                    }
//            ));
//        }
        allResponse.setLiveGoodsList(liveGoodsList);
        return BaseResponse.success(allResponse);
        //根据所属店铺名称模糊查询
//        if (StringUtils.isNotBlank(pageReq.getStoreName())) {
//            //查询所有匹配的storeId
//            List<StoreSimpleInfo> storeSimpleInfos = storeQueryProvider.listByStoreName(new ListStoreByNameRequest(pageReq.getStoreName())).getContext().getStoreSimpleInfos();
//            storeSimpleInfos = storeSimpleInfos.stream()
//                    .filter(liveRoomVO -> {
//                        if(Objects.isNull(liveRoomVO.getStoreId())){
//                            return  false;
//                        }
//                        //过滤已关店和已过期的店铺
//                        StoreVO storeVO = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(liveRoomVO.getStoreId()))
//                                .getContext().getStoreVO();
//                        if (Objects.nonNull(storeVO)&&Objects.nonNull(storeVO.getContractEndDate())&& Objects.nonNull(storeVO.getStoreState())) {
//                            if(storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING){
//                                return true;
//                            }
//                        }
//                        return false;
//                    }).collect(Collectors.toList());
//            Map<Long, String> storeIdAndNameMap = storeSimpleInfos.stream()
//                    .filter(c -> c.getStoreId() != null)
//                    .collect(Collectors.toMap(StoreSimpleInfo::getStoreId, c -> {
//                        String name = c.getStoreName();
//                        if (StringUtils.isEmpty(name)) {
//                            name = "-";
//                        }
//                        return name;
//                    }, (oldValue, newValue) -> newValue));
//            //用于获取所属店铺名称
//            Map<Long, String> storeName = new HashMap<>();
//            //查询所有结果
//            LiveRoomListRequest request = KsBeanUtil.convert(pageReq, LiveRoomListRequest.class);
//            List<LiveRoomVO> liveRoomVOList = liveRoomQueryProvider.list(request).getContext().getLiveRoomVOList();
//
//            List<LiveRoomVO> collect = new ArrayList<>();
//            //映射
//            LiveCompanyByIdRequest liveCompanyByIdRequest = new LiveCompanyByIdRequest();
//            for (LiveRoomVO liveRoomVO : liveRoomVOList) {
//                if (storeIdAndNameMap.keySet().contains(liveRoomVO.getStoreId())) {
//                    //过滤已禁用的商家
//                    liveCompanyByIdRequest.setStoreId(liveRoomVO.getStoreId());
//                    LiveCompanyVO liveCompanyVO = liveCompanyQueryProvider.getById(liveCompanyByIdRequest).getContext().getLiveCompanyVO();
//                    if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
//                        collect.add(liveRoomVO);
//                        //封装所属店铺名称
//                        storeName.put(liveRoomVO.getStoreId(), storeIdAndNameMap.get(liveRoomVO.getStoreId()));
//                    }
//
//                }
//            }
//
//            //根据roomId查询直播间商品
//            Map<Long, List<LiveGoodsByWeChatVO>> liveGoodsList = collect.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, c -> {
//                        //根据roomId查询中间表 查到对应的goodsId集合
//                        BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider.getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(c.getRoomId()));
//                        //根据goodId 去查询商品信息 返回商品的图片
//                        List<LiveGoodsByWeChatVO> goodsVOList = list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
//                                    LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(), c.getStoreId())).getContext().getLiveGoodsVO();
//                                    return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
//                                }
//                        ).collect(Collectors.toList());
//                        //返回图片集合 作为map的值
//                        return goodsVOList;
//                    }
//            ));
//            LiveRoomPageAllResponse result = new LiveRoomPageAllResponse();
//            PageImpl<LiveRoomVO> newPage = new PageImpl<>(collect, pageReq.getPageable(), collect.size());
//            MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
//            result.setLiveRoomVOPage(microPage);
//            result.setLiveGoodsList(liveGoodsList);
//            result.setStoreName(storeName);
//            return BaseResponse.success(result);
//        } else {
//            pageReq.setStoreId(commonUtil.getStoreId());
//            BaseResponse<LiveRoomPageResponse> add = liveRoomQueryProvider.page(pageReq);
//            //获取分页对象数据
//            List<LiveRoomVO> content = add.getContext().getLiveRoomVOPage().getContent().stream().filter(liveRoomVO -> Objects.nonNull(liveRoomVO.getStoreId())).collect(Collectors.toList());
//
//            Map<Long, String> storeName = new HashMap<>();
//            LiveCompanyByIdRequest liveCompanyByIdRequest = new LiveCompanyByIdRequest();
//            //查询店铺状态是否已过期/是否禁用
//            List<LiveRoomVO> liveRoomVOList = content.stream().map(liveRoomVO -> {
//                StoreVO storeVO = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(liveRoomVO.getStoreId())).getContext().getStoreVO();
//                if (storeVO.getContractEndDate().isAfter(LocalDateTime.now()) && storeVO.getStoreState() == StoreState.OPENING) {
//                    //过滤已禁用的商家
//                    liveCompanyByIdRequest.setStoreId(liveRoomVO.getStoreId());
//                    LiveCompanyVO liveCompanyVO = liveCompanyQueryProvider.getById(liveCompanyByIdRequest).getContext().getLiveCompanyVO();
//                    if (Objects.nonNull(liveCompanyVO) && liveCompanyVO.getLiveBroadcastStatus() == 2) {
//                        //获取所属店铺名称
//                        if (StringUtils.isEmpty(storeVO.getStoreName())) {
//                            storeName.put(liveRoomVO.getStoreId(), "-");
//                        } else {
//                            storeName.put(liveRoomVO.getStoreId(), storeVO.getStoreName());
//                        }
//                        return liveRoomVO;
//                    } else {
//                        return null;
//                    }
//                } else {
//                    return null;
//                }
//            }).filter(Objects::nonNull).collect(Collectors.toList());
//
//            //根据roomId查询直播间商品
//            Map<Long, List<LiveGoodsByWeChatVO>> liveGoodsList = liveRoomVOList.stream().collect(Collectors.toMap(LiveRoomVO::getRoomId, c -> {
//                        //根据roomId查询中间表 查到对应的goodsId集合
//                        BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider.getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(c.getRoomId()));
//                        //根据goodId 去查询商品信息 返回商品的图片
//                        List<LiveGoodsByWeChatVO> goodsVOList = list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
//                                    LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(), c.getStoreId())).getContext().getLiveGoodsVO();
//                                    return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
//                                }
//                        ).collect(Collectors.toList());
//                        //返回图片集合 作为map的值
//                        return goodsVOList;
//                    }
//            ));
//            LiveRoomPageAllResponse result = new LiveRoomPageAllResponse();
//            PageImpl<LiveRoomVO> newPage = new PageImpl<>(liveRoomVOList, pageReq.getPageable(), liveRoomVOList.size());
//            MicroServicePage<LiveRoomVO> microPage = new MicroServicePage<>(newPage, pageReq.getPageable());
//            result.setLiveRoomVOPage(microPage);
//            result.setLiveGoodsList(liveGoodsList);
//            result.setStoreName(storeName);
//            return BaseResponse.success(result);
//        }

    }


    @ApiOperation(value = "列表查询直播间")
    @PostMapping("/list")
    public BaseResponse<LiveRoomListResponse> getList(@RequestBody @Valid LiveRoomListRequest listReq) {
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        return liveRoomQueryProvider.list(listReq);
    }

    @ApiOperation(value = "根据id查询直播间详情")
    @GetMapping("/{id}")
    public BaseResponse<LiveRoomByIdResponse> getById(@PathVariable Long id) {
        //判断直播开关开关是否开启
        this.isOpen();
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveRoomByIdRequest idReq = new LiveRoomByIdRequest();
        idReq.setId(id);
        //查询单个直播间信息
        LiveRoomVO liveRoomVO = liveRoomQueryProvider.getById(idReq).getContext().getLiveRoomVO();
        //根据直播间id查询中间表goodsId
        BaseResponse<LiveRoomLiveGoodsRelListResponse> list = liveRoomLiveGoodsRelQueryProvider.
                getByRoomId(new LiveRoomLiveGoodsRelByRoomIdRequest(liveRoomVO.getRoomId()));
        //根据goodId查询商品详情集合
        List<LiveGoodsByWeChatVO> goodsVOList = list.getContext().getLiveRoomLiveGoodsRelVOList().stream().map(i -> {
                    LiveGoodsVO liveGoodsVO = liveGoodsQueryProvider.getById(new LiveGoodsByIdRequest(i.getGoodsId(), liveRoomVO.getStoreId())).getContext().getLiveGoodsVO();
                    //实时获取库存展示
                    if (StringUtils.isNotBlank(liveGoodsVO.getGoodsInfoId())) {
                        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(new GoodsInfoByIdRequest(liveGoodsVO.getGoodsInfoId(), liveGoodsVO.getStoreId())).getContext();
                        if (Objects.nonNull(goodsInfo) && Objects.nonNull(goodsInfo.getStock())) {
                            liveGoodsVO.setStock(goodsInfo.getStock());
                        } else {
                            liveGoodsVO.setStock(0L);
                        }
                    }
                    return KsBeanUtil.convert(liveGoodsVO, LiveGoodsByWeChatVO.class);
                }
        ).collect(Collectors.toList());

        List<String> goodsInfoList = goodsVOList.stream().filter(Objects::nonNull).map(LiveGoodsByWeChatVO::getGoodsInfoId).collect(Collectors.toList());
        GoodsInfoViewPageRequest goodsInfoViewPageRequest = new GoodsInfoViewPageRequest();
        goodsInfoViewPageRequest.setDelFlag(DeleteFlag.NO.toValue());//可用
        goodsInfoViewPageRequest.setGoodsInfoIds(goodsInfoList);
        String name = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(liveRoomVO.getStoreId())).getContext().getStoreVO().getStoreName();
        if (StringUtils.isEmpty(name)) {
            name = "-";
        }
        List<GoodsInfoVO> goodsInfoVOList = goodsInfoQueryProvider.pageView(goodsInfoViewPageRequest).getContext().getGoodsInfoPage().getContent();
        List<LiveGoodsInfoVO> liveGoodsInfoVOS = goodsInfoVOList.stream().map(c -> KsBeanUtil.convert(c, LiveGoodsInfoVO.class)).collect(Collectors.toList());
        LiveRoomByIdResponse response = new LiveRoomByIdResponse();
        response.setGoodsInfoVOList(liveGoodsInfoVOS);
        response.setLiveRoomVO(liveRoomVO);
        response.setLiveGoodsList(goodsVOList);
        response.setStoreName(name);
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "新增直播间")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @MultiSubmit
    public BaseResponse<LiveRoomAddResponse> add(@RequestBody @Valid LiveRoomAddRequest addReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        //创建直播间
        addReq.setDelFlag(DeleteFlag.NO);
        addReq.setCreatePerson(commonUtil.getOperatorId());
        addReq.setCreateTime(LocalDateTime.now());
        String accessToken = miniProgramUtil.getToken();
        addReq.setAccessToken(accessToken);
        addReq.setStoreId(commonUtil.getStoreId());
        BaseResponse<LiveRoomAddResponse> add = liveRoomProvider.add(addReq);
        //直播间添加直播商品
        if (CollectionUtils.isNotEmpty(addReq.getGoodsIdList())) {
            Long roomId = add.getContext().getLiveRoomVO().getRoomId();
            LiveGoodsAddRequest liveGoodsAddRequest = new LiveGoodsAddRequest();
            liveGoodsAddRequest.setRoomId(roomId);
            liveGoodsAddRequest.setGoodsIdList(addReq.getGoodsIdList());
            liveGoodsAddRequest.setAccessToken(accessToken);
            liveGoodsProvider.add(liveGoodsAddRequest);
        }
        return add;
    }


    @ApiOperation(value = "修改直播间")
    @PutMapping("/modify")
    public BaseResponse<LiveRoomModifyResponse> modify(@RequestBody @Valid LiveRoomModifyRequest modifyReq) {
        modifyReq.setUpdatePerson(commonUtil.getOperatorId());
        modifyReq.setUpdateTime(LocalDateTime.now());
        return liveRoomProvider.modify(modifyReq);
    }


    @ApiOperation(value = "是否推荐")
    @RequestMapping(value = "/recommend", method = RequestMethod.PUT)
    public BaseResponse recommend(@RequestBody @Valid LiveRoomByRoomIdRequest recommendReq) {
        //判断直播开关开关是否开启
        this.isOpen();
        return liveRoomProvider.recommend(recommendReq);
    }

    @ApiOperation(value = "根据id删除直播间")
    @DeleteMapping("/{id}")
    public BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        LiveRoomDelByIdRequest delByIdReq = new LiveRoomDelByIdRequest();
        delByIdReq.setId(id);
        return liveRoomProvider.deleteById(delByIdReq);
    }

    @ApiOperation(value = "根据idList批量删除直播间")
    @DeleteMapping("/delete-by-id-list")
    public BaseResponse deleteByIdList(@RequestBody @Valid LiveRoomDelByIdListRequest delByIdListReq) {
        return liveRoomProvider.deleteByIdList(delByIdListReq);
    }

    @ApiOperation(value = "导出直播间列表")
    @GetMapping("/export/{encrypted}")
    public void exportData(@PathVariable String encrypted, HttpServletResponse response) {
        String decrypted = new String(Base64.getUrlDecoder().decode(encrypted.getBytes()));
        LiveRoomListRequest listReq = JSON.parseObject(decrypted, LiveRoomListRequest.class);
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("id", "desc");
        List<LiveRoomVO> dataRecords = liveRoomQueryProvider.list(listReq).getContext().getLiveRoomVOList();

        try {
            String nowStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String fileName = URLEncoder.encode(String.format("直播间列表_%s.xls", nowStr), "UTF-8");
            response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\";filename*=\"utf-8''%s\"", fileName, fileName));
            exportDataList(dataRecords, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 判断直播开关是否开启
     */
    public void isOpen() {
        ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
        configQueryRequest.setDelFlag(0);
        configQueryRequest.setConfigKey("liveSwitch");
        configQueryRequest.setConfigType("liveSwitch");
        Integer status = systemConfigQueryProvider.findByConfigKeyAndDelFlag(configQueryRequest).getContext().getConfigVOList().get(0).getStatus();
        if (status == 0) {
            throw new SbcRuntimeException("10001", LiveErrCodeUtil.getErrCodeMessage(10001));
        }
    }

    /**
     * 导出列表数据具体实现
     */
    private void exportDataList(List<LiveRoomVO> dataRecords, OutputStream outputStream) {
        ExcelHelper excelHelper = new ExcelHelper();
        Column[] columns = {
                new Column("直播房间id", new SpelColumnRender<LiveRoomVO>("roomId")),
                new Column("直播房间名", new SpelColumnRender<LiveRoomVO>("name")),
                new Column("是否推荐", new SpelColumnRender<LiveRoomVO>("recommend")),
                new Column("开始时间", new SpelColumnRender<LiveRoomVO>("startTime")),
                new Column("结束时间", new SpelColumnRender<LiveRoomVO>("endTime")),
                new Column("主播昵称", new SpelColumnRender<LiveRoomVO>("anchorName")),
                new Column("主播微信", new SpelColumnRender<LiveRoomVO>("anchorWechat")),
                new Column("直播背景墙", new SpelColumnRender<LiveRoomVO>("coverImg")),
                new Column("分享卡片封面", new SpelColumnRender<LiveRoomVO>("shareImg")),
                new Column("直播状态 0: 直播中, 1: 未开始, 2: 已结束, 3: 禁播, 4: 暂停中, 5: 异常, 6: 已过期", new SpelColumnRender<LiveRoomVO>("liveStatus")),
                new Column("直播类型，1：推流，0：手机直播", new SpelColumnRender<LiveRoomVO>("type")),
                new Column("1：横屏，0：竖屏", new SpelColumnRender<LiveRoomVO>("screenType")),
                new Column("1：关闭点赞 0：开启点赞，关闭后无法开启", new SpelColumnRender<LiveRoomVO>("closeLike")),
                new Column("1：关闭货架 0：打开货架，关闭后无法开启", new SpelColumnRender<LiveRoomVO>("closeGoods")),
                new Column("1：关闭评论 0：打开评论，关闭后无法开启", new SpelColumnRender<LiveRoomVO>("closeComment")),
                new Column("店铺id", new SpelColumnRender<LiveRoomVO>("storeId")),
                new Column("直播商户id", new SpelColumnRender<LiveRoomVO>("liveCompanyId")),
                new Column("删除人", new SpelColumnRender<LiveRoomVO>("deletePerson")),
                new Column("删除时间", new SpelColumnRender<LiveRoomVO>("deleteTime"))
        };
        excelHelper.addSheet("直播间列表", columns, dataRecords);
        excelHelper.write(outputStream);
    }

}
