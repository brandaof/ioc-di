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

import java.lang.reflect.Method;
import org.brandao.brutos.bean.BeanInstance;
import org.brandao.brutos.bean.SetterProperty;
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.Injectable;
import org.brandao.ioc.mapping.PropertyInject;
import org.brandao.ioc.mapping.ValueInject;

/**
 *
 * @author Afonso Brandao
 */
public class BeanBuilder extends Injectable{

    private IOCContainer container;
    private BeanInstance beanInstance;

    public BeanBuilder( Class<?> target, String name, ScopeType scope, boolean singleton, String factory, IOCContainer container ) {
        super( target, name, scope, singleton, factory );
        this.container = container;
        this.beanInstance = new BeanInstance( null, target );

    }

    public BeanBuilder addConstructiorArg( Object value, Class<?> type ){
        getConstructor().addArg( new ValueInject( type, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( Object value ){
        return addConstructiorArg( value, value == null? null : value.getClass() );
    }

    public BeanBuilder addConstructiorArg( boolean value ){
        getConstructor().addArg( new ValueInject(boolean.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( byte value ){
        getConstructor().addArg( new ValueInject( byte.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( char value ){
        getConstructor().addArg( new ValueInject( char.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( double value ){
        getConstructor().addArg( new ValueInject( double.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( float value ){
        getConstructor().addArg( new ValueInject( float.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( int value ){
        getConstructor().addArg( new ValueInject( int.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( long value ){
        getConstructor().addArg( new ValueInject( long.class, value ) );
        return this;
    }

    public BeanBuilder addConstructiorArg( short value ){
        getConstructor().addArg( new ValueInject( short.class, value ) );
        return this;
    }

    public void setFactoryMethod( String method ){
        getConstructor().setMethodFactory(method);
    }

    public BeanBuilder addConstructiorArg(Class clazz){
        String name = IOCContainer.getNextId()+"#Bean";
        BeanBuilder beanBuilder = container.addBean(name, clazz);
        addConstructiorRefArg( name );
        return beanBuilder;
    }

    public BeanBuilder addConstructiorRefArg( String ref ){
        BeanBuilder refBean = (BeanBuilder) container.getBeanDefinition( ref );

        if( refBean == null )
            throw new BeanNotFoundException( ref );

        getConstructor().addArg( refBean );
        return this;
    }

    public BeanBuilder addProperty(String name, Class clazz){
        String beanName = IOCContainer.getNextId()+"#Bean";
        BeanBuilder beanBuilder = container.addBean(beanName, clazz);
        addPropertyRef( name, beanName );
        return beanBuilder;
    }

    public BeanBuilder addPropertyRef( String name, String arg ){
        BeanBuilder refBean = (BeanBuilder) container.getBeanDefinition( arg );

        if( refBean == null )
            throw new BeanNotFoundException( arg );

        SetterProperty set = beanInstance.getSetter(name);
        Method method = set == null? null : set.getMethod();

        if( method == null ){
            Class<?> classType = getTarget();

            throw new IOCException( "can not set property: " + name + " in " + classType.getName() );
        }

        getProperties().add(
            new PropertyInject(
                name,
                refBean,
                method
            )
        );
        return this;
        

    }

    public BeanBuilder addPropertyValue( String name, Object arg ){
        return addPropertyValue( name,arg == null? Object.class : arg.getClass(), arg );
    }

    public BeanBuilder addPropertyValue( String name, boolean value ){
        return addPropertyValue( name, boolean.class, value );
    }

    public BeanBuilder addPropertyValue( String name, byte value ){
        return addPropertyValue( name, byte.class, value );
    }

    public BeanBuilder addPropertyValue( String name, char value ){
        return addPropertyValue( name, char.class, value );
    }

    public BeanBuilder addPropertyValue( String name, double value ){
        return addPropertyValue( name, double.class, value );
    }

    public BeanBuilder addPropertyValue( String name, float value ){
        return addPropertyValue( name, float.class, value );
    }

    public BeanBuilder addPropertyValue( String name, int value ){
        return addPropertyValue( name, int.class, value );
    }

    public BeanBuilder addPropertyValue( String name, long value ){
        return addPropertyValue( name, long.class, value );
    }

    public BeanBuilder addPropertyValue( String name, short value ){
        return addPropertyValue( name, short.class, value );
    }

    public BeanBuilder addPropertyValue( String name, Class argType, Object arg ){
        Method method = null;
        try{
            String methodName =
                    "set" +
                    name.substring( 0, 1 ).toUpperCase() +
                    name.substring( 1 );

            Class<?> classType = getTarget();

            for( Method m: classType.getMethods() ){
                if( m.getParameterTypes().length == 1 && methodName.equals( m.getName() ) && ClassType.getWrapper( m.getParameterTypes()[0] ).isAssignableFrom( ClassType.getWrapper(argType) ) ){
                    method = classType.getMethod( methodName, m.getParameterTypes()[0] );
                    break;
                }
            }

            if( method == null )
                throw new IOCException( "not found: " + methodName + "(" + argType.getName()  + ")" );

        }
        catch( IOCException e ){
            throw e;
        }
        catch( Exception e ){
            throw new IOCException( e );
        }

        getProperties().add(
            new PropertyInject(
                name,
                new ValueInject( argType, arg ),
                method
            )
        );
        return this;
    }

}
