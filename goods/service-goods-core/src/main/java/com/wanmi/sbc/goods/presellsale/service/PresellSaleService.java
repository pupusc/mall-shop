package com.wanmi.sbc.goods.presellsale.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.goods.api.request.presellsale.*;
import com.wanmi.sbc.goods.api.response.presellsale.PresellSaleResponse;
import com.wanmi.sbc.goods.bean.vo.PresellSaleGoodsVO;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;
import com.wanmi.sbc.goods.presellsale.model.root.PresellSaleGoods;
import com.wanmi.sbc.goods.presellsale.repository.PresellSaleGoodsRepository;
import com.wanmi.sbc.goods.presellsale.repository.PresellSaleRepository;
import com.wanmi.sbc.goods.presellsale.request.PresellSaleQueryRequest;
import com.wanmi.sbc.goods.presellsale.response.PresellSaleQueryResponse;
import com.wanmi.sbc.goods.presellsale.response.TotalNum;
import com.wanmi.sbc.goods.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PresellSaleService {

    @Autowired
    private PresellSaleGoodsService presellSaleGoodsService;

    @Autowired
    private PresellSaleGoodsRepository presellSaleGoodsRepository;

    @Autowired
    private PresellSaleRepository presellSaleRepository;
    @Autowired
    private StoreQueryProvider storeQueryProvider;

    /**
     * 预售商品购买数量缓存Redis，key固定前缀
     */
    public static final String PRESELL_SALE_COUNT = "presellSaleCount";

    @Autowired
    private RedisService redisService;

    //创建预售活动
    public PresellSaleResponse add(PresellSaleSaveRequest request) {

        //校验是否勾选全选
        Integer selectAll = request.getSelectAll();
        List<PresellSaleGoodsSaveRequest> presellSaleGoodsList = request.getPresellSaleGoodsSaveRequestList();
        //拿出所有商品的skuid
        List<String> goodsInfoIds = presellSaleGoodsList.parallelStream().map(PresellSaleGoodsSaveRequest::getGoodsInfoId).collect(Collectors.toList());
        //拿出所有商品的spuid
        Set<String> collect = presellSaleGoodsList.stream().map(PresellSaleGoodsSaveRequest::getGoodsId).collect(Collectors.toSet());
        //校验商品是否参加其他活动 todo
        //根据店铺id查询店铺名称
        Long storeId = request.getStoreId();
        StoreByIdRequest storeByIdRequest = new StoreByIdRequest();
        storeByIdRequest.setStoreId(storeId);
        BaseResponse<StoreByIdResponse> store = storeQueryProvider.getById(storeByIdRequest);
        String storeName = store.getContext().getStoreVO().getStoreName();
        PresellSale presellSale = KsBeanUtil.convert(request, PresellSale.class);
        presellSale.setCreateTime(LocalDateTime.now());
        presellSale.setUpdateTime(LocalDateTime.now());
        presellSale.setDelFlag(DeleteFlag.NO);
        presellSale.setStoreName(storeName);

        //定金时，活动总的开始，结束时间
        if(presellSale.getPresellType()==0){
            presellSale.setStartTime(presellSale.getHandselStartTime());
            presellSale.setEndTime(presellSale.getFinalPaymentEndTime());
        }
        //全款时，活动总的开始，结束时间
        if(presellSale.getPresellType()==1){
            presellSale.setStartTime(presellSale.getPresellStartTime());
            presellSale.setEndTime(presellSale.getPresellEndTime());
        }
        presellSale.setSuspended(Integer.valueOf(0));
        String id = presellSaleRepository.save(presellSale).getId();
        presellSaleGoodsList.forEach(i -> i.setPresellSaleId(id));
        presellSaleGoodsList.forEach(i -> i.setCreate_person(request.getCreatePerson()));

        @NotNull Integer presellType = request.getPresellType();
        LocalDateTime finalPaymentEndTime = request.getFinalPaymentEndTime();
        LocalDateTime presellEndTime = request.getPresellEndTime();

        //计算Redis key过期时间
        long milli = getMilli(presellType, finalPaymentEndTime, presellEndTime);
        List<PresellSaleGoodsVO> presellSaleGoodsVOS = presellSaleGoodsService.add(presellSaleGoodsList, milli);
        PresellSaleResponse presellSaleResponse = KsBeanUtil.convert(presellSale, PresellSaleResponse.class);
        presellSaleResponse.setPresellSaleGoodsSaveRequestList(presellSaleGoodsVOS);
        return presellSaleResponse;
    }


    /**
     * @param presellSaleId 根据预售活动id查询活动信息
     */
    public PresellSaleResponse findPresellSaleById(String presellSaleId) {
        PresellSale presellSale = presellSaleRepository.findPresellSaleByAndDelFlag(presellSaleId);
        if (presellSale == null) {
            return null;
        }
        PresellSaleResponse presellSaleResponse = KsBeanUtil.convert(presellSale, PresellSaleResponse.class);
        List<PresellSaleGoodsVO> presellSaleGoodsVOList = presellSaleGoodsService.findPresellSaleGoodsByPresellSaleId(presellSaleId);
        presellSaleResponse.setPresellSaleGoodsSaveRequestList(presellSaleGoodsVOList);
        return presellSaleResponse;
    }

    /**
     * 计算Redis过期时间
     *
     * @param
     * @return
     */
    public long getMilli(Integer presellSaleType, LocalDateTime finalPaymentEndDate, LocalDateTime presellEndDate) {
        LocalDateTime localDateTime = LocalDateTime.now();
        long l1 = localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
        long milli = 0L;
        //根据活动的结束时间设置过期时间往后加上整一天
        if (presellSaleType == 0) {
            //通过尾款结束时间(多延后一天)计算过期时间
            LocalDateTime finalPaymentEndTime = finalPaymentEndDate.plusDays(1);
            long l = finalPaymentEndTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
            milli = (l - l1) / 1000;
        }
        if (presellSaleType == 1) {
            //通过预付结束时间（多延后一天）计算过期时间
            LocalDateTime presellEndTime = presellEndDate.plusDays(1);
            long l = presellEndTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
            milli = (l - l1) / 1000;
        }
        return milli;
    }


    /**
     * 编辑修改预售活动信息
     *
     * @param request
     */
    public void modify(PresellSaleModifyRequest request) {
        //校验是否勾选全选
        Integer selectAll = request.getSelectAll();
        String presellSaleId = request.getPresellSaleId();

        //更新保存预售活动信息
        PresellSale presellSale = presellSaleRepository.findById(presellSaleId).get();
        presellSale.setPresellSaleName(request.getPresellSaleName());
        presellSale.setPresellType(request.getPresellType());
        presellSale.setHandselStartTime(request.getHandselStartTime());
        presellSale.setHandselEndTime(request.getHandselEndTime());
        presellSale.setFinalPaymentStartTime(request.getFinalPaymentStartTime());
        presellSale.setFinalPaymentEndTime(request.getFinalPaymentEndTime());
        presellSale.setPresellStartTime(request.getPresellStartTime());
        presellSale.setPresellEndTime(request.getPresellEndTime());
        presellSale.setDeliverTime(request.getDeliverTime());
        presellSale.setUpdateTime(LocalDateTime.now());
        presellSale.setJoinLevel(request.getJoinLevel());
        presellSale.setJoinLevelType(request.getJoinLevelType());
        presellSale.setUpdate_person(request.getCreatePerson());


        //定金时，活动总的开始，结束时间
        if(presellSale.getPresellType()==0){
            presellSale.setStartTime(presellSale.getHandselStartTime());
            presellSale.setEndTime(presellSale.getFinalPaymentEndTime());
        }
        //全款时，活动总的开始，结束时间
        if(presellSale.getPresellType()==1){
            presellSale.setStartTime(presellSale.getPresellStartTime());
            presellSale.setEndTime(presellSale.getPresellEndTime());
        }

        presellSaleRepository.save(presellSale);

        List<PresellSaleGoodsSaveRequest> presellSaleGoodsList = request.getPresellSaleGoodsSaveRequestList();
        //拿出所有商品的skuid（新修改的）
        List<String> goodsInfoIds = presellSaleGoodsList.parallelStream().map(PresellSaleGoodsSaveRequest::getGoodsInfoId).collect(Collectors.toList());

        //根据商品id组合信息城Map
        Map<String, PresellSaleGoodsSaveRequest> result = presellSaleGoodsList.parallelStream().
                collect(Collectors.toMap(presellSaleGoodsSaveRequest -> {
                    return presellSaleGoodsSaveRequest.getGoodsInfoId();
                }, presellSaleGoodsSaveRequest -> {
                    return presellSaleGoodsSaveRequest;
                }));


        //计算新的缓存时间
        @NotNull Integer presellType = request.getPresellType();
        LocalDateTime finalPaymentEndTime = request.getFinalPaymentEndTime();
        LocalDateTime presellEndTime = request.getPresellEndTime();
        long milli = getMilli(presellType, finalPaymentEndTime, presellEndTime);
        //先把所有关联商品设置为删除状态，再根据商品信息修改对应商品信息
        int i = presellSaleGoodsService.deleteAllPresellSaleGoods(presellSaleId);
        //根据活动id和商品ID查询活动关联表，有就在原来的里面修改，没有就新建
        for (String goodsInfoId : goodsInfoIds) {
            PresellSaleGoods presellSaleGoods = presellSaleGoodsRepository.findByPresellSaleIdAndGoodsInfoId(presellSaleId, goodsInfoId);
            PresellSaleGoodsSaveRequest presellSaleGoodsSaveRequest = result.get(goodsInfoId);
            if (presellSaleGoods == null) {
                //新建一条记录
                //根据商品id查出修改的内容
                presellSaleGoodsSaveRequest.setPresellSaleId(presellSaleId);
                presellSaleGoodsSaveRequest.setCreate_person(request.getCreatePerson());
                presellSaleGoodsService.addPresellSaleGoods(presellSaleGoodsSaveRequest, milli);
            } else {
                //修改里面的属性
                KsBeanUtil.copyProperties(presellSaleGoodsSaveRequest, presellSaleGoods);
                presellSaleGoods.setUpdate_person(request.getCreatePerson());
                presellSaleGoods.setUpdateTime(LocalDateTime.now());
                presellSaleGoods.setDelFlag(DeleteFlag.NO);
                presellSaleGoodsRepository.saveAndFlush(presellSaleGoods);

                //更新Redis的缓存（商品购买量）
                String presellSaleCount = presellSaleGoods.getPresellSaleCount().toString();
                String id = presellSaleGoods.getId();
                redisService.setString(PRESELL_SALE_COUNT + id, presellSaleCount, milli);
            }
        }

    }

    /**
     * 删除预售活动 （待修改返回值）
     *
     * @param request
     */
    @Transactional
    public void delete(PresellSaleByIdDeleteRequest request) {
        //先删除活动下的所有商品
        presellSaleGoodsService.deleteAllPresellSaleGoods(request.getPresellSaleId());
        //再将活动设置为删除状态
        String update_person = request.getUpdate_person();
        LocalDateTime now = LocalDateTime.now();
        String presellSaleId = request.getPresellSaleId();
        PresellSale presellSale = presellSaleRepository.findById(presellSaleId).get();
        presellSale.setDelFlag(DeleteFlag.YES);
        presellSale.setDelTime(now);
        presellSale.setDelPerson(update_person);
        presellSaleRepository.save(presellSale);

    }


    /**
     * 预售活动开始暂停设置
     * @param request
     */
    public void suspended(PresellSaleByIdDeleteRequest request) {
        //根据id查询预售活动
        PresellSale presellSale = presellSaleRepository.findById(request.getPresellSaleId()).get();
        //判断预售活动是开始还是暂停，如果是开始，改为暂停，如果是暂停改为开始
        Integer suspended = presellSale.getSuspended();
        if(0 == suspended){
            presellSale.setSuspended(1);
        }else if(1== suspended){
            presellSale.setSuspended(0);
        }
        presellSale.setUpdate_person(request.getUpdate_person());
        presellSale.setUpdateTime(LocalDateTime.now());
        presellSaleRepository.saveAndFlush(presellSale);
    }

    /**
     * 分页查询预售活动列表
     *
     * @param request 参数
     * @return list
     */
    public PresellSaleQueryResponse page(PresellSaleQueryRequest request) {
        PresellSaleQueryResponse response = new PresellSaleQueryResponse();
        Page<PresellSale> goodsPage = presellSaleRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        //循环查询每个类型为定金预售活动的活动id
        List<String> presellSaleIds = goodsPage.getContent().stream().map(PresellSale::getId).collect(Collectors.toList());
        // 对应的活动id查询关联商品的定金支付人数和尾款支付人数并求和
        Map<String, TotalNum> map = new HashMap<>();

        if(presellSaleIds.size()>0){
            for (String presellSaleId : presellSaleIds) {
                TotalNum totalNum = new TotalNum();
                List<PresellSaleGoodsVO> presellSaleGoodsVOS = presellSaleGoodsService.findPresellSaleGoodsByPresellSaleId(presellSaleId);
                int handselTotalNum = presellSaleGoodsVOS.stream().mapToInt(PresellSaleGoodsVO::getHandselNum).sum();
                int finalPaymentNum = presellSaleGoodsVOS.stream().mapToInt(PresellSaleGoodsVO::getFinalPaymentNum).sum();
                int fullPaymntNum = presellSaleGoodsVOS.stream().mapToInt(PresellSaleGoodsVO::getFullPaymentNum).sum();
                totalNum.setHandselTotalNum(handselTotalNum);
                totalNum.setFinalPaymentNum(finalPaymentNum);
                totalNum.setFullPaymentNum(fullPaymntNum);

                map.put(presellSaleId, totalNum);

            }
        }


        response.setPresellSalesPage(goodsPage);
        response.setCountNumList(map);
        return response;
    }


    /**
     * 預售活動發送尾款通知
     * @param request
     */
    public void notice(PresellSaleNoticeRequest request) {
        //查询所有预售活动，根据预售活动类型为定金的活动，拿出所有的活动id
        List<PresellSale> presellSales = presellSaleRepository.findAllByType();
        Map<String, LocalDateTime> collect = presellSales.stream().collect(Collectors.toMap(i -> i.getId(), i -> i.getFinalPaymentStartTime()));
        List<String> presellSaleIds = new LinkedList<>();
        LocalDateTime now = LocalDateTime.now();
        collect.keySet().forEach(key ->{
            LocalDateTime localDateTime = collect.get(key);
            //判断现在时间是否比结束时间大，相差的时间是5分钟
            if(now.isAfter(localDateTime)){
                Duration duration = Duration.between(now, localDateTime);
                long l = duration.toMinutes();
                if (l<5){
                    presellSaleIds.add(key);
                }
            }
        } );

        //判断时间是否要发
        //找到对应的交易订单，根据活动id
        //推送消息
    }

}
