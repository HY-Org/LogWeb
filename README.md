LogWeb

高性能在线记录日志系统（支持集群）



1. 初始数据库 日志系统在设计初就考虑到了方便部署的方案。可分为三步。

    第一步：配置好数据库连接信息(db.DB.config.xml)，只须一个空的数据库即可。

    第二步：运行日志服务。就是启动Tomcat了。

    第三步：浏览器中访问 http://127.0.0.1:8080/LogWeb/services/server/initDB 页面，片刻之后数据库所有对象及初始数据就建立好了。



2. 接口及监控页面

➢ 接口API页面 http://127.0.0.1:8080/LogWeb/services/server/showInterface

➢ 接口访问量页面 http://127.0.0.1:8080/LogWeb/services/server/showTotal

➢ 数据库SQL执行情况的监控页面 http://127.0.0.1:8080/LogWeb/services/analyseDB

➢ 数据库SQL组执行情况的监控页面 http://127.0.0.1:8080/LogWeb/analyses/analyseDB?type=Group

➢ 配置文件重新加载页面(默认密码logweb) http://127.0.0.1:8080/LogWeb/analyses/analyseObject

➢ SQL执行日志页面(默认密码logweb) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBusway

➢ SQL执行异常的日志页面(默认密码logweb) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBuswayError

➢ 查看配置参数页面(默认密码logweb) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=SYSParam

➢ 手工执行SQL组页面(默认密码logweb) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=GXSQL_MailTime