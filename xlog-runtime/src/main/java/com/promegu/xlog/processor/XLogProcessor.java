package com.promegu.xlog.processor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.promegu.xlog.base.MethodToLog;
import com.promegu.xlog.base.XLog;
import com.promegu.xlog.base.XLogUtils;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class XLogProcessor extends AbstractProcessor {
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

        for (Element element : env.getElementsAnnotatedWith(XLog.class)) {
            if (!(element instanceof ExecutableElement) || (element.getKind() != ElementKind.METHOD
                    && element.getKind() != ElementKind.CONSTRUCTOR)) {
                throw new IllegalStateException(
                        String.format("@%s annotation must be on as method or constructor.", element.getSimpleName()));
            }
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
            MethodToLog methodToLog = new MethodToLog(type, te.getQualifiedName().toString(), e.getSimpleName().toString(), parameters, parameterNames);
            methodToLogs.add(methodToLog);
        }

        if (methodToLogs.size() > 0) {
            generateXLogProcessor("_" + DigestUtils.md5Hex(env.toString()), methodToLogs);
        }

        return true;
    }

    private void generateXLogProcessor(String classSuffix, List<MethodToLog> methodToLogs) {
        String methodToLogStr = new Gson().toJson(methodToLogs, new TypeToken<List<MethodToLog>>() {
        }.getType());
        System.out.println(methodToLogStr);
        FieldSpec fieldSpec = FieldSpec.builder(String.class, XLogUtils.FIELD_NAME, Modifier.PUBLIC, Modifier.STATIC)
                .initializer("$S", methodToLogStr)
                .build();

        TypeSpec xLoggerMethods = TypeSpec.classBuilder(XLogUtils.CLASS_NAME + classSuffix)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(fieldSpec)
                .build();

        JavaFile javaFile = JavaFile.builder(XLogUtils.PKG_NAME, xLoggerMethods)
                .build();

        try {
            JavaFileObject jfo = filer.createSourceFile(XLogUtils.PKG_NAME + "." + xLoggerMethods.name);
            Writer writer = jfo.openWriter();
            writer.write(javaFile.toString());
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


}
