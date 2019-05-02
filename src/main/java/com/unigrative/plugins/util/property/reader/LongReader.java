// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property.reader;

public class LongReader implements Reader<Long>
{
    @Override
    public Long read(final String value) {
        try {
            return Long.valueOf(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}
