// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property;

import com.unigrative.plugins.util.property.reader.Reader;

public abstract class PluginProperty<T>
{
    protected final String key;
    protected final Reader<T> reader;
    
    protected PluginProperty(final String key, final Reader<T> transformer) {
        this.key = key;
        this.reader = transformer;
    }
}
