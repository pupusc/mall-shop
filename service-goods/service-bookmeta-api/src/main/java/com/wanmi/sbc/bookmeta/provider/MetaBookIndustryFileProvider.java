package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryFileQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookIndustryFileBO;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import javax.validation.Valid;
import java.util.List;

/**
 * 行业数据文件(MetaBookIndustryFile)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookIndustryFileProvider")
public interface MetaBookIndustryFileProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryFile/queryById")
    BusinessResponse<MetaBookIndustryFileBO> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryFile/queryByPage")
    BusinessResponse<List<MetaBookIndustryFileBO>> queryByPage(@RequestBody @Valid MetaBookIndustryFileQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryFile/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookIndustryFileBO metaBookIndustryFile);

    /**
     * 修改数据
     *
     * @param metaBookIndustryFile 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryFile/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookIndustryFileBO metaBookIndustryFile);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookIndustryFile/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

}
