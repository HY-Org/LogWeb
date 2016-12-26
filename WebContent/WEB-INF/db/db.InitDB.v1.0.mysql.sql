CREATE TABLE TLog
(
  id                           VARCHAR(64)        NOT NULL
 ,sysID                        VARCHAR(16)        NOT NULL
 ,logID                        VARCHAR(64)
 ,logType                      VARCHAR(1000)
 ,logClass                     VARCHAR(1000)
 ,logContent                   TEXT
 ,logInfo                      TEXT
 ,operatorNo                   VARCHAR(64)
 ,operationType                VARCHAR(64)
 ,operationRemark              VARCHAR(1000)
 ,operationTime                DATETIME          NOT NULL 
 ,waitTime                     INT(10)           NOT NULL
);


ALTER TABLE TLog                                 ADD PRIMARY KEY (id);

ALTER TABLE TLog                                 ADD INDEX (sysID ,logID);
  
ALTER TABLE TLog                                 ADD INDEX (operationTime);





CREATE TABLE TLogRegister
(
  sysID                        VARCHAR(16)        NOT NULL
 ,registerType                 VARCHAR(16)        NOT NULL
 ,dbBuildTime                  DATETIME           
 ,createTime                   DATETIME           NOT NULL
 ,updateTime                   DATETIME 
);


ALTER TABLE TLogRegister                          ADD PRIMARY KEY (sysID);





CREATE TABLE TMessageKey
(
  sysid                        VARCHAR(32)        NOT NULL
 ,sid                          VARCHAR(32)
 ,msgKey                       VARCHAR(64)        NOT NULL
);





CREATE TABLE TMailTime
(
  ID                           INT(10)            NOT NULL AUTO_INCREMENT
 ,Title                        VARCHAR(320)       NOT NULL
 ,Content                      VARCHAR(4000)      NOT NULL
 ,Reciver                      VARCHAR(320)       NOT NULL
 ,SendTime                     DATETIME           NOT NULL
 ,PRIMARY KEY(ID)
);
