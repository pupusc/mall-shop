package com.soybean.elastic.spu.service;

import com.soybean.elastic.spu.model.EsSpuNew;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/1 2:59 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Service
public class SpuCollect extends AbstractSpuCollect{

    public void test() {
        List<EsSpuNew> spus = new ArrayList<>();

    }

    @Override
    public <T> List<T> collect(LocalDateTime lastCollectTime) {

        return null;
    }

    @Autowired
    private SpuCollect spuCollect;

}
