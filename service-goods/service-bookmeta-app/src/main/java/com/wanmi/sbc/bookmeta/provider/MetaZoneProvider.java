package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaZoneAddReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneByIdResBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEditReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneEnableReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaZoneQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaZone;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 图书分区(MetaZone)表服务接口
 *
 * @author Liang Jun
 * @since 2022-07-27 10:45:10
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaZoneProvider")
public interface MetaZoneProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/queryById")
    BusinessResponse<MetaZoneByIdResBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/queryByPage")
    BusinessResponse<List<MetaZone>> queryByPage(@RequestBody @Valid MetaZoneQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaZone 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaZoneAddReqBO metaZone);

    /**
     * 修改数据
     *
     * @param metaZone 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaZoneEditReqBO metaZone);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

    /**
     * 修改数据
     *
     * @param metaZone 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaZone/enable")
    BusinessResponse<Boolean> enable(@RequestBody @Valid MetaZoneEnableReqBO metaZone);
}
