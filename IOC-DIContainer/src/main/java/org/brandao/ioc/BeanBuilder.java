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

import java.lang.reflect.Method;
import org.brandao.ioc.bean.BeanInstance;
import org.brandao.ioc.bean.SetterProperty;
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.GenericValueInject;
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

    public BeanBuilder( Class target, String name, ScopeType scope,
            boolean singleton, String factory, IOCContainer container ) {
        super( target, name, scope, singleton, factory );
        this.container = container;
        this.beanInstance = new BeanInstance( null, target );

    }

    public BeanBuilder addConstructorArg( Object value, Class type ){
        getConstructor().addArg( new ValueInject( type, value ) );
        return this;
    }

    public BeanBuilder addConstructorArg( Object value ){
        return addConstructorArg( value, value == null? null : value.getClass() );
    }

    public BeanBuilder addConstructorArg( boolean value ){
        getConstructor().addArg( new ValueInject(boolean.class, Boolean.valueOf(value)) );
        return this;
    }

    public BeanBuilder addConstructorArg( byte value ){
        getConstructor().addArg( new ValueInject( byte.class, Byte.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( char value ){
        getConstructor().addArg( new ValueInject( char.class, Character.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( double value ){
        getConstructor().addArg( new ValueInject( double.class, Double.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( float value ){
        getConstructor().addArg( new ValueInject( float.class, Float.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( int value ){
        getConstructor().addArg( new ValueInject( int.class, Integer.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( long value ){
        getConstructor().addArg( new ValueInject( long.class, Long.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg( short value ){
        getConstructor().addArg( new ValueInject( short.class, Short.valueOf(value) ) );
        return this;
    }

    public BeanBuilder addConstructorArg(){
        getConstructor().addArg( new GenericValueInject() );
        return this;
    }

    public BeanBuilder setFactoryMethod( String method ){
        getConstructor().setMethodFactory(method);
        return this;
    }

    public BeanBuilder setFactory(Class factory){
        return setFactory(factory.getName());
    }

    public BeanBuilder setFactory(String factory) {
        super.setFactoryRef(factory);
        return this;
    }

    public BeanBuilder addConstructorArg(Class clazz){
        String name = IOCContainer.getNextId()+"#Bean";
        BeanBuilder beanBuilder = 
            container.addBean(name,
                clazz,
                ScopeType.PROTOTYPE,
                false,
                null);
        
        addConstructorRefArg( name );
        return beanBuilder;
    }

    public BeanBuilder addConstructorRefArg( Class ref ){
        return addConstructorRefArg( ref.getName() );
    }
    
    public BeanBuilder addConstructorRefArg( String ref ){
        BeanBuilder refBean = (BeanBuilder) container.getBeanDefinition( ref );

        if( refBean == null )
            throw new BeanNotFoundException( ref );

        getConstructor().addArg( refBean );
        return this;
    }

    public BeanBuilder addProperty(String name, Class clazz){
        String beanName = IOCContainer.getNextId()+"#Bean";
        BeanBuilder beanBuilder = 
            container.addBean(
                beanName,
                clazz,
                ScopeType.PROTOTYPE,
                false,
                null);
        
        addPropertyRef( name, beanName );
        return beanBuilder;
    }

    public BeanBuilder addPropertyRef( String name, String arg ){
        BeanBuilder refBean = (BeanBuilder) container.getBeanDefinition( arg );

        if( refBean == null )
            throw new BeanNotFoundException( arg );

        addProperty(name, refBean);
        return this;
        /*
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
        */
    }

    protected PropertyInject addProperty( String name, Injectable arg ){
        SetterProperty set = beanInstance.getSetter(name);
        Method method = set == null? null : set.getMethod();

        if( method == null ){
            Class classType = getTarget();
            throw new IOCException( "can not set property: " + name + " in " + classType.getName() );
        }

        PropertyInject inject =  new PropertyInject(
                name,
                arg,
                method
            );


        getProperties().add( inject );
        return inject;
    }

    public BeanBuilder addPropertyValue( String name, Object arg ){
        return addPropertyValue( name,arg == null? Object.class : arg.getClass(), arg );
    }

    public BeanBuilder addPropertyValue( String name, boolean value ){
        return addPropertyValue( name, boolean.class, Boolean.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, byte value ){
        return addPropertyValue( name, byte.class, Byte.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, char value ){
        return addPropertyValue( name, char.class, Character.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, double value ){
        return addPropertyValue( name, double.class, Double.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, float value ){
        return addPropertyValue( name, float.class, Float.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, int value ){
        return addPropertyValue( name, int.class, Integer.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, long value ){
        return addPropertyValue( name, long.class, Long.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, short value ){
        return addPropertyValue( name, short.class, Short.valueOf(value) );
    }

    public BeanBuilder addPropertyValue( String name, Class argType, Object arg ){
        ValueInject inject = new ValueInject( argType, arg );
        addProperty(name, inject);
        return this;
        /*
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
        return this;*/
    }


    public BeanBuilder addProperty( String name ){
        addProperty(name, new GenericValueInject());
        return this;
    }

    public BeanBuilder initMethod(String name){
        this.setInitMethod(name);
        return this;
    }

    public BeanBuilder destroyMethod(String name){
        this.setDestroyMethod(name);
        return this;
    }

}
