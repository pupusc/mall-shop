package com.wanmi.sbc.goods.notice.model.root;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/10/26 2:11 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
@Entity
@Table(name = "t_notice")
public class NoticeDTO {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 有效开始时间
     */
    @Column(name = "beginTime")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Version
    private Integer version;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "del_flag")
    private Integer delFlag;
}
