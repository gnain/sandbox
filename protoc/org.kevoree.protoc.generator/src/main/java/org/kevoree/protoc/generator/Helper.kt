package org.kevoree.protoc.generator;

import java.io.File;

/**
 * Created by gregory.nain on 05/02/2014.
 */
public object Helper {

    public fun checkOrCreateFolder( path : String) {
        val file = File(path);
        if (!file.exists()) file.mkdirs();
    }

    public fun toCamelCase(str:String) : String {
        return str.substring(0,1).toUpperCase() + str.substring(1)
    }

}
