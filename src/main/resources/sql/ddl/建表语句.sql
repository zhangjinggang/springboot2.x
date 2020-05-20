CREATE TABLE `mn_remind_conf`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `conf_id` varchar(36)  NOT NULL COMMENT '配置项Id',
  `conf_name` varchar(64)  NULL DEFAULT NULL COMMENT '配置项名称',
  `conf_type` varchar(16)  NULL DEFAULT NULL COMMENT '配置类型：url',
  `conf_value` varchar(255)  NULL DEFAULT NULL COMMENT '配置项值',
  `user_name` varchar(16)  NULL DEFAULT NULL COMMENT 'URL的用户名，如果有',
  `password` varchar(16)  NULL DEFAULT NULL COMMENT 'URL的密码，如果有',
  `conf_desc` varchar(255)  NULL DEFAULT NULL COMMENT '配置简述',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(36)  NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` tinyint(4) NULL DEFAULT NULL COMMENT '逻辑删除标识：0未删除，1已删除',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_conf_id`(`conf_id`) COMMENT '配置项id唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4  COMMENT = '邮件提醒配置表' ;

CREATE TABLE `mn_remind_receive`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `conf_id` varchar(36)  NOT NULL COMMENT '配置项Id',
  `receive_name` varchar(16)  NULL DEFAULT NULL COMMENT '接收人姓名',
  `receive_type` varchar(16)  NULL DEFAULT NULL COMMENT '接收方式：MC主送，CC抄送',
  `receive_mail` varchar(64)  NULL DEFAULT NULL COMMENT '接收邮件地址',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(36)  NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `delete_flag` tinyint(4) NULL DEFAULT NULL COMMENT '逻辑删除标识：0未删除，1已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_conf_id`(`conf_id`) COMMENT '配置项id唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4  COMMENT = '邮件提醒接收邮件地址表' ;


