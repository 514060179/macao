
CREATE TABLE `spcar_comment` (
`spcar_comment_id`  int(11) NOT NULL AUTO_INCREMENT,
`spcar_passenger_id`  int(11) NULL COMMENT '乘客Id' ,
`comments`  text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '评价内容' ,
`score`  double NULL COMMENT '评分' ,
`order_id`  int(11) NULL COMMENT '订单Id' ,
`order_status`  int(11) NULL COMMENT '订单状态' ,
PRIMARY KEY (`spcar_comment_id`)
)
;

ALTER TABLE `spcar_comment`
ADD COLUMN `create_time`  datetime NULL COMMENT '创建时间' AFTER `order_status`;


ALTER TABLE `spcar_comment`
ADD COLUMN `spcar_passenger_name`  varchar(64) NULL COMMENT '乘客姓名' AFTER `create_time`,
ADD COLUMN `spcar_passenger_tel`  varchar(255) NULL COMMENT '乘客手机号' AFTER `spcar_passenger_name`;
