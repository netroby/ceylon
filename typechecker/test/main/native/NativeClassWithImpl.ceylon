shared class NativeClassWithImplBase(Integer i) {
    shared Integer base() => 0;
}

native shared class NativeClassWithImpl(Integer x, Integer y)
        extends NativeClassWithImplBase(x) {
    native shared void test() {}
    native shared Integer foo => 0;
    native shared Integer bar => 0;
    native assign bar {}
    native shared void test2() {}
    shared void nat() {}
}

native("jvm") shared class NativeClassWithImpl(Integer x, Integer y)
        extends NativeClassWithImplBase(x) {
    void jvmimpl() {}
    native("jvm") shared void test2() {
        base();
        test();
        nat();
        jvmimpl();
    }
}

native("js") shared class NativeClassWithImpl(Integer x, Integer y)
        extends NativeClassWithImplBase(x) {
    void jsimpl() {}
    native("js") shared void test2() {
        base();
        test();
        nat();
        jsimpl();
    }
}

native shared class NativeClassWithImpl2(Integer x, Integer y) {
    native shared void test() {}
    native shared Integer foo => 0;
    native shared Integer bar => 0;
    native assign bar {}
    native shared void test2() {}
    shared void nat() {}
}

native("jvm") shared class NativeClassWithImpl2(Integer x, Integer y) {
    void jvmimpl() {}
    native("jvm") shared void test2() {
        test();
        nat();
        jvmimpl();
    }
}

native("js") shared class NativeClassWithImpl2(Integer x, Integer y) {
    void jsimpl() {}
    native("js") shared void test2() {
        test();
        nat();
        jsimpl();
    }
}


native shared class NativeClassWithImpl3(Integer x, Integer y) {
    native shared void test() { privmeth(); }
    native shared Integer foo => privattr;
    native shared void test2();
    native void privmeth() {}
    native Integer privattr => 0;
}

native("jvm") shared class NativeClassWithImpl3(Integer x, Integer y) {
    native("jvm") shared void test2() {
        test();
        value x = foo;
    }
}

native("js") shared class NativeClassWithImpl3(Integer x, Integer y) {
    native("js") shared void test2() {
        test();
        value x = foo;
    }
}


native shared class NativeClassWithImpl4(Integer x, Integer y) {
    native shared void test() { privmeth(); }
    native shared Integer foo => privattr;
    native shared void test2();
    native void privmeth();
    native Integer privattr;
}

native("jvm") shared class NativeClassWithImpl4(Integer x, Integer y) {
    native("jvm") shared void test2() {
        test();
        value x = foo;
    }
    native("jvm") void privmeth() {}
    native("jvm") Integer privattr => 0;
}

native("js") shared class NativeClassWithImpl4(Integer x, Integer y) {
    native("js") shared void test2() {
        test();
        value x = foo;
    }
    native("js") void privmeth() {}
    native("js") Integer privattr => 0;
}


void testNativeClassWithImpl() {
    value klz = NativeClassWithImpl(1, 2);
    value a = klz.base();
    klz.test();
    value c = klz.foo;
    value d = klz.bar;
    klz.bar = a;
    klz.test2();
    
    value klz2 = NativeClassWithImpl2(1, 2);
    klz2.test();
    value c2 = klz2.foo;
    value d2 = klz2.bar;
    klz2.bar = a;
    klz2.test2();
    
    value klz3 = NativeClassWithImpl3(1, 2);
    klz3.test2();
    
    value klz4 = NativeClassWithImpl4(1, 2);
    klz4.test2();
    
    throw Exception("NativeClassWithImpl-JVM");
}
