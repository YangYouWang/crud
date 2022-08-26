/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : crud

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 14/08/2022 23:10:19
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型key',
  `dict_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类型名称',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `enabled` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '数据字典类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (17, 'sex', '性别', 1, 'Y', 'admin', '2021-04-16 12:27:04', 'admin', '2022-06-25 21:47:42', 0, '性别字典');
INSERT INTO `sys_dict_type` VALUES (18, 'menuType', '菜单类型', 2, 'Y', 'admin', '2021-04-16 20:42:06', 'admin', '2022-06-25 22:43:44', 0, '菜单类型');
INSERT INTO `sys_dict_type` VALUES (19, 'enabled', '是否启用', 3, 'Y', 'admin', '2021-04-29 21:00:41', 'admin', '2021-12-09 23:51:44', 0, '是否启用');

-- ----------------------------
-- Table structure for sys_dict_value
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_value`;
CREATE TABLE `sys_dict_value`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_type_id` bigint(0) NULL DEFAULT NULL COMMENT '字典类型ID',
  `dict_value_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '字典值key',
  `dict_value_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '字典值名称',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `enabled` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_value
-- ----------------------------
INSERT INTO `sys_dict_value` VALUES (22, 17, '0', '男', 0, 'Y', '', NULL, 'admin', '2022-06-25 21:58:53', '');
INSERT INTO `sys_dict_value` VALUES (23, 17, '1', '女', 1, 'Y', '', NULL, 'admin', '2022-06-25 21:59:02', NULL);
INSERT INTO `sys_dict_value` VALUES (24, 18, 'M', '目录', 1, 'Y', '', NULL, 'admin', '2022-06-25 21:56:49', NULL);
INSERT INTO `sys_dict_value` VALUES (25, 18, 'C', '菜单', 2, 'Y', '', NULL, 'admin', '2022-06-25 21:28:28', NULL);
INSERT INTO `sys_dict_value` VALUES (26, 18, 'F', '按钮', 3, 'Y', '', NULL, 'admin', '2022-06-25 21:28:30', NULL);
INSERT INTO `sys_dict_value` VALUES (27, 17, '2', '未知', 2, 'Y', '', NULL, 'admin', '2022-06-25 21:26:22', NULL);
INSERT INTO `sys_dict_value` VALUES (30, 19, 'Y', '启用', 1, 'Y', '', NULL, 'admin', '2022-06-25 21:29:01', NULL);
INSERT INTO `sys_dict_value` VALUES (31, 19, 'N', '禁用', 2, 'Y', '', NULL, 'admin', '2022-06-25 21:29:13', NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `package_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '包名称',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类名称',
  `method_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '方法名称',
  `args_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '参数名',
  `args_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '参数值',
  `exception_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '异常类型',
  `err_msg` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '错误信息',
  `stack_trace` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '异常堆栈信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '图标',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '#' COMMENT '请求地址',
  `menu_type` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '菜单状态（Y显示 N隐藏）',
  `perms` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3054 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'layui-icon-set', '#', 'M', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-07 11:22:47', 0, '系统管理目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 2, 'layui-icon-util', '#', 'M', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-07 12:10:46', 0, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'layui-icon-username', '/sysUser/listPage', 'C', 'Y', 'user:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:14:38', 0, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'layui-icon-group', '/sysRole/listPage', 'C', 'Y', 'role:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:15:08', 0, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'layui-icon-theme', '/sysMenu/listPage', 'C', 'Y', 'menu:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:15:26', 0, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (115, '系统接口', 3, 3, 'layui-icon-rate', '/sysTool/swagger', 'C', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:17:25', 0, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (116, '数据库监控', 3, 3, 'layui-icon-video', '/sysTool/druid', 'C', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:17:40', 0, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (1005, '添加', 100, 1, NULL, '#', 'F', 'Y', 'user:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-29 21:30:49', 0, '添加用户');
INSERT INTO `sys_menu` VALUES (1006, '编辑', 100, 2, NULL, '#', 'F', 'Y', 'user:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:25:48', 0, '编辑用户');
INSERT INTO `sys_menu` VALUES (1007, '删除', 100, 3, NULL, '#', 'F', 'Y', 'user:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:26:13', 0, '删除用户');
INSERT INTO `sys_menu` VALUES (2005, '添加', 101, 1, NULL, '#', 'F', 'Y', 'role:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-03-31 16:57:49', 0, '添加用户');
INSERT INTO `sys_menu` VALUES (2006, '编辑', 101, 2, NULL, '#', 'F', 'Y', 'role:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:26:24', 0, '编辑角色');
INSERT INTO `sys_menu` VALUES (2007, '删除', 101, 3, NULL, '#', 'F', 'Y', 'role:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:26:32', 0, '删除角色');
INSERT INTO `sys_menu` VALUES (3005, '添加', 102, 1, NULL, '#', 'F', 'Y', 'menu:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-10 11:26:45', 0, '添加菜单');
INSERT INTO `sys_menu` VALUES (3006, '编辑', 102, 2, NULL, '#', 'F', 'Y', 'menu:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:27:40', 0, '编辑菜单');
INSERT INTO `sys_menu` VALUES (3007, '删除', 102, 3, NULL, '#', 'F', 'Y', 'menu:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:27:49', 0, '删除菜单');
INSERT INTO `sys_menu` VALUES (3008, '异常日志', 1, 5, 'layui-icon-chart-screen', '/sysLog/listPage', 'C', 'Y', 'log:list', 'admin', '2021-04-01 11:01:52', 'admin', '2021-05-12 11:17:10', 0, '异常日志列表');
INSERT INTO `sys_menu` VALUES (3013, '流程管理', 0, 3, 'layui-icon-release', '#', 'M', 'Y', '', 'admin', '2021-04-10 20:06:40', 'admin', '2021-05-07 12:11:21', 0, '工作流管理');
INSERT INTO `sys_menu` VALUES (3014, '模型管理', 3013, 1, 'layui-icon-set-fill', '/actReModel/listPage', 'C', 'Y', 'model:list', 'admin', '2021-04-10 20:10:56', 'admin', '2021-05-12 11:19:24', 0, '模型管理');
INSERT INTO `sys_menu` VALUES (3015, '添加', 3014, 1, NULL, '#', 'F', 'Y', 'model:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-10 11:26:53', 0, '添加模型');
INSERT INTO `sys_menu` VALUES (3016, '编辑', 3014, 2, NULL, '#', 'F', 'Y', 'model:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:27:05', 0, '编辑模型');
INSERT INTO `sys_menu` VALUES (3017, '删除', 3014, 3, NULL, '#', 'F', 'Y', 'model:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-03-31 17:01:57', 0, '删除模型');
INSERT INTO `sys_menu` VALUES (3018, '设计', 3014, 3, NULL, '#', 'F', 'Y', 'model:design', 'admin', '2021-03-31 17:01:57', 'admin', '2021-03-31 17:01:57', 0, '设计模型');
INSERT INTO `sys_menu` VALUES (3019, '部署', 3014, 3, NULL, '#', 'F', 'Y', 'model:deploy', 'admin', '2021-03-31 17:01:57', 'admin', '2021-03-31 17:01:57', 0, '部署模型');
INSERT INTO `sys_menu` VALUES (3020, '字典管理', 1, 5, 'layui-icon-edit', '/sysDictType/listPage', 'C', 'Y', 'dictType:list', 'admin', '2021-04-13 13:35:48', 'admin', '2021-07-01 14:58:37', 0, '字典管理');
INSERT INTO `sys_menu` VALUES (3021, '添加', 3020, 1, NULL, '#', 'F', 'Y', 'dictType:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-10 11:26:53', 0, '添加字典');
INSERT INTO `sys_menu` VALUES (3022, '编辑', 3020, 2, NULL, '#', 'F', 'Y', 'dictType:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:27:05', 0, '编辑字典');
INSERT INTO `sys_menu` VALUES (3023, '删除', 3020, 3, NULL, '#', 'F', 'Y', 'dictType:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-03-31 17:01:57', 0, '删除字典');
INSERT INTO `sys_menu` VALUES (3027, '重置密码', 100, 4, '', '#', 'F', 'Y', 'user:pass', 'admin', '2021-12-07 23:37:52', 'admin', '2021-12-07 23:38:04', 0, '重置密码');
INSERT INTO `sys_menu` VALUES (3028, '添加字典项', 3020, 4, '', '#', 'F', 'Y', 'dictValue:add', 'admin', '2021-12-08 22:50:27', 'admin', '2021-12-08 23:42:49', 0, '添加字典项');
INSERT INTO `sys_menu` VALUES (3029, '修改字典项', 3020, 5, '', '#', 'F', 'Y', 'dictValue:edit', 'admin', '2021-12-08 23:19:20', 'admin', '2021-12-08 23:43:00', 0, '修改字典项');
INSERT INTO `sys_menu` VALUES (3030, '删除字典项', 3020, 6, '', '#', 'F', 'Y', 'dictValue:del', 'admin', '2021-12-08 23:42:39', 'admin', '2021-12-08 23:43:09', 0, '删除字典项');
INSERT INTO `sys_menu` VALUES (3038, '删除日志', 3008, 1, '', '#', 'F', 'Y', 'log:del', 'admin', '2021-12-18 21:59:23', '', NULL, 0, '删除日志');
INSERT INTO `sys_menu` VALUES (3043, '任务管理', 1, 6, 'layui-icon-ok-circle', '/qrtz/job/listPage', 'C', 'Y', 'job:list', '', NULL, 'admin', '2022-07-30 22:12:46', 0, '');
INSERT INTO `sys_menu` VALUES (3044, '添加任务表', 3043, 1, '', '#', 'F', 'Y', 'job:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3045, '编辑任务表', 3043, 2, '', '#', 'F', 'Y', 'job:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3046, '删除任务表', 3043, 3, '', '#', 'F', 'Y', 'job:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3047, '广告表管理', 3051, 1, 'layui-icon-home', '/app/ad/listPage', 'C', 'Y', 'ad:list', '', NULL, 'admin', '2022-08-11 19:34:04', 0, '');
INSERT INTO `sys_menu` VALUES (3048, '添加广告表', 3047, 1, '', '#', 'F', 'Y', 'ad:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3049, '编辑广告表', 3047, 2, '', '#', 'F', 'Y', 'ad:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3050, '删除广告表', 3047, 3, '', '#', 'F', 'Y', 'ad:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3051, 'APP管理', 0, 4, 'layui-icon-cellphone', '#', 'M', 'Y', '', 'admin', '2022-08-11 19:33:57', 'admin', '2022-08-11 19:34:19', 0, '');
INSERT INTO `sys_menu` VALUES (3052, '暂停某个定时任务', 3043, 4, '', '#', 'F', 'Y', 'job:pause', 'admin', '2022-08-14 17:45:30', '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3053, '恢复某个定时任务', 3043, 5, '', '#', 'F', 'Y', 'job:resume', 'admin', '2022-08-14 17:45:52', 'admin', '2022-08-14 17:46:10', 0, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名称',
  `role_key` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '标识',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ADMIN', '', NULL, 'admin', '2022-08-14 22:57:33', 0, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '基本角色', 'USER', '', NULL, 'admin', '2022-08-14 23:02:39', 0, '基本角色');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
  INDEX `FKf3mud4qoc7ayew8nml4plkevo`(`menu_id`) USING BTREE,
  INDEX `FKkeitxsgxwayackgqllio4ohn5`(`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 3047);
INSERT INTO `sys_role_menu` VALUES (2, 3048);
INSERT INTO `sys_role_menu` VALUES (2, 3049);
INSERT INTO `sys_role_menu` VALUES (2, 3050);
INSERT INTO `sys_role_menu` VALUES (2, 3051);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '账户',
  `pass_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `enabled` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '头像路径',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$u3Re8vB2J3pPAw8RYdpSA.5z/RR/oCg/2/WcSzEe.JZ0DJIz.VacO', 'Y', '616505453@qq.com', '17515087128', '0', 'https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png', '', NULL, 'admin', '2022-08-12 15:43:55', 0, '超级管理员');
INSERT INTO `sys_user` VALUES (24, 'test', '$2a$10$6ppgQN77P8WufCdWftbJGO4D8qRMcoPruIAga2yvm0./K4oBh1Q1C', 'Y', '123', '123', '0', 'https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png', 'admin', '2021-03-30 20:25:07', 'admin', '2021-12-09 23:14:09', 0, '测试账号');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `role_id` bigint(0) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `FKhh52n8vd4ny9ff4x9fb8v65qx`(`role_id`) USING BTREE,
  INDEX `FKb40xxfch70f5qnyfw8yme1n1s`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (24, 2);

SET FOREIGN_KEY_CHECKS = 1;
