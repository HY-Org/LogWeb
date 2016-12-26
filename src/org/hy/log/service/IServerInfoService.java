package org.hy.log.service;

import java.util.List;

import org.hy.common.Return;





/**
 * 服务运行信息的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-28
 * @version     v1.0
 */
public interface IServerInfoService
{
    
    /**
     * 初始化数据库对象
     * 
     * 这应当是个安全的接口。只有探测到表不存在时，才会创建表。
     * 由脚本编写者来保证没有Drop、Delete、Update语句，只应有Create、Insert语句
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-28
     * @version     v1.0
     * 
     * @return  Return.int   表示是否执行语句。0：放弃执行  1：执行语句
     *          Return.str   表示执行语句的表名称
     *          Return.Obj   表示执行语句的类型
     */
    public List<Return<String>> initDB();
    
}
