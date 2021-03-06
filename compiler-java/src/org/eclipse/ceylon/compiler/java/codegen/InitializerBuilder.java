/*
 * Copyright Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the authors tag. All rights reserved.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License version 2.
 * 
 * This particular file is subject to the "Classpath" exception as provided in the 
 * LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.eclipse.ceylon.compiler.java.codegen;

import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PRIVATE;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PROTECTED;
import static org.eclipse.ceylon.langtools.tools.javac.code.Flags.PUBLIC;

import org.eclipse.ceylon.langtools.tools.javac.code.Flags;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree;
import org.eclipse.ceylon.langtools.tools.javac.tree.TreeCopier;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCAnnotation;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCMethodDecl;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCStatement;
import org.eclipse.ceylon.langtools.tools.javac.tree.JCTree.JCThrow;
import org.eclipse.ceylon.langtools.tools.javac.util.List;
import org.eclipse.ceylon.langtools.tools.javac.util.ListBuffer;
import org.eclipse.ceylon.model.typechecker.model.Constructor;

public class InitializerBuilder implements ParameterizedBuilder<InitializerBuilder> {

    private final AbstractTransformer gen;
    private long modifiers = 0;
    // TODO remove this field
    private JCStatement delegateCall;
    private final ListBuffer<ParameterDefinitionBuilder> params = new ListBuffer<ParameterDefinitionBuilder>();
    private boolean deprecated = false;
    /** 
     * For classes with parameter lists this is a {@code List<JCStatement>}.
     * For classes with constructors it's a 
     * {@code List<JCStatement|Tree.Constructor>} and this we know which 
     * statements need to be prepended to each transformed constructor body
     * and which need to be appended. 
     */
    private final java.util.List<Object/* JCStatement|Constructor*/> init = new java.util.ArrayList<Object>();
    private final ListBuffer<JCAnnotation> userAnnos = new ListBuffer<JCAnnotation>();
    
    public InitializerBuilder(AbstractTransformer gen) {
        this.gen = gen;
    }
    
    /** Only called for classes with parameter lists */
    JCMethodDecl build() {
        if (delegateCall != null/* && !isAlias*/) {
            init.add(0, delegateCall);
        }
        List<JCStatement> body = statementsBetween(null, null);
        int index = 0;
        for (JCStatement stmt : body) {
            if (stmt instanceof JCThrow) {
                ListBuffer<JCStatement> filtered = new ListBuffer<JCStatement>();
                filtered.addAll(body.subList(0, index+1));
                body = filtered.toList();
                break;
            }
            index++;
        }
        MethodDefinitionBuilder constructor = MethodDefinitionBuilder.constructor(gen, deprecated);
        constructor.modifiers(modifiers)
            .userAnnotations(userAnnos.toList())
            .parameters(params.toList())
            .body(body);
        return constructor.build();
    }
    

    public InitializerBuilder modifiers(long mods) {
        if ((mods & ~(PUBLIC|PRIVATE|PROTECTED)) != 0) {
            throw new BugException("illegal modifier for constructor " + Flags.toString(mods));
        }
        this.modifiers = mods;
        return this;
    }
    
    public InitializerBuilder userAnnotations(List<JCAnnotation> annos) {
        this.userAnnos.addAll(annos);
        return this;
    }
    

    // Create a parameter for the constructor
    public InitializerBuilder parameter(ParameterDefinitionBuilder pdb) {
        params.append(pdb);
        return this;
    }
    
    public InitializerBuilder init(JCStatement statement) {
        if (statement != null) {
            this.init.add(statement);
        }
        return this;
    }
    
    public InitializerBuilder init(List<JCStatement> init) {
        if (init != null) {
            this.init.addAll(init);
        }
        return this;
    }
    
    public InitializerBuilder constructor(
            org.eclipse.ceylon.compiler.typechecker.tree.Tree.Constructor ctor) {
        if (ctor != null) {
            this.init.add(ctor.getConstructor());
        }
        return this;
    }
    
    public InitializerBuilder singleton(
            org.eclipse.ceylon.compiler.typechecker.tree.Tree.Enumerated ctor) {
        if (ctor != null) {
            this.init.add(ctor.getEnumerated());
        }
        return this;
    }

    /** 
     * Set the expression used to invoke {@code super()} or {@code this()}.
     * (i.e. delegate to another constructor). 
     */
    public InitializerBuilder delegateCall(JCStatement delegateCall) {
        // TODO remove this method
        this.delegateCall = delegateCall;
        return this;
    }
    

    public boolean isEmptyInit() {
        return init.isEmpty();
    }
    
    private List<JCStatement> statementsBetween(Constructor first, Constructor second) {
        ListBuffer<JCStatement> buffer = new ListBuffer<JCStatement>();
        boolean found = first == null;
        for (Object o : this.init) {
            if (found && o instanceof JCStatement) {
                buffer.add((JCStatement)o);
            } else if (first != null && first.equals(o)){
                found = true;
            } else if (second != null && second.equals(o)){
                break;
            }
        }
        return buffer.toList();
    }
    private <T extends JCTree> List<T> copyOf(List<T> list) {
        return new TreeCopier<T>(gen.make()).copy(list);
    }
    List<JCStatement> copyStatementsBetween(Constructor first, Constructor second) {
        return copyOf(statementsBetween(first, second));
    }
    
    public List<ParameterDefinitionBuilder> getParameterList() {
        return params.toList();
    }

    public void deprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

}
