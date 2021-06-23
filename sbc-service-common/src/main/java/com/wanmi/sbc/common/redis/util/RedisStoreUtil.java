package com.wanmi.sbc.common.redis.util;

import com.wanmi.sbc.common.redis.CacheKeyConstant;

/**
 * 门店缓存key集合
 */
public class RedisStoreUtil {

    /**
     * 门店小程序配置
     * @param storeId
     * @return
     */
    public static String getWechatMiniProgramConfig(Long storeId){
        return CacheKeyConstant.STORE_WECHAT_MINI_PROGRAM_CONFIG+"-"+storeId;
    }

    /**
     * 小程序码AccessToken
     * @param storeId
     * @return
     */
    public static String getWechatAccessToken(Long storeId){
        return CacheKeyConstant.WECHAT_SMALL_PROGRAM_ACCESS_TOKEN+"-"+storeId;
    }

    /**
     * 门店微信分享配置
     * @param storeId
     * @return
     */
    public static String getWechatShareSet(Long storeId){
        return CacheKeyConstant.STORE_WECHAT_SHARE_SET+"-"+storeId;
    }
}
