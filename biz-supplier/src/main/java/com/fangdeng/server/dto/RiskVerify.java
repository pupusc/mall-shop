package com.fangdeng.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskVerify {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.id
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.goods_no
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private String goodsNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.image_url
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private String imageUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.verify_type
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Integer verifyType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.status
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.deleted
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Byte deleted;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.create_time
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.update_time
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.request_id
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private String requestId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column risk_verify.error_msg
     *
     * @mbggenerated Wed Sep 29 14:54:57 CST 2021
     */
    private String errorMsg;


}