package org.hy.log.model;

import org.hy.common.app.Param;





/**
 * 项目版本信息，及对应的数据库脚本的版本信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-06
 * @version     v1.0
 */
public class Version extends Param
{
    
    /** 数据库脚本的版本 */
    private String dbVersion;

    
    
    /**
     * 获取：数据库脚本的版本
     */
    public String getDbVersion()
    {
        return dbVersion;
    }

    
    
    /**
     * 设置：数据库脚本的版本
     * 
     * @param dbVersion 
     */
    public void setDbVersion(String dbVersion)
    {
        this.dbVersion = dbVersion;
    }
    
}
