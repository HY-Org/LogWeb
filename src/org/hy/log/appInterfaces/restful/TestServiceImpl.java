package org.hy.log.appInterfaces.restful;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.hy.log.common.BaseAppMessage;
import org.hy.log.appInterfaces.ITestService;
import org.hy.common.Date;





/**
 * 测试服务
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: Restful
 * 对方联调人: -
 * 相关文档有: -
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0  
 */
@Path("test")
public class TestServiceImpl extends BaseAppMessage implements ITestService
{

    /**
     * 测试服务
     * 
     * 接口的编号: Order.Test.A001
     */
    @Path("hello")
    @POST
    @Produces("text/plain")
    public String TestService(String i_MsgInfo)
    {
        return Date.getNowTime().getFull() + " : Hello World! " + i_MsgInfo;
    }

}