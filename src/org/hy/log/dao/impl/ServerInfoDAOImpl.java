package org.hy.log.dao.impl;

import java.util.List;
import java.util.Map;

import org.hy.log.common.BaseDAO;
import org.hy.log.dao.IServerInfoDAO;
import org.hy.log.model.DBTableInfo;

import org.hy.common.Help;
import org.hy.common.xml.annotation.Xjava;





/**
 * 服务运行信息的DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-28
 * @version     v1.0
 */
@Xjava(id="ServerInfoDAO")
public class ServerInfoDAOImpl extends BaseDAO implements IServerInfoDAO
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
    public boolean execute(String i_SQL)
    {
        try
        {
            this.getXSQL("XSQL_God").execute(i_SQL);
        }
        catch (Exception exce)
        {
            return false;
        }
        
        return true;
    }
    
    
    
    /**
     * 获取数据库中所有表的元数据信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-29
     * @version     v1.0
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String ,DBTableInfo> getTables()
    {
        Map<String ,DBTableInfo> v_Ret = null;
        
        try
        {
            v_Ret = (Map<String ,DBTableInfo>)this.getXSQL("XSQL_DBTableInfo_Query").query();
        }
        catch (Exception exce)
        {
            // Nothing.
        }
        
        return v_Ret;
    }
    
    
    
    
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
    @SuppressWarnings("unchecked")
    public boolean isExists(String i_TableName)
    {
        List<DBTableInfo> v_Ret = null;
        
        try
        {
            v_Ret = (List<DBTableInfo>)this.getXSQL("XSQL_DBTableInfo_QueryByName").query(new DBTableInfo(i_TableName));
            
            return !Help.isNull(v_Ret);
        }
        catch (Exception exce)
        {
            // Nothing.
        }
        
        return false;
    }
    
}
