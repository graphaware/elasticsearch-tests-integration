/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of the
 * GNU General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.graphaware.integration.es.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An invocation handler that passes on any calls made to it directly to its
 * delegate. This is useful to handle identical classes loaded in different
 * classloaders - the VM treats them as different classes, but they have
 * identical signatures.
 *
 * Note this is using class.getMethod, which will only work on public methods.
 */
public class PassThroughProxyHandler implements InvocationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PassThroughProxyHandler.class);
    private final Object delegate;

    public PassThroughProxyHandler(Object delegate) {
        this.delegate = delegate;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            Method delegateMethod = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
            return delegateMethod.invoke(delegate, args);
        }
        catch (NoSuchMethodException ex) {
            LOG.error("Error during method delegation. Available methods are:");
            for (Method methodItem : delegate.getClass().getMethods()) {
                Class<?>[] parameterTypes = methodItem.getParameterTypes();
                String types = "";
                for (Class param: parameterTypes)
                  types += param.getName() + ", ";
                LOG.warn(methodItem.getName() + ": " + types);
            }
            
            throw ex;
        }
    }
}
