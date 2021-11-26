package com.wanmi.sbc.crm.autotagstatistics;

import com.wanmi.sbc.crm.bean.enums.DimensionName;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sbc-micro-service-A
 * @description:
 * @create: 2020-08-28 11:25
 **/
@Component
public class SqlActionChoose implements ApplicationContextAware {
    private ApplicationContext context;

    private Map<DimensionName, SqlTool> chooseMap = new HashMap<>();

    public SqlTool choose(DimensionName type) {
        return chooseMap.get(type);
    }

    @PostConstruct
    public void register() {
        Map<String, SqlTool> solverMap = context.getBeansOfType(SqlTool.class);
        for (SqlTool solver : solverMap.values()) {
            for (DimensionName support : solver.supports()) {
                chooseMap.put(support, solver);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}