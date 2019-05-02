// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property.reader;

import com.evnt.util.Util;

public class DoubleReader implements Reader<Double>
{
    private final double defaultValue;
    
    public DoubleReader(final double defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    @Override
    public Double read(final String value) {
        if (Util.isEmpty(value)) {
            return this.defaultValue;
        }
        try {
            return Double.valueOf(value);
        }
        catch (NumberFormatException e) {
            return this.defaultValue;
        }
    }
}
