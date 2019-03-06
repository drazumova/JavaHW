package com.task.kr2;

import org.junit.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class InjectorTest {

    @Test
    public void injectorShouldInitializeClassWithoutDependencies()
            throws Exception {
        Object object = Injector.initialize("com.task.kr2.ClassWithoutDependencies", Collections.emptyList());
        assertTrue(object instanceof ClassWithoutDependencies);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.task.kr2.ClassWithOneClassDependency",
                Collections.singletonList("com.task.kr2.ClassWithoutDependencies")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void injectorShouldInitializeClassWithOneClassDependencyByExtended()
            throws Exception {
        Object object = Injector.initialize(
                "com.task.kr2.ClassWithOneClassDependency",
                Collections.singletonList("com.task.kr2.ClassExtend")
        );
        assertTrue(object instanceof ClassWithOneClassDependency);
        ClassWithOneClassDependency instance = (ClassWithOneClassDependency) object;
        assertTrue(instance.dependency != null);
    }

    @Test
    public void ThrowCheckZeroParameters()
            throws Exception {
        assertThrows(ImplementationNotFoundException.class, () -> Injector.initialize(
                "com.task.kr2.ClassWithOneClassDependency",
                Collections.emptyList()));
    }

    @Test
    public void ThrowCheckManyParameters()
            throws Exception {
        assertThrows(AmbiguousImplementationException.class, () -> Injector.initialize(
                "com.task.kr2.ClassWithOneClassDependency",
                List.of("com.task.kr2.ClassExtend", "com.task.kr2.ClassExtend")));
    }

    @Test
    public void injectorShouldInitializeClassWithOneInterfaceDependency()
            throws Exception {
        Object object = Injector.initialize(
                "com.task.kr2.ClassWithOneInterfaceDependency",
                Collections.singletonList("com.task.kr2.InterfaceImpl")
        );
        assertTrue(object instanceof ClassWithOneInterfaceDependency);
        ClassWithOneInterfaceDependency instance = (ClassWithOneInterfaceDependency) object;
        assertTrue(instance.dependency instanceof InterfaceImpl);
    }
}