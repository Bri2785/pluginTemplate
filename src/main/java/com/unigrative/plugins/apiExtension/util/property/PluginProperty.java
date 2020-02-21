// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.apiExtension.util.property;

import com.unigrative.plugins.apiExtension.util.property.reader.Reader;

public abstract class PluginProperty<T>
{
    protected final String key;
    protected final com.unigrative.plugins.apiExtension.util.property.reader.Reader<T> reader;
    
    protected PluginProperty(final String key, final Reader<T> transformer) {
        this.key = key;
        this.reader = transformer;
    }
}
