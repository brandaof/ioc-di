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

import java.util.HashMap;
import java.util.Map;
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
        beanDefinitions.put(inject.getTarget(),inject);
    }

    public Object getBean( Class clazz ){
        return getBean( (Object)clazz );
    }

    public Object getBean( Object key ){
        if( beanDefinitions.containsKey(key) )
            return beanFactory.getInstance(beanDefinitions.get(key));
        else
        if( parent != null )
            return parent.getBean(key);
        else
            throw new BeanNotFoundException(String.valueOf(key));
    }
}
