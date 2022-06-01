package com.soybean.elastic.spu.service;

import com.soybean.elastic.spu.model.EsSpuNew;
import org.apache.poi.ss.formula.functions.T;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:51 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
public abstract class AbstractCollect {


    public abstract <T> List<T> collect(LocalDateTime lastCollectTime);
}
