// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property.reader;

import com.evnt.util.Util;

public final class BooleanReader implements Reader<Boolean>
{
    private final boolean defaultValue;
    
    public BooleanReader(final boolean defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Boolean read(final String value) {
        return Util.isEmpty(value) ? this.defaultValue : Boolean.valueOf(value);
    }
}
