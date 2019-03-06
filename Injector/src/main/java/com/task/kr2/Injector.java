package com.task.kr2;

import java.util.*;


public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */
    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {
        var listClasses = new ArrayList<Class<?>>();
        for (var name : implementationClassNames) {
               listClasses.add(Class.forName(name));
        }

        var clazz = Class.forName(rootClassName);
        var constr = clazz.getDeclaredConstructors()[0];
        var parameters = new ArrayList<>();
        var paramList = constr.getParameterTypes();
        if (paramList.length == 0) {
               return constr.newInstance();
        }

        for (var kek : constr.getParameterTypes()) {
            int counter = 0;
            for (var impl : listClasses) {
                if (kek.isInterface()) {
                    for (var intrf : impl.getInterfaces()) {
                        if (kek.equals(intrf)) {
                            counter++;
                        }
                    }
                }
                else if (impl.equals(kek)) {
                   counter++;
                }
            }

            if (counter == 0) {
                   throw new ImplementationNotFoundException();
            }

            if (counter > 1) {
               throw new AmbiguousImplementationException();
            }

            for (var impl : listClasses) {
                if (kek.isInterface()) {
                    for (var intrf : impl.getInterfaces()) {
                        if (kek.equals(intrf)) {
                            parameters.add(initialize(impl.getName(), implementationClassNames));
                        }
                    }
                }
                else if (impl.equals(kek)) {
                    parameters.add(initialize(impl.getName(), implementationClassNames));
                }
            }

       }

       return constr.newInstance(parameters.toArray());
    }
}