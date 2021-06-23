package com.wanmi.sbc.setting.popupadministration.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "application_page")
public class ApplicationPage {

    /**
     * 主键application_page_id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_page_id")
    private Long applicationPageId;

    /**
     * 落地页名称
     */
    @Column(name = "application_page_name")
    private String applicationPageName;

    /**
     * 弹窗id
     */
    @Column(name = "popup_id")
    private Long popupId;

    /**
     * 排序号
     */
    @Column(name = "sort_number")
    private Long sortNumber;
}
