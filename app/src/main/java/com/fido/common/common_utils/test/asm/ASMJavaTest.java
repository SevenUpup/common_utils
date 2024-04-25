package com.fido.common.common_utils.test.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import com.fido.common.common_utils.asm.ASMTestAc;
import com.fido.common.common_utils.test.java.Sington;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author: FiDo
 * @date: 2024/4/9
 * @des:
 */
public class ASMJavaTest {

    public static void main(String[] args) throws Exception {

        char[] arr = {'A', '*', '*', 'B', '*', 'C', 'C', 'C', '*', 'D', '*', 'E', 'E', 'E', '*', '*'};
        int n = arr.length;
        int writeIndex = 0;

        for (int i = 0; i < n; i++) {
            if (arr[i] != '*') {
                char ch = arr[i];
                int count = 0;
                while (i < n && arr[i] == ch) {
                    count++;
                    i++;
                }
                i--;
                for (int j = 0; j < count; j++) {
                    arr[writeIndex++] = ch;
                }
            } else {
                if (writeIndex > 0) {
                    arr[writeIndex++] = arr[writeIndex - 1];
                }
            }
        }

        for (int i = 0; i < writeIndex; i++) {
            System.out.print(arr[i] + " ");
        }

//        for (int i = 0; i < 10; i++) {
//            if (i == 5)
//                continue;
//            if (i == 8){
//                return;
//            }
//            System.out.println("i="+i);
//        }

        TimeAsmView.m();

        Class clazz = Sington.class;
        String clazzFilePath = getClassFilePath(clazz);
        ClassReader classReader = new ClassReader(new FileInputStream(clazzFilePath));
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//                System.out.println("visit field:" + name + " , desc = " + descriptor);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                System.out.println("visit method:" + name + " , desc = " + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }

        };
        classReader.accept(classVisitor, 0);

        Class originalClz = ASMTestAc.class;
        String originalPath = getClassFilePath(originalClz);
        String prefixPath = "G:\\PraticeDemo\\common_utils\\app\\src\\main\\java\\com\\fido\\common\\common_utils\\test\\asm\\";
        ClassReader reader = new ClassReader(new FileInputStream(originalPath));
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        AddTimerClassVisitor addTimerClassVisitor = new AddTimerClassVisitor(Opcodes.ASM9,classWriter);
        reader.accept(addTimerClassVisitor,0);

        //写入文件
        byte[] byteArray = classWriter.toByteArray();
//        System.out.println("byteArray=" + byteArray.length);
        FileOutputStream fos = new FileOutputStream(prefixPath + "copy.class");
        fos.write(byteArray);
        fos.flush();
        fos.close();

        ClassReaderUtils.readClassMethodAndFiled(prefixPath + "copy.class");
    }


    /**
     * 获取 class 在 运行时的绝对路劲
     * @param clazz
     * @return
     */
    public static String getClassFilePath(Class clazz) {
        // file:/Users/zhy/hongyang/repo/BlogDemo/app/build/intermediates/javac/debug/classes/
        String buildDir = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        String fileName = clazz.getSimpleName() + ".class";
        File file = new File(buildDir + clazz.getPackage().getName().replaceAll("[.]", "/") + "/", fileName);
        return file.getAbsolutePath();
    }

    private static boolean isStaticField(int acc){
        if (acc >= (Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC) && acc <= (Opcodes.ACC_PROTECTED + Opcodes.ACC_STATIC)) {
            return true;
        }
        return false;
    }
    // 字节码添加 timer 字段
    static class AddTimerClassVisitor extends ClassVisitor{

        private String mOwner;

        public AddTimerClassVisitor(int api, ClassVisitor classVisitor) {
            super(api, classVisitor);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            mOwner = name;
        }

        @Override
        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//            System.out.println("find hook name="+name + " value=" + value + " access=" + access + " descriptor=" + descriptor + " signature=" + signature + " access=" + access + " isStaticField=" + isStaticField(access));
//            if (Objects.equals(name, "AA")){
//                System.out.println("find hook name="+name + " value=" + value + " access=" + access);
//                return super.visitField(access, name, descriptor, signature, "apple");
//            }
//            if ("BB".equals(name)){
//                System.out.println("find hook name="+name + " value=" + value+ " access=" + access);
//                return super.visitField(access, name, descriptor, signature, "banana");
//            }
//            if (name.equals("CC")){
//                System.out.println("find hook name="+name + " value=" + value+ " access=" + access);
//                return super.visitField(access, name, descriptor, signature, 99);
//            }
//            if (name.equals("EE")){
//                System.out.println("find hook name="+name + " value=" + value+ " access=" + access);
//                return super.visitField(access, name, descriptor, signature, true);
//            }
            if (name.equals("INT_VAL")){
                System.out.println("find hook name="+name + " value=" + value+ " access=" + access);
//                return super.visitField(access, name, descriptor, signature, 999);
            }
            return super.visitField(access, name, descriptor, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

//            System.out.println("visit method:" + name + " , desc = " + descriptor);

//            if (methodVisitor != null && name.equals("<clinit>")) {
//                ClInitMethodVisitor visitor = new ClInitMethodVisitor(api, methodVisitor);
//                visitor.mOwner = mOwner;
//                return visitor;
//            }

            String methodName = name;
            if (methodVisitor != null && name.equals("<init>")) {
                MethodVisitor initMethodVisitor = new MethodVisitor(api,methodVisitor) {

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        super.visitFieldInsn(opcode, owner, name, descriptor);

                        System.out.println("<init> visitFieldInsn opcode = " + opcode + " name=" + name);

                        if (name.equals("AA")) {
                            //赋值成员字段
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitLdcInsn("hello world"); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTFIELD, mOwner, "AA", "Ljava/lang/String;"); //将hello world赋值给HelloWorld的字段word
                        }else if (name.equals("BB")) {
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitLdcInsn("hello world BB"); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTFIELD, mOwner, "BB", "Ljava/lang/String;"); //将hello world赋值给HelloWorld的字段word
                        } else if (name.equals("F_Double")) {
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitLdcInsn(999.0); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTFIELD, mOwner, "F_Double", "D"); //将hello world赋值给HelloWorld的字段word
                        }else if (name.equals("P_INT")) {
                            mv.visitVarInsn(ALOAD, 0);
                            mv.visitLdcInsn(999); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTFIELD, mOwner, "P_INT", "I"); //将hello world赋值给HelloWorld的字段word
                        }
//                        else if (name.equals("INT_VAL")) {
//                            mv.visitVarInsn(ALOAD, 0);
//                            mv.visitLdcInsn(999); //通过LDC指令将常量hello world压入栈顶
//                            mv.visitFieldInsn(PUTFIELD, mOwner, name, "I"); //将hello world赋值给HelloWorld的字段word
//                        }

                    }

                };
                return initMethodVisitor;
            }

            if (methodVisitor != null && name.equals("<clinit>")) {
                MethodVisitor newMethodVisitor = new MethodVisitor(api, methodVisitor) {

                    @Override
                    public void visitCode() {
                        mv.visitCode();

                        System.out.println("=================== mOwner = " + mOwner);

                        mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                                "currentTimeMillis", "()J");
//                        mv.visitMethodInsn(INVOKESTATIC, "android/os/SystemClock",
//                                "elapsedRealtime", "()J");
                        mv.visitInsn(LSUB);
                        mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");

                    }


                    @Override
                    public void visitInsn(int opcode) {

//                        System.out.println("=====visitInsn=====");
                        if (name.equals("<clinit>")) {
                            System.out.println("<clinit> opcode="+opcode);
                            //赋值静态字段
                            mv.visitLdcInsn(666); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTSTATIC, mOwner, "CC", "I"); //将hello world赋值给HelloWorld的静态字段word

                            //赋值静态字段
                            mv.visitLdcInsn(-10086); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTSTATIC, mOwner, "K", "I"); //将hello world赋值给HelloWorld的静态字段word


                            //赋值静态字段
                            mv.visitLdcInsn(66.6); //通过LDC指令将常量hello world压入栈顶
                            mv.visitFieldInsn(PUTSTATIC, mOwner, "pLong", "D"); //将hello world赋值给HelloWorld的静态字段word

//                            mv.visitLdcInsn(999);
//                            mv.visitFieldInsn(PUTSTATIC,mOwner,"INT_VAL","I");
                        }

                        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                            mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                                    "currentTimeMillis", "()J");
                            mv.visitInsn(LADD);
                            mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");
                        }
                        mv.visitInsn(opcode);
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        super.visitFieldInsn(opcode, owner, name, descriptor);
                        System.out.println("=======visitFieldInsn methodName =" + methodName + "opcode = " + opcode + " owner=" + owner + " name=" + name + " descriptor=" + descriptor);
                    }

                };
                return newMethodVisitor;
            }

            return methodVisitor;
        }

        @Override
        public void visitEnd() {

            FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "timer",
                    "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }

            FieldVisitor fieldVisitor = cv.visitField(Opcodes.ACC_PUBLIC, "testA", "I", null, 10);
            if (fieldVisitor != null) {
                fieldVisitor.visitEnd();
            }



            cv.visitEnd();
        }
    }

    private static class ClInitMethodVisitor extends MethodVisitor{

        public String mOwner;
        protected ClInitMethodVisitor(int api) {
            super(api);
        }

        protected ClInitMethodVisitor(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            super.visitFieldInsn(opcode, owner, name, descriptor);
//            //赋值静态字段
            mv.visitLdcInsn(666); //通过LDC指令将常量hello world压入栈顶
            mv.visitFieldInsn(PUTSTATIC, mOwner, "CC", "I"); //将hello world赋值给HelloWorld的静态字段word

        }
    }

}
