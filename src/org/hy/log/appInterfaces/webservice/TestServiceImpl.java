package org.hy.log.appInterfaces.webservice;

import org.hy.log.appInterfaces.ITestService;





/**
 * 测试服务
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: WebService
 * 对方联调人: -
 * 相关文档有: -
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-12
 * @version     v1.0  
 */
public class TestServiceImpl implements ITestService
{

    /**
     * 测试服务
     * 
     * 接口的编号: Order.Test.A0001
     */
    @Override
    public String TestService(String i_MsgInfo)
    {
        return "HY";
    }
    
}
