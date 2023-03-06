package com.wanmi.sbc.goods.collect;

import com.wanmi.sbc.goods.jpa.JpaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheService {

    @Autowired
    JpaManager jpaManager;

    public static List isbnSPUList = null;

    //初始化 isbnSPUList
    public void initIsbnSPUList() {
        String spu_no = null;

        String sql = " select a.goods_no as spu,b.prop_value as isbn,a.goods_id as spu_id from goods a left join goods_prop_detail_rel b on a.goods_id = b.goods_id where b.prop_id = 5 ";
        Object[] obj = new Object[]{};
        isbnSPUList = jpaManager.queryForList(sql,obj);
    }

    //查询 isbnSPUList
    public List findIsbnSPUList(String isbn) {
        if(isbnSPUList != null){

        }
        return null;
    }

    public void clear(){
        isbnSPUList = null;
    }

}
