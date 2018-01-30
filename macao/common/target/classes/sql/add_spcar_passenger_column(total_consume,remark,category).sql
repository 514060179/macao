ALTER TABLE `spcar_passenger`
ADD COLUMN `total_consume`  int(11) NOT NULL DEFAULT 0 COMMENT '消费总额' AFTER `finish_count`,
ADD COLUMN `remark`  varchar(255) NULL COMMENT '备注' AFTER `sign`,
ADD COLUMN `category` varchar(255) NULL COMMENT '类别' AFTER `remark`;






