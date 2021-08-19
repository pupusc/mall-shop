package com.wanmi.sbc.presellsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleProvider;
import com.wanmi.sbc.goods.api.provider.presellsale.PresellSaleQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.presellsale.*;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleDetailsResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleGoodsResponse;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import com.wanmi.sbc.goods.bean.constant.PresellSaleErrorCode;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.PresellSaleGoodsVO;
import com.wanmi.sbc.order.api.provider.trade.TradeItemProvider;
import com.wanmi.sbc.order.api.request.trade.TradeItemSnapshotRequest;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 预售活动
 * Created by xiaoqianh on 2020/5/25.
 */
@Api(tags = "PresellSaleController", description = "预售活动 API")
@RestController
@RequestMapping("/presell_sale")
public class PresellSaleController {
    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private PresellSaleProvider presellSaleProvider;

    @Autowired
    private PresellSaleQueryProvider presellSaleQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private TradeItemProvider tradeItemProvider;

    @Autowired
    private PresellSaleGoodsQueryProvider presellSaleGoodsQueryProvider;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    /**
     * 预售商品购买数量缓存Redis，key固定前缀
     */
    public static final String PRESELL_SALE_COUNT = "presellSaleCount";
    /**
     * 新增预售活动
     */
    @ApiOperation(value = "新增预售活动")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResponse<String> add(@RequestBody PresellSaleSaveRequest request) {

        Integer presellType = request.getPresellType();
        //校验时间
        checkTime(request, presellType);

        request.setCreatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        //设置是否平台等级
        request.setJoinLevelType(request.getJoinLevelType());
        BaseResponse<PresellSaleResponse> response = presellSaleProvider.addPresellSale(request);

        //生成操作日志
        operateLogMQUtil.convertAndSend("预售活动", "新增",
                "新增预售活动id" + response.getContext().getId());

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 新增预售活动
     */
    @ApiOperation(value = "查询预售活动详情")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "presellSaleId", value = "预售活动id", required = true)
    @RequestMapping(value = "/edit/{presellSaleId}", method = RequestMethod.GET)
    public BaseResponse<PresellSaleDetailsResponse> edit(@PathVariable("presellSaleId") String presellSaleId) {
        PresellSaleByIdRequest request = new PresellSaleByIdRequest();
        request.setPresellSaleId(presellSaleId);
        BaseResponse<PresellSaleResponse> response = presellSaleQueryProvider.presellSaleById(request);
        PresellSaleResponse context = response.getContext();
        //拿出活动关联的商品id
        @NotNull List<PresellSaleGoodsVO> presellSaleGoodsSaveRequestList = context.getPresellSaleGoodsSaveRequestList();
        Long storeId = context.getStoreId();
        List<String> goodsInfos = presellSaleGoodsSaveRequestList.parallelStream().map(PresellSaleGoodsVO::getGoodsInfoId).collect(Collectors.toList());
        //通过goodsInfoIds查询goodsInfo
        GoodsInfoViewByIdsRequest goodsInfoByIdRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoByIdRequest.setStoreId(storeId);
        goodsInfoByIdRequest.setDeleteFlag(DeleteFlag.NO);
        goodsInfoByIdRequest.setGoodsInfoIds(goodsInfos);
        goodsInfoByIdRequest.setIsHavSpecText(1);
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = goodsInfoQueryProvider.listViewByIds(goodsInfoByIdRequest).getContext();
        List<GoodsInfoVO> goodsInfos1 = goodsInfoViewByIdsResponse.getGoodsInfos();
        PresellSaleDetailsResponse presellSaleDetailsResponse = KsBeanUtil.convert(context, PresellSaleDetailsResponse.class);

        presellSaleDetailsResponse.setGoodsInfoVOList(goodsInfos1);

        return BaseResponse.success(presellSaleDetailsResponse);
    }

    /**
     * 新增预售活动
     */
    @ApiOperation(value = "修改预售活动")
    @RequestMapping(value = "/modify", method = RequestMethod.PUT)
    public BaseResponse<String> modify(@RequestBody PresellSaleModifyRequest request) {
        //检验该活动是否存在
        PresellSaleByIdRequest presellSaleByIdRequest = new PresellSaleByIdRequest();
        presellSaleByIdRequest.setPresellSaleId(request.getPresellSaleId());
        PresellSaleResponse presellSaleResponse = presellSaleQueryProvider.presellSaleById(presellSaleByIdRequest).getContext();
        if (presellSaleResponse == null) {
            //查询不到预售活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_DELETE);
        }
        //校验该活动是否开始，如果开始不可以修改

        LocalDateTime now = LocalDateTime.now();
        //先判断活动类型
        @NotNull Integer presellType = presellSaleResponse.getPresellType();
        if (presellType == 0) {
            LocalDateTime handselStartTime = presellSaleResponse.getHandselStartTime();
            if (now.isAfter(handselStartTime)) {
                //活动已开始无法修改活动信息
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_ALREADY_BEGUN);
            }

        } else {
            LocalDateTime presellStartTime = presellSaleResponse.getPresellStartTime();
            if (now.isAfter(presellStartTime)) {
                //活动已开始,无法修改活动信息
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_ALREADY_BEGUN);
            }
        }

        //先校验时间
        PresellSaleSaveRequest presellSaleSaveRequest = new PresellSaleSaveRequest();
        KsBeanUtil.convert(request, PresellSaleByIdRequest.class);
        checkTime(presellSaleSaveRequest, presellType);
        //修改活动信息
        request.setCreatePerson(commonUtil.getOperatorId());
        request.setStoreId(commonUtil.getStoreId());
        presellSaleProvider.modifyPresellSale(request);
        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 删除预售活动
     */
    @ApiOperation(value = "删除预售活动")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public BaseResponse<String> delete(@RequestBody PresellSaleByIdDeleteRequest request) {

        //检验该活动是否存在
        PresellSaleByIdRequest presellSaleByIdRequest = new PresellSaleByIdRequest();
        presellSaleByIdRequest.setPresellSaleId(request.getPresellSaleId());
        PresellSaleResponse presellSaleResponse = presellSaleQueryProvider.presellSaleById(presellSaleByIdRequest).getContext();
        if (presellSaleResponse == null) {
            //查询不到预售活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_DELETE);
        }
        //校验活动是否未开始
        //判断活动状态
        LocalDateTime startTime = presellSaleResponse.getStartTime();
        LocalDateTime endTime = presellSaleResponse.getEndTime();
        LocalDateTime now = LocalDateTime.now();
        if(startTime.isAfter(now)){
            //提示活动未开始无法删除
            request.setUpdate_person(commonUtil.getOperatorId());
            //删除预售活动
            BaseResponse<String> delete = presellSaleProvider.delete(request);
        }
        if(endTime.isBefore(now)){
            //提示活动已结束无法删除
            //活动已开始,无法删除活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_ALREADY_END_DEL);
        }
        if(startTime.isBefore(now)){
            //提示活动已开始无法删除
            //活动已开始,无法修改活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_ALREADY_BEGUN_DEL);
        }


        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 设置预售活动开始，暂停
     */
    @ApiOperation(value = "设置预售活动开始暂停")
    @RequestMapping(value = "/suspended", method = RequestMethod.POST)
    public BaseResponse<String> setSuspended(@RequestBody PresellSaleByIdDeleteRequest request){
        //检验该活动是否存在
        PresellSaleByIdRequest presellSaleByIdRequest = new PresellSaleByIdRequest();
        presellSaleByIdRequest.setPresellSaleId(request.getPresellSaleId());
        PresellSaleResponse presellSaleResponse = presellSaleQueryProvider.presellSaleById(presellSaleByIdRequest).getContext();
        if (presellSaleResponse == null) {
            //查询不到预售活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_DELETE);
        }
        request.setUpdate_person(commonUtil.getOperatorId());
        presellSaleProvider.setSuspended(request);

        return BaseResponse.SUCCESSFUL();
    }


    /**
     * 分页查询预售活动列表
     */
    @ApiOperation(value = "分页查询预售活动列表")
    @RequestMapping(value = "/queryList", method = RequestMethod.POST)
    public BaseResponse queryList(@RequestBody PresellSalePageRequest pageRequest) {
        pageRequest.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
        pageRequest.putSort("createTime", SortType.DESC.toValue());
        pageRequest.setStoreId(commonUtil.getStoreId());
        BaseResponse page = presellSaleQueryProvider.page(pageRequest);
        return page;
    }


    @ApiOperation(value = "预付活动支付定金或全款")
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public BaseResponse<String> payMoney(@RequestBody PresellSalePaymentRequest request){
    //根据预售活动id和关联商品id校验活动是否存在，时间是否在开始时间内，库存值是否充足
        //1.校验活动存在和是否在对应开始时间内
        //检验该活动是否存在
        PresellSaleByIdRequest presellSaleByIdRequest = new PresellSaleByIdRequest();
        presellSaleByIdRequest.setPresellSaleId(request.getPresellSaleId());
        PresellSaleResponse presellSaleResponse = presellSaleQueryProvider.presellSaleById(presellSaleByIdRequest).getContext();
        if (presellSaleResponse == null) {
            //查询不到预售活动信息
            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_DELETE);
        }
        @NotNull Integer presellType = presellSaleResponse.getPresellType();
        if(presellType==0){
            LocalDateTime handselStartTime = presellSaleResponse.getHandselStartTime();
            LocalDateTime handselEndTime = presellSaleResponse.getHandselEndTime();
            LocalDateTime now = LocalDateTime.now();
            if(handselStartTime.isAfter(now)){
                //抛出预售活动还没开始
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_HANDSEL_NOT_BEGIN);

            }
            if(handselEndTime.isBefore(now)){
                //抛出预售活动已经结束
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_HANDSEL_YET_END);
            }
            //校验预售活动的目标类型
            DefaultFlag joinLevelType = presellSaleResponse.getJoinLevelType();
            Boolean result=false;
            //目标客户店铺会员
            if(joinLevelType==DefaultFlag.NO){
                Long storeId = presellSaleResponse.getStoreId();
                //校验该用户的等级是否有资格参加
                CustomerLevelMapByCustomerIdAndStoreIdsRequest requestBody = new CustomerLevelMapByCustomerIdAndStoreIdsRequest();
                requestBody.setCustomerId(commonUtil.getOperator().getUserId());
                List<Long> linkedList = new LinkedList();
                linkedList.add(storeId);
                requestBody.setStoreIds(new ArrayList<>());
                //取出用户的等级名，进行确认比较
                Map<Long, CommonLevelVO> storeLevelMap = customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(requestBody).getContext().getCommonLevelVOMap();
                if(storeLevelMap.size()!=0&&storeLevelMap.containsKey(storeId)){
                    //判断用户是否有资格
                    @NotBlank String joinLevel = presellSaleResponse.getJoinLevel();
                    Long levelId = storeLevelMap.get(storeId).getLevelId();
                    //判断是多个还是单个
                    if(joinLevel.contains(",")){
                        Map<String, String> collect = Arrays.asList(joinLevel.split(",")).stream().collect(Collectors.toMap(i -> i, i -> i));
                        result = collect.containsKey(levelId);
                        if(result){
                            //没资格
                            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_NOT_QUALIFICATION);
                        }
                    }else{
                        result = levelId.equals(joinLevel);
                        if(!result){
                            //没资格
                            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_NOT_QUALIFICATION);
                        }
                    }
                }
            }else{
                //全平台类型设置为true
                result=true;
            }
            Lock lock = new ReentrantLock();
            try{

                if(result){
                    lock.lock();
                    String presellSaleGoodsId = request.getPresellSaleGoodsId();
                    //redis中根据key值拿取关联活动商品购买数量  (加锁-----ReentrantLock)
                    String presellSaleCount = redisService.get(PRESELL_SALE_COUNT + presellSaleGoodsId).toString();
                    Integer count = Integer.valueOf(presellSaleCount);
                    //先判断预售活动量是否大于0，大于0 就继续判断，等于0 或小于0就提示预售商品已售罄
                    if(count<=0){
                        //抛出预售活动已经结束
                        throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_SELL_OUT);
                    }else {
                        Integer purchases = request.getPurchases();
                        Integer realityCount=count-purchases;
                        if(realityCount<0){
                            //抛出预售活动关联商品已售罄无法预订，或支付全款
                            throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_INADEQUATE);
                        }else{
                            //将计算完的值重新缓存到redis
                            String newpPesellSaleCount = String.valueOf(realityCount);
                            redisService.setString(PRESELL_SALE_COUNT + presellSaleGoodsId,newpPesellSaleCount);
                            //处理订单，前端
                            //判断全款还是定金
                            //定金走支付快照，不包含优惠信息
                            TradeItemSnapshotRequest tradeItemSnapshotRequest = getTradeItemSnapshotRequest(request, presellSaleByIdRequest);
                            if(presellType ==0){
                                //定金模式下不需要开启积分了，在尾款时处理
                                tradeItemSnapshotRequest.setPointGoodsFlag(false);
                            }else {
                                //全款走快照支付,整个包含了优惠信息
                                tradeItemSnapshotRequest.setPointGoodsFlag(true);
                            }
                            tradeItemProvider.snapshot(tradeItemSnapshotRequest);

                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }



        }
        return BaseResponse.SUCCESSFUL();
    }

    public TradeItemSnapshotRequest getTradeItemSnapshotRequest(@RequestBody PresellSalePaymentRequest request, PresellSaleByIdRequest presellSaleByIdRequest) {
        TradeItemSnapshotRequest tradeItemSnapshotRequest = new TradeItemSnapshotRequest();
        //获得用户id todo 接口要转移到mobile，暂时先写这边
        tradeItemSnapshotRequest.setCustomerId(null);
        tradeItemSnapshotRequest.setStoreBagsFlag(DefaultFlag.NO);
        //分装商品基本信息
        TradeItemDTO tradeItemDTO = new TradeItemDTO();
        //根据预售活动关联商品id，查询skuId
        PresellSaleGoodsByIdRequest presellSaleGoodsByIdRequest = new PresellSaleGoodsByIdRequest();
        presellSaleByIdRequest.setPresellSaleId(request.getPresellSaleGoodsId());
        BaseResponse<PresellSaleGoodsResponse> presellSaleGoodsResponseBaseResponse = presellSaleGoodsQueryProvider.presellSaleGoodsById(presellSaleGoodsByIdRequest);
        tradeItemDTO.setSkuId(presellSaleGoodsResponseBaseResponse.getContext().getGoodsInfoId());
        tradeItemDTO.setNum(request.getPurchases().longValue());
        List<TradeItemDTO> list = new LinkedList<>();
        list.add(tradeItemDTO);
        tradeItemSnapshotRequest.setTradeItems(list);
        return tradeItemSnapshotRequest;

    }


    /**
     * 活动时间校验
     *
     * @param request
     * @param presellType
     */
    public void checkTime(PresellSaleSaveRequest request, Integer presellType) {
        //当预售类型是定金类型，检验时间格式，校验时间规则
        if (presellType == 0) {
            //定金开始时间
            LocalDateTime handselStartTime = request.getHandselStartTime();
            //定金结束
            LocalDateTime handselEndTime = request.getHandselEndTime();
            //尾款开始时间
            LocalDateTime finalPaymentStartTime = request.getFinalPaymentStartTime();
            //尾款结束时间
            LocalDateTime finalPaymentEndTime = request.getFinalPaymentEndTime();
            //首先校验时间格式是否为 yyyy-MM-dd HH:mm:ss 如果不是默认补齐TODO

            //校验规则，定金开始时间不得早于当前时间，定金结束时间不得早于打定金开始时间
            if (handselStartTime.isBefore(LocalDateTime.now())) {
                //抛出异常，定金开始时间不得早于当前时间
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_BEFORE_NOW);
            }
            if (handselEndTime.isBefore(handselStartTime)) {
                //抛出异常定金结束时间不得早于打定金开始时间
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_BEFORE_END);

            }

            //定金 结束实际可以小于等于尾款开始时间，尾款结束时间要晚于尾款开始时间
            if (handselEndTime.isAfter(finalPaymentStartTime)) {
                //抛出异常定金结束实际可以小于等于尾款开始时间
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_FINAL_BEFORE);

            }
            if (finalPaymentEndTime.isBefore(finalPaymentStartTime)) {
                //抛出异常尾款结束时间要晚于尾款开始时间
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_FINAL_END_BEFORE);

            }
        }
        if (presellType == 1) {
            //预售开始时间
            LocalDateTime presellStartTime = request.getPresellStartTime();
            //预售结束时间
            LocalDateTime presellEndTime = request.getPresellEndTime();
            if (presellStartTime.isAfter(presellEndTime)) {
                //抛出异常尾款结束时间要晚于尾款开始时间
                throw new SbcRuntimeException(PresellSaleErrorCode.PRESELLSALE_END_BEFORE);
            }
        }
    }
}
