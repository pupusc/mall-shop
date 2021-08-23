package com.wanmi.ares.request.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 终端流量统计
 * Created by sunkun on 2017/9/25.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TerminalStatistics {

    @Builder.Default
    private Long H5 = 0l;

    @Builder.Default
    private Long PC = 0l;

    @Builder.Default
    private Long APP = 0l;

    @Builder.Default
    private Set H5UserIds = new HashSet();

    @Builder.Default
    private Set PcUserIds = new HashSet();

    @Builder.Default
    private Set AppUserIds = new HashSet();

    @Builder.Default
    private Set totalUserIds = new HashSet();

    private Long total;

    public Long getAll() {
        if (total == null) {
            return this.H5 + this.APP + this.PC;
        } else {
            return total;
        }
    }
}
