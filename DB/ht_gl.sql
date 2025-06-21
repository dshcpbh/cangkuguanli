/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80042
 Source Host           : localhost:3306
 Source Schema         : ht_gl

 Target Server Type    : MySQL
 Target Server Version : 80042
 File Encoding         : 65001

 Date: 09/06/2025 11:45:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE DATABASE ht_gl;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '书名',
  `storage` int NULL DEFAULT NULL COMMENT '仓库ID',
  `goodstype` int NULL DEFAULT NULL COMMENT '分类ID',
  `count` int NULL DEFAULT NULL COMMENT '库存数量',
  `imagepath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片路径',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '另一个地球', 1, 1, 190, 'http://localhost:8080/files/download/kexuelei1.jpg', '2025-06-05 22:33:19');
INSERT INTO `book` VALUES (3, '美术', 2, 2, 275, 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg', '2025-06-05 23:31:26');
INSERT INTO `book` VALUES (4, '艺术的故事', 1, 2, 222, 'http://localhost:8080/files/download/v2-31fc27ba38976d2101440w.jpg', '2025-06-06 13:42:02');
INSERT INTO `book` VALUES (5, '如何看懂印象派', 3, 2, 55, 'http://localhost:8080/files/download/v2-8c2d921badaf2c175w.jpg', '2025-06-06 13:42:31');
INSERT INTO `book` VALUES (6, '科学是什么', 4, 1, 600, 'http://localhost:8080/files/download/OIP-C (3).jpg', '2025-06-07 22:59:08');
INSERT INTO `book` VALUES (7, '另一个地球', 3, 1, 130, 'http://localhost:8080/files/download/kexuelei1.jpg', '2025-06-07 23:00:10');
INSERT INTO `book` VALUES (8, '计算机网络技术', 2, 4, 300, 'http://localhost:8080/files/download/OIP-C (5).jpg', '2025-06-07 23:01:07');
INSERT INTO `book` VALUES (9, '减糖生活', 1, 5, 110, 'http://localhost:8080/files/download/R-C.jpg', '2025-06-07 23:02:36');

-- ----------------------------
-- Table structure for goodstype
-- ----------------------------
DROP TABLE IF EXISTS `goodstype`;
CREATE TABLE `goodstype`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of goodstype
-- ----------------------------
INSERT INTO `goodstype` VALUES (1, '科学类', '1');
INSERT INTO `goodstype` VALUES (2, '艺术类', '');
INSERT INTO `goodstype` VALUES (4, '技术类', '');
INSERT INTO `goodstype` VALUES (5, '生活类', '');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `henucode` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单编码',
  `menuname` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单名字',
  `menulevel` varchar(2) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单级别',
  `menuparentCode` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单的父code',
  `menuclick` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '点击触发的函数',
  `menuright` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '权限：0超级管理员，1管理员，2普通用户',
  `menucomponent` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `menuicon` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '001', '管理员管理', '1', NULL, 'Admin', '0', 'admin/AdminMenage.vue', 'Avatar');
INSERT INTO `menu` VALUES (2, '002', '用户管理', '1', NULL, 'User', '0,1', 'user/UserMenage.vue', 'User');
INSERT INTO `menu` VALUES (3, '003', '存放仓库管理', '1', NULL, 'Storage', '0,1', 'storage/StorageMenage.vue', 'Management');
INSERT INTO `menu` VALUES (4, '004', '书籍分类', '1', NULL, 'Goodstype', '0,1,2', 'goodstype/GoodstypeMenage.vue', 'CreditCard');
INSERT INTO `menu` VALUES (5, '005', '书籍管理', '1', NULL, 'Book', '0,1,2', 'book/BookMenage.vue', 'Shop');
INSERT INTO `menu` VALUES (6, '006', '出入库记录', '1', NULL, 'Record', '0,1,2', 'record/RecordMenage.vue', 'Camera');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `book` int NULL DEFAULT NULL COMMENT '书本id',
  `userid` int NULL DEFAULT NULL COMMENT '取货人id',
  `adminid` int NULL DEFAULT NULL COMMENT '操作人id',
  `count` int NULL DEFAULT NULL COMMENT '数量',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `imagepath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of record
-- ----------------------------
INSERT INTO `record` VALUES (1, 1, 2, 1, 21, '2025-06-06 17:37:19', NULL, 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (2, 3, 9, 1, 55, '2025-06-06 17:59:10', NULL, 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (3, 1, 6, 1, 11, '2025-06-06 20:10:53', '', NULL);
INSERT INTO `record` VALUES (4, 3, 6, 1, 55, '2025-06-06 20:11:10', '', NULL);
INSERT INTO `record` VALUES (5, 1, 6, 1, 1, '2025-06-06 20:14:17', '', NULL);
INSERT INTO `record` VALUES (6, 1, 6, 1, 5, '2025-06-06 20:14:57', '', NULL);
INSERT INTO `record` VALUES (7, 3, 6, 1, 2, '2025-06-06 20:17:45', '', 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (8, 3, 6, 1, 2, '2025-06-06 20:18:45', '', 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (9, 1, 6, 1, 55, '2025-06-06 20:20:22', '', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (10, 1, 6, 1, 2, '2025-06-06 20:28:27', '', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (11, 1, 6, 1, -3, '2025-06-06 20:28:53', '', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (12, 3, 6, 1, -26, '2025-06-06 20:29:53', '', 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (13, 1, 6, 1, 20, '2025-06-06 20:32:52', '11', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (14, 1, 3, 1, 11, '2025-06-06 23:57:04', '', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (15, 1, 20, 1, -1, '2025-06-06 23:57:32', '11', 'http://localhost:8080/files/download/kexuelei1.jpg');
INSERT INTO `record` VALUES (16, 3, 9, 1, 20, '2025-06-07 00:00:48', '20', 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');
INSERT INTO `record` VALUES (17, 9, 10, 1, 20, '2025-06-07 23:03:34', '哈哈哈哈', 'http://localhost:8080/files/download/R-C.jpg');
INSERT INTO `record` VALUES (18, 3, 3, 1, 55, '2025-06-08 22:26:29', '6666', 'http://localhost:8080/files/download/f4bde08439fd4ee.jpg');

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '仓库名',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `createtime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storage
-- ----------------------------
INSERT INTO `storage` VALUES (1, '仓库1', '1111', '2025-06-05 16:20:08');
INSERT INTO `storage` VALUES (2, '仓库2', '1', '2025-06-05 17:38:39');
INSERT INTO `storage` VALUES (3, '仓库3', '', '2025-06-05 18:01:02');
INSERT INTO `storage` VALUES (4, '仓库5', '', '2025-06-05 18:02:41');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` int NULL DEFAULT NULL,
  `age` int NULL DEFAULT NULL,
  `role_id` int NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `txpath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', '123456', 1, 20, 0, '17898766235', 'http://localhost:8080/files/download/DWSOzMwYU0.jpg');
INSERT INTO `user` VALUES (2, 'sa6', 'hua', '123456', 0, 18, 1, '12345678909', 'http://localhost:8080/files/download/.thumb.400_0.jpg');
INSERT INTO `user` VALUES (3, '555', '55', '555555', 1, 44, 2, '13655685459', 'http://localhost:8080/files/download/20220408205533_7d1cc.jpg');
INSERT INTO `user` VALUES (4, 'jxx', '今夕', '555555', 1, 20, 1, '16555555555', 'http://localhost:8080/files/download/8401cfb6c4ac4e2c.jpg');
INSERT INTO `user` VALUES (5, '122', 'xuebao', '123456', 1, 10, 2, '13454564892', 'http://localhost:8080/files/download/OIP-C (2).jpg');
INSERT INTO `user` VALUES (6, '888', '莫塔里', '123456', 0, 20, 1, '13546568975', 'http://localhost:8080/files/download/20200526115556_N5aS5.jpg');
INSERT INTO `user` VALUES (7, '99999', '小明3', '12121', 1, 12, 0, '43434242543', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (8, 'sa1', '小明5', '123456', 0, 22, 0, '65656565655', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (9, 'pbh', 'pbh', '123456', 0, 20, 2, '15556165165', 'http://localhost:8080/files/download/OIP-C (2).jpg');
INSERT INTO `user` VALUES (10, 'hhhh', 'hhhh', 'sdafsad', 1, 23, 2, '16222222223', 'http://localhost:8080/files/download/u=1513626988,10.jpg');
INSERT INTO `user` VALUES (11, '5454', '2424', '564567', 0, 20, 2, '16455555556', 'http://localhost:8080/files/download/u=1090776131,1528510745&fm=253&gp=0.jpg');
INSERT INTO `user` VALUES (12, '2424', '245', '2412412', 1, 45, 2, '15214555555', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (13, '9969', '455578', '1234567', 0, 55, 2, '16888885555', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (14, '223', '555', '888888', 1, 5, 2, '18558585858', 'http://localhost:8080/files/download/tx.png');
INSERT INTO `user` VALUES (19, '555', '555', '555', 0, 5, 2, '55555555555', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (20, '555', '5566', '555555', 0, 23, 2, '13999999999', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (21, 'aaa', 'aaa', 'aaa', 1, 5, 2, '6969696969', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (22, '696969', 'kakaluo', '123456', 1, 12, 1, '13366666666', 'http://localhost:8080/files/download/OIP-C.jpg');
INSERT INTO `user` VALUES (23, '222', '急急急', '222222', 1, 26, 1, '13222211111', 'http://localhost:8080/files/download/1668419337598639.jpg');
INSERT INTO `user` VALUES (24, '45555', '2555555', '55555', 1, 5, 2, '55555555555', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (25, 'aaaaaaaaaa', 'ssss', '123456', 1, 52, 1, '12555555555', 'http://localhost:8080/files/download/OIP-C (1).jpg');
INSERT INTO `user` VALUES (26, '111', '11', '111111', 1, 1, 1, '12777777777', 'http://localhost:8080/files/download/1668419337598639.jpg');
INSERT INTO `user` VALUES (27, '555', '4444', '555555', 1, 33, 1, '13666666666', 'http://localhost:8080/files/download/OIP-C (1).jpg');
INSERT INTO `user` VALUES (28, '66666', '11', '55555555', 1, 11, 1, '12565656565', 'http://localhost:8080/files/download/tx.png');
INSERT INTO `user` VALUES (29, '3333', '33333', '333333', 1, 33, 2, '18333333333', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (30, '666666', '666666', '666666', 1, 6, 2, '16666666666', 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (31, '777777', '', '777777', 1, 0, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (32, '888888', '', '888888', 1, 0, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (33, '999999', '', '999999', 1, 0, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (34, '555555', '', '555555', 1, 0, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (35, '333333', '', '333333', 1, 0, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');
INSERT INTO `user` VALUES (36, '9999', '999999', '999999', 0, 20, 1, NULL, 'http://localhost:8080/files/download/OIP.jpg');

SET FOREIGN_KEY_CHECKS = 1;
