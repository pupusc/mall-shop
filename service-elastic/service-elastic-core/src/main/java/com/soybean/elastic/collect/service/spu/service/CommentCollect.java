package com.soybean.elastic.collect.service.spu.service;

import com.soybean.elastic.collect.service.spu.AbstractSpuCollect;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Description: 采集评论信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/13 3:31 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public class CommentCollect extends AbstractSpuCollect {
    @Override
    public <F> Set<F> collectId(LocalDateTime lastCollectTime, LocalDateTime now) {
        return null;
    }

    @Override
    public <F> List<F> collect(List<F> list) {
        return null;
    }
}
