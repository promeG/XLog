package com.promegu.xlog.processor;

import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLog;
import com.promegu.xlog.base.XLogUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.JavaFileObject;

public final class XLogProcessor extends AbstractProcessor {

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<String>();
        supportTypes.add(XLog.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) {
        System.out.println("processing: " + env.toString());
        List<MethodToLog> methodToLogs = new ArrayList<>();
        List<String> classNames = new ArrayList<>();

        for (Element element : env.getElementsAnnotatedWith(XLog.class)) {
            if (element.getKind() != ElementKind.METHOD
                    && element.getKind() != ElementKind.CONSTRUCTOR
                    && element.getKind() != ElementKind.CLASS) {
                throw new IllegalStateException(
                        String.format("@%s annotation must be on as method, constructor or class.",
                                element.getSimpleName()));
            }
            if (element instanceof TypeElement) {
                // class
                String pkgName = ((TypeElement) element).getQualifiedName().toString();
                if (!classNames.contains(pkgName)) {
                    classNames.add(pkgName);
                }
            } else if (element instanceof ExecutableElement) {
                // method or constructor
                ExecutableElement e = (ExecutableElement) element;

                int type = XLogUtils.TYPE_METHOD;
                if (e.getKind() == ElementKind.METHOD) {
                    type = XLogUtils.TYPE_METHOD;
                } else if (e.getKind() == ElementKind.CONSTRUCTOR) {
                    type = XLogUtils.TYPE_CONSTRUCTOR;
                }

                TypeElement te = findEnclosingTypeElement(e);
                System.out.println(te.getQualifiedName().toString() + "." + e.getSimpleName());
                System.out.println(e.getReturnType());
                List<String> parameters = new ArrayList<>();
                List<String> parameterNames = new ArrayList<>();
                for (VariableElement ve : e.getParameters()) {
                    System.out.println(ve.asType());
                    System.out.println(ve.getSimpleName());
                    parameters.add(ve.asType().toString());
                    parameterNames.add(ve.getSimpleName().toString());

                }
                MethodToLog methodToLog = new MethodToLog(type, te.getQualifiedName().toString(),
                        e.getSimpleName().toString(), parameters, parameterNames);
                methodToLogs.add(methodToLog);

                if (!classNames.contains(methodToLog.getClassName())) {
                    classNames.add(methodToLog.getClassName());
                }
            }
        }

        if (methodToLogs.size() > 0) {
            generateXLogProcessor("_" + md5(env.toString()), methodToLogs, classNames);
        }

        return true;
    }

    private void generateXLogProcessor(String classSuffix, List<MethodToLog> methodToLogs,
            List<String> classNames) {
        StringBuilder methodsSb = new StringBuilder();
        methodsSb.append("[");
        if (methodToLogs != null) {
            for (int i = 0; i < methodToLogs.size(); i++) {
                methodsSb.append(methodToLogs.get(i).toString());
                if (i < methodToLogs.size() - 1) {
                    methodsSb.append(",");
                }
            }
        }
        methodsSb.append("]");

        String methodToLogStr = methodsSb.toString();

        StringBuilder classSb = new StringBuilder();
        classSb.append("[");
        if (methodToLogs != null) {
            for (int i = 0; i < classNames.size(); i++) {
                classSb.append(classNames.get(i).toString());
                if (i < classNames.size() - 1) {
                    classSb.append(",");
                }
            }
        }
        classSb.append("]");

        String classNamesStr = classSb.toString();

        System.out.println(methodToLogStr);

        try {
            JavaFileObject jfo = filer.createSourceFile(
                    XLogUtils.PKG_NAME + "." + XLogUtils.CLASS_NAME + classSuffix);
            Writer writer = jfo.openWriter();
            writer.write(brewJava(methodToLogStr, classNamesStr, classSuffix));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TypeElement findEnclosingTypeElement(Element e) {
        while (e != null && !(e instanceof TypeElement)) {
            e = e.getEnclosingElement();
        }
        return TypeElement.class.cast(e);
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    String brewJava(String methodStr, String classNamesStr, String classSuffix) {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code from XLog. Do not modify!\n");
        builder.append("package ").append(XLogUtils.PKG_NAME).append(";\n\n");

        builder.append("import java.lang.String;\n");
        builder.append('\n');

        builder.append("public final class ").append(XLogUtils.CLASS_NAME + classSuffix);

        builder.append(" {\n");

        builder.append("\tpublic static String ")
                .append(XLogUtils.FIELD_NAME_METHODS)
                .append(" = ")
                .append(stringLiteral(methodStr))
                .append(";\n");

        builder.append("\tpublic static String ")
                .append(XLogUtils.FIELD_NAME_CLASSES)
                .append(" = ")
                .append(stringLiteral(classNamesStr))
                .append(";\n");

        builder.append("}\n");
        return builder.toString();
    }

    String stringLiteral(String value) {
        StringBuilder result = new StringBuilder();
        result.append('"');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\n':
                    result.append("\\n");
                    if (i + 1 < value.length()) {
                        result.append("\"\n").append("  ").append("  ").append("+ \"");
                    }
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                default:
                    if (Character.isISOControl(c)) {
                        new Formatter(result).format("\\u%04x", (int) c);
                    } else {
                        result.append(c);
                    }
            }
        }
        result.append('"');
        return result.toString();
    }

    String md5(String str) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes("UTF-8"));

            return new String(encodeHex(array, DIGITS_LOWER));
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            //CHECKSTYLE:OFF
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
            //CHECKSTYLE:ON
        }
        return out;
    }

}
