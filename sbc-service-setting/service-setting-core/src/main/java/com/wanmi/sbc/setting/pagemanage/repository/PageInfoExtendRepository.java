package com.wanmi.sbc.setting.pagemanage.repository;


import com.wanmi.sbc.setting.pagemanage.model.root.PageInfoExtend;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 页面信息扩展记录
 * Created by dyt on 2020/4/17.
 */
public interface PageInfoExtendRepository extends MongoRepository<PageInfoExtend, String> {

}

