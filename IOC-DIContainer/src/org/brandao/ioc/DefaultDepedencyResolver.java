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
import java.util.ArrayList;
import java.util.List;
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public class DefaultDepedencyResolver implements DepedencyResolver{

    private DependencyFactory dependencyFactory;
    private boolean createDependecyConstructor;
    private boolean createDependecyProperty;
    private IOCContainer container;

    public DefaultDepedencyResolver( boolean arg0, boolean arg1, IOCContainer container ){
        this.createDependecyConstructor = arg0;
        this.createDependecyProperty = arg0;
        this.container = container;
    }

    public Injectable[] getAppropriateConstructor( Constructor[] constructors ){
        Constructor c = getConstructor( constructors );
        Class[] params = c.getParameterTypes();
        Injectable[] args = new Injectable[params.length];
        int i=0;
        for( Class param: params ){
            if( !container.contains(param) && this.createDependecyConstructor )
                dependencyFactory.createDependency( param );

            Injectable ref = container.getBeanDefinition(param);

            if( ref == null )
                throw new BeanNotFoundException( param.getName() );

            args[i++] = ref;
        }

        return args;
    }

    public Injectable getAppropriateProperty( Class propertyType ){
        Class param = ClassType.getWrapper( propertyType );

        if( !container.contains(param) && this.createDependecyProperty )
            dependencyFactory.createDependency( param );


        Injectable ref = container.getBeanDefinition(param);
        
        if( ref == null )
            throw new BeanNotFoundException( propertyType.getName() );
        
        return ref;
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
                    if( !container.contains( param ) ){
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

    public boolean isCreateDependecyConstructor() {
        return this.createDependecyConstructor;
    }

    public boolean isCreateDependecyProperty(){
        return this.createDependecyProperty;
    }

    public void setDependencyFactory(DependencyFactory dependencyFactory) {
        this.dependencyFactory = dependencyFactory;
    }

    public DependencyFactory getDependencyFactory() {
        return this.dependencyFactory;
    }

    public void setCreateDependecyConstructor(boolean createDependecyConstructor) {
        this.createDependecyConstructor = createDependecyConstructor;
    }

    public void setCreateDependecyProperty(boolean createDependecyProperty) {
        this.createDependecyProperty = createDependecyProperty;
    }

}
