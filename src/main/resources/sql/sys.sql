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
  `deleted` int(11) DEFAULT '0' COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '数据字典值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '标题',
  `business_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '业务类型',
  `package_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '包名称',
  `class_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '类名称',
  `method_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '方法名称',
  `args_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '参数名',
  `args_value` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '参数值',
  `exception_name` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT '异常类型',
  `err_msg` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT  '错误信息',
  `stack_trace` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '异常堆栈信息',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志唯一ID',
    `account` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '账号',
    `login_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '登录IP',
    `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '创建者',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '更新者',
    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
    `deleted` int DEFAULT '0' COMMENT '逻辑删除 0 否、1 是',
    `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT='用户登录日志记录表';

-- ----------------------------
-- Records of sys_login_log
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
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '部门外键',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '用户昵称或网络名称',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '账户',
  `pass_word` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '密码',
  `enabled` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '0' COMMENT '用户性别（1男 2女 0未知）',
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
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '部门名称',
  `order_num` int(0) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '邮箱',
  `parent_id` bigint(0) NULL DEFAULT 0 COMMENT '父部门ID',
  `enabled` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(0) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '岗位名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `enabled` char(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '是否启用 Y 启用 N 禁用',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` int(11) NULL DEFAULT 0 COMMENT '逻辑删除 0 否、1 是',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '岗位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户外键',
  `post_id` bigint(20) NOT NULL COMMENT '岗位外键',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(65) DEFAULT NULL,
  `series` varchar(65) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `token` varchar(255) DEFAULT NULL,
  `last_used` datetime DEFAULT NULL,
  PRIMARY KEY (`series`)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '记住我' ROW_FORMAT = Dynamic;
-- ----------------------------
-- Records of persistent_logins
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
