package org.hy.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.log.common.BaseService;
import org.hy.log.dao.IServerInfoDAO;
import org.hy.log.model.DBTableInfo;
import org.hy.log.service.IServerInfoService;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;





/**
 * 服务运行信息的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-28
 * @version     v1.0
 */
@Xjava
public class ServerInfoService extends BaseService implements IServerInfoService
{
    
    /** 服务运行信息的DAO层 */
    @Xjava
    private IServerInfoDAO serverInfoDAO;
    
    

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
     *
     * @see org.hy.log.service.IServerInfoService#initDB()
     */
    @Override
    public List<Return<String>> initDB()
    {
        List<Return<String>>     v_Rets       = new ArrayList<Return<String>>();
        String []                v_SQLs       = null;
        Map<String ,DBTableInfo> v_OldTables  = null;
        
        try
        {
            v_OldTables = this.serverInfoDAO.getTables();
            if ( v_OldTables == null )
            {
                // 数据配置或访问异常返回
                return v_Rets;
            }
            
            v_SQLs = this.getFileContent("db/db.InitDB.v" + this.getDBVersion() + "." + this.getDataSourceType().toLowerCase() + ".sql").split(";");
            
            for (String v_SQL : v_SQLs)
            {
                String         v_TableName  = StringHelp.getString(v_SQL.toUpperCase() ,"CREATE TABLE " ,"\\(");
                Return<String> v_Ret        = new Return<String>();
                boolean        v_IsValidate = false;
                
                if ( Help.isNull(v_TableName) )
                {
                    // 去除所有相连的两个空格，变为一个空格
                    v_SQL = v_SQL.trim();
                    while ( v_SQL.indexOf("  ") >= 0 )
                    {
                        v_SQL = v_SQL.trim().replaceAll("  " ," ");
                    }
                    
                    String [] v_SQLSplit = v_SQL.split(" ");
                    v_TableName = v_SQLSplit[2];
                    
                    if ( "ALTER".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        if ( "TABLE".equalsIgnoreCase(v_SQLSplit[1]) )
                        {
                            v_Ret.paramStr(v_TableName);
                            v_IsValidate = true;
                            
                            if ( v_SQL.toUpperCase().indexOf(" PRIMARY ") > 0 )
                            {
                                v_Ret.paramObj("操作主键");
                            }
                            else if ( v_SQL.toUpperCase().indexOf(" INDEX ") > 0 )
                            {
                                v_Ret.paramObj("操作索引");
                            }
                            else if ( v_SQL.toUpperCase().indexOf(" FOREIGN ") > 0 )
                            {
                                v_Ret.paramObj("操作外键");
                            }
                            else
                            {
                                v_Ret.paramObj("未知操作");
                            }
                        }
                        else
                        {
                            v_Ret.paramStr("");
                            v_Ret.paramObj("其它结构更新");
                        }
                    }
                    else if ( "CREATE".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        // SQL Server 创建索引
                        if ( "INDEX".equalsIgnoreCase(v_SQLSplit[2]) )
                        {
                            v_IsValidate = true;
                            v_Ret.paramStr(v_SQLSplit[5]);
                            v_Ret.paramObj("操作索引");
                        }
                    }
                    else if ( "INSERT".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        v_Ret.paramStr(v_TableName);
                        v_Ret.paramObj("插入记录");
                    }
                    else if ( "DROP".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        v_Ret.paramStr(v_TableName);
                        v_Ret.paramObj("删除表");
                    }
                    else if ( "DELETE".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        if ( !"FROM".equalsIgnoreCase(v_SQLSplit[1]) )
                        {
                            v_TableName = v_SQLSplit[1];
                        }
                        
                        v_Ret.paramStr(v_TableName);
                        v_Ret.paramObj("删除记录");
                    }
                    else if ( "UPDATE".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        if ( !"FROM".equalsIgnoreCase(v_SQLSplit[1]) )
                        {
                            v_TableName = v_SQLSplit[1];
                        }
                        
                        v_Ret.paramStr(v_TableName);
                        v_Ret.paramObj("更新记录");
                    }
                    else if ( "SELECT".equalsIgnoreCase(v_SQLSplit[0]) )
                    {
                        v_Ret.paramStr("");
                        v_Ret.paramObj("查询操作");
                    }
                    else
                    {
                        v_Ret.paramStr("");
                        v_Ret.paramObj("其它操作");
                    }
                }
                else
                {
                    v_Ret.paramStr(v_TableName.substring(0 ,v_TableName.length() - 1).substring(13).trim());
                    v_Ret.paramObj("创建表");
                    v_IsValidate = true;
                }
                
                v_Ret.paramStr(v_Ret.paramStr.toUpperCase());
                
                if ( v_IsValidate )
                {
                    if ( !v_OldTables.containsKey(v_Ret.paramStr) )
                    {
                        v_Ret.paramInt(1);
                        v_Ret.set(this.serverInfoDAO.execute(v_SQL));
                    }
                    else
                    {
                        v_Ret.paramInt(0);
                    }
                }
                else
                {
                    v_Ret.paramInt(1);
                    v_Ret.set(this.serverInfoDAO.execute(v_SQL));  
                }
                
                v_Rets.add(v_Ret);
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return v_Rets;
    }

    

    /**
     * 获取：服务运行信息的DAO层
     */
    public IServerInfoDAO getServerInfoDAO()
    {
        return serverInfoDAO;
    }

    

    /**
     * 设置：服务运行信息的DAO层
     * 
     * @param serverInfoDAO 
     */
    public void setServerInfoDAO(IServerInfoDAO serverInfoDAO)
    {
        this.serverInfoDAO = serverInfoDAO;
    }
    
}
