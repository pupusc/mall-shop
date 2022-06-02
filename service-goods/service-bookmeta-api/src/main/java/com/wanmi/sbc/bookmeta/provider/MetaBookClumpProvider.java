package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookClumpBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookClumpQueryByPageReqBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 丛书(MetaBookClump)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookClumpProvider")
public interface MetaBookClumpProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookClump/queryById")
    BusinessResponse<MetaBookClumpBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookClump/queryByPage")
    BusinessResponse<List<MetaBookClumpBO>> queryByPage(@RequestBody @Valid MetaBookClumpQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookClump 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookClump/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookClumpBO metaBookClump);

    /**
     * 修改数据
     *
     * @param metaBookClump 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookClump/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookClumpBO metaBookClump);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookClump/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
