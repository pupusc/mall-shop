package com.wanmi.sbc.page;

import com.wanmi.sbc.setting.api.provider.page.MagicPageQueryProvider;
import com.wanmi.sbc.setting.api.request.page.MagicPageMainQueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 魔方首页面缓存Controller
 */
@RestController
@RequestMapping("/magic-page")
@Api(tags = "MagicPageController", description = "mobile 查询缓存的html页面信息bff")
@Slf4j
public class MagicPageController {

    Logger logger = LoggerFactory.getLogger(MagicPageController.class);

    @Autowired
    private MagicPageQueryProvider magicPageQueryProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${magic.page.main.config}")
    private String magicPageMainConfig;

    /**
     * 获取魔方首页缓存，缓存的是html静态页信息
     * 两级缓存，实际配置在魔方应用中。业务数据库缓存一份，本接口再缓存1分钟
     * 本接口在小程序的iframe中无法直接访问，需在ngxin做转发
     *
     * @return 返回html内容
     */
    @ApiOperation(value = "获取魔方首页缓存html")
    @Cacheable(value = "MAGIC_PAGE_MAIN_HTML")
    @RequestMapping(value = "/main.html", method = RequestMethod.GET)
    public String getMainHtml() {
        logger.info("获取并缓存魔方首页DOM信息");
        return magicPageQueryProvider.getMain(MagicPageMainQueryRequest.builder().build()).getContext().getHtmlString();
    }

    /**
     * 获取魔方首页配置信息的缓存，缓存的是 https://app-render.xxxx/magic/d2cStore/000000/weixin/index 的返回值
     * 两级缓存，实际配置在魔方应用中。业务数据库缓存一份，本接口再缓存1分钟
     *
     * @return 返回魔方首页配置内容
     */
    @ApiOperation(value = "获取魔方首页配置信息")
    @RequestMapping(value = "/main-config", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @Cacheable(value = "MAGIC_PAGE_MAIN_CONFIG")
    public String getMainConfig() {
        String mainConfig = restTemplate.getForObject(magicPageMainConfig, String.class);
        logger.info("获取并缓存魔方首页配置信息");
        return mainConfig;
    }

}
