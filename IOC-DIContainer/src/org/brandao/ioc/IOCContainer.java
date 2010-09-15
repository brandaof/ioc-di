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

/**
 *
 * @author Afonso Brandao
 */
public class IOCContainer {

    private ScopeManager scopeManager;
    private IOCContainer parent;

    public IOCContainer( ScopeManager scopeManager, IOCContainer parent ){
        this.scopeManager = scopeManager;
        this.parent = parent;
    }

    public IOCContainer(){
        this( new ScopeManager(), null );
    }

    public IOCContainer( IOCContainer parent ){
        this( null, parent );
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

    public BeanBuilder addBean( String name, Class clazz ){
        return null;
    }
}
