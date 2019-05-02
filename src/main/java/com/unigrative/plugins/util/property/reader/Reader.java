
package com.unigrative.plugins.util.property.reader;

@FunctionalInterface
public interface Reader<T>
{
    T read(final String p0);
}
