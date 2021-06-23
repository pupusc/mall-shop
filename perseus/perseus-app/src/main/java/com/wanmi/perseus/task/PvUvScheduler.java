package com.wanmi.perseus.task;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ares.request.mq.CompanyFlow;
import com.wanmi.ares.request.mq.FlowRequest;
import com.wanmi.ares.request.mq.GoodsInfoFlow;
import com.wanmi.ares.request.mq.TerminalStatistics;
import com.wanmi.perseus.param.StaticParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

/**
 * PV/UV汇总redis统计数据并推送到统计系统
 *
 * @author bail 2017-09-21
 */
@JobHandler(value = "PvUvJobHandler")
@Component
@EnableBinding
public class PvUvScheduler extends IJobHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * @param type 1 一天执行一次 2、上一次任务执行后间隔多长时间执行下一次任务
     * @return
     */
    @Override
    public ReturnT<String> execute(String type) {
        XxlJobLogger.log("PV/UV汇总redis统计数据并推送到统计系统.");
        if ("1".equals(type)) {
            this.pushYesterdaySumDatas();//昨天
        } else {
            this.fixedRatePushSumDatas();//今天
        }
        return SUCCESS;
    }

    /**
     * 定时间隔汇总推送pv/uv汇总数据(5分钟执行一次)
     *
     * @author bail 2017-09-22
     */
    //fixedDelay: 上一次任务执行后间隔多长时间执行下一次任务
//    @Scheduled(fixedRate=300000)
    public void fixedRatePushSumDatas() {
        sumAndPush(LocalDate.now());//今天
    }

    /**
     * 凌晨0点01分推送昨日汇总数据(防止定时间隔推送会遗漏24:00前的一段间隔的数据)
     * 注意: 数据在凌晨2点就会过期失效
     *
     * @author bail 2017-09-22
     */
//    @Scheduled(cron = "0 1 0 * * ?")
    public void pushYesterdaySumDatas() {
        sumAndPush(LocalDate.now().minusDays(1L));//昨天
    }


    /**
     * 汇总redsi统计数据并推送统计系统
     *
     * @author bail 2017-09-21
     */
    public void sumAndPush(LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        /**1.定义汇总JSON对象(3层)*/
        FlowRequest sumObj = new FlowRequest();//统计对象
        sumObj.setOperationDate(LocalDateTime.now().toLocalDate());

        /**2.汇总整站分端pv*/
        sumObj.setPv(setPvCommon(StaticParam.PV_KEY, dateStr));

        /**3.汇总整站分端uv*/
        sumObj.setUv(setUvCommon(StaticParam.UV_KEY, dateStr));

        /**4.汇总各Sku分端pv以及uv*/
        Set<String> unoinSkuKeys;//各端出现的skuId的并集(即所有的skuId)
        List<GoodsInfoFlow> goodsInfoFlowList;//各sku的pv,uv汇总结果List
        GoodsInfoFlow goodsInfoFlow;//某sku的pv,uv
        /**4.1.获取各端skuId的并集(pv的sku并集同时也代表uv的sku并集)*/
        String allPatternSkuUv = String.format(StaticParam.SKU_UV_KEY, StaticParam.END_ALL, dateStr, "*");
        unoinSkuKeys = stringRedisTemplate.keys(allPatternSkuUv);
        /**4.2.遍历各端skuId的并集,获取某skuId的各端统计数字*/
        Iterator<String> itSku = unoinSkuKeys.iterator();
        int beginIndexSku = allPatternSkuUv.length() - 1;
        String skuId;
        goodsInfoFlowList = new ArrayList<>();
        while (itSku.hasNext()) {
            skuId = itSku.next().substring(beginIndexSku);//截取出skuId
            goodsInfoFlow = new GoodsInfoFlow();
            goodsInfoFlowList.add(goodsInfoFlow);//多个skuId的各端pv与uv
            goodsInfoFlow.setSkuId(skuId);//记录skuId
            /**4.3.汇总各Sku分端pv*/
            goodsInfoFlow.setPv(setPvCommon(StaticParam.SKU_PV_KEY, dateStr, skuId));
            /**4.4.汇总各Sku分端uv*/
            goodsInfoFlow.setUv(setUvCommon(StaticParam.SKU_UV_KEY, dateStr, skuId));
        }
        sumObj.setSkus(goodsInfoFlowList);

        /**5.汇总整站商品页总的分端pv*/
        sumObj.setSkuTotalPv(setPvCommon(StaticParam.PLAT_SKU_PV_KEY, dateStr));

        /**6.汇总整站商品页总的分端uv*/
        sumObj.setSkuTotalUv(setUvCommon(StaticParam.PLAT_SKU_UV_KEY, dateStr));

        /**7.汇总各店铺分端pv以及uv*/
        Set<String> unoinShopKeys;//各端出现的shopId的并集(即所有的shopId)
        List<CompanyFlow> companyFlowList;//各店铺的pv,uv汇总结果List
        CompanyFlow companyFlow;//某店铺的pv,uv
        /**7.1.获取各端shopId的并集(uv的shopId并集同时也是pv的shopId并集)*/
        String allPatternShopUv = String.format(StaticParam.SHOP_UV_KEY, StaticParam.END_ALL, dateStr, "*");
        unoinShopKeys = stringRedisTemplate.keys(allPatternShopUv);
        /**7.2.遍历各端shopId的并集,获取某shopId的各端统计数字*/
        Iterator<String> itShop = unoinShopKeys.iterator();
        int beginIndexShop = allPatternShopUv.length() - 1;
        String shopId;
        companyFlowList = new ArrayList<>();
        while (itShop.hasNext()) {
            shopId = itShop.next().substring(beginIndexShop);//截取出shopId
            companyFlow = new CompanyFlow();
            companyFlowList.add(companyFlow);//多个shopId的各端pv与uv
            companyFlow.setCompanyId(shopId);//记录shopId
            /**7.3.汇总各店铺分端总的pv*/
            companyFlow.setPv(setPvCommon(StaticParam.SHOP_PV_KEY, dateStr, shopId));
            /**7.4.汇总各店铺分端总的uv*/
            companyFlow.setUv(setUvCommon(StaticParam.SHOP_UV_KEY, dateStr, shopId));
            /**7.5.汇总各店铺商品页分端pv*/
            companyFlow.setSkuTotalPv(setPvCommon(StaticParam.SHOP_SKU_PV_KEY, dateStr, shopId));
            /**7.6.汇总各店铺商品页分端uv*/
            companyFlow.setSkuTotalUv(setUvCommon(StaticParam.SHOP_SKU_UV_KEY, dateStr, shopId));
        }
        sumObj.setCompanyFlows(companyFlowList);

        /**8.形成推送JSON对象,并通过Rabbit MQ推送*/
        sumObj.setTime(date);
        sumObj.setSendTime(LocalDateTime.now());
        resolver.resolveDestination(StaticParam.QUEUE_NAME).send(new GenericMessage<>(JSONObject.toJSONString(sumObj)));
    }

    /**
     * 设置pv汇总数据的公共方法
     *
     * @param redisKey pv的redis key模板
     * @param dateStr  替换redis key模板中的日期
     * @return 各端统计结果
     */
    private TerminalStatistics setPvCommon(String redisKey, String dateStr) {
        TerminalStatistics jsonObjTmp = TerminalStatistics.builder().build();//具体的分端汇总数量{pc:10, h5:20, app:30}
        String countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_PC, dateStr));//统计数字-字符串格式
        jsonObjTmp.setPC(countStr == null ? 0L : Long.parseLong(countStr));
        countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_H5, dateStr));
        jsonObjTmp.setH5(countStr == null ? 0L : Long.parseLong(countStr));
        countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_APP, dateStr));
        jsonObjTmp.setAPP(countStr == null ? 0L : Long.parseLong(countStr));
        return jsonObjTmp;
    }

    /**
     * 设置pv汇总数据的公共方法_重载(3个参数)
     *
     * @param redisKey pv的redis key模板
     * @param dateStr  替换redis key模板中的日期
     * @param singleId 替换redis key模板中的单个id
     * @return 各端统计结果
     */
    private TerminalStatistics setPvCommon(String redisKey, String dateStr, String singleId) {
        TerminalStatistics jsonObjTmp = TerminalStatistics.builder().build();//具体的分端汇总数量{pc:10, h5:20, app:30}
        String countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_PC, dateStr, singleId));////统计数字-字符串格式
        jsonObjTmp.setPC(countStr == null ? 0L : Long.parseLong(countStr));
        countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_H5, dateStr, singleId));
        jsonObjTmp.setH5(countStr == null ? 0L : Long.parseLong(countStr));
        countStr = stringRedisTemplate.opsForValue().get(String.format(redisKey, StaticParam.END_APP, dateStr, singleId));
        jsonObjTmp.setAPP(countStr == null ? 0L : Long.parseLong(countStr));
        return jsonObjTmp;
    }

    /**
     * 设置uv汇总数据的公共方法
     *
     * @param redisKey uv的redis key模板
     * @param dateStr  替换redis key模板中的日期
     * @return 各端uv以及所有端汇总的统计结果
     */
    private TerminalStatistics setUvCommon(String redisKey, String dateStr) {
        TerminalStatistics jsonObjTmp = TerminalStatistics.builder().build();//具体的分端的用户标识集合{pc:[1,2,3], h5:[1,2,3], app:[1,2,3], total:[1,2,3]}
        Set<String> userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_PC, dateStr));//用户id的Set集合
        jsonObjTmp.setPcUserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_H5, dateStr));
        jsonObjTmp.setH5UserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_APP, dateStr));
        jsonObjTmp.setAppUserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_ALL, dateStr));
        jsonObjTmp.setTotalUserIds(userIds);//汇总的uv
        return jsonObjTmp;
    }

    /**
     * 设置uv汇总数据的公共方法_重载(3个参数)
     *
     * @param redisKey uv的redis key模板
     * @param dateStr  替换redis key模板中的日期
     * @param singleId 替换redis key模板中的单个id
     * @return 各端uv以及所有端汇总的统计结果
     */
    private TerminalStatistics setUvCommon(String redisKey, String dateStr, String singleId) {
        TerminalStatistics jsonObjTmp = TerminalStatistics.builder().build();//具体的分端的用户标识集合{pc:[1,2,3], h5:[1,2,3], app:[1,2,3], total:[1,2,3]}
        Set<String> userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_PC, dateStr, singleId));//用户id的Set集合
        jsonObjTmp.setPcUserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_H5, dateStr, singleId));
        jsonObjTmp.setH5UserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_APP, dateStr, singleId));
        jsonObjTmp.setAppUserIds(userIds);
        userIds = stringRedisTemplate.opsForSet().members(String.format(redisKey, StaticParam.END_ALL, dateStr, singleId));
        jsonObjTmp.setTotalUserIds(userIds);//汇总的uv
        return jsonObjTmp;
    }

    /**
     * 汇总各端的skuId(取skuId并集)_暂无用了
     * @author bail 2017-09-22
     * @param unoinKeys 并集
     * @param matchKeys 各端的key
     * @param pattern 截取出skuId的前缀
     */
/*    private static void addUnionSkus(Set<String> unoinKeys, Set<String>matchKeys, String pattern){
        if(matchKeys!=null){
            Iterator<String> it = matchKeys.iterator();
            int beginIndex = pattern.length()-1;
            while(it.hasNext()) {
                unoinKeys.add(it.next().substring(beginIndex));//截取出skuId
            }
        }
    }*/

}
