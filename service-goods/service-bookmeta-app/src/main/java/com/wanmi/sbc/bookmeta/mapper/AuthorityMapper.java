package com.wanmi.sbc.bookmeta.mapper;

import com.wanmi.sbc.bookmeta.bo.AuthorityBO;
import com.wanmi.sbc.bookmeta.entity.Authority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: pushicheng
 * @Date: 2023/03/02/14:12
 * @Description:
 */
@Repository
public interface AuthorityMapper extends Mapper<Authority> {
    int insertAuthority(Authority authority);
    int updateAuthority(Authority authority);

    int deleteAuthority(String authorityId);
    List<AuthorityBO> getAuthorityByUrl(@Param("authorityUrl") String authorityUrl, @Param("limitIndex") Integer limitIndex, @Param("limitSize") Integer limitSize);

    Integer getAuthorityByUrlCount(@Param("authorityUrl") String authorityUrl);

}
