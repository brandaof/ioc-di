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
        this( RootContainer.getInstance().getScopeManager() );
    }

    public ScopeManager( ScopeManager parent ) {
        this.parent = parent;
        scopes      = new HashMap<String,Scope>();
        register( "singleton", new SingletonScope() );
        register( "prototype", new ProtoTypeScope() );
    }

    public void register( String id, Scope scope ){
        scopes.put( id, scope );
    }

    public void remove( String id ){
        scopes.remove( id );
    }

    public Scope get( String id ){
        if( scopes.containsKey(id) )
            return scopes.get( id );
        else
            return parent == null? null : parent.get(id);
    }

    public ScopeManager getParent() {
        return parent;
    }

    public void setParent(ScopeManager parent) {
        this.parent = parent;
    }
}
