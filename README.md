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
   
5. 接口通讯签名：可对接口加签名验证，防止接口报文数据被捕获后模拟发包。可分为如下两种类型：

    5.1 系统级粒度：系统下所有接口均使用相同的密钥签名。
    
    5.2 接口级粒度：每个接口都可设置不同的密钥签名。
    
    5.3 可通过数据库TMessageKeys表进行配置。表中密钥，也是[定时缓存](WebContent/WEB-INF/job.Config.xml)的，见job.Config.xml中的"缓存消息密钥"。

6. 接口及监控页面(默认密码logweb)

➢ 分析中心页面 http://127.0.0.1:8080/LogWeb/analyses

➢ 接口API页面 http://127.0.0.1:8080/LogWeb/services/server/showInterface

➢ 接口访问量页面 http://127.0.0.1:8080/LogWeb/services/server/showTotal

➢ 数据库SQL执行情况的监控页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseDB

➢ 数据库SQL组执行情况的监控页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseDB?type=Group

➢ 配置文件重新加载页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject

➢ 查看集群服务及状态列表 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?cluster=Y

➢ SQL执行日志页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBusway

➢ SQL执行异常的日志页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=$SQLBuswayError

➢ 查看配置参数页面 http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=SYSParam

➢ 手工执行SQL组页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=GXSQL_MailTime

➢ 手工执行定时任务页面(支持集群) http://127.0.0.1:8080/LogWeb/analyses/analyseObject?xid=JOB_CacheMessageKey



=====
#### 客户端调用演示代码
```java
	import java.net.URLDecoder;
	import java.util.Hashtable;
	import java.util.Map;
	
	import com.sun.jersey.api.client.Client;
	import com.sun.jersey.api.client.ClientResponse;
	import com.sun.jersey.api.client.WebResource;
	
	
	
	/**
	 * Rest服务请求方法
	 * 
	 * @author      ZhengWei(HY)
	 * @createDate  2014-12-30
	 * @version     v1.0
	 */
	public class BaseAppMessage
	{
	    
	    private static Client                   $Client       = Client.create();
	    
	    private static Map<String ,WebResource> $WebResources = new Hashtable<String ,WebResource>();
	    
	    
	    
	    /**
	     * Rest服务请求方法
	     * 
	     * @author      ZhengWei(HY)
	     * @createDate  2014-12-30
	     * @version     v1.0
	     *
	     * @param i_URL
	     * @param i_Data
	     * @param i_DataECode  响应结果的字符类型。如UTF-8
	     * @return
	     */
	    public static String restRequest(String i_URL ,Object i_Data ,String i_DataECode)
	    {
	        String v_Result = null;
	        
	        try 
	        {
	            WebResource v_WebResource = null;
	            synchronized ( BaseAppMessage.class )
	            {
	                v_WebResource = $WebResources.get(i_URL);
	                if ( v_WebResource == null )
	                {
	                    v_WebResource = $Client.resource(i_URL);
	                    $WebResources.put(i_URL ,v_WebResource);
	                }
	            }
	            
	            ClientResponse v_Response    = v_WebResource.post(ClientResponse.class ,i_Data);
	            
	            v_Result = v_Response.getEntity(String.class);
	            v_Result = URLDecoder.decode(v_Result ,i_DataECode);
	        } 
	        catch (Exception exce) 
	        {
	            exce.printStackTrace();
	        }
	        
	        return v_Result;
	    }
	    
	}
	
	
	
	public static void main(String [] args)
	{
		// 注册普通单表
		BaseAppMessage.restRequest("http://IP:Port/LogWeb/services/log/register" ,"Json报文。请参见testScript/restful/Log.Register.A001.注册系统.01.普通单表.xml中的Json字符串" ,"UTF-8");
		
		// 记录日志
		BaseAppMessage.restRequest("http://IP:Port/LogWeb/services/log/log"      ,"Json报文。请参见testScript/restful/Log.Create.A001.记录日志.001.xml中的Json字符串"          ,"UTF-8");
	}
```


---
#### 本项目引用Jar包，其源码链接如下
引用 https://github.com/HY-ZhengWei/hy.common.base 类库

引用 https://github.com/HY-ZhengWei/hy.common.berkeley 类库

引用 https://github.com/HY-ZhengWei/hy.common.db 类库

引用 https://github.com/HY-ZhengWei/hy.common.file 类库

引用 https://github.com/HY-ZhengWei/hy.common.mail 类库

引用 https://github.com/HY-ZhengWei/hy.common.net 类库

引用 https://github.com/HY-ZhengWei/hy.common.redis 类库

引用 https://github.com/HY-ZhengWei/hy.common.tpool 类库

引用 https://github.com/HY-ZhengWei/XJava 类库
