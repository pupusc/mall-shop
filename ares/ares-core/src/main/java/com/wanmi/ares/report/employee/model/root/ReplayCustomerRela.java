package com.wanmi.ares.report.employee.model.root;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplayCustomerRela {

    List<ReplayStoreCustomerRela> replayStoreCustomerRelas;

    List<Map<String, Object>> replayBossCustomerRelas;
}
