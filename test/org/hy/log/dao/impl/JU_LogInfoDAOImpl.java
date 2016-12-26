package org.hy.log.dao.impl;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.hy.log.common.BaseJunit;
import org.hy.log.dao.ILogRegisterDAO;





/**
 * 测试单元：业务操作日志DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2014-12-13
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_LogInfoDAOImpl extends BaseJunit
{
    private ILogRegisterDAO logRegisterDAO = (ILogRegisterDAO)this.getObject("LogRegisterDAO");
    
    
    
    @Test
    public void registerCreateTable()
    {
        this.logRegisterDAO.createTable("HY");
    }
    
}
