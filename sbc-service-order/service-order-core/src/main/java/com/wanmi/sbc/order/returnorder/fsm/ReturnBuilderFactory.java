package com.wanmi.sbc.order.returnorder.fsm;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.order.returnorder.fsm.event.ReturnEvent;
import com.wanmi.sbc.order.bean.enums.ReturnFlowState;
import com.wanmi.sbc.order.returnorder.model.root.ReturnOrder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Just for build each state builder
 * Created by mac on 2017/4/7.
 */
@Component
public class ReturnBuilderFactory implements InitializingBean {

    /**
     *
     */
    private Map<ReturnFlowState, Builder> stateBuilderMap = new ConcurrentHashMap<>();

    /**
     *
     */
    @Autowired
    private List<Builder> stateBuilders;

    /**
     *
     */
    @Autowired
    private BeanFactory beanFactory;

    /**
     * @param returnOrder
     * @return
     */
    public StateMachine<ReturnFlowState, ReturnEvent> create(ReturnOrder returnOrder) {
        ReturnFlowState flowState = returnOrder.getReturnFlowState();
        Builder builder = stateBuilderMap.get(flowState);
        if (builder == null) {
            throw new SbcRuntimeException("K-050002",new Object[]{String.format("[退货]创建状态[%s]失败 => [未知状态]", flowState)});
        }

        //
        StateMachine<ReturnFlowState, ReturnEvent> sm;
        try {
            sm = builder.build(returnOrder, beanFactory);
            sm.start();
        } catch (Exception e) {
            throw new SbcRuntimeException(String.format("[退货]创建状态[%s]失败 => [%s]", flowState, e.getCause()));
        }

        //
        sm.getExtendedState().getVariables().put(ReturnOrder.class, returnOrder);
        return sm;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        stateBuilderMap = stateBuilders.stream().collect(Collectors.toMap(Builder::supportState, Function.identity()));
    }
}
