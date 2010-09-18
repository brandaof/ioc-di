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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.brandao.ioc.mapping.ConstructorInject;
import org.brandao.ioc.mapping.GenericValueInject;
import org.brandao.ioc.mapping.Injectable;
import org.brandao.ioc.mapping.PropertyInject;
import org.brandao.ioc.mapping.ValueInject;

/**
 *
 * @author Afonso Brandao
 */
public class BeanFactory {

    private IOCContainer container;
    private ScopeManager scopeManager;

    public BeanFactory( IOCContainer container, ScopeManager scopeManager ){
        this.container = container;
        this.scopeManager = scopeManager;
    }

    private Object getCurrentInstance(Injectable beanDefinition) throws IOCException {
        Scope scope = null;

        if( beanDefinition.isSingleton() )
            scope = scopeManager.get( "singleton" );
        else
            scope = scopeManager.get( beanDefinition.getScope().toString() );

        if( scope == null )
            throw new IOCException( beanDefinition.getScope().toString()
                        + " scope: not configured!" );
        else
            return scope.get( beanDefinition.getName() );

    }

    private void registerInstance( Object instance, Injectable beanDefinition ) throws IOCException {
        Scope scope = null;

        if( beanDefinition.isSingleton() )
            scope = scopeManager.get( "singleton" );
        else
            scope = scopeManager.get( beanDefinition.getScope().toString() );

        scope.put( beanDefinition.getName(), instance );
    }

    private Object getInstanceByConstructor( Injectable beanDefinition ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
        ConstructorInject cons = beanDefinition.getConstructor();
        //List<Object> values    = new ArrayList();
        //List<Class<?>> types   = new ArrayList();
        /*
        if( cons != null ){
            List<Injectable> args = cons.getArgs();
            for( Injectable arg: args ){
                if( arg instanceof ValueInject ){
                    values.add( getValueInject( (ValueInject)arg ) );
                    types.add( arg.getTarget() );
                }
                else
                if( arg instanceof GenericValueInject ){
                    values.add( container.getBean( arg.getTarget() ) );
                    types.add( arg.getTarget() );
                }
                else{
                    values.add( container.getBean( arg.getName() ) );
                    types.add( arg.getTarget() );
                }
            }
        }
        */
        //Constructor insCons = inject.getTarget().getConstructor( types.toArray( new Class[]{} ) );
        ConstructorInject conInject = beanDefinition.getConstructor();
        if( conInject.isConstructor() ){
            Constructor insCons = beanDefinition.getConstructor().getContructor();
            //return insCons.newInstance( values.toArray( new Object[]{} ) );
            return insCons.newInstance( this.getValues(cons.getArgs()) );
        }
        else{
            Object factory =
                beanDefinition.getFactory() != null?
                    container.getBean(beanDefinition.getFactory()) :
                    null;

            Method method = beanDefinition.getConstructor().getMethod( factory );
            return method.invoke(
                    factory == null?
                        beanDefinition.getTarget() :
                        factory,
                    getValues(cons.getArgs())
                    /*values.toArray( new Object[]{} )*/ );
        }
    }

    private Object[] getValues( List<Injectable> args ){

        Object[] values = new Object[ args.size() ];

        for( int i=0;i<args.size();i++ ){
            Injectable arg = args.get(i);

            if( arg instanceof ValueInject )
                values[i] = getValueInject( (ValueInject)arg );
            else
            if( arg instanceof GenericValueInject )
                values[i] = container.getBean( arg.getTarget() );
            else
                values[i] = container.getBean( arg.getName() );
        }

        return values;
    }

    private Object getValueInject( ValueInject value ){
        return value.getValue();
    }

    public Object getInstance( Injectable beanDefinition ) throws IOCException {
        try{
            Object instance = getCurrentInstance(beanDefinition);

            if( instance != null )
                return instance;

            instance = getInstance0( beanDefinition );

            registerInstance( instance, beanDefinition );

            return instance;
        }
        catch( IOCException e ){
            throw e;
        }
        catch( Exception e ){
            throw new IOCException( "error when instantiating " + beanDefinition.getName(), e );
        }


    }

    private Object getInstance0( Injectable beanDefinition ) throws Exception{
        Object instance = getInstanceByConstructor( beanDefinition );

        //ConstructorInject cons = inject.getConstructor();
        List<PropertyInject> property = beanDefinition.getProperties();
        //List<Class<?>> valueArgs = new ArrayList();

        if( property != null ){

            for( PropertyInject prop: property ){
                Injectable arg = prop.getProperty();
                Object value = null;
                if( arg instanceof ValueInject )
                    value = getValueInject( (ValueInject)arg );
                else
                if( arg instanceof GenericValueInject ){
                    value = container.getBean( prop.getName() );
                }
                else
                    value = container.getBean( arg.getName() );

                Method m = prop.getMethod();
                m.invoke( instance, value );

            }
        }
        return instance;
    }

}
