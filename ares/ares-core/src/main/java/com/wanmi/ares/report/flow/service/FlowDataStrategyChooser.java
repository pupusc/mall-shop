package com.wanmi.ares.report.flow.service;

import com.wanmi.ares.enums.StatisticsDataType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FlowDataInspectionSolverChooser
 * @Description 保存流量统计数据采用策略模式--将业务处理器和其支持处理的类型放到一个容器中
 * @Author lvzhenwei
 * @Date 2019/8/26 9:52
 **/
@Component
public class FlowDataStrategyChooser implements ApplicationContextAware {

    private ApplicationContext context;

    private Map<StatisticsDataType, FlowDataStrategy> chooseMap = new HashMap<>();

    public FlowDataStrategy choose(StatisticsDataType type) {
        return chooseMap.get(type);
    }

    @PostConstruct
    public void register() {
        Map<String, FlowDataStrategy> solverMap = context.getBeansOfType(FlowDataStrategy.class);
        for (FlowDataStrategy solver : solverMap.values()) {
            for (StatisticsDataType support : solver.supports()) {
                chooseMap.put(support, solver);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
