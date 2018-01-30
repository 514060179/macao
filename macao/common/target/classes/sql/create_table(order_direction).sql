CREATE TABLE `order_direction` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` INT(11) NOT NULL COMMENT '订单id',
  `locX` DOUBLE NOT NULL COMMENT '经度',
  `locY` DOUBLE NOT NULL COMMENT '纬度',
  `descript` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址描述',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `remark` VARCHAR(225) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8