package com.fido.common.common_utils.test.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;

/**
 * @author: FiDo
 * @date: 2024/4/9
 * @des:
 */
public class ClassReaderUtils {

    public static void readClassMethodAndFiled(String clzFilePath){
        try {
            System.out.println("ClassReaderUtils readClassMethodAndFiled Start =============================");
            ClassReader classReader = new ClassReader(new FileInputStream(clzFilePath));
            ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                    System.out.println("visit method:" + name + " , desc = " + descriptor + " ,signature=" + signature);
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }

                @Override
                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                    System.out.println("visit field:" + name + " , desc = " + descriptor + " ,signature=" + signature);
                    return super.visitField(access, name, descriptor, signature, value);
                }
            };
            classReader.accept(classVisitor,0);
            System.out.println("ClassReaderUtils readClassMethodAndFiled End  =============================");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
