package com.wanmi.sbc.crm.customerplan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * customer_plan_trigger_send
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPlanTriggerSend extends CustomerPlanSend implements Serializable {
    private Integer customerLimit;

    private List<Integer> triggerList;
}