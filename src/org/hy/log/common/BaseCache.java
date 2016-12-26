package org.hy.log.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.TablePartitionRID;
import org.hy.common.redis.RData;
import org.hy.common.redis.Redis;
import org.hy.common.MethodReflect;
import org.hy.common.xml.XJava;





/**
 * Redis数据库一级缓存--基类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-10-10
 * @version     v1.0  
 */
public abstract class BaseCache<O extends BaseModel> extends Base
{
    
    /**
     * 获取数据库表名称
     * 
     * 建议此名称仅可能的短些
     * 
     * @return
     */
    protected abstract String getTableName();
    
    
    
    /**
     * 获取缓存对象的元类型
     * 
     * @return
     */
    protected abstract Class<?> getCacheClass();
    
    
    
    public BaseCache()
    {
        // 具体方法不再判断，这样是为了快。
        // 如果调用具体方法时，系统配置没有启用缓存，将会报空指针异常
        if ( this.isEnableCache() )
        {
            if ( Help.isNull(this.getTableName()) )
            {
                throw new NullPointerException("Table name is null.");
            }
            
            if ( !this.getRedis().isExistsTable(this.getTableName()) )
            {
                this.getRedis().createTable(this.getTableName());
            }
        }
    }
    
    
    
    protected Redis getRedis()
    {
        return (Redis)XJava.getObject("REDIS");
    }
    
    
    
    /**
     * 插入对象信息
     * 
     * @param i_BaseModel
     * @return            返回行主键。异常时返回 null
     */
    public String insert(O i_BaseModel)
    {
        if ( i_BaseModel == null )
        {
            throw new NullPointerException("BaseModel is null for Call insert() method.");
        }
        
        if ( Help.isNull(i_BaseModel.getRowKey()) )
        {
            throw new NullPointerException("BaseModel.RowKey is null for Call insert() method.");
        }
        
        List<String> v_RowKeys = this.getRedis().inserts(this.getTableName() ,this.toRDatas(i_BaseModel));
        if ( Help.isNull(v_RowKeys) )
        {
            return null;
        }
        else
        {
            return v_RowKeys.get(0);
        }
    }
    
    
    
    /**
     * 插入一组对象信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_BaseModels
     * @return              返回行主键。异常时返回 null
     */
    public List<String> insert(List<O> i_BaseModels)
    {
        if ( Help.isNull(i_BaseModels) )
        {
            throw new NullPointerException("BaseModels is null for Call insert() method.");
        }
        
        return this.getRedis().inserts(this.getTableName() ,this.toRDatas(i_BaseModels));
    }
    
    
    
    /**
     * 按主键信息获取信息
     * 
     * @param i_Key
     * @return
     */
    public O getRow(String i_Key)
    {
        if ( Help.isNull(i_Key) )
        {
            throw new NullPointerException("Key is null for Call getRow() method.");
        }
        
        return this.toCacheObject(this.getRedis().getRow(this.getTableName() ,i_Key));
    }
    
    
    
    /**
     * 按多个主键信息获取多个数据信息
     * 
     * @param i_Keys
     * @return        按入参顺序返回。当某信息查询不到时，List元素为null对象。
     */
    public List<O> getRows(List<String> i_Keys)
    {
        if ( Help.isNull(i_Keys) )
        {
            throw new NullPointerException("Keys is null for Call getRows() method.");
        }
        
        return toCacheObjects(this.getRedis().getRows(this.getTableName() ,i_Keys));
    }
    
    
    
    /**
     * 按多个主键信息获取多个数据信息(返回格式为Map集合)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_Keys
     * @return        Map.key   为行主键
     *                Map.Value 为一行数据信息的对象实例
     */
    public Map<String ,O> getRowsMap(List<String> i_Keys)
    {
        if ( Help.isNull(i_Keys) )
        {
            throw new NullPointerException("Keys is null for Call getRowMaps() method.");
        }
        
        return toCacheObjectsMap(this.getRedis().getRows(this.getTableName() ,i_Keys));
    }
    
    
    
    /**
     * 按多个主键信息获取多个数据信息(返回格式为TablePartitionRID集合)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_Keys
     * @param i_PName
     * @param i_IndexName
     * @return        TablePartitionRID.key   为指定的分区字段值
     *                TablePartitionRID.index 为行中索引名称指定的字段值
     *                TablePartitionRID.Value 为一行数据信息的对象实例
     */
    public TablePartitionRID<String ,O> getRowsMap(List<String> i_Keys ,String i_PName ,String i_IndexName)
    {
        if ( Help.isNull(i_Keys) )
        {
            throw new NullPointerException("Keys is null for Call getRowMaps() method.");
        }
        
        if ( Help.isNull(i_PName) )
        {
            throw new NullPointerException("PName is null for Call getRowMaps() method.");
        }
        
        if ( Help.isNull(i_IndexName) )
        {
            throw new NullPointerException("IndexName is null for Call getRowMaps() method.");
        }
        
        return toCacheObjectsMap(this.getRedis().getRows(this.getTableName() ,i_Keys) ,i_PName.trim() ,i_IndexName.trim());
    }
    
    
    
    /**
     * 按主键信息获取信息
     * 
     * @param i_Key
     * @return
     */
    public O getRowByKey(O i_BaseModel)
    {
        if ( i_BaseModel == null )
        {
            throw new NullPointerException("BaseModel is null for Call getRowByKey() method.");
        }
        
        return this.getRow(i_BaseModel.getRowKey());
    }
    
    
    
    /**
     * 按主键信息获取信息
     * 
     * @param i_Key
     * @return
     */
    public List<O> getRowsByKeys(List<O> i_BaseModels)
    {
        if ( Help.isNull(i_BaseModels) )
        {
            throw new NullPointerException("i_BaseModels is null for Call getRowsByKeys() method.");
        }
        
        return this.getRows(this.toRowKeys(i_BaseModels));
    }
    
    
    
    /**
     * 删除多行数据
     * 
     * @param i_Keys
     */
    public void deleteRow(String ... i_Keys)
    {
        if ( Help.isNull(i_Keys) )
        {
            throw new NullPointerException("Keys is null for Call delete() method.");
        }
        
        this.getRedis().deleteRow(this.getTableName() ,i_Keys);
    }
    
    
    
    /**
     * 删除多行数据
     * 
     * @param i_BaseModels
     */
    public void deleteRow(List<O> i_BaseModels)
    {
        if ( Help.isNull(i_BaseModels) )
        {
            throw new NullPointerException("BaseModels is null for Call delete() method.");
        }
        
        this.getRedis().deleteRow(this.getTableName() ,this.toRowKeys(i_BaseModels));
    }
    
    
    
    /**
     * 删除多行数据
     * 
     * @param i_BaseModels
     */
    @SuppressWarnings("unchecked")
    public void deleteRow(O ... i_BaseModels)
    {
        if ( Help.isNull(i_BaseModels) )
        {
            throw new NullPointerException("BaseModels is null for Call delete() method.");
        }
        
        this.getRedis().deleteRow(this.getTableName() ,this.toRowKeys(i_BaseModels));
    }
    
    
    
    /**
     * 智能更新一行数据有变化的信息
     * 
     * @param i_BaseModel
     * @return             返回更新之前老的对象数据
     *                     当验证异常时，返回null
     */
    public O update(O i_BaseModel)
    {
        if ( i_BaseModel == null )
        {
            return null;
        }
        
        if ( Help.isNull(i_BaseModel.getRowKey()) )
        {
            return null;
        }
        
        O v_Old = this.getRow(i_BaseModel.getRowKey());
        if ( v_Old == null || Help.isNull(v_Old.getRowKey()) )
        {
            return null;
        }
        
        this.getRedis().update(this.getTableName() ,this.toRDatas_OnlyChange(i_BaseModel ,v_Old));

        return v_Old;
    }
    
    
    
    /**
     * 智能更新一行数据有变化的信息
     * 
     * @param i_BaseModels
     * @return             返回更新之前老的对象数据
     *                     当验证异常时，返回null
     */
    public List<O> update(List<O> i_BaseModels)
    {
        if ( Help.isNull(i_BaseModels) )
        {
            return null;
        }
        
        List<O> v_Olds = this.getRowsByKeys(i_BaseModels);
        if ( Help.isNull(v_Olds) || v_Olds.size() != i_BaseModels.size() )
        {
            return null;
        }
        
        for (O v_BaseModel : v_Olds)
        {
            if ( v_BaseModel == null )
            {
                return null;
            }
        }
        
        this.getRedis().update(this.getTableName() ,this.toRDatas_OnlyChange(i_BaseModels ,v_Olds));

        return v_Olds;
    }
    
    
    
    @SuppressWarnings("unchecked")
    protected O newCacheObject()
    {
        try
        {
            return (O) this.getCacheClass().newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    /**
     * 转为缓存对象
     * 
     * @param i_Datas
     */
    protected O toCacheObject(Map<String ,String> i_Datas)
    {
        O v_Ret = null;
        
        if ( !Help.isNull(i_Datas) )
        {
            Iterator<String> v_Iter = i_Datas.keySet().iterator();
            v_Ret  = this.newCacheObject();
            
            while ( v_Iter.hasNext() )
            {
                String v_Key = v_Iter.next();
                
                try
                {
                    Object v_Value = i_Datas.get(v_Key);
                    if ( v_Value != null && !Help.isNull(v_Value.toString()) )
                    {
                        Method v_Method = MethodReflect.getSetMethod(this.getCacheClass() ,v_Key ,true);
                        
                        if ( v_Method != null )
                        {
                            v_Method.invoke(v_Ret ,Help.toObject(v_Method.getParameterTypes()[0] ,v_Value.toString()));
                        }
                    }
                }
                catch (Exception exce)
                {
                    exce.printStackTrace();
                }
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 转为缓存对象集合
     * 
     * @param i_Datas
     */
    protected List<O> toCacheObjects(Map<String ,Map<String ,String>> i_Rows)
    {
        List<O> v_Ret = null;
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = new ArrayList<O>(0);
        }
        else
        {
            v_Ret = new ArrayList<O>(i_Rows.size());
            
            for (Map<String ,String> v_Datas : i_Rows.values())
            {
                v_Ret.add(this.toCacheObject(v_Datas));
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 转为缓存对象集合(返回格式为Map集合)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-18
     * @version     v1.0
     *
     * @param i_Rows  
     * @return        Map.key   为行主键
     *                Map.Value 为一行数据信息的对象实例
     */
    protected Map<String ,O> toCacheObjectsMap(Map<String ,Map<String ,String>> i_Rows)
    {
        LinkedHashMap<String ,O> v_Ret = null;
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = new LinkedHashMap<String ,O>(0);
        }
        else
        {
            v_Ret = new LinkedHashMap<String ,O>(i_Rows.size());
            
            for (Map<String ,String> v_Datas : i_Rows.values())
            {
                O v_Obj = this.toCacheObject(v_Datas);
                v_Ret.put(v_Obj.getRowKey() ,v_Obj);
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 转为缓存对象集合(返回格式为Map集合)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2014-11-20
     * @version     v1.0
     *
     * @param i_Rows  
     * @param i_PName
     * @param i_IndexName
     * @return        TablePartitionRID.key   为行主键
     *                TablePartitionRID.index 为行中索引名称指定的字段值
     *                TablePartitionRID.Value 为一行数据信息的对象实例
     */
    protected TablePartitionRID<String ,O> toCacheObjectsMap(Map<String ,Map<String ,String>> i_Rows ,String i_PName ,String i_IndexName)
    {
        TablePartitionRID<String ,O> v_Ret = null;
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = new TablePartitionRID<String ,O>(0);
        }
        else
        {
            v_Ret = new TablePartitionRID<String ,O>(i_Rows.size());
            
            for (Map<String ,String> v_Datas : i_Rows.values())
            {
                O      v_Obj        = this.toCacheObject(v_Datas);
                String v_PValue     = v_Datas.get(i_PName);
                String v_IndexValue = v_Datas.get(i_IndexName);
                
                if ( v_PValue == null )
                {
                    throw new NullPointerException("PValue is null for RowKey = '" + v_Obj.getRowKey() + "'.");
                }
                
                if ( v_IndexValue == null )
                {
                    throw new NullPointerException("IndexValue is null for RowKey = '" + v_Obj.getRowKey() + "'.");
                }
                
                v_Ret.putRow(v_PValue ,v_IndexValue ,v_Obj);
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 将对象转为RData的集合
     * 
     * @param i_BaseModel
     * @return
     */
    protected List<RData> toRDatas(O i_BaseModel)
    {
        List<RData> v_RDatas = new ArrayList<RData>();
        
        if ( !Help.isNull(i_BaseModel.getRowKey()) )
        {
            for (int i=0; i<i_BaseModel.gatPropertySize(); i++)
            {
                Object v_Value = i_BaseModel.gatPropertyValue(i);
                if ( v_Value != null )
                {
                    if ( v_Value instanceof List 
                      || v_Value instanceof Map
                      || v_Value instanceof BaseModel )
                    {
                        continue;
                    }
                    
                    RData  v_RData = new RData();
                    
                    v_RData.setKey(  i_BaseModel.getRowKey());
                    v_RData.setField(i_BaseModel.gatPropertyShortName(i));
                    v_RData.setValue(this.getValue(v_Value).toString());
                    
                    v_RDatas.add(v_RData);
                }
            }
        }
        
        return v_RDatas;
    }
    
    
    
    /**
     * 将对象集合转为RData的集合
     * 
     * @param i_BaseModels
     * @return
     */
    protected List<RData> toRDatas(List<O> i_BaseModels)
    {
        List<RData> v_RDatas = new ArrayList<RData>(i_BaseModels.size() * 16);
        
        for (O v_BaseModel : i_BaseModels)
        {
            v_RDatas.addAll(this.toRDatas(v_BaseModel));
        }
        
        return v_RDatas;
    }
    
    
    
    /**
     * 获取属性值，对枚举类型特殊处理
     * 
     * @param i_Value
     * @return
     */
    private Object getValue(Object i_Value)
    {
        if ( i_Value.getClass() == Enum.class || MethodReflect.isExtendImplement(i_Value ,Enum.class) )
        {
            return ((Enum<?>)i_Value).ordinal();
        }
        else
        {
            return i_Value;
        }
    }
    
    
    
    /**
     * 提取出对象集合RowKey集合
     * 
     * @param i_BaseModels
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<String> toRowKeys(O ... i_BaseModels)
    {
        List<String> v_Ret = new ArrayList<String>(i_BaseModels.length);
        
        for (O v_BaseModel : i_BaseModels)
        {
            v_Ret.add(v_BaseModel.getRowKey());
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 提取出对象集合RowKey集合
     * 
     * @param i_BaseModels
     * @return
     */
    protected List<String> toRowKeys(List<O> i_BaseModels)
    {
        List<String> v_Ret = new ArrayList<String>(i_BaseModels.size());
        
        for (O v_BaseModel : i_BaseModels)
        {
            if ( !Help.isNull(v_BaseModel.getRowKey()) )
            {
                v_Ret.add(v_BaseModel.getRowKey());
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 将对象转为RData的集合
     * 
     * @param i_BaseModel
     * @return
     */
    @SuppressWarnings("unchecked")
    protected List<RData> toRDatas_OnlyChange(O i_BaseModel)
    {
        return this.toRDatas_OnlyChange(i_BaseModel ,(O)i_BaseModel.newObject());
    }
    
    
    
    /**
     * 将对象转为RData的集合
     * 
     * @param i_New
     * @param i_Old
     * @return
     */
    protected List<RData> toRDatas_OnlyChange(O i_New ,O i_Old)
    {
        List<Integer> v_PIndexs = i_New.changeValues(i_Old);
        List<RData>   v_RDatas = new ArrayList<RData>();
        
        for (int i=0; i<v_PIndexs.size(); i++)
        {
            int    v_PIndex = v_PIndexs.get(i);
            Object v_Value  = i_New.gatPropertyValue(v_PIndex);
            if ( v_Value != null )
            {
                RData v_RData = new RData();
                
                if ( !Help.isNull(i_New.getRowKey()) )
                {
                    v_RData.setKey(i_New.getRowKey());
                }
                v_RData.setField(i_New.gatPropertyShortName(v_PIndex));
                v_RData.setValue(this.getValue(v_Value).toString());
                
                v_RDatas.add(v_RData);
            }
        }

        return v_RDatas;
    }
    
    
    
    /**
     * 将对象转为RData的集合
     * 
     * @param i_New
     * @param i_Old
     * @return
     */
    protected List<RData> toRDatas_OnlyChange(List<O> i_News ,List<O> i_Olds)
    {
        List<RData> v_RDatas = new ArrayList<RData>(i_News.size() * 10);
        
        for (int i=0; i<v_RDatas.size(); i++)
        {
            v_RDatas.addAll(this.toRDatas_OnlyChange(i_News.get(i) ,i_Olds.get(i)));
        }
        
        return v_RDatas;
    }
    
}
