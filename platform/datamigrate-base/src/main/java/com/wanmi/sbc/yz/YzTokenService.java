package com.wanmi.sbc.yz;

import com.wanmi.sbc.redis.RedisService;
import com.youzan.cloud.open.sdk.common.exception.SDKException;
import com.youzan.cloud.open.sdk.core.client.core.DefaultYZClient;
import com.youzan.cloud.open.sdk.core.oauth.model.OAuthToken;
import com.youzan.cloud.open.sdk.core.oauth.token.TokenParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 有赞token获取服务
 */
@Service
public class YzTokenService {

    static String CLIENT_ID = "a918a2c3c50053c152";
    static String CLIENT_SECRET = "b556f5702cf3d221eb884f6f01204a57";
    static String KDT_ID = "18434307";

    @Autowired
    private RedisService redisService;

    /**
     * 获取token
     * @return
     */
    public String getToken() {
        String accessToken = redisService.getString("yz_access_token");
        if(accessToken!=null) {
            return accessToken;
        }
        DefaultYZClient yzClient = new DefaultYZClient();
        TokenParameter tokenParameter = null;
        try {
            tokenParameter = TokenParameter.self()
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .grantId(KDT_ID)
                    .refresh(true)
                    .build();
            OAuthToken oAuthToken = yzClient.getOAuthToken(tokenParameter);
            redisService.setString("yz_access_token",oAuthToken.getAccessToken());
            redisService.setString("yz_refresh_token",oAuthToken.getRefreshToken());
            redisService.expireByMilliseconds("yz_access_token",oAuthToken.getExpires());
            System.out.println("accessToken:"+oAuthToken.getAccessToken());
            return oAuthToken.getAccessToken();
        } catch (SDKException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 删除redis中的token
     */
    public void removeToken() {
        redisService.delete("yz_access_token");
    }

}
