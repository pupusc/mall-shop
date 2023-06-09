package com.wanmi.sbc.bookmeta.provider;

import com.wanmi.sbc.bookmeta.bo.BookSeletorBo;
import com.wanmi.sbc.bookmeta.bo.SeletorBookInfoBo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "${application.goods.name}", contextId = "BookSeletorProvider")
public interface BookSeletorProvider {
    /**
     * 分页查询
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/bookSeletor/queryByPage")
    List<BookSeletorBo> queryById(@RequestBody BookSeletorBo bookSeletorBo);


    /**
     * 增加
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/bookSeletor/add")
    int add(@RequestBody BookSeletorBo bookSeletorBo);

    /**
     * 增加
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/bookSeletor/update")
    int update(@RequestBody BookSeletorBo bookSeletorBo);

    /**
     * 根据商品isbn获取推荐人及其推荐信息
     * @return 实例对象
     */
    @PostMapping("/goods/${application.goods.version}/bookSeletor/getRecomment")
    List<SeletorBookInfoBo> getRecomment(@RequestParam(value = "isbn") String isbn);
}
