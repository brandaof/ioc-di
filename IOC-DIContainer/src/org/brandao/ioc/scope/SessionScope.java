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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.brandao.ioc.scope;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.brandao.ioc.ObjectFactory;
import org.brandao.ioc.Scope;
import org.brandao.ioc.ScopeType;

/**
 *
 * @author Afonso Brandao
 */
public class SessionScope implements Scope{

    private ThreadLocal<ServletRequest> requests;

    public SessionScope( ThreadLocal<ServletRequest> requests ){
        this.requests = requests;
    }
    
    public String getName(){
        return ScopeType.SESSION.toString();
    }

    public Object get(String beanName, ObjectFactory factory) {
        HttpSession session = ((HttpServletRequest)requests.get()).getSession();
        Object value = session.getAttribute(beanName);
        if( value == null ){
            value = factory.getObject();
            session.setAttribute(beanName, value);
        }
        return value;
    }

    public void remove(String name) {
        HttpSession session = ((HttpServletRequest)requests.get()).getSession();
        session.removeAttribute(name);
    }

}
