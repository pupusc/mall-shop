package com.wanmi.sbc.flashsale.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByCompanyIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByCompanyIdResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleGoodsRequest;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleOrderNumRequest;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName FlashSaleService
 * @Description 秒杀活动service
 * @Author lvzhenwei
 * @Date 2019/6/15 9:48
 **/
@Service
public class FlashSaleService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    /**
     * @return java.lang.Boolean
     * @Author lvzhenwei
     * @Description 判断抢购商品抢购时的条件判断
     * @Date 9:52 2019/6/15
     * @Param [request]
     **/
    public Boolean judgeBuyFalshSaleGoodsCondition(RushToBuyFlashSaleGoodsRequest request) {
        //从redis缓存中获取对应的秒杀抢购商品信息
        FlashSaleGoodsVO flashSaleGoodsVO = getFlashSaleGoodsInfoForRedis(request);
        //1、判断抢购抢购商品是否存在
        if (Objects.isNull(flashSaleGoodsVO)) {
            //抛出不存在该秒杀活动的抢购商品异常信息
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_ACTIVITY_NOT_PRESENCE);
        }

        GoodsInfoByIdResponse response = goodsInfoQueryProvider.getById(
                GoodsInfoByIdRequest.builder().storeId(flashSaleGoodsVO.getStoreId()).goodsInfoId(flashSaleGoodsVO.getGoodsInfoId()).build()).getContext();
        if (Objects.isNull(response)) {
            //抛出不存在该秒杀活动的抢购商品异常信息
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_ACTIVITY_NOT_PRESENCE);
        }
        //含积分的商品不允许秒杀
//        if (response.getBuyPoint() != null && response.getBuyPoint() > 0) {
////            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_PANIC_BUYING_FAIL);
////        }

        //判断店铺是否关店或者禁用
        StoreByIdResponse storeByIdResponse = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(flashSaleGoodsVO.getStoreId()).build()).getContext();
        EmployeeByCompanyIdResponse employeeByCompanyIdResponse = employeeQueryProvider.getByCompanyId(EmployeeByCompanyIdRequest.builder()
                .companyInfoId(storeByIdResponse.getStoreVO().getCompanyInfo().getCompanyInfoId()).build()).getContext();
        if (storeByIdResponse.getStoreVO().getStoreState() == StoreState.CLOSED || employeeByCompanyIdResponse.getAccountState() == AccountState.DISABLE) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_PANIC_BUYING_FAIL);
        }
        //2、判断该商品对应的场次是否存在并且该商品对应的抢购活动是否
        if (LocalDateTime.now().isBefore(flashSaleGoodsVO.getActivityFullTime())) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_ACTIVITY_NOT_START);
        }
        //3、判断该商品对应的场次是否存在并且该商品对应的抢购活动是否已结束
        if (LocalDateTime.now().isAfter(flashSaleGoodsVO.getActivityFullTime().plusHours(Constants.FLASH_SALE_LAST_HOUR))) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_ACTIVITY_END);
        }
        String qualificationsKey = RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() + flashSaleGoodsVO.getId();
        String havePanicBuyingKey = RedisKeyConstant.FLASH_SALE_GOODS_HAVE_PANIC_BUYING + request.getCustomerId() + flashSaleGoodsVO.getId();
        //4、抢购商品数量是否大于该上限数量以及商品库存
        if (request.getFlashSaleGoodsNum() > flashSaleGoodsVO.getStock() || request.getFlashSaleGoodsNum() > response.getStock()) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_GOODS_PANIC_BUYING_NUM_MORE_THAN_STOCK);
        }
        //5、判断抢购商品是否小于起购数
        if (request.getFlashSaleGoodsNum() < flashSaleGoodsVO.getMinNum()) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_GOODS_PANIC_BUYING_NUM_LESS_THAN_MIN_NUM);
        }
        //6、判断该用户已经抢购购过该商品的数量（分为抢购资格数量和已购数量）是否已经超过该商品抢购限制数量
        Integer rushToBuyNum = request.getFlashSaleGoodsNum();
        if (Objects.nonNull(redisService.getString(qualificationsKey))) {
            rushToBuyNum = rushToBuyNum + redisService.getObj(qualificationsKey, RushToBuyFlashSaleGoodsRequest.class).getFlashSaleGoodsNum();
        }
        if (Objects.nonNull(redisService.getString(havePanicBuyingKey))) {
            rushToBuyNum = rushToBuyNum + Integer.valueOf(redisService.getString(havePanicBuyingKey));
        }
        if (rushToBuyNum > flashSaleGoodsVO.getMaxNum()) {
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_GOODS_HAVE_PANIC_BUYING_NUM_MORE_THAN_MAX_NUM);
        }
        return true;
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 从Redis缓存中某个秒杀活动的商品信息
     * @Date 16:32 2019/6/15
     * @Param []
     **/
    public FlashSaleGoodsVO getFlashSaleGoodsInfoForRedis(RushToBuyFlashSaleGoodsRequest request) {
        String flashSaleGoodsInfoKey = RedisKeyConstant.FLASH_SALE_GOODS_INFO + request.getFlashSaleGoodsId();
        //从redis缓存中获取对应的秒杀抢购商品信息
        FlashSaleGoodsVO flashSaleGoodsVO = redisService.getObj(flashSaleGoodsInfoKey, FlashSaleGoodsVO.class);
        if (Objects.isNull(flashSaleGoodsVO)) {
            //如果redis缓存中不存在秒杀抢购信息从数据获取，重新放入缓存中
            flashSaleGoodsVO = flashSaleGoodsQueryProvider.getById(FlashSaleGoodsByIdRequest.builder()
                    .id(request.getFlashSaleGoodsId())
                    .build())
                    .getContext().getFlashSaleGoodsVO();
            redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
        }
        return flashSaleGoodsVO;
    }

    /**
     * @return java.lang.Boolean
     * @Author lvzhenwei
     * @Description 判断抢购资格是否存在
     * @Date 9:26 2019/6/17
     * @Param [request]
     **/
    public RushToBuyFlashSaleGoodsRequest getFlashSaleGoodsQualifications(RushToBuyFlashSaleGoodsRequest request) {
        String qualificationsInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                request.getCustomerId() + request.getFlashSaleGoodsId());
        String qualificationsInfoFailed = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED +
                request.getCustomerId() + request.getFlashSaleGoodsId());
        RushToBuyFlashSaleGoodsRequest rushToBuyFlashSaleGoodsRequest = new RushToBuyFlashSaleGoodsRequest();
        FlashSaleGoodsVO flashSaleGoodsVO = getFlashSaleGoodsInfoForRedis(request);
        //如果抢购资格不存在并且(抢购商品对应的库存为零或者所剩库存小于当前商品抢购数量)，则抢购失败
        if (StringUtils.isBlank(qualificationsInfo) && ((StringUtils.isNotBlank(qualificationsInfoFailed)&&qualificationsInfoFailed.equals("Failed")) || flashSaleGoodsVO.getStock() == 0 || flashSaleGoodsVO.getStock() < request.getFlashSaleGoodsNum())) {
            redisService.delete(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS_FAILED +
                    request.getCustomerId() + request.getFlashSaleGoodsId());
            if(StringUtils.isBlank(qualificationsInfo)){
                throw new SbcRuntimeException("k-140008");
            }
            if (flashSaleGoodsVO.getStock() == 0){
                throw new SbcRuntimeException("K-140004");
            }
            throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_PANIC_BUYING_FAIL);
        } else if (StringUtils.isNotBlank(qualificationsInfo)) {
            rushToBuyFlashSaleGoodsRequest = JSONObject.parseObject(qualificationsInfo, RushToBuyFlashSaleGoodsRequest.class);
        }
        return rushToBuyFlashSaleGoodsRequest;
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 删除抢购资格
     * @Date 17:54 2019/6/22
     * @Param [request]
     **/
    public void delFlashSaleGoodsQualification(RushToBuyFlashSaleGoodsRequest request) {
        redisService.delete(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                request.getCustomerId() + request.getFlashSaleGoodsId());
    }

    /**
     * 获取秒杀活动详情
     *
     * @param request
     * @return
     */
    public FlashSaleGoodsVO getFlashSaleGoodsInfo(RushToBuyFlashSaleGoodsRequest request) {
        return flashSaleGoodsQueryProvider.getById(FlashSaleGoodsByIdRequest.builder()
                .id(request.getFlashSaleGoodsId())
                .build())
                .getContext().getFlashSaleGoodsVO();
    }

    /**
     * 秒杀商品订单信息
     *
     * @param request
     */
    public void updateFlashSaleGoodsOrderNum(RushToBuyFlashSaleGoodsRequest request) {
        String qualificationsInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                request.getCustomerId() + request.getFlashSaleGoodsId());
        RushToBuyFlashSaleGoodsRequest rushToBuyFlashSaleGoodsRequest = JSONObject.parseObject(qualificationsInfo, RushToBuyFlashSaleGoodsRequest.class);
        String flashSaleOrderInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                request.getCustomerId() + request.getFlashSaleGoodsId()+"flashSaleOrder");
        RushToBuyFlashSaleOrderNumRequest rushToBuyFlashSaleOrderNumRequest = new RushToBuyFlashSaleOrderNumRequest();
        if(StringUtils.isNotBlank(qualificationsInfo)){
            if(StringUtils.isNotBlank(flashSaleOrderInfo)){
                rushToBuyFlashSaleOrderNumRequest = JSONObject.parseObject(flashSaleOrderInfo, RushToBuyFlashSaleOrderNumRequest.class);
                rushToBuyFlashSaleOrderNumRequest.getFlashSaleNumList().add(rushToBuyFlashSaleGoodsRequest.getFlashSaleNum());
            } else {
                List<Integer> flashSaleNumList = new ArrayList<>();
                flashSaleNumList.add(rushToBuyFlashSaleGoodsRequest.getFlashSaleNum());
                rushToBuyFlashSaleOrderNumRequest.setFlashSaleNumList(flashSaleNumList);
            }
            //设置对应秒杀商品下单数缓存为两小时
            redisService.setObj(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS + request.getCustomerId() +
                            request.getFlashSaleGoodsId() + "flashSaleOrder",
                    rushToBuyFlashSaleOrderNumRequest, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
        }
    }
}
