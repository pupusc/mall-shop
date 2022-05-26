package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaPublisherQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaPublisherBO;
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
 * @since 2022-05-23 22:54:32
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
    BusinessResponse<MetaPublisherBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/queryByPage")
    BusinessResponse<List<MetaPublisherBO>> queryByPage(@RequestBody @Valid MetaPublisherQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaPublisherBO metaPublisher);

    /**
     * 修改数据
     *
     * @param metaPublisher 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaPublisherBO metaPublisher);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaPublisher/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
