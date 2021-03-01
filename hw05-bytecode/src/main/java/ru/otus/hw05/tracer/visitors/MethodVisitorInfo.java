package ru.otus.hw05.tracer.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * To gather additional information of method.
 *
 * @see {@link ClassVisitorInfo} for usage
 *
 */
final class MethodVisitorInfo extends MethodVisitor 
{

    // list to hold methods annotations
    private final Set<String> annotations;

    // list to hold methods local varialbes
    private final List<String> localVariables;

    public MethodVisitorInfo( final int api ) {
        super(api);
        annotations = new HashSet<>();
        localVariables = new ArrayList<>();
    }

    /**
     * To populate method annotations list.
     *
     */
    @Override
    public AnnotationVisitor visitAnnotation( final String descriptor, final boolean visible ) {
        annotations.add( descriptor );
        return super.visitAnnotation( descriptor, visible );
    }

    /**
     * To populate local variables list.
     *
     */
    @Override
    public void visitLocalVariable(
            final String name,
            final String descriptor,
            final String signature,
            final Label start,
            final Label end,
            final int index) {
        this.localVariables.add( name );
            }

    /**
     * Determines whether method has an anootation
     *
     * @param annotationDescriptor to check
     * @return true if method contains anootation
     */
    public boolean hasAnnotation( final String annotationDescriptor ) {
        boolean result = false;
        result = this.annotations.stream().anyMatch( annotationDescriptor::equals );
        return result;
    }

    /**
     * Get local variables of the method (including parameters).
     *
     * @return list of variable names
     */
    public List<String> getLocalVariables() {
        return this.localVariables;
    }

}


