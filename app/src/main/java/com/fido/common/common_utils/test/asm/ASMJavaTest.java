package com.fido.common.common_utils.test.asm;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LADD;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.PUTSTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

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

        for (int i = 0; i < 10; i++) {
            if (i == 5)
                continue;
            if (i == 8){
                return;
            }
            System.out.println("i="+i);
        }

        TimeAsmView.m();

        Class clazz = Sington.class;
        String clazzFilePath = getClassFilePath(clazz);
        ClassReader classReader = new ClassReader(new FileInputStream(clazzFilePath));
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                System.out.println("visit field:" + name + " , desc = " + descriptor);
                return super.visitField(access, name, descriptor, signature, value);
            }

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                System.out.println("visit method:" + name + " , desc = " + descriptor);
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        classReader.accept(classVisitor, 0);

        Class originalClz = Sington.class;
        String originalPath = getClassFilePath(originalClz);
        String prefixPath = "G:\\PraticeDemo\\common_utils\\app\\src\\main\\java\\com\\fido\\common\\common_utils\\test\\asm\\";
        ClassReader reader = new ClassReader(new FileInputStream(originalPath));
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        AddTimerClassVisitor addTimerClassVisitor = new AddTimerClassVisitor(Opcodes.ASM9,classWriter);
        reader.accept(addTimerClassVisitor,0);

        //写入文件
        byte[] byteArray = classWriter.toByteArray();
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
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

            if (methodVisitor != null && !name.equals("<init>")) {
                MethodVisitor newMethodVisitor = new MethodVisitor(api, methodVisitor) {
                    @Override
                    public void visitCode() {
                        mv.visitCode();

                        System.out.println("=================== mOwner = " + mOwner);

                        mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
                        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                                "currentTimeMillis", "()J");
                        mv.visitInsn(LSUB);
                        mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");

                    }

                    @Override
                    public void visitInsn(int opcode) {

                        if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                            mv.visitFieldInsn(GETSTATIC, mOwner, "timer", "J");
                            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
                                    "currentTimeMillis", "()J");
                            mv.visitInsn(LADD);
                            mv.visitFieldInsn(PUTSTATIC, mOwner, "timer", "J");
                        }
                        mv.visitInsn(opcode);

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
            cv.visitEnd();
        }
    }

}
