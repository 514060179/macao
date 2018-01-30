CREATE TABLE `yinghai_taxigo`.`operation_log`(
  `id` INT NOT NULL AUTO_INCREMENT,
  `operator_id` INT NOT NULL COMMENT '操作人id',
  `operator_type` VARCHAR(225) NOT NULL COMMENT '操作人类型',
  `content` VARCHAR(225) COMMENT '操作内容',
  `class_name` VARCHAR(225) NOT NULL COMMENT '操作类名',
  `method` VARCHAR(225) NOT NULL COMMENT '操作方法',
  `create_time` DATETIME NOT NULL COMMENT '操作时间',
  `remark` VARCHAR(225) COMMENT '备注',
  PRIMARY KEY (`id`)
);
