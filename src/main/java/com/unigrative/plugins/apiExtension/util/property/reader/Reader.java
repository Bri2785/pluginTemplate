
package com.unigrative.plugins.apiExtension.util.property.reader;

@FunctionalInterface
public interface Reader<T>
{
    T read(final String p0);
}
