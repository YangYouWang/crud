-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (17, 'sex', '性别', 1, 'Y', 'admin', '2021-04-16 12:27:04', 'admin', '2022-06-25 21:47:42', 0, '性别字典');
INSERT INTO `sys_dict_type` VALUES (18, 'menuType', '菜单类型', 2, 'Y', 'admin', '2021-04-16 20:42:06', 'admin', '2022-06-25 22:43:44', 0, '菜单类型');
INSERT INTO `sys_dict_type` VALUES (19, 'enabled', '是否启用', 3, 'Y', 'admin', '2021-04-29 21:00:41', 'admin', '2021-12-09 23:51:44', 0, '是否启用');
INSERT INTO `sys_dict_type` VALUES (20, 'noticeType', '公告类型', 5, 'Y', 'admin', '2022-10-04 20:46:07', 'admin', '2022-10-04 22:18:42', 0, '公告类型');
INSERT INTO `sys_dict_type` VALUES (21, 'executeStatus', '执行状态', 6, 'Y', 'admin', '2022-10-26 18:17:07', '', NULL, 0, '执行状态');
INSERT INTO `sys_dict_type` VALUES (22, 'sysYesNo', '系统是否', 7, 'Y', 'admin', '2022-12-07 04:01:25', 'admin', '2022-12-08 00:02:29', 0, '');

-- ----------------------------
-- Records of sys_dict_value
-- ----------------------------
INSERT INTO `sys_dict_value` VALUES (1, 17, '1', '男', 1, 'Y', 'admin', '2022-06-25 01:58:53', 'admin', '2022-12-05 18:14:08', 0, '');
INSERT INTO `sys_dict_value` VALUES (2, 17, '2', '女', 2, 'Y', 'admin', '2022-06-25 01:59:02', 'admin', '2022-12-05 18:14:19', 0, '');
INSERT INTO `sys_dict_value` VALUES (3, 18, 'M', '目录', 1, 'Y', 'admin', '2022-06-25 21:56:49', 'admin', '2022-06-25 21:56:49', 0, NULL);
INSERT INTO `sys_dict_value` VALUES (4, 18, 'C', '菜单', 2, 'Y', 'admin', '2022-06-25 21:28:28', 'admin', '2022-06-25 21:28:28', 0, NULL);
INSERT INTO `sys_dict_value` VALUES (5, 18, 'F', '按钮', 3, 'Y', 'admin', '2022-06-25 21:28:30', 'admin', '2022-06-25 21:28:30', 0, NULL);
INSERT INTO `sys_dict_value` VALUES (6, 19, 'Y', '启用', 1, 'Y', 'admin', '2022-06-25 21:29:01', 'admin', '2022-06-25 21:29:01', 0, NULL);
INSERT INTO `sys_dict_value` VALUES (7, 19, 'N', '禁用', 2, 'Y', 'admin', '2022-06-25 21:29:13', 'admin', '2022-06-25 21:29:13', 0, NULL);
INSERT INTO `sys_dict_value` VALUES (8, 20, '1', '通知', 1, 'Y', 'admin', '2022-06-25 21:29:01', 'admin', '2022-06-25 21:29:01', 0, '');
INSERT INTO `sys_dict_value` VALUES (9, 20, '2', '公告', 2, 'Y', 'admin', '2022-06-25 21:29:01', 'admin', '2022-06-25 21:29:01', 0, '');
INSERT INTO `sys_dict_value` VALUES (10, 21, '0', '正常', 1, 'Y', 'admin', '2022-06-25 21:29:01', 'admin', '2022-06-25 21:29:01', 0, '正常');
INSERT INTO `sys_dict_value` VALUES (11, 21, '1', '失败', 2, 'Y', 'admin', '2022-06-25 21:29:01', 'admin', '2022-06-25 21:29:01', 0, '失败');
INSERT INTO `sys_dict_value` VALUES (12, 22, 'Y', '是', 1, 'Y', 'admin', '2022-12-08 00:02:02', '', NULL, 0, '');
INSERT INTO `sys_dict_value` VALUES (13, 22, 'N', '否', 2, 'Y', 'admin', '2022-12-08 00:02:11', '', NULL, 0, '');

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'layui-icon-set', '#', 'M', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-07 11:22:47', 0, '系统管理目录');
INSERT INTO `sys_menu` VALUES (3, '系统工具', 0, 2, 'layui-icon-util', '#', 'M', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-07 12:10:46', 0, '系统工具目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'layui-icon-username', '/sysUser/listPage', 'C', 'Y', 'user:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:14:38', 0, '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'layui-icon-group', '/sysRole/listPage', 'C', 'Y', 'role:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:15:08', 0, '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 5, 'layui-icon-theme', '/sysMenu/listPage', 'C', 'Y', 'menu:list', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:15:26', 0, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (115, '系统接口', 3, 3, 'layui-icon-rate', '/sysTool/swagger', 'C', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:17:25', 0, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (116, '数据库监控', 3, 3, 'layui-icon-video', '/sysTool/druid', 'C', 'Y', '', '1', '2018-03-16 11:33:00', 'admin', '2021-05-12 11:17:40', 0, '系统接口菜单');
INSERT INTO `sys_menu` VALUES (1005, '添加用户', 100, 1, NULL, '#', 'F', 'Y', 'user:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-29 21:30:49', 0, '添加用户');
INSERT INTO `sys_menu` VALUES (1006, '编辑用户', 100, 2, NULL, '#', 'F', 'Y', 'user:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:25:48', 0, '编辑用户');
INSERT INTO `sys_menu` VALUES (1007, '删除用户', 100, 3, NULL, '#', 'F', 'Y', 'user:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:26:13', 0, '删除用户');
INSERT INTO `sys_menu` VALUES (2005, '添加角色', 101, 1, NULL, '#', 'F', 'Y', 'role:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-03-31 16:57:49', 0, '添加角色');
INSERT INTO `sys_menu` VALUES (2006, '编辑角色', 101, 2, NULL, '#', 'F', 'Y', 'role:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:26:24', 0, '编辑角色');
INSERT INTO `sys_menu` VALUES (2007, '删除角色', 101, 3, NULL, '#', 'F', 'Y', 'role:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:26:32', 0, '删除角色');
INSERT INTO `sys_menu` VALUES (3005, '添加菜单', 102, 1, NULL, '#', 'F', 'Y', 'menu:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-10 11:26:45', 0, '添加菜单');
INSERT INTO `sys_menu` VALUES (3006, '编辑菜单', 102, 2, NULL, '#', 'F', 'Y', 'menu:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:27:40', 0, '编辑菜单');
INSERT INTO `sys_menu` VALUES (3007, '删除菜单', 102, 3, NULL, '#', 'F', 'Y', 'menu:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-04-10 11:27:49', 0, '删除菜单');
INSERT INTO `sys_menu` VALUES (3008, '操作日志管理', 1, 8, 'layui-icon-chart-screen', '/sysLog/listPage', 'C', 'Y', 'log:list', 'admin', '2021-04-01 11:01:52', 'admin', '2022-09-15 22:10:39', 0, '异常日志列表');
INSERT INTO `sys_menu` VALUES (3020, '字典管理', 1, 6, 'layui-icon-edit', '/sysDictType/listPage', 'C', 'Y', 'dictType:list', 'admin', '2021-04-13 13:35:48', 'admin', '2022-09-03 23:50:38', 0, '字典管理');
INSERT INTO `sys_menu` VALUES (3021, '添加字典', 3020, 1, NULL, '#', 'F', 'Y', 'dictType:add', 'admin', '2021-03-31 16:55:04', 'admin', '2021-04-10 11:26:53', 0, '添加字典');
INSERT INTO `sys_menu` VALUES (3022, '编辑字典', 3020, 2, NULL, '#', 'F', 'Y', 'dictType:edit', 'admin', '2021-03-31 17:01:35', 'admin', '2021-04-10 11:27:05', 0, '编辑字典');
INSERT INTO `sys_menu` VALUES (3023, '删除字典', 3020, 3, NULL, '#', 'F', 'Y', 'dictType:del', 'admin', '2021-03-31 17:01:57', 'admin', '2021-03-31 17:01:57', 0, '删除字典');
INSERT INTO `sys_menu` VALUES (3027, '重置密码', 100, 4, '', '#', 'F', 'Y', 'user:pass', 'admin', '2021-12-07 23:37:52', 'admin', '2021-12-07 23:38:04', 0, '重置密码');
INSERT INTO `sys_menu` VALUES (3028, '添加字典项', 3020, 4, '', '#', 'F', 'Y', 'dictValue:add', 'admin', '2021-12-08 22:50:27', 'admin', '2021-12-08 23:42:49', 0, '添加字典项');
INSERT INTO `sys_menu` VALUES (3029, '修改字典项', 3020, 5, '', '#', 'F', 'Y', 'dictValue:edit', 'admin', '2021-12-08 23:19:20', 'admin', '2021-12-08 23:43:00', 0, '修改字典项');
INSERT INTO `sys_menu` VALUES (3030, '删除字典项', 3020, 6, '', '#', 'F', 'Y', 'dictValue:del', 'admin', '2021-12-08 23:42:39', 'admin', '2021-12-08 23:43:09', 0, '删除字典项');
INSERT INTO `sys_menu` VALUES (3038, '删除日志', 3008, 1, '', '#', 'F', 'Y', 'log:del', 'admin', '2021-12-18 21:59:23', '', NULL, 0, '删除日志');
INSERT INTO `sys_menu` VALUES (3039, '日志详情', 3008, 2, '', '#', 'F', 'Y', 'log:info', 'admin', '2021-12-18 21:59:23', '', NULL, 0, '日志详情');
INSERT INTO `sys_menu` VALUES (3043, '任务管理', 1, 7, 'layui-icon-ok-circle', '/qrtz/job/listPage', 'C', 'Y', 'job:list', '', NULL, 'admin', '2022-09-15 22:10:59', 0, '');
INSERT INTO `sys_menu` VALUES (3044, '添加任务表', 3043, 1, '', '#', 'F', 'Y', 'job:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3045, '编辑任务表', 3043, 2, '', '#', 'F', 'Y', 'job:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3046, '删除任务表', 3043, 3, '', '#', 'F', 'Y', 'job:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3056, '登录日志管理', 1, 9, 'layui-icon-location', '/sysLoginLog/listPage', 'C', 'Y', 'sysLoginLog:list', '', NULL, 'admin', '2022-09-15 22:10:11', 0, '');
INSERT INTO `sys_menu` VALUES (3057, '删除登录日志', 3056, 1, '', '#', 'F', 'Y', 'sysLoginLog:del', 'admin', '2022-08-29 22:31:24', '', NULL, 0, '删除登录日志');
INSERT INTO `sys_menu` VALUES (3063, '部门管理', 1, 3, 'layui-icon-home', '/system/sysDept/listPage', 'C', 'Y', 'sysDept:list', '', NULL, 'admin', '2022-09-15 22:11:13', 0, '');
INSERT INTO `sys_menu` VALUES (3064, '添加部门表', 3063, 1, '', '#', 'F', 'Y', 'sysDept:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3065, '编辑部门表', 3063, 2, '', '#', 'F', 'Y', 'sysDept:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3066, '删除部门表', 3063, 3, '', '#', 'F', 'Y', 'sysDept:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3067, '岗位管理', 1, 4, 'layui-icon-home', '/system/sysPost/listPage', 'C', 'Y', 'sysPost:list', '', NULL, 'admin', '2022-09-15 22:11:31', 0, '');
INSERT INTO `sys_menu` VALUES (3068, '添加岗位表', 3067, 1, '', '#', 'F', 'Y', 'sysPost:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3069, '编辑岗位表', 3067, 2, '', '#', 'F', 'Y', 'sysPost:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3070, '删除岗位表', 3067, 3, '', '#', 'F', 'Y', 'sysPost:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3075, '任务日志管理', 1, 11, 'layui-icon-home', '/qrtz/jobLog/listPage', 'C', 'N', 'jobLog:list', '', NULL, 'admin', '2022-10-30 14:02:24', 0, '');
INSERT INTO `sys_menu` VALUES (3076, '添加任务日志', 3075, 1, '', '#', 'F', 'Y', 'jobLog:add', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3077, '编辑任务日志', 3075, 2, '', '#', 'F', 'Y', 'jobLog:edit', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3078, '删除任务日志', 3075, 3, '', '#', 'F', 'Y', 'jobLog:del', '', NULL, '', NULL, 0, '');
INSERT INTO `sys_menu` VALUES (3079, '调度', 3043, 6, '', '#', 'F', 'Y', 'job:log', 'admin', '2022-10-30 14:03:16', 'admin', '2022-10-30 14:03:25', 0, '');


-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ADMIN', 'admin', '2022-08-14 22:57:33', 'admin', '2022-08-14 22:57:33', 0, '管理员');
INSERT INTO `sys_role` VALUES (2, '基本角色', 'USER', 'admin', '2022-08-14 23:02:39', 'admin', '2022-08-14 23:02:39', 0, '基本角色');

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 3047);
INSERT INTO `sys_role_menu` VALUES (2, 3048);
INSERT INTO `sys_role_menu` VALUES (2, 3049);
INSERT INTO `sys_role_menu` VALUES (2, 3050);
INSERT INTO `sys_role_menu` VALUES (2, 3051);

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 26, '管理员', 'admin', '$2a$10$u3Re8vB2J3pPAw8RYdpSA.5z/RR/oCg/2/WcSzEe.JZ0DJIz.VacO', 'Y', '616505453@qq.com', '17515087128', '1', NULL, '', NULL, 'admin', '2022-09-05 07:51:57', 0, '超级管理员');
INSERT INTO `sys_user` VALUES (24, 25, '测试', 'test', '$2a$10$6ppgQN77P8WufCdWftbJGO4D8qRMcoPruIAga2yvm0./K4oBh1Q1C', 'Y', '616505453@qq.com', '17515087128', '1', NULL, 'admin', '2021-03-30 20:25:07', 'admin', '2021-12-09 23:14:09', 0, '测试账号');

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (24, 2);

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (25, '总部', 1, '杨先生', '17515087128', '616505453@qq.com', 0, 'Y', 'admin', '2022-09-11 00:21:08', 'admin', '2022-09-11 11:02:41', 0, '');
INSERT INTO `sys_dept` VALUES (26, '研发部', 1, '杨先生', '17515087128', '616505453@qq.com', 25, 'Y', 'admin', '2022-09-11 00:21:25', 'admin', '2022-09-11 11:02:37', 0, '');

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, '100', '研发岗', 1, 'Y', 'admin', '2022-09-15 23:01:03', '', NULL, 0, '研发岗');
INSERT INTO `sys_post` VALUES (2, '200', '销售岗', 2, 'Y', 'admin', '2022-09-15 23:21:54', '', NULL, 0, '销售岗');

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (24, 1);
INSERT INTO `sys_user_post` VALUES (24, 2);