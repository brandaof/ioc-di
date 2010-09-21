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

package org.brandao.ioc.editors;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;

/**
 *
 * @author Afonso Brandao
 */
public class DateEditorSupport extends PropertyEditorSupport{

    private String text;
    private SimpleDateFormat pattern;

    public DateEditorSupport( SimpleDateFormat pattern, boolean lenient ){
        this.pattern = pattern;
        pattern.setLenient(lenient);
    }

    public void setAsText(String text) {
        this.text = text;
        try{
            setValue( pattern.parse(text) );
        }
        catch( Exception e ){
            setValue( null );
        }
    }

    public String getAsText() {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss:sss" );
        return this.text;
    }

    public SimpleDateFormat getPattern() {
        return pattern;
    }

    public void setPattern(SimpleDateFormat pattern) {
        this.pattern = pattern;
    }
    
}
