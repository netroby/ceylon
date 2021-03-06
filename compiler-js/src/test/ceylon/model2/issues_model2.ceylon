import ceylon.language.meta.declaration {
  FunctionDeclaration, ClassOrInterfaceDeclaration, Package, ValueDeclaration
}
import ceylon.language.meta.model {
  MemberClass
}
import ceylon.language.meta { type }

import natest { fun661=f }
import check { check, fail }

shared class Fuera() {
  shared class Dentro() {
    shared void f() {}
    shared class Cebolla() {
      shared void g() {}
    }
  }
  shared interface Vacia {}
}

shared annotation DumbAnnotation dumb() => DumbAnnotation();

shared final annotation class DumbAnnotation()
        satisfies OptionalAnnotation<DumbAnnotation,FunctionDeclaration> {}

shared class Padre() {
  shared void a() {}
  shared dumb void b() {}
}
shared class Hijo() extends Padre() {
  shared void c() {}
  shared dumb void d() {}
}
class Issue669<Z,A>() {
  shared class Middle<T>() {
    shared class Inner(){}
  }
}

void foo666(List<Integer> l) {
    `function bar666`.invoke([], l);
}
void bar666(List<Integer> l) {
    check(l.size == 1, "#666");
}

void foo671(Alias671 x) {
  `function bar671`.invoke([],x);
}
void bar671(Alias671 x) {
  check(x.size == 1, "#671.1");
  if (exists f=x.first) {
    check(f==1, "#671.2");
  } else {
    fail("#671.3");
  }
}
interface Alias671 => List<Integer>;

interface I5871 {}
class C5871 {
    shared new() {}
    shared variable Integer x = 0;
    shared new cc() {}
    shared new vc {}

    shared Object getAttributeModel() => `x`;
    shared Object getClassModel() => `C5871`;
    shared Object getIfaceModel() => `I5871`;
    shared Object getCons1Model() => `cc`;
    shared Object getCons2Model() => `vc`;
    shared Object getMifaceModel() => `Bar`;
    shared Object getMclassModel() => `Baz`;

    shared interface Bar {}
    shared class Baz {
      shared new(){}
      shared new icc() {}
      shared new ivc {}
      shared Object getCons1Model() => `icc`;
      shared Object getCons2Model() => `ivc`;
    }
}

void check5871(Object a, Object b) {
  check(className(a)==className(b), "#5871.1 classnames don't match ``className(a)`` vs ``className(b)``");
  check(a==b, "#5871.2 ``a`` vs ``b``");
  check(a.hash == b.hash, "hashes not equal ``a.hash`` vs ``b.hash``");
}

void issues() {
    try {
        value g1 = `function Fuera.Dentro.Cebolla.g`;
        value g2 = `Fuera.Dentro.Cebolla.g`;
        check(g1.name == "g", "Member method of member type declaration");
        check(g2.declaration.name == "g", "Member method of member type");
        value methods = `class Hijo`.annotatedMemberDeclarations<FunctionDeclaration,DumbAnnotation>();
        value declMethods = `class Hijo`.annotatedDeclaredMemberDeclarations<FunctionDeclaration,DumbAnnotation>();
        check(methods.size==2, "annotatedMemberDeclarations expected 2, got ``methods.size``: ``methods``");
        check(declMethods.size==1, "annotatedMemberDeclarations expected 2, got ``declMethods.size``: ``declMethods``");
        value types = `class Fuera`.memberDeclarations<ClassOrInterfaceDeclaration>();
        check(types.size==2, "member types expected 2, got ``types.size``: ``types``");
    } catch (Throwable e) {
        if ("Cannot read property '$$' of undefined" in e.message) {
            fail("Member declaration tests won't work in lexical scope style");
        } else {
            fail("Something bad: ``e.message``");
            e.printStackTrace();
        }
    }
    value module489=`module nesting`;
    check(module489.string=="module nesting/0.1", "#489.1 module is ' ``module489.string``'");
    value packs489=module489.members;
    check(packs489.size==2, "#489.2");
    void checkPackage(Package p) {
        value funs=p.members<FunctionDeclaration>();
        check(funs.size>=2, "#489.5 expected 2 functions found ``funs``");
        value vals=p.members<ValueDeclaration>();
        check(vals.size>=4, "#489.6 expected 4 values found ``vals``");
        value typs=p.members<ClassOrInterfaceDeclaration>();
        check(typs.size>=2, "#489.7 expected 2 classes found ``typs``");
    }
    if (exists rootp489=packs489.find((p)=>p.name=="nesting")) {
        checkPackage(rootp489);
    } else {
        fail("#489.3 package 'nesting' not found");
    }
    if (exists subp489=packs489.find((p)=>p.name=="nesting.sub")) {
        checkPackage(subp489);
    } else {
        fail("#489.4 package 'nesting.sub' not found");
    }
    value model669 = type(Issue669<String,Integer>().Middle<Float>().Inner());
    assert (is MemberClass<Issue669<String,Integer>.Middle<Float>,Issue669<String,Integer>.Middle<Float>.Inner,[]> model669);
    if (exists c669=model669.container) {
      check(c669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>", "langmod #669.1 says ``c669``");
    } else {
      fail("langmod #669 model669.container should exist");
    }
    check(model669.declaringType.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>", "langmod #669.2 says ``model669.declaringType``");
    check(model669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>.Inner", "langmod #669.3 says ``model669``");
    value static669=`Issue669<String,Integer>.Middle<Float>.Inner`;
    check(static669.string == "model2::Issue669<ceylon.language::String,ceylon.language::Integer>.Middle<ceylon.language::Float>.Inner", "langmod #669.4 says ``static669``");
    check("ceylon.language::Integer," in type([].withTrailing(1).withTrailing(1).withTrailing(1)).string, "lang#703.1");
    variable [Integer*] issue703=[];
    issue703=issue703.withTrailing(1);
    issue703=issue703.withTrailing(1);
    issue703=issue703.withTrailing(1);
    check("ceylon.language::Integer," in type(issue703).string, "lang#703.2");
    check(type(identity<Integer>).string=="ceylon.language::identity<ceylon.language::Integer>", "#591");
    //ceylon.language#724
    Object a724 = 1;
    Object b724 = 2;
    if (is Comparison c724=`function Comparable.compare`.memberInvoke(a724, [], b724)) {
        check(c724==smaller, "c.l#724 expected smaller got ``c724``");
    } else {
        fail("c.l#724 expected Comparison");
    }
    //543
    check(`module nesting`.annotations<AuthorsAnnotation>() nonempty, "#543.1");
    if (exists p543=`module nesting`.members.first) {
      check(p543.annotations<AuthorsAnnotation>() nonempty, "#543.2 missing 'by' annotation on ``p543``");
    } else {
      fail("WTF #543");
    }
    if (nonempty anns661 = `function fun661`.annotations<NativeAnnotation>()) {
        check(anns661.first.backends.size==2, "native #661");
    } else {
        fail("Annotations #661");
    }
    foo666([1]);
    foo671([1]);
    class Issue5901Def(String s, Integer i){}
    class Issue5901Cons {
      shared new(String s, Integer i){}
    }
    value def5901 = `Issue5901Def`.defaultConstructor?.parameterTypes;
    value cons5901 = `Issue5901Cons`.defaultConstructor?.parameterTypes;
    if (exists x=def5901) {
      check(x.size==2, "#5901.1 expected 2 params");
    } else {
      fail("#5901.1 constructor not found");
    }
    if (exists x=cons5901) {
      check(x.size==2, "#5901.2 expected 2 params");
    } else {
      fail("#5901.2 constructor not found");
    }
    check5871(`C5871`, C5871().getClassModel());
    check5871(`I5871`, C5871().getIfaceModel());
    check5871(`C5871.x`, C5871().getAttributeModel());
    check5871(`C5871.cc`, C5871().getCons1Model());
    check5871(`C5871.vc`, C5871().getCons2Model());
    check5871(`C5871.Bar`, C5871().getMifaceModel());
    check5871(`C5871.Baz`, C5871().getMclassModel());
    check5871(`C5871.Baz.icc`, C5871().Baz().getCons1Model());
    check5871(`C5871.Baz.ivc`, C5871().Baz().getCons2Model());

    class C6363 {
        shared new c {}
        shared new d() {}
    }
    check(`C6363`.getValueConstructors() nonempty, "#6363.1");
    check(`C6363`.getCallableConstructors() nonempty, "#6363.2");
}
