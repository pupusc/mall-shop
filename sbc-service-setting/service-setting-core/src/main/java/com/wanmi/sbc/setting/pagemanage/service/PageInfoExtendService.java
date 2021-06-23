package com.wanmi.sbc.setting.pagemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.setting.api.request.MiniProgramQrCodeRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByPageIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendModifyRequest;
import com.wanmi.sbc.setting.pagemanage.model.root.PageInfoExtend;
import com.wanmi.sbc.setting.pagemanage.repository.PageInfoExtendRepository;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.wechat.WechatApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.util.Objects;

/**
 * 物流信息服务
 * Created by dyt on 2020/4/17.
 */
@Slf4j
@Service
@Transactional(readOnly = true, timeout = 10)
public class PageInfoExtendService {

    @Autowired
    private PageInfoExtendRepository pageInfoExtendRepository;

    @Autowired
    private WechatApiUtil wechatApiUtil;

    @Autowired
    private RedisService redisService;

    private final static String mainPageType = "index";

    private final static String recruitingCustomersFlag = "RECRUITING_CUSTOMERS_FLAG";


    @Transactional
    public PageInfoExtend findById(PageInfoExtendByIdRequest request) {
        PageInfoExtend extend = pageInfoExtendRepository.findById(request.getPageId()).orElse(null);
        MiniProgramQrCodeRequest codeRequest = new MiniProgramQrCodeRequest();
        codeRequest.setPage("pages/sharepage/sharepage");
        codeRequest.setWidth(375);

        byte[] miniQrCode = null;
        String base64Head = "data:image/png;base64,";
        //初始化时，首页获取店铺首页程序二维码
        if (Objects.isNull(extend) && mainPageType.equalsIgnoreCase(request.getPageType())) {
            if (Objects.nonNull(request.getStoreId())) {//店铺首页
                codeRequest.setScene(String.format("/store-main/%s", request.getStoreId()));
                miniQrCode = wechatApiUtil.getWxaCodeBytesUnlimit(codeRequest, "PUBLIC");
            } else {
                //平台首页
                codeRequest.setScene("/");
                miniQrCode = wechatApiUtil.getWxaCodeBytesUnlimit(codeRequest, "PUBLIC");
            }
        }

        //非首页的页面，根据redis是否过期存储
        String key = "TF".concat(request.getPageId());
        if ((!mainPageType.equalsIgnoreCase(request.getPageType())) && (!redisService.hasKey(key))) {
            String storePram = "";
            if (Objects.nonNull(request.getStoreId())) {
                storePram = String.format("&storeId=%s", request.getStoreId());
            }
            String param = String.format("?pageType=%s&pageCode=%s%s", request.getPageType(), request.getPageCode(), storePram);
            redisService.setString(key, param, 15000000L);
            codeRequest.setScene(key);
            miniQrCode = wechatApiUtil.getWxaCodeBytesUnlimit(codeRequest, "PUBLIC");

            //如果只更新小程序二维码
            if (Objects.nonNull(extend) && miniQrCode != null) {
                extend.setMiniProgramQrCode(base64Head.concat(new BASE64Encoder().encode(miniQrCode)));
                pageInfoExtendRepository.save(extend);
            }
        }

        //新增
        if (Objects.isNull(extend)) {
            extend = new PageInfoExtend();
            extend.setPageId(request.getPageId());
            extend.setPageCode(request.getPageCode());
            extend.setPageType(request.getPageType());
            extend.setPlatform(request.getPlatform());
            extend.setStoreId(request.getStoreId());
            if (miniQrCode != null) {
                extend.setMiniProgramQrCode(base64Head.concat(new BASE64Encoder().encode(miniQrCode)));
            }
            extend.setUseType(0);
            //店铺
            if (Objects.nonNull(extend.getStoreId())) {
                //首页
                if (mainPageType.equalsIgnoreCase(extend.getPageType())) {
                    extend.setUrl(String.format("pages/package-A/store/store-main/index?storeId=%s", request.getStoreId()));
                } else {
                    extend.setUrl(String.format("pages/package-B/x-site/page-link/index?pageType=%s&pageCode=%s&storeId=%s", extend.getPageType(), extend.getPageCode(), request.getStoreId()));
                }
            } else {//商城
                //非首页
                if (!mainPageType.equalsIgnoreCase(extend.getPageType())) {
                    extend.setUrl(String.format("pages/package-B/x-site/page-link/index?pageType=%s&pageCode=%s", extend.getPageType(), extend.getPageCode()));
                }
            }
            pageInfoExtendRepository.save(extend);
        }
        return extend;
    }

    @Transactional
    public void modifyExtendById(PageInfoExtendModifyRequest request) {
        PageInfoExtend oldExtend = pageInfoExtendRepository.findById(request.getPageId()).orElse(null);
        if (oldExtend != null) {
            oldExtend.setBackgroundPic(request.getBackGroundPic());
            oldExtend.setUseType(request.getUseType());
            oldExtend.setSources(request.getSources());
            pageInfoExtendRepository.save(oldExtend);
        }
    }

    @Transactional
    public PageInfoExtend findRecruitingCustomersById(PageInfoExtendByPageIdRequest request) {
        PageInfoExtend extend = new PageInfoExtend();
        if (redisService.hasKey(recruitingCustomersFlag)) {
            extend = JSONObject.parseObject(redisService.getString(recruitingCustomersFlag), PageInfoExtend.class);
        } else {
            MiniProgramQrCodeRequest codeRequest = new MiniProgramQrCodeRequest();
            codeRequest.setPage("pages/user-center/index");
            codeRequest.setWidth(375);
            codeRequest.setScene(request.getPageId());
            byte[] miniQrCode = wechatApiUtil.getWxaCodeBytesUnlimit(codeRequest, "PUBLIC");

            String base64Head = "data:image/png;base64,";

            extend.setPageId(request.getPageId());
            extend.setPlatform(request.getPlatform());
            if (Objects.nonNull(miniQrCode)) {
                extend.setMiniProgramQrCode(base64Head.concat(new BASE64Encoder().encode(miniQrCode)));
            }
            extend.setUseType(0);
            //跳转到会员中心
            extend.setUrl("pages/user-center/index");
            pageInfoExtendRepository.save(extend);
            redisService.setString(recruitingCustomersFlag, JSONObject.toJSONString(extend), 15000000L);
        }
        return extend;
    }
}
