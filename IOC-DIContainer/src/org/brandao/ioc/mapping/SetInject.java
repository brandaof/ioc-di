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
package org.brandao.ioc.mapping;

/**
 *
 * @author Afonso Brandao
 */
public class SetInject extends ComplexObjectInject{
    
    public SetInject( String name, Class<?> valueType, Class<?> type, String factory, Property ... props ) {
        //super( type == null? java.util.HashSet.class : type, name, props );
        super( name, null, valueType, java.util.Set.class, factory, props );
        setType( type == null? java.util.HashSet.class : type );
    }
    
}
