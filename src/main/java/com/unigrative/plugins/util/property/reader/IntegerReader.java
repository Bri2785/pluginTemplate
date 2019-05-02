// 
// Decompiled by Procyon v0.5.30
// 

package com.unigrative.plugins.util.property.reader;

public final class IntegerReader implements Reader<Integer>
{
    @Override
    public Integer read(final String value) {
        try {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
}
