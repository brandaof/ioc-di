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
package org.brandao.ioc.scope;

import javax.servlet.ServletRequest;
import org.brandao.ioc.IOCException;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class RequestScope implements Scope{

    private ThreadLocal<ServletRequest> requests;

    public RequestScope( ThreadLocal<ServletRequest> requests ){
        this.requests = requests;
    }

    public void put(String name, Object value) {
        ServletRequest request = requests.get();
        request.setAttribute(name, value);
    }

    public Object get(String name) {
        ServletRequest request = requests.get();
        return request.getAttribute(name);
    }

    public String getName(){
        return ScopeType.REQUEST.toString();
    }

}
