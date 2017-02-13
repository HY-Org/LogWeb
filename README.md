LogWeb

高性能在线记录日志系统（支持集群管理）



1. 初始数据库：日志系统在设计初就考虑到了方便部署的方案。可分为三步。

    第一步：配置好数据库连接信息(db.DB.config.xml)，只须一个空的数据库即可。

    第二步：运行日志服务。就是启动Tomcat了。

    第三步：浏览器中访问 http://127.0.0.1:8080/LogWeb/services/server/initDB 页面，片刻之后数据库所有对象及初始数据就建立好了。

2. 日志接口服务：采用Restful，客户端可以使用各种语言（比如 Java 程序、Perl、Ruby、Python、PHP 和 Javascript[包括 Ajax]）实现。

3. 服务间通讯：采用Socket TCP通讯协议。主端口为1921，浮动端口为17000~17999，均可在sys.ServerConfig.xml配置中修改。请保证服务器防火墙允许这些端口的通讯。

4. 故障恢复：采用Berkeley DB文件级数据库，即使关系性数据库异常无法访问时，也能保证日志数据不丢失。
   等关系性数据库正常后，重启本服务即可自动恢复日志数据并写入到数据库中。
   是否启用可以配置sys.config.xml中设置，开启后安全性提高，但性能所有下降。

5. 接口及监控页面(默认密码logweb)

➢ 接口API页面 http://127.0.0.1:8080/LogWeb/services/server/showInterface

➢ 接口访问量页面 http://127.0.0.1:8080/LogWeb/services/server/showTotal

➢ 数据库SQL执行情况的监控页面(支持集群) http://127.0.0.1:8080/LogWeb/services/analyseDB

➢ 数据库SQL组执行情况的监控页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseDB?type=Group

➢ 配置文件重新加载页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject

➢ 查看集群服务及状态列表 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?cluster=Y

➢ SQL执行日志页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBusway

➢ SQL执行异常的日志页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBuswayError

➢ 查看配置参数页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=SYSParam

➢ 手工执行SQL组页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=GXSQL_MailTime

➢ 手工执行定时任务页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=JOB_CacheMessageKey