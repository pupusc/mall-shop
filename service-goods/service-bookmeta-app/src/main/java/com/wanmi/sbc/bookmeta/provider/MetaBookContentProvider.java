package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.MetaBookContentByBookIdReqBO;
import com.wanmi.sbc.bookmeta.bo.MetaBookContentQueryByPageReqBO;
import com.wanmi.sbc.bookmeta.entity.MetaBookContent;
import com.wanmi.sbc.common.base.BusinessResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 书籍内容描述(MetaBookContent)表服务接口
 *
 * @author Liang Jun
 * @since 2022-05-23 22:54:32
 */
@Validated
@FeignClient(value = "${application.goods.name}", contextId = "MetaBookContentProvider")
public interface MetaBookContentProvider {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/queryById")
    BusinessResponse<MetaBookContent> queryById(@RequestBody @NotNull Integer id);

    /**
     * 分页查询
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/queryByPage")
    BusinessResponse<List<MetaBookContent>> queryByPage(@RequestBody @Valid MetaBookContentQueryByPageReqBO pageRequest);

    /**
     * 新增数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/insert") 
    BusinessResponse<Integer> insert(@RequestBody @Valid MetaBookContent metaBookContent);

    /**
     * 修改数据
     *
     * @param metaBookContent 实例对象
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/update")
    BusinessResponse<Boolean> update(@RequestBody @Valid MetaBookContent metaBookContent);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/deleteById")
    BusinessResponse<Boolean> deleteById(@RequestBody @NotNull Integer id);

    /**
     * 书籍内容查询（出版内容）
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/queryByBookId")
    BusinessResponse<List<MetaBookContent>> queryByBookId(@RequestBody @NotNull Integer id);

    /**
     * 书籍内容编辑（出版内容）
     */
    @PostMapping("/goods/${application.goods.version}/metaBookContent/editByBookId")
    BusinessResponse<Boolean> editByBookId(@RequestParam Integer bookId, @RequestBody List<MetaBookContentByBookIdReqBO> editReqBO);

}
