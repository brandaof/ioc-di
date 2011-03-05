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


package org.brandao.ioc;

import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public abstract class IOCContainer {

    private ScopeManager scopeManager;
    private IOCContainer parent;
    private BeanFactory beanFactory;
    private static long id = 0;
    private DependencyFactory dependencyFactory;

    public IOCContainer( ScopeManager scopeManager, IOCContainer parent ){
        this.scopeManager = scopeManager;
        this.parent = parent;
        this.beanFactory = new DefaultBeanFactory( this );
        this.dependencyFactory = new DefaultDependencyFactory(this);
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

    protected BeanBuilder addBean( String name, Class classType, ScopeType scope, boolean singleton, String factory ){

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
        ((MutableBeanFactory)getBeanFactory()).removeBeanDefinition(name);
    }

    protected Injectable getBeanDefinition( Object key ){
        
        if(getBeanFactory().contains(key))
            return ((MutableBeanFactory)getBeanFactory()).getBeanDefinition(key);
        else
        if( parent != null )
            return parent.getBeanDefinition(key);
        else
            return null;
    }

    protected void addBeanDefinition( Injectable inject ){
        ((MutableBeanFactory)getBeanFactory()).addBeanDefinition(inject);
    }

    public Object getBean( Class clazz ){
        return getBean( (Object)clazz );
    }

    public Object getBean( Object key ){
        if( getBeanFactory().contains(key) )
            return getBeanFactory().getBean(key);
        else
        if( parent != null && parent.contains(key) )
            return parent.getBean(key);
        else
        if( (isAutoDefinitionConstructor() || isAutoDefinitionProperty())
                    && key instanceof Class ){
            //createDefinition( (Class)key );
            getDependencyFactory().createDependency((Class)key);
            return getBean( key );
        }
        else
            throw new BeanNotFoundException(String.valueOf(key));
    }

    public boolean contains( Object key ){

        boolean exist = getBeanFactory().contains(key);
        
        if( !exist && parent != null )
            exist = parent.contains(key);

        return exist;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public boolean isAutoDefinitionConstructor() {
        return getDependencyFactory()
                .getDependencyResolver().isCreateDependecyConstructor();
    }

    public void setAutoDefinitionConstructor(boolean autoDefinitionConstructor) {
        getDependencyFactory()
                .getDependencyResolver()
                .setCreateDependecyConstructor(autoDefinitionConstructor);
    }

    public boolean isAutoDefinitionProperty() {
                return getDependencyFactory()
                .getDependencyResolver().isCreateDependecyProperty();

    }

    public void setAutoDefinitionProperty(boolean autoDefinitionProperty) {
        getDependencyFactory()
                .getDependencyResolver()
                .setCreateDependecyProperty(autoDefinitionProperty);

    }

    public DependencyFactory getDependencyFactory() {
        return dependencyFactory;
    }

    public void setDependencyFactory(DependencyFactory dependencyFactory) {
        this.dependencyFactory = dependencyFactory;
    }

}
