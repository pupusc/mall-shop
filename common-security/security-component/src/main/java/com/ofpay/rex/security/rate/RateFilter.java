package com.ofpay.rex.security.rate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ofpay.rex.util.IPUtil;

/**
 * A simple servlet filter that limits the request rate to a certain threshold of requests per second.
 * The default rate is 5 hits in 10 seconds. This can be overridden in the web.xml file by adding
 * parameters named "hits" and "period" with the desired values. When the rate is exceeded, a short
 * string is written to the response output stream and the chain method is not invoked. Otherwise,
 * processing proceeds as normal.
 */

/**
 * Created with IntelliJ IDEA.
 * User: freeman983
 * Date: 14-6-18
 * Time: 下午1:21
 * To change this template use File | Settings | File Templates.
 */

public class RateFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RateFilter.class);

    //请求数
    private static final String HITS = "hits";

    //请求周期
    private static final String PERIOD = "period";

    //请求路径
    private static final String URI = "uri";

    //请求存储key
    private static final String TIMES = "times";

    private long hits = 50;

    private long period = 100;

    private int errCode = 403;

    private String errMsg = "Request rate too high";

    private Map<Integer, Long> hitsMap = new HashMap<Integer, Long>();

    private Map<Integer, Long> periodMap = new HashMap<Integer, Long>();

    private List<Pattern> uriList = new ArrayList<Pattern>();

    public static String getIp(HttpServletRequest request) {
        return IPUtil.getIp(request);
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * placed into service. The servlet container calls the init method exactly
     * once after instantiating the filter. The init method must complete
     * successfully before the filter is asked to do any filtering work.
     *
     * @param filterConfig configuration object
     */
    public void init(FilterConfig filterConfig) {

        if (filterConfig.getInitParameter(URI) != null) {
            String uriStr = filterConfig.getInitParameter(URI);
            String[] uriArr = uriStr.split(",");
            for (int i = 0; i < uriArr.length; i++) {
                if (!"".equals(uriArr[i].trim())) {
                    uriList.add(Pattern.compile(uriArr[i].trim()));
                }
            }
        }

        if (uriList.size() > 0) {
            if (filterConfig.getInitParameter(HITS) != null) {
                String hitsStr = filterConfig.getInitParameter(HITS);
                String[] hitsArr = hitsStr.split(",");
                for (int i = 0; i < uriList.size(); i++) {
                    if (hitsArr.length > i) {
                        hitsMap.put(Integer.valueOf(i), Long.parseLong(hitsArr[i].trim()));
                    } else {
                        hitsMap.put(Integer.valueOf(i), Long.parseLong(hitsArr[hitsArr.length - 1].trim()));
                    }
                }
            }

            if (filterConfig.getInitParameter(PERIOD) != null) {
                String periodStr = filterConfig.getInitParameter(PERIOD);
                String[] periodArr = periodStr.split(",");
                for (int i = 0; i < uriList.size(); i++) {
                    if (periodArr.length > i) {
                        periodMap.put(Integer.valueOf(i), Long.parseLong(periodArr[i].trim()));
                    } else {
                        periodMap.put(Integer.valueOf(i), Long.parseLong(periodArr[periodArr.length - 1].trim()));
                    }
                }
            }

            if (filterConfig.getInitParameter("errCode") != null) {
                errCode = Integer.valueOf(filterConfig.getInitParameter("errCode")).intValue();
            }

            if (filterConfig.getInitParameter("errMsg") != null) {
                errMsg = filterConfig.getInitParameter("errMsg");
            }
        }
    }

    /**
     * Checks to see if the current session has exceeded the allowed number
     * of requests in the specified time period. If the threshold has been
     * exceeded, then a short error message is written to the output stream and
     * no further processing is done on the request. Otherwise the request is
     * processed as normal.
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(true);

        String requestURI = httpRequest.getRequestURI();

        boolean hasMatch = false;
        Integer index = 0;
        for (int i = 0; i < uriList.size(); i++) {
            Pattern pattern = uriList.get(i);
            Matcher matcher = pattern.matcher(requestURI);
            if (matcher.find()) {
                logger.info("pattern match:" + pattern.toString());
                hasMatch = true;
                index = Integer.valueOf(i);
                break;
            }
        }

        if (hasMatch) {
            if (SessionRateControl.checkRate(session, index.toString(), hitsMap.get(index).intValue(), periodMap.get(index).intValue())) {
                logger.error("ip:" + getIp(httpRequest) + " 请求地址:" + requestURI + "  " + periodMap.get(index) + "秒内，超过" + hitsMap.get(index) + "次请求被拦截!");
                //返回403拒绝服务错误
                ((HttpServletResponse) response).sendError(errCode, errMsg);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Called by the web container to indicate to a filter that it is being
     * taken out of service. This method is only called once all threads within
     * the filter's doFilter method have exited or after a timeout period has
     * passed. After the web container calls this method, it will not call the
     * doFilter method again on this instance of the filter.
     */
    public void destroy() {
        // finalize
    }

}
