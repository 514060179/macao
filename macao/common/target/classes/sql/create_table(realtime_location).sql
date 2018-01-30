CREATE TABLE `realtime_location` (
  	`id` INT(11) NOT NULL  AUTO_INCREMENT PRIMARY KEY COMMENT '主键id',
	`user_id` INT(11) NOT NULL COMMENT '用户id',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`update_time` DATETIME COMMENT '更新时间',
	`type` INT(5) COMMENT '类型位置1为起点2为终点，默认为空',
	`point` POINT NOT NULL COMMENT '位置点'
) ENGINE=MYISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;