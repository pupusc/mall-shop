package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaProducerQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaProducerBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 出品方(MetaProducer)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaProducerProvider")
public interface MetaProducerProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaProducer/queryById")
    BusinessResponse<MetaProducerBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaProducer/queryByPage")
    BusinessResponse<List<MetaProducerBO>> queryByPage(@RequestBody @Valid MetaProducerQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaProducer/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaProducerBO metaProducer);

    /**
     * 修改数据
     *
     * @param metaProducer 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaProducer/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaProducerBO metaProducer);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaProducer/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
