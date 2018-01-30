ALTER TABLE `parameter`
ADD COLUMN `remark`  varchar(255) NULL COMMENT '备注' AFTER `coefficient`,
ADD COLUMN `item`  varchar(255) NULL COMMENT '项目' AFTER `remark`;