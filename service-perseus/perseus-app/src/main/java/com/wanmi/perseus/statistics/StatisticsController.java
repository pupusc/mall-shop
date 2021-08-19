package com.wanmi.perseus.statistics;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.perseus.param.CustomerFollowInfoParam;
import com.wanmi.perseus.param.FollowParam;
import com.wanmi.perseus.param.StaticParam;
import com.wanmi.perseus.param.TerminalSource;
import com.wanmi.perseus.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * PV/UV统计数据接收类
 * @author bail 2017-09-21
 */
@Slf4j
@RestController
public class StatisticsController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Value("${kafka.send.enabled:false}")
    private Boolean sendFlag;

    /**
     * 获取访问的pv数据 , 形成redis统计数据(即方便业务汇总的数据)
     * @author bail 2017-09-21
     */
    @RequestMapping(value = "/wm.gif", method = RequestMethod.POST)
    @ResponseBody
    public void create(@RequestBody List<FollowParam> params) {
        for(FollowParam param: params) {
            log.info("会员浏览行为数据信息skuId1===" + param.getSkuId());
            if (StringUtils.isAnyEmpty(param.getUrl(), param.getClientType()) ||
                    (!param.getClientType().equals(StaticParam.END_PC)
                            && !param.getClientType().equals(StaticParam.END_H5)
                            && !param.getClientType().equals(StaticParam.END_APP)
                            && !param.getClientType().equals(StaticParam.END_MINIPROGRAM))) {//简单校验,避免伪造信息s
                return;
            }
      /*  if(StringUtils.isNoneBlank(param.getSkuId())){
            skuId = skuId.split(",")[0];
        }*/
            if (sendFlag) {
                //会员浏览行为数据信息
                if (StringUtils.isNotBlank(param.getId())) {
                    CustomerFollowInfoParam customerFollowInfoParam = new CustomerFollowInfoParam();
                    log.info("会员浏览行为数据信息customerId====" + param.getId());
                    customerFollowInfoParam.setCustomer_id(param.getId());
                    customerFollowInfoParam.setTerminal_source(TerminalSource.getTerminalSource(param.getClientType()).toValue());
                    if (StringUtils.isNotBlank(param.getSkuId())) {
                        customerFollowInfoParam.setGoods_info_id(param.getSkuId());
                    } else {
                        customerFollowInfoParam.setGoods_info_id(null);
                    }
                    if (StringUtils.isNotBlank(param.getSkuId())) {
                        customerFollowInfoParam.setCompany_info_id(param.getShopId());
                    } else {
                        customerFollowInfoParam.setCompany_info_id(null);
                    }
                    customerFollowInfoParam.setUrl(param.getUrl());
                    customerFollowInfoParam.setCreate_time(LocalDateTime.now());
                    customerFollowInfo(customerFollowInfoParam);
                }
            }
            if (StringUtils.isEmpty(param.getId())) {//id可能为空,比如cookie未开启,则将用户唯一标识转为ip进行计算
                param.setId(HttpUtil.getIpAddr());
            }

            String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));//当天日期的字符串格式
            Date expireTime = getExpireTime();//获取失效时间,默认次日凌晨2点

            /**1.分端获取整站当日pv 统计数量*/
            setDatePv(param.getClientType(), dateStr, expireTime);
            /**2.分端获取整站当日uv 统计数量*/
            setDateUv(param.getId(), param.getClientType(), dateStr, expireTime);

            if (StringUtils.isNoneEmpty(param.getSkuId(), param.getShopId())) {//skuId,shopId都不为空,表示商品详情页,既计算商品的pv/uv , 也计算平台中所有商品的pv/uv , 也计算店铺所有商品的pv/uv , 也计算店铺总的pv/uv
                /**3.分端获取某Sku当日pv 平台商品页pv 店铺商品页pv 店铺总pv 统计数量*/
                setDateSkuPv(param.getSkuId(), param.getShopId(), param.getClientType(), dateStr, expireTime);
                /**4.分端获取某Sku当日uv 平台商品页uv 店铺商品页uv 店铺总uv 统计数量*/
                setDateSkuUv(param.getId(), param.getSkuId(), param.getShopId(), param.getClientType(), dateStr, expireTime);
            }

            if (StringUtils.isEmpty(param.getSkuId()) && StringUtils.isNotEmpty(param.getShopId())) {//只有shopId不为空,skuId为空,表示店铺页,则只计算入店铺总的pv/uv中
                /**5.分端获取某店铺的当日pv 统计数量(相当于单独统计店铺页的流量,一并汇入店铺总的流量)*/
                setDateShopPv(param.getShopId(), param.getClientType(), dateStr, expireTime);
                /**6.分端获取某店铺的当日uv 统计数量(相当于单独统计店铺页的流量,一并汇入店铺总的流量)*/
                setDateShopUv(param.getId(), param.getShopId(), param.getClientType(), dateStr, expireTime);
            }
        }
    }

    /**
     * 分端获取整站当日pv 统计数量
     * @author bail 2017-09-21
     */
    private void setDatePv(String clientType, String dateStr, Date expireTime){
        String pvDateKey = String.format(StaticParam.PV_KEY,clientType,dateStr);
        setPv(pvDateKey,expireTime);
    }

    /**
     * 分端获取某Sku当日pv 平台商品页pv 店铺商品页pv 店铺总pv 统计数量
     * @author bail 2017-09-21
     */
    private void setDateSkuPv(String skuId, String shopId, String clientType, String dateStr, Date expireTime){
        if(StringUtils.isEmpty(skuId)){
            return;
        }
        //1.统计某Sku当日pv
        String pvDateKey = String.format(StaticParam.SKU_PV_KEY,clientType,dateStr,skuId);//三端某sku当日pv的key,形如pc-pv-20170921-sku-123asd2asdf
        setPv(pvDateKey,expireTime);
        //2.统计平台商品页pv
        pvDateKey = String.format(StaticParam.PLAT_SKU_PV_KEY,clientType,dateStr);//三端平台中商品页当日pv的key,形如pc-pv-20170921-platsku
        setPv(pvDateKey,expireTime);

        if(StringUtils.isEmpty(shopId)){
            return;
        }
        //3.统计店铺sku的pv
        pvDateKey = String.format(StaticParam.SHOP_SKU_PV_KEY,clientType,dateStr,shopId);//三端某店铺商品页当日pv的key,形如pc-pv-20170921-shopsku-2
        setPv(pvDateKey,expireTime);
        //4.统计店铺总的pv
        setDateShopPv(shopId,clientType,dateStr,expireTime);
    }

    /**
     * 分端获取某店铺总的当日pv 统计数量
     * @author bail 2017-09-29
     */
    private void setDateShopPv(String shopId, String clientType, String dateStr, Date expireTime){
        if(StringUtils.isEmpty(shopId)){
            return;
        }
        //统计店铺总的pv
        String pvDateKey = String.format(StaticParam.SHOP_PV_KEY,clientType,dateStr,shopId);//三端某店铺总的当日pv的key,形如pc-pv-20170921-shop-2
        setPv(pvDateKey,expireTime);
    }

    /**
     * 根据key存储pv公共方法
     * @author bail 2017-09-21
     * @param pvDateKey redis存储的key
     * @param expireTime 失效时间
     */
    private void setPv(String pvDateKey, Date expireTime){
        /*使用increment代替先get,自增,再set方式,保证原子性以及高并发问题*/
        Long pvActual = stringRedisTemplate.opsForValue().increment(pvDateKey,1l);//获取某端当日当前实际pv

        if(pvActual != null && pvActual.longValue() == 1l){
            stringRedisTemplate.expireAt(pvDateKey,expireTime);//初始化过期时间
        }
    }

    /**
     * 分端获取整站当日uv 统计数量
     * @author bail 2017-09-21
     */
    private void setDateUv(String id, String clientType, String dateStr, Date expireTime){
        String uvDateKey = String.format(StaticParam.UV_KEY,clientType,dateStr);//三端分端统计整站当日uv的key,形如pc-uv-20170921
        setUv(id,uvDateKey,expireTime);

        uvDateKey = String.format(StaticParam.UV_KEY,StaticParam.END_ALL,dateStr);//整站当日uv的key,形如ALL-uv-20170921(包括所有端的总量)
        setUv(id,uvDateKey,expireTime);
    }

    /**
     * 分端获取某Sku当日uv 平台商品页uv 店铺商品页uv 店铺总uv 统计数量
     * @author bail 2017-09-21
     */
    private void setDateSkuUv(String id, String skuId, String shopId, String clientType, String dateStr, Date expireTime){
        if(StringUtils.isEmpty(skuId)){
            return;
        }
        //1.某Sku当日分端uv 以及 所有端汇总uv
        String uvDateKey = String.format(StaticParam.SKU_UV_KEY,clientType,dateStr,skuId);//三端某sku当日uv的key,形如pc-uv-20170921-sku-fsd2ffaakl32fas
        setUv(id,uvDateKey,expireTime);
        uvDateKey = String.format(StaticParam.SKU_UV_KEY,StaticParam.END_ALL,dateStr,skuId);//某sku当日汇总uv的key,形如ALL-uv-20170921-sku-fsd2ffaakl32fas(包括所有端的总量)
        setUv(id,uvDateKey,expireTime);

        //2.统计平台商品页uv 以及 所有端汇总uv
        uvDateKey = String.format(StaticParam.PLAT_SKU_UV_KEY,clientType,dateStr);//三端平台中商品页当日uv的key,形如pc-uv-20170921-platsku
        setUv(id,uvDateKey,expireTime);
        uvDateKey = String.format(StaticParam.PLAT_SKU_UV_KEY,StaticParam.END_ALL,dateStr);//平台中商品页当日汇总uv的key,形如ALL-uv-20170921-platsku(包括所有端的总量)
        setUv(id,uvDateKey,expireTime);

        if(StringUtils.isEmpty(shopId)){
            return;
        }
        //3.统计店铺sku的uv 以及 所有端汇总uv
        uvDateKey = String.format(StaticParam.SHOP_SKU_UV_KEY,clientType,dateStr,shopId);//三端某店铺商品页当日pv的key,形如pc-uv-20170921-shopsku-2
        setUv(id,uvDateKey,expireTime);
        uvDateKey = String.format(StaticParam.SHOP_SKU_UV_KEY,StaticParam.END_ALL,dateStr,shopId);//某店铺商品页当日汇总uv的key,形如ALL-uv-20170921-shopsku-2(包括所有端的总量)
        setUv(id,uvDateKey,expireTime);

        //4.统计店铺总的uv 以及 所有端汇总uv
        setDateShopUv(id,shopId,clientType,dateStr,expireTime);
    }

    /**
     * 分端获取某店铺总的当日uv 统计数量
     * @author bail 2017-09-29
     */
    private void setDateShopUv(String id, String shopId, String clientType, String dateStr, Date expireTime){
        if(StringUtils.isEmpty(shopId)){
            return;
        }
        //统计店铺总的uv 以及 所有端汇总uv
        String uvDateKey = String.format(StaticParam.SHOP_UV_KEY,clientType,dateStr,shopId);//三端某店铺当日pv的key,形如pc-uv-20170921-shop-2
        setUv(id,uvDateKey,expireTime);
        uvDateKey = String.format(StaticParam.SHOP_UV_KEY,StaticParam.END_ALL,dateStr,shopId);//某店铺当日汇总uv的key,形如ALL-uv-20170921-shop-2(包括所有端的总量)
        setUv(id,uvDateKey,expireTime);
    }

    /**
     * 根据key存储uv公共方法
     * @param id 用户唯一标识
     * @param uvDateKey redis存储的key
     * @param expireTime 失效时间
     */
    private void setUv(String id, String uvDateKey, Date expireTime){
        stringRedisTemplate.opsForSet().add(uvDateKey, id);//插入用户唯一标识,返回成功插入个数

        Long expireTm = stringRedisTemplate.getExpire(uvDateKey);//获取key的过期时间,默认为-1
        if(expireTm == null || expireTm.longValue() < 0l){
            stringRedisTemplate.expireAt(uvDateKey,expireTime);//若没有过期时间,则初始化过期时间
        }
    }

    /**
     * 获取redis统计数据的失效时间
     * @author bail 2017-09-21
     * @return 失效的时间
     */
    private Date getExpireTime(){
        //明天凌晨2点整
        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(StaticParam.EXPIRE_HOUR, StaticParam.EXPIRE_MINUTE));
        //转换成Date类型
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 根据url获取skuId_暂无用了
     * @author bail 2017-09-21
     * @param url 当前访问的全路径
     * @param clientType 哪个端的请求(pc/h5/app)
     * @return 若存在,则返回skuId,若不存在,则返回null
     */
 /*   private String getSkuIdFromUrl(String url, String clientType){
        if(StringUtils.isAnyEmpty(url,clientType)){
            return null;
        }

        String ruleStr = clientRules.get(clientType);
        int ind;
        if((ind = url.indexOf(ruleStr)) != -1){
            return url.substring(ind+ruleStr.length());
        }
        return null;
    }*/

    /**
     * @Author lvzhenwei
     * @Description 会员浏览行为数据信息
     * @Date 15:53 2020/3/11
     * @Param [customerFollowInfoParam]
     * @return void
     **/
    private void customerFollowInfo(CustomerFollowInfoParam customerFollowInfoParam){
        log.info("会员浏览行为数据信息2===="+customerFollowInfoParam.getGoods_info_id());
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("customer_follow",
                JSONObject.toJSONStringWithDateFormat(customerFollowInfoParam,"yyyy-MM-dd HH:mm:ss"));
        kafkaTemplate.send(producerRecord);
    }

}
