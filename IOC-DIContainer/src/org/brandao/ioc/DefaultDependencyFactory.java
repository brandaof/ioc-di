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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import org.brandao.ioc.bean.BeanInstance;
import org.brandao.ioc.bean.SetterProperty;
import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public class DefaultDependencyFactory extends AbstractDependencyFactory{

    private IOCContainer container;

    public DefaultDependencyFactory( IOCContainer container ){
        this.container = container;

        DepedencyResolver dr =
                new DefaultDepedencyResolver(false,false,container);

        dr.setDependencyFactory(this);
        
        this.setDependencyResolver( dr );
    }

    public Injectable createDependency(Class clazz) {

        Constructor[] cons = clazz.getConstructors();
        BeanBuilder builder = container.addBean(clazz);

        if( getDependencyResolver().isCreateDependecyConstructor() ){

            Injectable[] args = getDependencyResolver()
                    .getAppropriateConstructor(cons);

            if( args.length == 0 )
                throw new IOCException(
                        "can not found constructor: " + clazz.getName() );

            for( Injectable arg: args )
                builder.addConstructorRefArg( arg.getName() );

        }

        if( getDependencyResolver().isCreateDependecyProperty() ){
            BeanInstance instance = new BeanInstance(null,clazz);
            List<SetterProperty> sets = instance.getSetters();
            for( SetterProperty set: sets ){
                Method method = set.getMethod();
                Injectable ref = getDependencyResolver()
                        .getAppropriateProperty(method.getParameterTypes()[0]);

                String methodName = method.getName();
                String id = methodName
                        .substring(3,methodName.length());

                id = Character.toLowerCase( id.charAt(0) ) +
                        id.substring(1, id.length() );

                builder.addPropertyRef(id, ref.getName());
            }

        }

        return builder;
    }


}
