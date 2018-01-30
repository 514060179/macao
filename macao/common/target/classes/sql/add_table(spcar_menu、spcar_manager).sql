CREATE TABLE `spcar_manager` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录账号',
  `password` VARCHAR(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录密码',
  `email` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱账号',
  `email_verified` TINYINT(4) DEFAULT NULL COMMENT '邮箱验证',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `last_updated` DATETIME NOT NULL COMMENT '最近更新时间',
  `role_id` INT(11) DEFAULT '0' COMMENT '角色id',
  `remark` VARCHAR(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

CREATE TABLE `spcar_menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单名字',
  `icon` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单图标',
  `url` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单url',
  `descript` VARCHAR(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `lable` VARCHAR(225) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单标识',
  `priority` INT(11) DEFAULT '999' COMMENT '优先级',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci