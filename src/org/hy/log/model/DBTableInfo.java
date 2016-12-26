package org.hy.log.model;

import org.hy.log.common.BaseModel;





/**
 * 数据库表的元数据信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-11-29
 * @version     v1.0
 */
public class DBTableInfo extends BaseModel
{

    private static final long serialVersionUID = -1916611295949978806L;
    
    
    /** 数据库表名称 */
    private String tableName;

    
    
    public DBTableInfo()
    {
        
    }
    
    
    
    public DBTableInfo(String i_TableName)
    {
        this.setTableName(i_TableName);
    }
    
    
    
    @Override
    public String getRowKey()
    {
        return this.tableName;
    }


    
    /**
     * 获取：数据库表名称
     */
    public String getTableName()
    {
        return tableName;
    }


    
    /**
     * 设置：数据库表名称
     * 
     * @param tableName 
     */
    public void setTableName(String tableName)
    {
        this.tableName = tableName.toUpperCase();
    }
    
}
