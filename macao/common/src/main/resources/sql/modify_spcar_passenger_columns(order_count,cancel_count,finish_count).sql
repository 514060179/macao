ALTER TABLE `yinghai_taxigo`.`spcar_passenger`
  CHANGE `order_count` `order_count` INT(11) DEFAULT 0 NOT NULL COMMENT '下单数',
  CHANGE `cancel_count` `cancel_count` INT(11) DEFAULT 0 NOT NULL COMMENT '取消单数',
  CHANGE `finish_count` `finish_count` INT(11) DEFAULT 0 NOT NULL COMMENT '完成单数';