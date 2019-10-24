package com.mvc.study.servlet;

import com.mvc.study.annotation.Controller;
import com.mvc.study.annotation.RequestMapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class DispatcherServlet extends HttpServlet {

    private Properties properties=new Properties();

    private List<String> scannerPackets=new ArrayList<String>();

    private Map<String,Object>  iocContainer=new HashMap<String, Object>();

    private Map<String, Method> handleMapping=new HashMap<String, Method>();

    private Map<Method,Object> objMethod=new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String servletPath=req.getPathInfo();
      Method method= handleMapping.get(servletPath);
      Object obj=objMethod.get(method);
        try {
            method.invoke(obj,req,resp);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }
//        super.doPost(req,resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        doScannerProperties(config.getInitParameter("contextConfigLocation"));
        doScannerBasePacket((String) properties.get("basePacket"));
        doInstance();
        doHandleMapping();

    }

    /**
     * 加载一个servlet
     * @param configuration 配置文件路径所在地
     */
    private void doScannerProperties(String configuration){
        InputStream in= DispatcherServlet.class.getClassLoader().getResourceAsStream(configuration.substring("classpath:".length()));
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行扫描操作，扫描路径下全部的文件名
     * @param basePacket 基本包路径地址
     */
    private void doScannerBasePacket(String basePacket)  {
        URL url=this.getClass().getClassLoader().getResource("/"+basePacket.replaceAll("\\.","\\/"));
        File file=new File(url.getFile());
        for (File f:file.listFiles()){
            if (f.isDirectory()){
                doScannerBasePacket(basePacket+"."+f.getName());
            }else {
                String className=basePacket+"."+f.getName().replace(".class","");
                scannerPackets.add(className);
            }
        }
    }

    private void doInstance() {
        for (String e:scannerPackets){
            try {
                Class<?> clazz=Class.forName(e);
                if (clazz.isAnnotationPresent(Controller.class)){
                    String name=clazz.getSimpleName();
                    iocContainer.put(name.substring(0,1).toLowerCase()+name.substring(1),clazz.newInstance());
                }
            } catch (ClassNotFoundException e1) {
//                e1.printStackTrace();
                continue;
            } catch (IllegalAccessException|InstantiationException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void doHandleMapping(){

        for (Map.Entry<String,Object> e:iocContainer.entrySet()){
            Object obj=e.getValue();
            String url="";
            Class<?> clazz=obj.getClass();
            RequestMapper requestMapper;
            if (clazz.isAnnotationPresent(RequestMapper.class)){
                requestMapper=clazz.getAnnotation(RequestMapper.class);
                url=requestMapper.value();
            }
            Method[] methods=clazz.getDeclaredMethods();
            for (Method m:methods){
                if (m.isAnnotationPresent(RequestMapper.class)){
                    requestMapper=m.getAnnotation(RequestMapper.class);
                    handleMapping.put(url+requestMapper.value(),m);
                    objMethod.put(m,obj);
                }
            }
        }
    }
}
