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
import org.brandao.ioc.scope.ProtoTypeScope;
import org.brandao.ioc.scope.SingletonScope;

/**
 *
 * @author Afonso Brandao
 */
public class ScopeManager{

    private Map<String,Scope> scopes;
    private ScopeManager parent;
    
    public ScopeManager() {
        scopes = new HashMap<String,Scope>();
        register( "singleton", new SingletonScope() );
        register( "prototype", new ProtoTypeScope() );
    }

    public ScopeManager( ScopeManager parent ) {
        this();
        this.parent = parent;
    }

    public void register( String id, Scope scope ){
        scopes.put( id, scope );
    }

    public void remove( String id ){
        scopes.remove( id );
    }

    public Scope get( String id ){
        return scopes.get( id );
    }

    public ScopeManager getParent() {
        return parent;
    }

    public void setParent(ScopeManager parent) {
        this.parent = parent;
    }
}
