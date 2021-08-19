-- marketing tables init
DROP TABLE marketing;
DROP TABLE marketing_full_discount_level;
DROP TABLE marketing_full_reduction_level;
DROP TABLE marketing_full_gift_level;
DROP TABLE marketing_full_gift_detail;
DROP TABLE marketing_scope;


-- 营销表
CREATE TABLE marketing
(
  marketing_id     BIGINT AUTO_INCREMENT
  COMMENT '促销Id'
    PRIMARY KEY,
  marketing_name   VARCHAR(40) NOT NULL
  COMMENT '促销名称',
  marketing_type   TINYINT     NOT NULL
  COMMENT '促销类型 0：满减 1:满折 2:满赠',
  sub_type         TINYINT     NOT NULL
  COMMENT '促销子类型 0：满金额减 1：满数量减 2:满金额折 3:满数量折 4:满金额赠 5:满数量赠',
  begin_time       DATETIME    NULL
  COMMENT '开始时间',
  end_time         DATETIME    NULL
  COMMENT '结束时间',
  scope_type       TINYINT     NOT NULL
  COMMENT '参加促销类型 0：全部货品 1：货品 2：品牌 3：分类',
  join_level       VARCHAR(500) NOT NULL
  COMMENT '参加会员  -1:全部客户 0:全部等级 other:其他等级',
  is_boss         TINYINT     NOT NULL
  COMMENT '是否是平台，1：boss，0：商家',
  store_id         BIGINT      NULL
  COMMENT '商铺Id  0：boss,  other:其他商家',
  del_flag         TINYINT     NOT NULL
  COMMENT '删除标记  0：正常，1：删除',
  create_time      DATETIME    NULL
  COMMENT '创建时间',
  create_person    VARCHAR(32) NULL
  COMMENT '创建人',
  update_time      DATETIME    NULL
  COMMENT '修改时间',
  update_person    VARCHAR(32) NULL
  COMMENT '修改人',
  delete_time      DATETIME    NULL
  COMMENT '删除时间',
  delete_person    VARCHAR(32) NULL
  COMMENT '删除人',
  is_pause         TINYINT     NOT NULL
  COMMENT '是否暂停（1：暂停，0：正常）'
)
  COMMENT '营销表';


-- 满折关联表
CREATE TABLE marketing_full_discount_level
(
  discount_level_id BIGINT AUTO_INCREMENT
  COMMENT '满折级别Id'
    PRIMARY KEY,
  marketing_id       BIGINT         NOT NULL
  COMMENT '营销Id',
  full_amount       DECIMAL(12, 2) NULL
  COMMENT '满金额',
  full_count        BIGINT(5)      NULL
  COMMENT '满数量',
  discount          DECIMAL(10, 2)  NOT NULL
  COMMENT '满金额|数量后折扣'
)
  COMMENT '满折关联表';


-- 满减关联表
CREATE TABLE marketing_full_reduction_level
(
  reduction_level_id BIGINT AUTO_INCREMENT
  COMMENT '满减级别Id'
    PRIMARY KEY,
  marketing_id       BIGINT         NOT NULL
  COMMENT '营销Id',
  full_amount        DECIMAL(12, 2) NULL
  COMMENT '满金额',
  full_count         BIGINT(5)      NULL
  COMMENT '满数量',
  reduction          DECIMAL(12, 2)  NOT NULL
  COMMENT '满金额|数量后减多少元'
)
  COMMENT '满减关联表';


-- 满赠关联表
CREATE TABLE marketing_full_gift_level
(
  gift_level_id BIGINT AUTO_INCREMENT
  COMMENT '满赠多级促销Id'
    PRIMARY KEY,
  marketing_id  BIGINT         NOT NULL
  COMMENT '营销Id',
  full_amount   DECIMAL(12, 2) NULL
  COMMENT '满金额赠',
  full_count    BIGINT(5)      NULL
  COMMENT '满数量赠',
  gift_type     TINYINT        NOT NULL
  COMMENT '赠品赠送的方式 0:全赠  1：赠一个'
)
  COMMENT '满赠关联表';

-- 满赠赠品细节表
CREATE TABLE marketing_full_gift_detail
(
  gift_detail_id BIGINT AUTO_INCREMENT
  COMMENT '满赠赠品Id'
    PRIMARY KEY,
  gift_level_id  BIGINT    NOT NULL
  COMMENT '满赠多级促销Id',
  product_id     VARCHAR(32)    NOT NULL
  COMMENT '赠品Id',
  product_num    BIGINT(5) NOT NULL
  COMMENT '赠品数量',
  marketing_id   BIGINT    NOT NULL
  COMMENT '满赠ID'
)
  COMMENT '满赠赠品细节表';

-- 营销和商品关联表
CREATE TABLE marketing_scope
(
  marketing_scope_id BIGINT AUTO_INCREMENT
  COMMENT '货品与促销规则表Id'
    PRIMARY KEY,
  marketing_id       BIGINT  NOT NULL
  COMMENT '促销Id',
  scope_id           VARCHAR(32)  NOT NULL
  COMMENT '促销范围Id'
)
  COMMENT '营销和商品关联表';
