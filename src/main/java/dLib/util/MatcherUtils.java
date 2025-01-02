package dLib.util;

import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.bytecode.*;

public class MatcherUtils {
    public static int getSuperCallLineNumber(CtMethod ctMethod) {
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LineNumberAttribute lineNumberAttribute = (LineNumberAttribute) codeAttribute.getAttribute(LineNumberAttribute.tag);

        CodeIterator codeIterator = codeAttribute.iterator();

        int superCallOffset = -1;

        try {
            while (codeIterator.hasNext()) {
                int index = codeIterator.next();
                int opcode = codeIterator.byteAt(index);

                if (opcode == Opcode.INVOKESPECIAL) {
                    superCallOffset = index;
                    break;
                }
            }
        } catch (BadBytecode e) {
            e.printStackTrace();
        }

        if (superCallOffset != -1 && lineNumberAttribute != null) {
            int lineNumber = lineNumberAttribute.toLineNumber(superCallOffset);
            return lineNumber;
        }

        return -1;
    }

    public static boolean callsSiblingConstructor(CtConstructor constructor) throws BadBytecode {
        MethodInfo methodInfo = constructor.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();

        if (codeAttribute == null) {
            return false;
        }

        CodeIterator codeIterator = codeAttribute.iterator();
        while (codeIterator.hasNext()) {
            int index = codeIterator.next();
            int op = codeIterator.byteAt(index);

            if (op == Opcode.INVOKESPECIAL) {
                int methodRefIndex = codeIterator.u16bitAt(index + 1);
                ConstPool constPool = methodInfo.getConstPool();
                String methodName = constPool.getMethodrefName(methodRefIndex);
                String className = constPool.getMethodrefClassName(methodRefIndex);

                if (methodName.equals("<init>") && className.equals(constructor.getDeclaringClass().getName())) {
                    int pos = codeIterator.lookAhead();
                    if (pos > 3) {
                        return false;
                    }
                    return true;
                }
            }
        }

        return false;
    }
}
