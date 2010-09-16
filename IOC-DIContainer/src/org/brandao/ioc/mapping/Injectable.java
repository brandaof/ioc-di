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

package org.brandao.ioc.mapping;

import java.util.ArrayList;
import java.util.List;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class Injectable {
    
    private Class<?> target;
    
    private String name;
    
    private ScopeType scope;
    
    private boolean singleton;
    
    private ConstructorInject constructor;
    
    private List<PropertyInject> properties;
    
    private String factory;
    
    public Injectable() {
    }

    public Injectable( Class<?> target, String name, ScopeType scope, boolean singleton, String factory ) {
        this.target     = target;
        this.name       = name;
        this.scope      = scope;
        this.singleton  = singleton;
        this.properties = new ArrayList();
        this.factory    = factory;
        
        this.constructor = new ConstructorInject( this );
    }
    
    public Class<?> getTarget() {
        return target;
    }

    public void setTarget(Class<?> target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ScopeType getScope() {
        return scope;
    }

    public void setScope(ScopeType scope) {
        this.scope = scope;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public ConstructorInject getConstructor() {
        return constructor;
    }

    public void setConstructor(ConstructorInject constructor) {
        this.constructor = constructor;
    }

    public List<PropertyInject> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyInject> properties) {
        this.properties = properties;
    }
    
    public boolean equals( Object o ){
        return o instanceof Injectable? this.name.equals( ((Injectable)o).name ) : false;
    }

    public String getFactory() {
        return factory;
    }

    protected void setFactoryRef(String factory) {
        this.factory = factory;
    }
}
