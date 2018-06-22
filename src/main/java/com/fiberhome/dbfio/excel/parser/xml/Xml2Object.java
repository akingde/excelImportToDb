package com.fiberhome.dbfio.excel.parser.xml;

import com.fiberhome.dbfio.excel.template.annotation.XPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.*;
import java.util.*;

public class Xml2Object {
    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    private Map<Class, Map<Field, String>> classAnnotations = new HashMap<>();

    private File file;
    private Document doc;

    public Xml2Object(File file) {
        logger.debug("load xml: " + file.getAbsolutePath());
        this.file = file;
        init();
    }

    public Xml2Object(String filePath) {
        this(new File(filePath));
    }

    private void cacheAnnotation() {

    }

    private void init() {
        logger.debug("Xml2Object init" );
        try {
            SAXReader reader = new SAXReader();
            this.doc = reader.read(this.file);
        } catch (DocumentException e) {
            logger.debug(e);
        }
    }

    public Element getRoot() {
        return this.doc.getRootElement();
    }

    public <T> List<T> getObject(String xpathFilter,Class<T> clazz){
        return null;
    }

    public <T> List<T> getObject(Class<T> clazz){
        try {
            return generateObject(clazz,null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> List<T> generateObject(Class<T> clazz,Node root) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        List<T> rtn = new ArrayList<>();
        if (classAnnotations.get(clazz) == null)
            mappingAnnotations(clazz);
        String rootXpath = clazz.getAnnotation(XPath.class).value();
        List<Node> clazzNodes = null;
        if(root != null) {
            rootXpath = "." + rootXpath.substring(rootXpath.lastIndexOf("/"));
        }else{
            root = doc;
        }
        logger.debug("parse object {} from rootXpath {}",clazz.getName(),rootXpath);
        clazzNodes = root.selectNodes(rootXpath);
        logger.debug("found {} node(s)",clazzNodes.size());

        for (Node clazzNode : clazzNodes) {
            T temp = (T)feedObject(clazzNode,clazz);
            rtn.add(temp);
        }

        return rtn;
    }

    private <T> T feedObject(Node clazzNode, Class<T> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        logger.debug("begin feed object {}",clazz.getName());
        T rtn = clazz.getConstructor().newInstance();
        for (Field field : classAnnotations.get(clazz).keySet()) {
            Object fieldObj = null;

            Class fieldClass = field.getType();
            XPath xPath = field.getAnnotation(XPath.class);
            String xPathStr = "";
            if(xPath!=null) {
                xPathStr = xPath.value();
            }

            String fieldName = field.getName();
            String fieldSetter = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            if (!fieldClass.getSimpleName().equalsIgnoreCase("string")) {
                Class parameterizedFieldClass = null;
                if(Collection.class.isAssignableFrom(fieldClass)){
                    Type t = ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
                    parameterizedFieldClass = Class.forName(t.getTypeName());
                }
                if(parameterizedFieldClass==null){
                    fieldObj = generateObject(fieldClass,clazzNode).get(0);
                }else {
                    fieldObj = generateObject(parameterizedFieldClass, clazzNode);
                }
            } else {
                if(xPathStr.equals(""))
                    continue;
                Node target = clazzNode.selectSingleNode(xPathStr);
                fieldObj = target==null?null:target.getStringValue();
                logger.debug("field value {}" , fieldObj==null?"":fieldObj.toString());
            }
            Method setter = clazz.getMethod(fieldSetter, fieldClass);
            logger.debug("invoke method {} param {}",setter.getName(),fieldObj );
            setter.invoke(rtn,fieldObj);
        }
        return rtn;
    }


    private void mappingAnnotations(Class clazz) {
        Map<Field, String> fieldAnnotationMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            XPath xPath = field.getAnnotation(XPath.class);
            fieldAnnotationMap.put(field, xPath==null?null:xPath.value());
        }
        this.classAnnotations.put(clazz, fieldAnnotationMap);
    }

}
