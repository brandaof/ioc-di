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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public class DefaultBeanFactory implements BeanFactory, MutableBeanFactory{

    private Map beanDefinitions;
    private IOCContainer container;

    public DefaultBeanFactory( IOCContainer container ){
        this.container = container;
        this.beanDefinitions = new LinkedHashMap();
    }

    public Injectable getBeanDefinition( Object key ){
        return (Injectable) beanDefinitions.get(key);
    }

    public Object getBean( Object key ){

        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = (Injectable) beanDefinitions.get(key);
        
        ScopeManager scopeManager = container.getScopeManager();
        Scope scope = scopeManager.get( 
                        beanDefinition.isSingleton()?
                            ScopeType.SINGLETON.toString() :
                            beanDefinition.getScope().toString() );

        Object objectScoped = 
                scope.get(
                    beanDefinition.getName(),
                    new DefaultObjectFactory( container, beanDefinition ) );

        DiscartedbeanFactory discartedFactory =
                contains("discartedBeanFactory")?
                    (DiscartedbeanFactory)getBean( "discartedBeanFactory" ) :
                    new DefaultDiscartedbeanFactory();

        scope.registerDestructionCallback(beanDefinition.getName(),
                discartedFactory.getDiscartedbean(beanDefinition, objectScoped));
        
        return objectScoped;
        
    }

    public boolean contains(Object key) {
        return this.beanDefinitions.containsKey(key);
    }

    public Class getType(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = (Injectable) beanDefinitions.get(key);
        return beanDefinition.getTarget();
    }

    public boolean isPrototype(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = (Injectable) beanDefinitions.get(key);
        return beanDefinition.getScope().equals( ScopeType.PROTOTYPE );
    }

    public boolean isSingleton(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = (Injectable) beanDefinitions.get(key);
        
        return beanDefinition.getScope().equals( ScopeType.SINGLETON ) ||
                beanDefinition.isSingleton();
        
    }

    public boolean isScoped(Object key, ScopeType scope) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = (Injectable) beanDefinitions.get(key);

        return beanDefinition.getScope().toString().equals( scope.toString() );
    }

    public void addBeanDefinition(Injectable beanDefinition) {
        beanDefinitions.put(beanDefinition.getName(),beanDefinition);
        beanDefinitions.put(ClassType.getWrapper( beanDefinition.getTarget() ),beanDefinition);
    }

    public void removeBeanDefinition(Object key) {
        if( contains( key ) ){
            Injectable beanDefinition = (Injectable) beanDefinitions.get(key);
            beanDefinitions.remove(beanDefinition.getName());
            //beanDefinitions.remove(ClassType.getWrapper( beanDefinition.getTarget() ));
        }
    }

}
