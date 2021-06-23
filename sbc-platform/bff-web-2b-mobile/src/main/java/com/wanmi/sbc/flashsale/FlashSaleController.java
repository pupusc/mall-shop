package com.wanmi.sbc.flashsale;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleGoodsRequest;
import com.wanmi.sbc.flashsale.service.FlashSaleService;
import com.wanmi.sbc.goods.api.provider.flashsaleactivity.FlashSaleActivityQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalecate.FlashSaleCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.flashsalegoods.FlashSaleGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.flashsaleactivity.FlashSaleActivityListRequest;
import com.wanmi.sbc.goods.api.request.flashsalecate.FlashSaleCateListRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.flashsaleactivity.FlashSaleActivityListResponse;
import com.wanmi.sbc.goods.api.response.flashsalecate.FlashSaleCateListResponse;
import com.wanmi.sbc.goods.api.response.flashsalegoods.FlashSaleGoodsPageResponse;
import com.wanmi.sbc.goods.bean.vo.FlashSaleActivityVO;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingGoodsForXsiteRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingLevelType;
import com.wanmi.sbc.mq.RushToBuyFlashSaleGoodsMqService;
import com.wanmi.sbc.redis.RedisService;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Api(description = "秒杀首页API", tags = "FlashSaleController")
@RestController
@RequestMapping(value = "/flashsale")
public class FlashSaleController {

    @Autowired
    private FlashSaleCateQueryProvider flashSaleCateQueryProvider;

    @Autowired
    private FlashSaleActivityQueryProvider flashSaleActivityQueryProvider;

    @Autowired
    private FlashSaleGoodsQueryProvider flashSaleGoodsQueryProvider;

    public static final String COMPLETED = "已结束";

    public static final String BEGIN = "已开抢";

    public static final String SALE = "抢购中";

    public static final String ABOUT_TO_START = "即将开始";

    public static final String NEXT_DAY_NOTICE = "次日预告";

    @Autowired
    private RushToBuyFlashSaleGoodsMqService rushToBuyFlashSaleGoodsMq;

    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @ApiOperation(value = "查询场次信息")
    @GetMapping(value = "/sceneList")
    public BaseResponse<FlashSaleActivityListResponse> sceneList() {
        //当前时间前后24小时 有商品参与的场次
        FlashSaleActivityListRequest listReq = new FlashSaleActivityListRequest();
        listReq.setFullTimeBegin(LocalDateTime.now().minusDays(1));
        listReq.setFullTimeEnd(LocalDateTime.now().plusDays(1));
        List<FlashSaleActivityVO> activityVOList = flashSaleActivityQueryProvider.list(listReq).getContext()
                .getFlashSaleActivityVOList();
        if (CollectionUtils.isEmpty(activityVOList)) {
            return BaseResponse.success(FlashSaleActivityListResponse.builder().flashSaleActivityVOList(activityVOList).build());
        }
        //获取离当前时间最近的时间 为默认选中场次
        LocalDateTime nowTime = LocalDateTime.parse(DateUtil.nowHourTime(), DateTimeFormatter.ofPattern(DateUtil.FMT_TIME_6));
        //默认第一个为最近
        long minDiff = Duration.between(nowTime, activityVOList.get(0).getActivityFullTime()).toMillis();
        minDiff = Math.abs(minDiff);
        String activityDate = activityVOList.get(0).getActivityDate();
        String activityTime = activityVOList.get(0).getActivityTime();
        for (FlashSaleActivityVO activity : activityVOList) {
            activity.setStatus(getActivityStatus(activity.getActivityDate(), activity.getActivityFullTime()));
            long timeDiff = Duration.between(nowTime, activity.getActivityFullTime()).toMillis();
            timeDiff = Math.abs(timeDiff);
            if (timeDiff < minDiff) {
                minDiff = timeDiff;
                activityDate = activity.getActivityDate();
                activityTime = activity.getActivityTime();
            }
        }

        return BaseResponse.success(FlashSaleActivityListResponse.builder().flashSaleActivityVOList(activityVOList)
                .recentDate(activityDate).recentTime(activityTime).build());
    }

    private String getActivityStatus(String date, LocalDateTime fullTime) {
        if (date.equals(DateUtil.nowDate())) {
            if (fullTime.isAfter(LocalDateTime.now())) {
                return ABOUT_TO_START;
            }
            if ((fullTime.isEqual(LocalDateTime.now()) ||
                    fullTime.isBefore(LocalDateTime.now())) &&
                    (fullTime.isAfter(LocalDateTime.now().minusHours(Constants.FLASH_SALE_LAST_HOUR)) ||
                            fullTime.isEqual(LocalDateTime.now().minusHours(Constants.FLASH_SALE_LAST_HOUR)))) {
                //距离当前时间不足一小时的场次即为*正在抢购
                if (fullTime.isAfter((LocalDateTime.now().minusHours(1)))) {
                    return SALE;
                }
                return BEGIN;
            }
        } else if (date.equals(DateUtil.tomorrowDate())) {
            return NEXT_DAY_NOTICE;
        }
        return COMPLETED;
    }

    @ApiOperation(value = "查询秒杀分类信息")
    @GetMapping(value = "/cateList")
    public BaseResponse<FlashSaleCateListResponse> cateList() {
        FlashSaleCateListRequest listReq = new FlashSaleCateListRequest();
        listReq.setDelFlag(DeleteFlag.NO);
        listReq.putSort("sort", "asc");
        return flashSaleCateQueryProvider.list(listReq);
    }

    /**
     * 当前场次有库存的秒杀商品
     *
     * @return
     */
    @ApiOperation(value = "查询秒杀商品")
    @PostMapping("/goodsList")
    public BaseResponse<FlashSaleGoodsPageResponse> goodsList(@RequestBody @Valid FlashSaleGoodsPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        this.fillSort(pageReq);
        System.out.println("activityDate" + pageReq.getActivityDate() + "activityTime" + pageReq.getActivityTime() + "cateId" + pageReq.getCateId());
        pageReq.setVendibility(Constants.yes);
        return flashSaleGoodsQueryProvider.page(pageReq);
    }

    /**
     * 当前场次有库存的秒杀商品
     *
     * @return
     */
    @ApiOperation(value = "查询秒杀商品")
    @PostMapping("/xsite/goodsList")
    public BaseResponse<FlashSaleGoodsPageResponse> goodsListForXsite(@RequestBody @Valid FlashSaleGoodsPageRequest pageReq) {
        pageReq.setDelFlag(DeleteFlag.NO);
        this.fillSort(pageReq);
        System.out.println("activityDate" + pageReq.getActivityDate() + "activityTime" + pageReq.getActivityTime() + "cateId" + pageReq.getCateId());
        pageReq.setVendibility(Constants.yes);
        List<FlashSaleGoodsVO> flashSaleGoodsVOS = flashSaleGoodsQueryProvider.page(pageReq).getContext().getFlashSaleGoodsVOPage().getContent();
        if(CollectionUtils.isNotEmpty(flashSaleGoodsVOS)) {
            List<String> ids = flashSaleGoodsVOS.stream().map(vo -> vo.getGoodsInfoId()).collect(Collectors.toList());
            MarketingGoodsForXsiteRequest marketingRequest = MarketingGoodsForXsiteRequest.builder()
                    .marketingLevelType(MarketingLevelType.FLASH_SALE)
                    .goodsInfoIds(ids).build();
            List<String> goodsInfoIds = marketingQueryProvider.queryForXsite(marketingRequest).getContext().getGoodsInfoIds();
            flashSaleGoodsVOS = flashSaleGoodsVOS.stream().filter(vo -> goodsInfoIds.contains(vo.getGoodsInfoId())).collect(Collectors.toList());
        }
        FlashSaleGoodsPageResponse response = FlashSaleGoodsPageResponse.builder()
                .flashSaleGoodsVOPage(new MicroServicePage<>(flashSaleGoodsVOS)).build();
        return BaseResponse.success(response);
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 立刻抢购秒杀商品
     * @Date 9:42 2019/6/14
     * @Param [request]
     **/
    @ApiOperation(value = "立刻抢购秒杀商品")
    @PostMapping("/rushToBuyFlashSaleGoods")
    public BaseResponse rushToBuyFlashSaleGoods(@RequestBody @Valid RushToBuyFlashSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        request.setTerminalToken(commonUtil.getTerminalToken());
        try {
            //判断是否具有抢购资格，如果存在资格，则不能抢购
            String qualificationsInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                    request.getCustomerId() + request.getFlashSaleGoodsId());
            if (StringUtils.isNotBlank(qualificationsInfo)) {
                throw new SbcRuntimeException(ErrorCodeConstant.FLASH_SALE_PANIC_BUYING_BUSY);
            }
            //抢购秒杀商品对应限制条件判断
            flashSaleService.judgeBuyFalshSaleGoodsCondition(request);
            //发送mq消息异步处理抢购资格
            rushToBuyFlashSaleGoodsMq.rushToBuyFlashSaleGoodsMq(JSONObject.toJSONString(request));
        } catch (SbcRuntimeException e) {
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        } catch (Exception e) {
            log.error("context",e);
        }
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 获取是否具有抢购资格
     * @Date 9:29 2019/6/17
     * @Param [request]
     **/
    @ApiOperation(value = "获取是否具有抢购资格")
    @PostMapping("/getFlashSaleGoodsQualifications")
    public BaseResponse<RushToBuyFlashSaleGoodsRequest> getFlashSaleGoodsQualifications(@RequestBody @Valid RushToBuyFlashSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        RushToBuyFlashSaleGoodsRequest flashSaleGoodsRequest = new RushToBuyFlashSaleGoodsRequest();
        try {
            flashSaleGoodsRequest = flashSaleService.getFlashSaleGoodsQualifications(request);
        } catch (SbcRuntimeException e) {
            throw new SbcRuntimeException(e.getErrorCode(), e.getParams());
        } catch (Exception e) {
            log.error("context",e);
        }
        return BaseResponse.success(flashSaleGoodsRequest);
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 删除抢购资格
     * @Date 17:55 2019/6/22
     * @Param [request]
     **/
    @ApiOperation(value = "删除抢购资格")
    @PostMapping("/delFlashSaleGoodsQualifications")
    public BaseResponse delFlashSaleGoodsQualifications(@RequestBody @Valid RushToBuyFlashSaleGoodsRequest request) {
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        FlashSaleGoodsVO flashSaleGoodsVO = new FlashSaleGoodsVO();
        flashSaleService.delFlashSaleGoodsQualification(request);
        return BaseResponse.success(flashSaleGoodsVO);
    }

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO>
     * @Author lvzhenwei
     * @Description 获取秒杀活动详情
     * @Date 10:58 2019/7/1
     * @Param [request]
     **/
    @ApiOperation(value = "获取秒杀活动详情")
    @PostMapping("/getFlashSaleInfo")
    public BaseResponse<FlashSaleGoodsVO> getFlashSaleInfo(@RequestBody @Valid RushToBuyFlashSaleGoodsRequest request) {
        return BaseResponse.success(flashSaleService.getFlashSaleGoodsInfo(request));
    }

    /**
     * @Author lvzhenwei
     * @Description 更新秒杀商品订单数量信息
     * @Date 19:29 2019/10/3
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse
     **/
    @ApiOperation(value = "更新秒杀商品订单数量信息")
    @PostMapping("/updateFlashSaleGoodsOrderNum")
    public BaseResponse updateFlashSaleGoodsOrderNum(@RequestBody @Valid RushToBuyFlashSaleGoodsRequest request){
        request.setCustomerId(commonUtil.getCustomer().getCustomerId());
        flashSaleService.updateFlashSaleGoodsOrderNum(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询结果排序
     * 0:销量倒序
     * 1:好评数倒序
     * 2:评论率倒序
     * 3:排序号倒序
     */
    private void fillSort(FlashSaleGoodsPageRequest pageReq) {
        if (Objects.nonNull(pageReq.getSortFlag())) {
            switch (pageReq.getSortFlag()) {
                case 0:
                    pageReq.putSort("goods.allSalesNum", SortType.DESC.toValue());
                    pageReq.putSort("createTime", SortType.DESC.toValue());
                    break;
                case 1:
                    pageReq.putSort("goods.goodsFavorableCommentNum", SortType.DESC.toValue());
                    pageReq.putSort("createTime", SortType.DESC.toValue());
                    break;
                case 2:
                    pageReq.putSort("goods.feedbackRate", SortType.DESC.toValue());
                    pageReq.putSort("createTime", SortType.DESC.toValue());
                    break;
                case 3:
                    pageReq.putSort("goods.sortNo", SortType.DESC.toValue());
                    pageReq.putSort("createTime", SortType.DESC.toValue());
                    break;
                default:
                    pageReq.putSort("salesVolume", SortType.DESC.toValue());
                    pageReq.putSort("createTime", SortType.DESC.toValue());
                    pageReq.putSort("id", "desc");
                    break;
            }
        } else {
            pageReq.putSort("salesVolume", SortType.DESC.toValue());
            pageReq.putSort("createTime", SortType.DESC.toValue());
            pageReq.putSort("id", "desc");
        }
    }
}
