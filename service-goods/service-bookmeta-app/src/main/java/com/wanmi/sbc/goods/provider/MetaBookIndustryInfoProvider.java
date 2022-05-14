package com.wanmi.sbc.goods.provider;

import com.wanmi.sbc.goods.bo.MetaBookIndustryInfoQueryByPageReqBO;
import com.wanmi.sbc.goods.entity.MetaBookIndustryInfo;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 行业数据(MetaBookIndustryInfo)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookIndustryInfoProvider")
public interface MetaBookIndustryInfoProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryInfo/queryById")
    BusinessResponse<MetaBookIndustryInfo> queryById(@RequestParam @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryInfo/queryByPage")
    BusinessResponse<List<MetaBookIndustryInfo>> queryByPage(@RequestBody @Valid MetaBookIndustryInfoQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryInfo/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookIndustryInfo metaBookIndustryInfo);

    /**
     * 修改数据
     *
     * @param metaBookIndustryInfo 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryInfo/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookIndustryInfo metaBookIndustryInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryInfo/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
