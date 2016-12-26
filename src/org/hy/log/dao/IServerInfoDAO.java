package org.hy.log.dao;

import java.util.Map;

import org.hy.log.model.DBTableInfo;





/**
 * 服务运行信息的DAO层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-28
 * @version     v1.0
 */
public interface IServerInfoDAO
{
    
    /**
     * 执行SQL
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-28
     * @version     v1.0
     *
     * @param i_SQL
     * @return
     */
    public boolean execute(String i_SQL);
    
    
    
    /**
     * 获取数据库中所有表的元数据信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-29
     * @version     v1.0
     *
     * @return
     */
    public Map<String ,DBTableInfo> getTables();
    
    
    
    /**
     * 判断表对象是否存在
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-12-14
     * @version     v1.0
     *
     * @param i_TableName
     * @return
     */
    public boolean isExists(String i_TableName);
    
}
