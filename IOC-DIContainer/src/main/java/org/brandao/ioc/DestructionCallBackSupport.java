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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Afons Brandao
 */
public class DestructionCallBackSupport {

    private List destructionCallBackList;
    
    public DestructionCallBackSupport(){
        this.destructionCallBackList = new ArrayList();
    }

    public void registerDestructionCallBack( String name, Discartedbean destroy ){
        getDestructionCallBackList().add( new DestructionCallBackItem(name,destroy) );
    }

    public void destroyAll(){
        int size = getDestructionCallBackList().size();
        for( int i=0;i<size;i++ ){
            DestructionCallBackItem item =
                    (DestructionCallBackItem) getDestructionCallBackList().get(i);

            Discartedbean destroy = item.getDestroy();

            destroy.destroy();
        }
    }

    public List getDestructionCallBackList() {
        return destructionCallBackList;
    }

}
class DestructionCallBackItem{

    private String name;
    private Discartedbean destroy;

    public DestructionCallBackItem( String name, Discartedbean destroy ){
        this.name = name;
        this.destroy = destroy;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Discartedbean getDestroy() {
        return destroy;
    }

    public void setDestroy(Discartedbean destroy) {
        this.destroy = destroy;
    }

}
