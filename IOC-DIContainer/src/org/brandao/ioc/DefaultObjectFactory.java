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
public class DefaultObjectFactory implements ObjectFactory{

    private Injectable beanDefinition;
    private IOCContainer container;

    public DefaultObjectFactory( IOCContainer container, Injectable beanDefinition ){
        this.container = container;
        this.beanDefinition = beanDefinition;
    }
    
    public Object getObject(){
        try{
            Object instance = getInstanceByConstructor();

            List<PropertyInject> property = beanDefinition.getProperties();

            if( property != null ){

                for( PropertyInject prop: property ){
                    Injectable arg = prop.getProperty();
                    Object value = null;
                    if( arg instanceof ValueInject )
                        value = getValueInject( (ValueInject)arg );
                    else
                    if( arg instanceof GenericValueInject ){
                        value = container.contains( prop.getName() )?
                                    container.getBean( prop.getName() ) :
                                    container.getBean( prop.getMethod().getParameterTypes()[0] );
                    }
                    else
                        value = container.getBean( arg.getName() );

                    Method m = prop.getMethod();
                    m.invoke( instance, value );

                }
            }
            return instance;
        }
        catch( IOCException e ){
            throw e;
        }
        catch( Exception e ){
            throw new IOCException( "error when instantiating " + beanDefinition.getName(), e );
        }
    }

    private Object getInstanceByConstructor() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException{
        ConstructorInject cons = beanDefinition.getConstructor();
        ConstructorInject conInject = beanDefinition.getConstructor();
        if( conInject.isConstructor() ){
            Constructor insCons = beanDefinition.getConstructor().getContructor();
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
                    getValues(cons.getArgs()) );
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

}
