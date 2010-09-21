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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


package org.brandao.ioc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
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
    //private Map<Object, Injectable> beanDefinitions;
    private BeanFactory beanFactory;
    private static long id = 0;
    //private boolean autoDefinitionConstructor;
    //private boolean autoDefinitionProperty;
    private DependencyFactory dependencyFactory;

    public IOCContainer( ScopeManager scopeManager, IOCContainer parent ){
        this.scopeManager = scopeManager;
        this.parent = parent;
        //this.beanDefinitions = new HashMap<Object, Injectable>();
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
        ((MutableBeanFactory)getBeanFactory()).removeBeanDefinition(name);
    }

    protected Injectable getBeanDefinition( Object key ){
        //key = key instanceof Class? ClassType.getWrapper((Class)key) : key;
        
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
        /*key = key instanceof Class? ClassType.getWrapper((Class)key) : key;
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
        */
        //key = key instanceof Class? ClassType.getWrapper((Class)key) : key;
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
        //key = key instanceof Class? ClassType.getWrapper((Class)key) : key;

        boolean exist = getBeanFactory().contains(key);
        
        if( !exist && parent != null )
            exist = parent.contains(key);

        return exist;
    }
    /*
    protected void createDefinition( Class clazz ){
        Constructor[] cons = clazz.getConstructors();
        if( cons.length == 0 )
            throw new IOCException( "can not found constructor: " + clazz.getName() );

        BeanBuilder builder = addBean(clazz);

        if( this.isAutoDefinitionConstructor() ){
            Constructor c = getConstructor( cons );
            Class[] params = c.getParameterTypes();

            for( Class param: params ){
                if( !contains(param) )
                    createDefinition( param );

                Injectable ref = this.getBeanDefinition(param);
                    //((MutableBeanFactory)beanFactory).getBeanDefinition(ClassType.getWrapper(param));
                builder.addConstructiorRefArg( ref.getName() );
            }
        }


        if( this.isAutoDefinitionProperty() ){
            BeanInstance instance = new BeanInstance(null,clazz);
            List<SetterProperty> sets = instance.getSetters();
            for( SetterProperty set: sets ){
                Method method = set.getMethod();

                Class param = ClassType.getWrapper( method.getParameterTypes()[0] );

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
    }

    protected Constructor getConstructor( Constructor[] cons ){

        Constructor noArgs = null;

        List<Constructor> list = new ArrayList<Constructor>();

        for( Constructor c: cons ){

            if( c.getParameterTypes().length == 0 )
                noArgs = c;
            else{
                boolean ok = true;
                for( Class param: c.getParameterTypes()  ){
                    if( !contains( param ) ){
                        ok = false;
                        break;
                    }
                }

                if( ok )
                    list.add( c );
            }
        }

        if( list.size() != 0 ){
            Constructor r = null;
            for( Constructor c: list ){
                if( r == null ||
                    r.getParameterTypes().length<c.getParameterTypes().length )
                    r = c;
            }
            return r;
        }
        else
        if( noArgs != null )
            return noArgs;
        else
            return cons[0];
        

    }
    */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public boolean isAutoDefinitionConstructor() {
        //return autoDefinitionConstructor;
        return getDependencyFactory()
                .getDependencyResolver().isCreateDependecyConstructor();
    }

    public void setAutoDefinitionConstructor(boolean autoDefinitionConstructor) {
        //this.autoDefinitionConstructor = autoDefinitionConstructor;
        getDependencyFactory()
                .getDependencyResolver()
                .setCreateDependecyConstructor(autoDefinitionConstructor);
    }

    public boolean isAutoDefinitionProperty() {
        //return autoDefinitionProperty;
                return getDependencyFactory()
                .getDependencyResolver().isCreateDependecyProperty();

    }

    public void setAutoDefinitionProperty(boolean autoDefinitionProperty) {
        //this.autoDefinitionProperty = autoDefinitionProperty;
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
