package ru.otus.hw05.tracer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import ru.otus.hw05.tracer.visitors.ClassVisitorInfo;

public class Agent {

    // api version of ASM
    private final static int ASM_API = Opcodes.ASM5;
    // annotation descriptor of methods that should be proxied
    private final static String PROXIED_ANNOTATION = "Lru/otus/hw05/tracer/annotations/Log;";
    // postfix of proxied methods
    private final static String PROXIED_POSTFIX = "_trace";
    // enumerator of instructions actions
    private static enum INST_ACTION { LOAD, STORE, RETURN };


    public static void premain(final String agentArgs, final Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(final ClassLoader loader, final String className,
                                    final Class<?> classBeingRedefined,
                                    final ProtectionDomain protectionDomain,
                                    final byte[] classfileBuffer) {
                return addProxyMethod(className, classfileBuffer);
            }
        });

    }

    private static byte[] addProxyMethod( final String className, final byte[] classfileBuffer ) {
        final ClassReader classReader = new ClassReader(classfileBuffer);
        final ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);

        // obtain class information
        final ClassVisitorInfo classVisitorInfo = new ClassVisitorInfo(ASM_API);
        classReader.accept(classVisitorInfo, ASM_API);
        // 

        // methods to be proxied
        final Set<Method> methods = classVisitorInfo.getMethodsWithAnnotation( PROXIED_ANNOTATION ).stream().filter( m -> isMethodToProxy(m) ).collect(Collectors.toSet());
        //
        
        // route method call to proxy
        final ClassVisitor classVisitor = new ClassVisitor(ASM_API, classWriter) {
            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
                String methodName = name;
                if ( methods.stream().anyMatch( m -> m.getName() == name && m.getDescriptor() == descriptor ) ) {
                    methodName = methodName + PROXIED_POSTFIX;
                }
                return super.visitMethod(access, methodName, descriptor, signature, exceptions);
            }
        };
        classReader.accept(classVisitor, ASM_API);
        //

        // build method wrappers
        for ( final Method method : methods )  {
            buildMethodWrapper( classWriter, className, method, PROXIED_POSTFIX, classVisitorInfo.getMethodParameters( method ) );
        }

        final byte[] finalClass = classWriter.toByteArray();

        return finalClass;
    }

    /**
     * Creates wrapper around original method to log call parameters and result values.
     * 
     * @param classWriter
     * @param className
     * @param method to be proxied
     * @param proxiedPostfix ending of the proxied method name
     * @param methodParameters list of method parameters in original order
     */
    private static void buildMethodWrapper( final ClassWriter classWriter, final String className, final Method method, final String proxiedPostfix, final List<String> methodParameters) {
        final String originalMethodName = method.getName();
        final String proxiedMethodName = method.getName() + proxiedPostfix;
        final String methodDescriptor = method.getDescriptor();
        final Type[] argumentTypes = method.getArgumentTypes();
        final int argumentCount = argumentTypes.length;
        final Type returnType = method.getReturnType();
        final String argumentsDescriptor = getParametersDescriptor( methodDescriptor );
        final MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, originalMethodName, methodDescriptor, null, null);
        final StringBuilder logMessageStringBuilder = new StringBuilder();

        final Handle handle = new Handle(
                Opcodes.H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                false);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // MESSAGE BEFORE CALL
        // create log message
        logMessageStringBuilder.append(originalMethodName+"(");
        logMessageStringBuilder.append( methodParameters.stream().map( str -> str+": "+ "'\u0001'" ).collect(Collectors.joining(", ")) );
        logMessageStringBuilder.append( ")" );
        // push arguments into stack
        for (int i = 0; i < argumentCount; i++) {
            mv.visitVarInsn( getVarOpcode( argumentTypes[i], INST_ACTION.LOAD ), i+1);
        } 
        // push message
        mv.visitInvokeDynamicInsn("makeConcatWithConstants", argumentsDescriptor+"Ljava/lang/String;", handle, "CALL "+logMessageStringBuilder.toString());
        // invoke println
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        //

        // ORIGINAL METHOD CALL
        // push all arguments back into stack
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        for (int i = 0; i < argumentCount; i++) {
            mv.visitVarInsn( getVarOpcode( argumentTypes[i], INST_ACTION.LOAD ), i+1);
        }
        // invoke original method
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, proxiedMethodName, methodDescriptor, false);
        //

        // handle results
        if ( returnType != Type.VOID_TYPE ) {
            mv.visitVarInsn( getVarOpcode( returnType, INST_ACTION.STORE ), argumentCount+1 );
            // MESSAGE AFTER CALL
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitVarInsn( getVarOpcode( returnType, INST_ACTION.LOAD ), argumentCount+1 );
            for (int i = 0; i < argumentCount; i++) {
                mv.visitVarInsn( getVarOpcode( argumentTypes[i], INST_ACTION.LOAD ), i+1);
            } 
            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "("+returnType+argumentsDescriptor.substring(1,argumentsDescriptor.length())+"Ljava/lang/String;", handle, "RET '\u0001' <- " + logMessageStringBuilder.toString());
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            //
            mv.visitVarInsn( getVarOpcode( returnType, INST_ACTION.LOAD ), argumentCount+1 );
        }

        mv.visitInsn( getVarOpcode( returnType, INST_ACTION.RETURN ) );
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    /**
     * Substring parameters part of the method descriptor (till the first right parenthesis).
     *
     * @param methodDescriptor
     * @return parameters descriptor
     */
    private static String getParametersDescriptor( final String methodDescriptor ) 
    {
        String result;
        final int endOfArguments = methodDescriptor.indexOf(")")+1;
        if ( endOfArguments > 0 ) {
            result = methodDescriptor.substring(0,endOfArguments);
        } else {
            throw new AssertionError("Cannot find end index of arguments in methodDescriptor="+methodDescriptor);
        }
        return result;
    }


    /**
     * Determine operation code depending on variable type and action.
     *
     * @param type of the variable
     * @param instAction instruction action
     * @return opcode
     */
    private static int getVarOpcode(final Type type, final INST_ACTION instAction) {
        int result = -1;
        switch ( type.getSort() ) {
            case Type.BOOLEAN:
            case Type.CHAR:
            case Type.BYTE:
            case Type.SHORT:
            case Type.INT:
                switch ( instAction ) {
                    case STORE:
                        result = Opcodes.ISTORE;
                        break;
                    case LOAD:
                        result = Opcodes.ILOAD;
                        break;
                    case RETURN:
                        result = Opcodes.IRETURN;
                        break;
                }
                break;
            case Type.FLOAT:
                switch ( instAction ) {
                    case STORE:
                        result = Opcodes.FSTORE;
                        break;
                    case LOAD:
                        result = Opcodes.FLOAD;
                        break;
                    case RETURN:
                        result = Opcodes.FRETURN;
                        break;
                }
                break;
            case Type.LONG:
                switch ( instAction ) {
                    case STORE:
                        result = Opcodes.LSTORE;
                        break;
                    case LOAD:
                        result = Opcodes.LLOAD;
                        break;
                    case RETURN:
                        result = Opcodes.LRETURN;
                        break;
                }
                break;
            case Type.DOUBLE:
                switch ( instAction ) {
                    case STORE:
                        result = Opcodes.DSTORE;
                        break;
                    case LOAD:
                        result = Opcodes.DLOAD;
                        break;
                    case RETURN:
                        result = Opcodes.DRETURN;
                        break;
                }
                break;
            case Type.ARRAY:
            case Type.OBJECT:
                switch ( instAction ) {
                    case STORE:
                        result = Opcodes.ASTORE;
                        break;
                    case LOAD:
                        result = Opcodes.ALOAD;
                        break;
                    case RETURN:
                        result = Opcodes.ARETURN;
                        break;
                }
                break;
            case Type.VOID:
                switch ( instAction ) {
                    case RETURN:
                        result = Opcodes.RETURN;
                        break;
                }
                break;
        }
        if ( result == -1 )  {
            throw new AssertionError("No opcode found for type="+type+" action="+instAction);
        }
        return result;
    }

    /**
     * Determines that the method could be proxied.
     *
     * @param method
     * @return
     */
    static private boolean isMethodToProxy( final Method method ) {
        boolean result = false;
        String methodName = method.getName();
        if ( !( methodName.contains("<init>") 
             || methodName.equals("main") 
             ) ) {
            result = true;
        }
        return result;
    }

}
