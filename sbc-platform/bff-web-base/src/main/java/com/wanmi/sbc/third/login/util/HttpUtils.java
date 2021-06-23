package com.wanmi.sbc.third.login.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-08 10:59
 **/
@UtilityClass
public class HttpUtils {

    private static RestTemplate restTemplate = new RestTemplate();

    static {
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (int i = 0, size = converters.size(); i < size; i++) {
            if (converters.get(i) instanceof StringHttpMessageConverter) {
                converters.remove(i);
                converters.add(i, new StringHttpMessageConverter(Charset.forName("UTF-8")));
                break;
            }
        }

        // 连接池
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setMaxConnTotal(512)
                .setMaxConnPerRoute(512)
                .evictIdleConnections(1200, TimeUnit.SECONDS)
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                .build();

        // 连接超时
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectionRequestTimeout(3000);
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(15000);

        restTemplate.setRequestFactory(factory);
    }

    public String get(String url) {
        return get(url, String.class);
    }

    public <T> T get(final String url, Class<T> clazz) {
        return restTemplate.getForObject(encodeUrl(url), clazz);
    }

    /**
     * get请求
     *
     * @param url          请求地址
     * @param responseType 响应类型
     * @param uriVariables url参数。(k,v) -> k=v
     */
    public <T> T get(final String url, final Class<T> responseType, final Map<String, Object> uriVariables) {
        String urlUse = encodeUrl(url).toString();
        if (MapUtils.isNotEmpty(uriVariables)) {
            final String parameters = uriVariables
                    .keySet()
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .map(StringUtils::trim)
                    .map(k -> k + "={" + k + "}")
                    .collect(Collectors.joining("&"));
            if (StringUtils.isNotBlank(parameters)) {
                urlUse += (StringUtils.contains(urlUse, "?") ? parameters : "?" + parameters);
            }
        }
        return restTemplate.getForObject(urlUse, responseType, uriVariables);
    }

    @SuppressWarnings("UnusedDeclaration")
    public <T> T post(final String url, final Object body, final Class<T> clazz) {
        return restTemplate.postForObject(encodeUrl(url), body, clazz);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static Object postForByte(String url, Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","application/json");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity responseEntity = restTemplate.postForEntity(url, request, byte[].class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            //logger.error("请求出错, 错误码: {}, 错误信息: {}", responseEntity.getStatusCode(), responseEntity.getBody());
            throw new IllegalArgumentException("请求出错");
        }
    }

    private static URI encodeUrl(String url) {
        return UriComponentsBuilder.fromHttpUrl(url).build().encode().toUri();
    }
}