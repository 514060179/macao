

CREATE TABLE `third_party` (
`id`  INT(11) NOT NULL ,
`openId`  VARCHAR(64) NOT NULL COMMENT '唯一标识' ,
`type`  INT(11) NOT NULL COMMENT '第三方登录类型：1QQ，2微信，3邮箱' ,
`passengerId`  VARCHAR(32) NULL COMMENT '乘客ID' ,
`create_time`  DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
PRIMARY KEY (`id`)
)
;
ALTER TABLE `passenger`
MODIFY COLUMN `country_code`  VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `given_name`,
MODIFY COLUMN `tel`  VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `country_code`;

ALTER TABLE `taxigouser`
MODIFY COLUMN `country_code`  VARCHAR(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `lastUpdated`,
MODIFY COLUMN `tel`  VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `country_code`;

CREATE TABLE `spcar_passenger` (
`spcar_id`  INT(11) NOT NULL AUTO_INCREMENT ,
`name`  VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '乘客姓名' ,
`family_name`  VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'Passenger Family name' ,
`given_name`  VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'Passenger Given Name' ,
`country_code`  VARCHAR(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '区号' ,
`tel`  VARCHAR(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机' ,
`verification`  TEXT CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '验证码' ,
`loc_x`  DOUBLE NOT NULL COMMENT '坐标X轴' ,
`loc_y`  DOUBLE NOT NULL COMMENT '坐标Y轴' ,
`last_login`  DATETIME DEFAULT NULL COMMENT '上一次登录时间' ,
`create_time`  DATETIME NOT NULL COMMENT '创建时间' ,
`device_id`  VARCHAR(255) NOT NULL COMMENT '设备ID' ,
`passenger_id`  VARCHAR(255) NOT NULL COMMENT '关联taxigo的乘客ID' ,
`status`  INT(3) NOT NULL DEFAULT 1 COMMENT '状态' ,
`sex`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '性别' ,
`order_count`  DOUBLE NOT NULL DEFAULT 0 COMMENT '下单数' ,
`cancel_count`  DOUBLE NOT NULL DEFAULT 0 COMMENT '取消单数' ,
`finish_count`  DOUBLE NOT NULL DEFAULT 0 COMMENT '完成单数' ,
`deleted`  TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否拉黑' ,
`rating`  DOUBLE NULL DEFAULT 4.5 COMMENT '评分',
`device_type`  VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '設備類型：1代表ios,2代表android' ,
`im_name`  VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'im账号' ,
`isgn`  VARCHAR(512) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '签名' ,
PRIMARY KEY (`id`)
);


CREATE TABLE `meter` (
`id`  INT(11) NOT NULL AUTO_INCREMENT ,
`create_time`  DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
`pay_no`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号' ,
`pay_code`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态' ,
`pay_intro`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付描述' ,
`order_no`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户订单号' ,
`pay_money`  INT(64) NULL DEFAULT 0 COMMENT '交易金额' ,
`bank_type`  VARCHAR(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款银行' ,
`fee_type`  VARCHAR(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货币类型' ,
PRIMARY KEY (`id`)
)
;


ALTER TABLE `passenger`
  ADD COLUMN `im_name` VARCHAR(32) NULL AFTER `device_type`,
  ADD COLUMN `sign` VARCHAR(255) NULL AFTER `im_name`;


ALTER TABLE `taxigouser`
ADD COLUMN `type`  INT(3) NULL DEFAULT 1 COMMENT '是否属于专车0不是1是' AFTER `user_wechat`,
ADD COLUMN `im_name`  VARCHAR(255) NULL COMMENT 'im账号，登录腾讯云通信' AFTER `type`,
ADD COLUMN `sign`  VARCHAR(1024) NULL COMMENT 'IM签名' AFTER `im_name`;


DELIMITER ;//
CREATE TRIGGER taxigouser_update
AFTER
UPDATE ON taxigouser
FOR EACH ROW
BEGIN
	IF old.realm='passenger'
		THEN
			UPDATE spcar_passenger SET NAME=new.name,family_name=new.family_name,given_name=new.given_name WHERE passenger_id=old.realm_id;
	END IF;
END


CREATE TABLE `spcar_driver` (
  `spcar_driver_id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL COMMENT '司机姓名',
  `loc_x` DOUBLE NOT NULL COMMENT '高德地图，经度',
  `loc_y` DOUBLE NOT NULL COMMENT '高德地图，纬度',
  `loc_str` TEXT NOT NULL,
  `status` VARCHAR(3) NOT NULL DEFAULT '999' COMMENT '司机状态',
  `license` VARCHAR(255) NOT NULL COMMENT '實際上被用作司機的「車牌號碼」',
  `image` VARCHAR(255) NOT NULL DEFAULT 'empty-person-image.jpg' COMMENT '司机头像',
  `driver_type` VARCHAR(2) NOT NULL COMMENT '司机类型',
  `spcar_type` VARCHAR(2) NOT NULL COMMENT '车型',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_update` DATETIME DEFAULT NULL COMMENT '更新时间',
  `device_id` VARCHAR(255) NOT NULL COMMENT '设备id',
  `verification` VARCHAR(25) DEFAULT NULL COMMENT '验证码',
  `country_code` VARCHAR(10) NOT NULL COMMENT '区号',
  `tel` VARCHAR(100) NOT NULL COMMENT '手机号码',
  `given_name` VARCHAR(255) NOT NULL COMMENT '名字',
  `family_name` VARCHAR(255) NOT NULL COMMENT '姓氏',
  `userId` INT(11) DEFAULT '0',
  `rating` DOUBLE NOT NULL DEFAULT '5' COMMENT '司机评分',
  `english_capability` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否懂英文',
  `profile_image` VARCHAR(255) NOT NULL DEFAULT '' COMMENT '驾照图片',
  `order_count` DOUBLE NOT NULL DEFAULT '0' COMMENT '总单数',
  `cancel_count` DOUBLE NOT NULL DEFAULT '0' COMMENT '取消单数',
  `finish_count` DOUBLE NOT NULL DEFAULT '0' COMMENT '完成单数',
  `deleted` TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已拉黑',
  `license_true` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '驾照编码',
  `license_till` DATETIME DEFAULT NULL COMMENT '驾照到期日期',
  `shift` VARCHAR(45) DEFAULT NULL COMMENT '工作时间段',
  `device_type` VARCHAR(255) DEFAULT '1' COMMENT '設備類型：1ios2android',
  PRIMARY KEY (`spcar_driver_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8



CREATE TABLE `spcar_order` (
  `spcar_order_id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_no` VARCHAR(64) DEFAULT NULL COMMENT '商户订单号',
  `status` INT(11) NOT NULL COMMENT '订单状态:0进行中，2匹配中，3已匹配，4接载中，5已完成，998已取消',
  `driver_id` INT(10) DEFAULT NULL COMMENT '专车司机ID',
  `passenger_id` INT(10) NOT NULL COMMENT '乘客ID',
  `confirm_id` INT(11) DEFAULT NULL,
  `confirm_type` VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `reject_id` MEDIUMTEXT CHARACTER SET utf8mb4,
  `reject_type` VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `memo` VARCHAR(1024) CHARACTER SET utf8mb4 NOT NULL COMMENT '备注',
  `start_x` DOUBLE DEFAULT '0' COMMENT '上车位置X轴',
  `start_y` DOUBLE DEFAULT '0' COMMENT '上车位置Y轴',
  `start_address` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '上车地点',
  `update_time`DATETIME NOT NULL COMMENT '更新时间',
  `create_time` DATETIME NOT NULL COMMENT '下单时间',
  `pay_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  `amount` DOUBLE(16,1) DEFAULT NULL COMMENT '总金额',
  `pay_status` INT(11) DEFAULT '0' COMMENT '支付状态：0待付款；1已支付；2申請退款；3已退款',
  `total_hour` INT(11) DEFAULT NULL COMMENT '约车时长',
  `start_time` DATETIME DEFAULT NULL COMMENT '开始计时时间',
  `cancel_type` VARCHAR(64) DEFAULT NULL COMMENT '取消类型',
  `pay_way` VARCHAR(64) DEFAULT NULL COMMENT ' 支付方式：微信支付，支付宝支付，信用卡支付',
  `pay_way_code` INT(11) DEFAULT NULL COMMENT '支付方式代码：0微信支付，1支付宝支付2应用卡支付',
  `end_x` DOUBLE DEFAULT '0' COMMENT '下车位置X轴',
  `end_y` DOUBLE DEFAULT '0' COMMENT '下车位置Y轴',
  `end_address` VARCHAR(255) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '下车地点',
  PRIMARY KEY (`spcar_order_id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `meter` (
`meter_id`  INT(11) NOT NULL AUTO_INCREMENT ,
`create_time`  DATETIME NULL DEFAULT NULL COMMENT '创建时间' ,
`pay_no`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流水号' ,
`pay_code`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '交易状态' ,
`pay_intro`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '支付描述' ,
`order_no`  VARCHAR(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户订单号' ,
`pay_money`  INT(64) NULL DEFAULT 0 COMMENT '交易金额' ,
`bank_type`  VARCHAR(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '付款银行' ,
`fee_type`  VARCHAR(8) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货币类型' ,
PRIMARY KEY (`meter_id`)
)
;



CREATE TABLE `spcar`(
     `spcar_id` INT(11) NOT NULL AUTO_INCREMENT,
     `spcar_type` VARCHAR(255) DEFAULT NULL COMMENT '车型',
     `spcar_color` VARCHAR(128) DEFAULT NULL COMMENT'车色',
     `spcar_no` VARCHAR(255) NOT NULL  COMMENT'车牌号码',
     `spcar_sit` INT(11) DEFAULT 7 COMMENT '限坐',
     `spcar_used`TINYINT(1) NOT NULL DEFAULT '0' COMMENT '是否已使用 0否1是',
     `create_time` DATETIME NOT NULL COMMENT '创建时间',
     `update_time` DATETIME NOT NULL COMMENT '更新时间',
       PRIMARY KEY (`spcar_id`)

)