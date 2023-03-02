package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.entity.Authority;
import tk.mybatis.mapper.common.Mapper;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:12
 * @Description:
 */
public interface AuthorityMapper extends Mapper<Authority> {
    int insertAuthority(Authority authority);
    int updateAuthority(Authority authority);

}
