package com.wanmi.sbc.configure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Description: 类的描述
 *
 * @menu: 模块名称
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : tangqinjing<br>
 * Date       : 2021/10/12 15:44<br>
 * Modify     : 修改日期           修改人员        修改说明          JIRA编号<br>
 * v1.0.0       2021/10/12          tangqinjing        新增                   1001<br>
 ********************************************************************/

@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    // 获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    // 通过name获取 Bean.
    public static String getBean(String name) {
         Environment env = getApplicationContext().getEnvironment();
         return env.getProperty(name);
    }

}
