package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaDataDictQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaDataDictBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 数据字典(MetaDataDict)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-24 00:37:02
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaDataDictProvider")
public interface MetaDataDictProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaDataDict/queryById")
    BusinessResponse<MetaDataDictBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaDataDict/queryByPage")
    BusinessResponse<List<MetaDataDictBO>> queryByPage(@RequestBody @Valid MetaDataDictQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaDataDict 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaDataDict/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaDataDictBO metaDataDict);

    /**
     * 修改数据
     *
     * @param metaDataDict 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaDataDict/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaDataDictBO metaDataDict);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaDataDict/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
