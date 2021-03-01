package ru.otus.hw05.tracer.visitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Method;

/**
 * {@link ClassVisitorInfo} is to gather and provide class metadata information.
 *
 */
public class ClassVisitorInfo extends ClassVisitor 
{

    // map to hold methods info
    private final Map<Method, MethodVisitorInfo> methods;

    public ClassVisitorInfo( final int api ) {
        super( api );
        this.methods = new HashMap<>();
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        final var key = new Method( name, descriptor );
        this.methods.put( key, new MethodVisitorInfo( this.api ) );
        return this.methods.get( key );
    }

    /**
     * Get methods get.
     *
     * @return set of methods
     */
    public Set<Method> getMethods() {
        return methods.keySet();
    }

    /**
     * Get methods set within specified annotation.
     *
     * @param annotationDescriptor to find
     * @return set of methods
     */
    public Set<Method> getMethodsWithAnnotation( final String annotationDescriptor ) {
        var result = new HashSet<Method>();
        for ( Map.Entry<Method, MethodVisitorInfo> methodEntry : methods.entrySet() )  {
            if ( methodEntry.getValue().hasAnnotation( annotationDescriptor ) ) {
                result.add( methodEntry.getKey() );
            }
        }
        return result;
    }

    /**
     * Get method parameters.
     * We use local variables instead of visitParameters method (which requires compilation with '-parameters' flag)
     *
     * @param method
     * @return list of names
     */
    public List<String> getMethodParameters( Method method ) {
        final List<String> result = new ArrayList<>();
        if ( this.methods.containsKey( method ) )  {
            result.addAll( this.methods.get( method ).getLocalVariables().subList(1, method.getArgumentTypes().length+1 ) );
        }
        return result;
    }

}


