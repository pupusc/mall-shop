package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaPublisherQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaPublisher;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 出版社(MetaPublisher)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:03
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaPublisherProvider")
public interface MetaPublisherProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/queryById")
    BusinessResponse<MetaPublisher> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/queryByPage")
    BusinessResponse<List<MetaPublisher>> queryByPage(@RequestBody @Valid MetaPublisherQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaPublisher metaPublisher);

    /**
     * 修改数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaPublisher metaPublisher);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
