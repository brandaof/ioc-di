/*
 * IOC-DI Container http://ioc-di.sourceforge.net/
 * Copyright (C) 2009 Afonso Brandao. (afonso.rbn@gmail.com)
 *
 * This library is free software. You can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License (GPL) version 3.0 or (at your option) any later
 * version.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/gpl.html
 *
 * Distributed WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 *
 */


package org.brandao.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.brandao.ioc.bean.BeanInstance;
import org.brandao.ioc.bean.SetterProperty;
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public class IOCContainer {

    private ScopeManager scopeManager;
    private IOCContainer parent;
    private Map<Object, Injectable> beanDefinitions;
    private BeanFactory beanFactory;
    private static long id = 0;
    private boolean autoDefinition;

    public IOCContainer( ScopeManager scopeManager, IOCContainer parent ){
        this.scopeManager = scopeManager;
        this.parent = parent;
        this.beanDefinitions = new HashMap<Object, Injectable>();
        this.beanFactory = new BeanFactory( this, scopeManager );
    }


    public IOCContainer(){
        this( new ScopeManager(), RootContainer.getInstance() );
    }

    public IOCContainer( IOCContainer parent ){
        this( null, parent );
    }

    static synchronized long getNextId(){
        return id++;
    }

    public ScopeManager getScopeManager() {
        return scopeManager;
    }

    public void setScopeManager(ScopeManager scopeManager) {
        this.scopeManager = scopeManager;
    }

    public IOCContainer getParent() {
        return parent;
    }

    public void setParent(IOCContainer parent) {
        this.parent = parent;
    }

    public BeanBuilder addBean( Class clazz ){
        return addBean( clazz.getName(), clazz );
    }

    public BeanBuilder addBean( String name, Class classType ){
        return addBean( name, classType, ScopeType.PROTOTYPE, false, null );
    }

    public BeanBuilder addBean( String name, Class classType, ScopeType scope ){
        return addBean( name, classType, scope, false, null );
    }

    public BeanBuilder addSingleton( String name, String factory ){
        return addBean( name, null, null, true, factory );
    }

    public BeanBuilder addSingleton( String name, Class classType ){
        return addBean( name, classType, null, true, null );
    }

    public BeanBuilder addBean( String name, Class classType, ScopeType scope, boolean singleton, String factory ){

        if( classType == null && factory == null )
            throw new IOCException( "the class type is null factory is required!" );

        Injectable oldBean = getBeanDefinition( name );

        if( oldBean != null && oldBean.getTarget() != classType )
            throw new BeanExistException( name );
        else
        if( oldBean != null )
            removeBeanDefinition( name );

        BeanBuilder bean = new BeanBuilder( classType, name, scope, singleton, factory, this );
        addBeanDefinition( bean );
        return bean;
    }

    protected void removeBeanDefinition( String name ){
        beanDefinitions.remove(name);
    }

    protected Injectable getBeanDefinition( String name ){
        return beanDefinitions.get(name);
    }

    protected void addBeanDefinition( Injectable inject ){
        beanDefinitions.put(inject.getName(),inject);
        beanDefinitions.put(ClassType.getWrapper( inject.getTarget() ),inject);
    }

    public Object getBean( Class clazz ){
        return getBean( (Object)clazz );
    }

    public Object getBean( Object key ){
        key = key instanceof Class? ClassType.getWrapper((Class)key) : key;
        if( beanDefinitions.containsKey(key) )
            return beanFactory.getInstance(beanDefinitions.get(key));
        else
        if( parent != null && parent.contains(key) )
            return parent.getBean(key);
        else
        if( isAutoDefinition() && key instanceof Class ){
            createDefinition( (Class)key );
            return getBean( key );
        }
        else
            throw new BeanNotFoundException(String.valueOf(key));
    }

    public boolean contains( Object key ){
        key = key instanceof Class? ClassType.getWrapper((Class)key) : key;
        boolean exist = beanDefinitions.containsKey(key);
        
        if( !exist && parent != null )
            exist = parent.contains(key);

        return exist;
    }

    protected void createDefinition( Class clazz ){
        Constructor[] cons = clazz.getConstructors();
        if( cons.length == 0 )
            throw new IOCException( "can not found constructor: " + clazz.getName() );

        BeanBuilder builder = addBean(clazz);

        Constructor c = cons[0];
        Class[] params = c.getParameterTypes();

        for( Class param: params ){
            if( !contains(param) )
                createDefinition( param );

            builder.addConstructiorArg();
        }

        BeanInstance instance = new BeanInstance(null,clazz);
        List<SetterProperty> sets = instance.getSetters();
        for( SetterProperty set: sets ){
            Method method = set.getMethod();

            Class param = method.getParameterTypes()[0];

            if( !contains(param) )
                createDefinition( param );

            String methodName = method.getName();
            String id = methodName
                    .substring(3,methodName.length());

            id = Character.toLowerCase( id.charAt(0) ) +
                    id.substring(1, id.length() );

            builder.addProperty(id);
        }
    }

    public boolean isAutoDefinition() {
        return autoDefinition;
    }

    public void setAutoDefinition(boolean autoDefinition) {
        this.autoDefinition = autoDefinition;
    }
}
