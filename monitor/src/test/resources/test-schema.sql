-- -----------------created by hehongbo 2017-07-06 ----------------创建定时任务依赖数据库
CREATE DATABASE monitor;

USE monitor;

/* Create table in target */
CREATE TABLE `QRTZ_BLOB_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`BLOB_DATA` blob NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	KEY `SCHED_NAME`(`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	CONSTRAINT `QRTZ_BLOB_TRIGGERS_ibfk_1`
	FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_CALENDARS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`CALENDAR_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`CALENDAR` blob NOT NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_CRON_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`CRON_EXPRESSION` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TIME_ZONE_ID` varchar(80) COLLATE utf8mb4_general_ci NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	CONSTRAINT `QRTZ_CRON_TRIGGERS_ibfk_1`
	FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_FIRED_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`ENTRY_ID` varchar(95) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`INSTANCE_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`FIRED_TIME` bigint(13) NOT NULL  ,
	`SCHED_TIME` bigint(13) NOT NULL  ,
	`PRIORITY` int(11) NOT NULL  ,
	`STATE` varchar(16) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_NAME` varchar(200) COLLATE utf8mb4_general_ci NULL  ,
	`JOB_GROUP` varchar(200) COLLATE utf8mb4_general_ci NULL  ,
	`IS_NONCONCURRENT` varchar(1) COLLATE utf8mb4_general_ci NULL  ,
	`REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_general_ci NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`) ,
	KEY `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`,`INSTANCE_NAME`) ,
	KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) ,
	KEY `IDX_QRTZ_FT_J_G`(`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) ,
	KEY `IDX_QRTZ_FT_JG`(`SCHED_NAME`,`JOB_GROUP`) ,
	KEY `IDX_QRTZ_FT_T_G`(`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	KEY `IDX_QRTZ_FT_TG`(`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_JOB_DETAILS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`DESCRIPTION` varchar(250) COLLATE utf8mb4_general_ci NULL  ,
	`JOB_CLASS_NAME` varchar(250) COLLATE utf8mb4_general_ci NOT NULL  ,
	`IS_DURABLE` varchar(1) COLLATE utf8mb4_general_ci NOT NULL  ,
	`IS_NONCONCURRENT` varchar(1) COLLATE utf8mb4_general_ci NOT NULL  ,
	`IS_UPDATE_DATA` varchar(1) COLLATE utf8mb4_general_ci NOT NULL  ,
	`REQUESTS_RECOVERY` varchar(1) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_DATA` blob NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) ,
	KEY `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`,`REQUESTS_RECOVERY`) ,
	KEY `IDX_QRTZ_J_GRP`(`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_LOCKS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`LOCK_NAME` varchar(40) COLLATE utf8mb4_general_ci NOT NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_SCHEDULER_STATE`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`INSTANCE_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`LAST_CHECKIN_TIME` bigint(13) NOT NULL  ,
	`CHECKIN_INTERVAL` bigint(13) NOT NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`REPEAT_COUNT` bigint(7) NOT NULL  ,
	`REPEAT_INTERVAL` bigint(12) NOT NULL  ,
	`TIMES_TRIGGERED` bigint(10) NOT NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_ibfk_1`
	FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`STR_PROP_1` varchar(512) COLLATE utf8mb4_general_ci NULL  ,
	`STR_PROP_2` varchar(512) COLLATE utf8mb4_general_ci NULL  ,
	`STR_PROP_3` varchar(512) COLLATE utf8mb4_general_ci NULL  ,
	`INT_PROP_1` int(11) NULL  ,
	`INT_PROP_2` int(11) NULL  ,
	`LONG_PROP_1` bigint(20) NULL  ,
	`LONG_PROP_2` bigint(20) NULL  ,
	`DEC_PROP_1` decimal(13,4) NULL  ,
	`DEC_PROP_2` decimal(13,4) NULL  ,
	`BOOL_PROP_1` varchar(1) COLLATE utf8mb4_general_ci NULL  ,
	`BOOL_PROP_2` varchar(1) COLLATE utf8mb4_general_ci NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_ibfk_1`
	FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';


/* Create table in target */
CREATE TABLE `QRTZ_TRIGGERS`(
	`SCHED_NAME` varchar(120) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_NAME` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`JOB_GROUP` varchar(200) COLLATE utf8mb4_general_ci NOT NULL  ,
	`DESCRIPTION` varchar(250) COLLATE utf8mb4_general_ci NULL  ,
	`NEXT_FIRE_TIME` bigint(13) NULL  ,
	`PREV_FIRE_TIME` bigint(13) NULL  ,
	`PRIORITY` int(11) NULL  ,
	`TRIGGER_STATE` varchar(16) COLLATE utf8mb4_general_ci NOT NULL  ,
	`TRIGGER_TYPE` varchar(8) COLLATE utf8mb4_general_ci NOT NULL  ,
	`START_TIME` bigint(13) NOT NULL  ,
	`END_TIME` bigint(13) NULL  ,
	`CALENDAR_NAME` varchar(200) COLLATE utf8mb4_general_ci NULL  ,
	`MISFIRE_INSTR` smallint(2) NULL  ,
	`JOB_DATA` blob NULL  ,
	PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) ,
	KEY `IDX_QRTZ_T_J`(`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) ,
	KEY `IDX_QRTZ_T_JG`(`SCHED_NAME`,`JOB_GROUP`) ,
	KEY `IDX_QRTZ_T_C`(`SCHED_NAME`,`CALENDAR_NAME`) ,
	KEY `IDX_QRTZ_T_G`(`SCHED_NAME`,`TRIGGER_GROUP`) ,
	KEY `IDX_QRTZ_T_STATE`(`SCHED_NAME`,`TRIGGER_STATE`) ,
	KEY `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) ,
	KEY `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) ,
	KEY `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`,`NEXT_FIRE_TIME`) ,
	KEY `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) ,
	KEY `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) ,
	KEY `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) ,
	KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) ,
	CONSTRAINT `QRTZ_TRIGGERS_ibfk_1`
	FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `QRTZ_JOB_DETAILS` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET='utf8mb4' COLLATE='utf8mb4_general_ci';
