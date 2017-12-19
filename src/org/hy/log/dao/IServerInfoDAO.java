package org.hy.log.dao;

import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.log.model.DBTableInfo;





/**
 * 服务运行信息的DAO层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-28
 * @version     v1.0
 */
@Xjava(id="ServerInfoDAO" ,value=XType.XSQL)
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
    @Xsql("XSQL_God")
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
    @Xsql("XSQL_DBTableInfo_Query")
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
    @Xsql(id="XSQL_DBTableInfo_QueryByName" ,returnOne=true)
    public DBTableInfo isExists(@Xparam("tableName") String i_TableName);
    
}
