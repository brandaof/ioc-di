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
import org.brandao.ioc.mapping.ClassType;
import org.brandao.ioc.mapping.Injectable;

/**
 *
 * @author Afonso Brandao
 */
public class DefaultBeanFactory implements BeanFactory, MutableBeanFactory{

    private Map<Object, Injectable> beanDefinitions;
    private IOCContainer container;

    public DefaultBeanFactory( IOCContainer container ){
        this.container = container;
        this.beanDefinitions = new HashMap<Object,Injectable>();
    }

    public Injectable getBeanDefinition( Object key ){
        return beanDefinitions.get(key);
    }

    public Object getBean( Object key ){

        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = beanDefinitions.get(key);
        
        ScopeManager scopeManager = container.getScopeManager();
        Scope scope = scopeManager.get( beanDefinition.getScope().toString() );

        Object objectScoped = 
                scope.get(
                    beanDefinition.getName(),
                    new DefaultObjectFactory( container, beanDefinition ) );

        return objectScoped;
        
    }

    public boolean contains(Object key) {
        return this.beanDefinitions.containsKey(key);
    }

    public Class getType(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = beanDefinitions.get(key);
        return beanDefinition.getTarget();
    }

    public boolean isPrototype(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = beanDefinitions.get(key);
        return beanDefinition.getScope().equals( ScopeType.PROTOTYPE );
    }

    public boolean isSingleton(Object key) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = beanDefinitions.get(key);
        
        return beanDefinition.getScope().equals( ScopeType.SINGLETON ) ||
                beanDefinition.isSingleton();
        
    }

    public boolean isScoped(Object key, ScopeType scope) {
        if( !contains( key ) )
            throw new BeanNotFoundException(String.valueOf(key));

        Injectable beanDefinition = beanDefinitions.get(key);

        return beanDefinition.getScope().toString().equals( scope.toString() );
    }

    public void addBeanDefinition(Injectable beanDefinition) {
        beanDefinitions.put(beanDefinition.getName(),beanDefinition);
        beanDefinitions.put(ClassType.getWrapper( beanDefinition.getTarget() ),beanDefinition);
    }

    public void removeBeanDefinition(Object key) {
        if( contains( key ) ){
            Injectable beanDefinition = beanDefinitions.get(key);
            beanDefinitions.remove(beanDefinition.getName());
            //beanDefinitions.remove(ClassType.getWrapper( beanDefinition.getTarget() ));
        }
    }

}
