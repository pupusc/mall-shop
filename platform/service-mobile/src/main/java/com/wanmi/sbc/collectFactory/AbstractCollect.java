package com.wanmi.sbc.collectFactory;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
public abstract class AbstractCollect {
    /**
     * 采集商品id
     * @return
     */
    public abstract <F> Set<F> collectId(Integer topicStoreyId,Integer storeyType);
}
