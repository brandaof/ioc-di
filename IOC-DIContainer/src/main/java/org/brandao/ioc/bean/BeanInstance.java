/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2010 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.brandao.ioc.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.brandao.ioc.IOCException;
import org.brandao.ioc.mapping.ClassType;


/**
 *
 * @author Afonso Brandao
 */
public class BeanInstance {

    private static Map cache;

    static{
        cache = new HashMap();
    }

    private Object object;
    private Class clazz;
    private BeanData data;
    
    public BeanInstance( Object object ){
        this( object, object.getClass() );
    }

    public BeanInstance( Object object, Class clazz ){
        this.object = object;
        this.clazz  = clazz;
        this.data   = getBeanData( this.clazz );
    }

    public List getSetters(){
        List list = new ArrayList();

        Object[] values = data.getSetter().values().toArray();
        for( int i=0;i<values.length;i++ ){
            Method m = (Method) values[i];
            list.add( new SetterProperty( m, null ) );
        }
        return list;
    }

    public List getGetters(){
        List list = new ArrayList();

        Object[] values = data.getGetter().values().toArray();
        for( int i=0;i<values.length;i++ ){
            Method m = (Method) values[i];
            list.add( new GetterProperty( m, null ) );
        }
        return list;
    }
    
    public SetterProperty getSetter( String property ){

        Method method = (Method) data.getSetter().get(property);
        if( method == null )
            throw new IOCException( "not found: " + clazz.getName() + "." + property );
        return new SetterProperty( method, object );
        //return new SetterProperty( clazz.getDeclaredField( fieldName ), object );
    }

    private BeanData getBeanData( Class clazz ){
        if( cache.containsKey(clazz) )
            return (BeanData) cache.get(clazz);
        else{
            BeanData data = new BeanData();
            data.setClassType(clazz);
            Method[] methods = clazz.getMethods();
            for( int i=0;i<methods.length;i++ ){
                
                Method method = methods[i];

                String methodName = method.getName();

                if( methodName.startsWith("set") && method.getParameterTypes().length == 1 ){
                    String id = methodName
                            .substring(3,methodName.length());

                    id = Character.toLowerCase( id.charAt(0) )+ id.substring(1, id.length());
                    data.getSetter().put(id, method);
                }
                else
                if( methodName.startsWith("get") &&
                    method.getParameterTypes().length == 0  &&
                    method.getReturnType() != void.class ){
                    String id = methodName
                            .substring(3,methodName.length());

                    id = Character.toLowerCase( id.charAt(0) )+ id.substring(1, id.length());
                    data.getGetter().put(id, method);
                }
                else
                if( methodName.startsWith("is") &&
                    method.getParameterTypes().length == 0  &&
                    ClassType.getWrapper(method.getReturnType()) == Boolean.class ){
                    String id = methodName
                            .substring(2,methodName.length());

                    id = Character.toLowerCase( id.charAt(0) )+ id.substring(1, id.length());
                    data.getGetter().put(id, method);
                }
            }
            cache.put( clazz, data );
            return data;
        }
    }

    public GetterProperty getGetter( String property ){
        Method method = (Method) data.getGetter().get(property);
        if( method == null )
            throw new IOCException( "not found: " + clazz.getName() + "." + property );

        return new GetterProperty( method, object );
        //return new GetterProperty( clazz.getDeclaredField( fieldName ), object );
    }

    public boolean containProperty( String property ){
        return data.getGetter().get(property) != null;
    }
    
    public Class getType( String property ){
        Method method = (Method) data.getGetter().get(property);
        if( method == null )
            throw new IOCException( "not found: " + clazz.getName() + "." + property );

        return method.getReturnType();
    }

    public Type getGenericType( String property ){
        Method method = (Method) data.getGetter().get(property);
        if( method == null )
            throw new IOCException( "not found: " + clazz.getName() + "." + property );

        return method.getGenericReturnType();
    }

    public Class getClassType(){
        return this.clazz;
    }
}

class BeanData{

    private Class classType;
    private Map setter;
    private Map getter;

    public BeanData(){
        this.setter = new HashMap();
        this.getter = new HashMap();
    }
    
    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public Map getSetter() {
        return setter;
    }

    public void setSetter(Map setter) {
        this.setter = setter;
    }

    public Map getGetter() {
        return getter;
    }

    public void setGetter(Map getter) {
        this.getter = getter;
    }

}