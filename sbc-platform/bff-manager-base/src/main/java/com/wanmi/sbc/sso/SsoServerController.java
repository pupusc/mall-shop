package com.wanmi.sbc.sso;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.wanmi.sbc.common.base.Operator;

import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.util.CommonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: songhanlin
 * @Date: Created In 下午2:19 2018/6/12
 * @Description: TODO
 */
@Api(tags = "SsoServerController", description = "魔方建站单点登录服务 Api")
@RestController
public class SsoServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoServerController.class);

    @Autowired
    private  CommonUtil commonUtil;

    @Autowired
    private BaseConfigQueryProvider baseConfigQueryProvider;

   /* @Autowired
    private DomainStoreRelaQueryProvider domainStoreRelaQueryProvider;*/

    /**
     * x-site项目访问url接口时,都要先携带token访问此接口验证是否返回正确的结果,返回结果会存在x-site项目中
     *
     * @return
     */
    @ApiOperation(value = "x-site项目访问url接口时,都要先携带token访问此接口验证是否返回正确的结果,返回结果会存在x-site项目中",
            notes = "userId: 用户Id, shopId: 店铺Id, shopName: 店铺名称, " +
                    "userNickName: 用户昵称, userQMId: 用户Id, icoUrl: logo地址")
    @RequestMapping(value = "/sso/server", method = RequestMethod.GET)
    public Map<String, Object> query() {
//        CompositeResponse<BaseConfigRopResponse> response = sdkClient.buildClientRequest()
//                .get(BaseConfigRopResponse.class, "baseConfig.query", "1.0.0");
//        BaseConfigRopResponse res = response.getSuccessResponse();
        BaseConfigRopResponse res = baseConfigQueryProvider.getBaseConfig().getContext();
        Operator ope = commonUtil.getOperator();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SSOConstant.PLAT_SHOP_ID);
        map.put("shopId", SSOConstant.PLAT_SHOP_ID);
        map.put("shopName", SSOConstant.PLAT_SHOP_NAME);
        map.put("userNickName", ope.getName());
        map.put("userQMId", ope.getUserId());
        List<JSONObject> icoList = JSON.parseArray(res.getPcIco(), JSONObject.class);
        if (icoList != null && icoList.size() > 0) {
            map.put("icoUrl", icoList.get(0).get("url"));
        }
        return map;
    }

    /**
     * 用于x-site建站项目获取当前建站的店铺信息
     * userId其实是shopId(丑陋的原因是因为x-site这么写了,我们原则是尽量不动x-site代码,s2b业务端适配x-site)
     *
     * @return
     */
    @ApiOperation(value = "用于x-site建站项目获取当前建站的店铺信息",
            notes = "data: (scene: 商城名称, systemCode: 系统编号, storeLogo: 店铺logo, storeName: 店铺名称, " +
                    "userId: 用户Id), message: 操作提示, result: 提示, status: 状态")
    @RequestMapping(value = "/sso/user", method = RequestMethod.GET)
    public Map<String, Object> getUser() {
//        CompositeResponse<BaseConfigRopResponse> response = sdkClient.buildClientRequest()
//                .get(BaseConfigRopResponse.class, "baseConfig.query", "1.0.0");
//        BaseConfigRopResponse res = response.getSuccessResponse();
        BaseConfigRopResponse res = baseConfigQueryProvider.getBaseConfig().getContext();
        Map<String, Object> wrapper = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("scene", SSOConstant.SCENE);
        map.put("systemCode", SSOConstant.SYSTEM_CODE);
        List<JSONObject> logoList = JSON.parseArray(res.getPcLogo(), JSONObject.class);
        if (logoList != null && logoList.size() > 0) {
            map.put("storeLogo", logoList.get(0).get("url"));
        }
        map.put("storeName", SSOConstant.PLAT_SHOP_NAME);
        map.put("userId", SSOConstant.PLAT_SHOP_ID);

        wrapper.put("data", map);
        wrapper.put("message", "操作成功");
        wrapper.put("result", "ok");
        wrapper.put("status", 1);
        return wrapper;
    }

    /**
     * x-site页面发布pc端模板时调用此接口,获取发布之后的页面url相关信息,用于立即查看
     *
     * @return
     */
    @ApiOperation(value = "x-site页面发布pc端模板时调用此接口,获取发布之后的页面url相关信息,用于立即查看",
            notes = "data: (adminId: 管理员Id, domainName: 网站名称, domainUrl: 网站地址, faviconUrl: logo, " +
                    "logoUrl: 登录地址, shopName: 店铺名称), message: 操作提示, result: 提示, status: 状态")
    @RequestMapping(value = "/open_site_api/site_info", method = RequestMethod.POST)
    public Map<String, Object> getPcSiteInfo(String platForm) {
        BaseConfigRopResponse res = baseConfigQueryProvider.getBaseConfig().getContext();

        String pcWebsite = res.getPcWebsite();

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", SSOConstant.PLAT_SHOP_ID);
        data.put("domainName", pcWebsite);
        data.put("domainUrl", pcWebsite);
        List<JSONObject> icoList = JSON.parseArray(res.getPcIco(), JSONObject.class);
        if (icoList != null && icoList.size() > 0) {
            data.put("faviconUrl", icoList.get(0).get("url"));
        }
        List<JSONObject> logoList = JSON.parseArray(res.getPcLogo(), JSONObject.class);
        if (logoList != null && logoList.size() > 0) {
            data.put("logoUrl", logoList.get(0).get("url"));
        }
        data.put("shopName", SSOConstant.PLAT_SHOP_NAME);

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("data", data);
        wrapper.put("msg", "操作成功");
        wrapper.put("rescode", 200);
        wrapper.put("result", "ok");
        return wrapper;
    }

    /**
     * x-site页面发布weixin端页面时调用此接口,获取发布之后的页面url相关信息,用于分享
     *
     * @return
     */
    @ApiOperation(value = "x-site页面发布weixin端页面时调用此接口,获取发布之后的页面url相关信息,用于分享",
            notes = "data: (adminId: 管理员Id, domainUrl: 网站地址, faviconUrl: logo, " +
                    "shopName: 店铺名称), message: 操作提示, result: 提示, status: 状态")
    @RequestMapping(value = "/open_site_api/site_wx_info", method = RequestMethod.POST)
    public Map<String, Object> getWxSiteInfo(String platForm) {
        BaseConfigRopResponse res = baseConfigQueryProvider.getBaseConfig().getContext();


        String  mobileWebsite = res.getMobileWebsite();

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", SSOConstant.PLAT_SHOP_ID);
        data.put("domainUrl", mobileWebsite);
        List<JSONObject> icoList = JSON.parseArray(res.getPcIco(), JSONObject.class);
        if (icoList != null && icoList.size() > 0) {
            data.put("faviconUrl", icoList.get(0).get("url"));
        }
        data.put("shopName", SSOConstant.PLAT_SHOP_NAME);

        Map<String, Object> wrapper = new HashMap<>();
        wrapper.put("data", data);
        wrapper.put("msg", "操作成功");
        wrapper.put("rescode", 200);
        wrapper.put("result", "ok");
        return wrapper;
    }

}
