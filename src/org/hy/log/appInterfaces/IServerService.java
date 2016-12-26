package org.hy.log.appInterfaces;





/**
 * 服务运行信息的接口
 * 
 * 接口服务端: 我方
 * 接口客户端: 通用
 * 接口协议为: 通用
 * 对方联调人: -
 * 相关文档有: -
 * 
 * @author      ZhengWei(HY)、WangZhanBin
 * @createDate  2014-11-14
 * @version     v1.0  
 */
public interface IServerService
{
    
    /**
     * 查看服务信息及判断服务是否存活
     * 
     * 接口的编号: Order.Server.A001
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-14
     * @version     v1.0
     *
     * @return
     */
    public String info();
    
    
    
    /**
     * 查看服务的接口错误列表
     * 
     * 接口的编号: Order.Server.A002
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     */
    public String showError();
    
    
    
    /**
     * 查看服务提供的接口列表
     * 
     * 接口的编号: Order.Server.A003
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-19
     * @version     v1.0
     *
     * @return
     */
    public String showInterface();
    
    
    
    /**
     * 初始化数据库对象
     * 
     * 接口的编号: Order.Server.A004
     * 
     * 这应当是个安全的接口。只有探测到表不存在时，才会创建表。
     * 由脚本编写者来保证没有Drop、Delete、Update语句，只应有Create、Insert语句
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-28
     * @version     v1.0
     *
     * @return
     */
    public String initDB();
    
    
    
    /**
     * 获取接口的输入参数信息
     * 
     * 接口的编号: Order.Server.A005
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-01
     * @version     v1.0
     *
     * @param i_InterfaceNo  接口对外的访问编号
     * @return
     */
    public String showInterfaceParam(String i_InterfaceNo);
    
    
    
    /**
     * 获取接口的返回输出参数信息
     * 
     * 接口的编号: Order.Server.A006
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-24
     * @version     v1.0
     *
     * @param i_InterfaceNo  接口对外的访问编号
     * @return
     */
    public String showInterfaceReturn(String i_InterfaceNo);
    
    
    
    /**
     * 获取接口访问量的概要统计数据 
     * 
     * 接口的编号: Order.Server.A007
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @return
     */
    public String showTotal();
    
    
    
    /**
     * 获取数据库访问量的概要统计数据 
     * 
     * 接口的编号: Order.Server.A008
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-04
     * @version     v1.0
     *
     * @return
     */
    public String showTotalDB();
    
    
    
    /**
     * 获取错误日志
     * 
     * 接口的编号: Order.Server.A009
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-08
     * @version     v1.0
     *
     * @param i_InterfaceNo  接口对外的访问编号
     * @return
     */
    public String showErrorLog(String i_InterfaceNo);
    
}
