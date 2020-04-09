package servlet;

import annotation.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;

/**
 *
 * @Description:
 * @author zhiwj
 * @date 2019-11-08 15:13
 */
@WebServlet(
        name = "dispatcherServlet", urlPatterns = "/*", loadOnStartup = 1,
        initParams = {@WebInitParam(name = "base-package", value = "com.bread.springmvc")}
)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 4569888802060131294L;

    // 类的全限定名
    List<String> packageNames = new ArrayList<>();
    // 注解的value:对象
    Map<String, Object> instanceMap = new HashMap<>();
    // url： method
    Map<String, Method> urlMethodMap = new HashMap<>();
    // method： className
    Map<Method, String> methodClassNameMap = new HashMap<>();


    @Override
    public void init() throws ServletException {
        try {
            String basePackage = this.getServletConfig().getInitParameter("base-package");
            // 获取base-package下所有的类的全限定名
            scanPackage(basePackage);
            // 获取所有需要管理的类到一个map
            getInstance();
            // ioc
            IOC();
            // url地址和方法的映射
            handlerUrlMap();

            System.out.println(1);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void handlerUrlMap() {
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            Object value = entry.getValue();
            Class<?> aClass = value.getClass();
            if (aClass.isAnnotationPresent(Controller.class)) {
                String baseUrl = "";
                if (aClass.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = aClass.getAnnotation(RequestMapping.class);
                    baseUrl = annotation.value();
                }
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String url = baseUrl + annotation.value();
                        urlMethodMap.put(url, method);
                        methodClassNameMap.put(method, entry.getKey());
                    }
                }
            }
        }
    }

    private void IOC() throws IllegalAccessException {
        for (Map.Entry<String, Object> entry : instanceMap.entrySet()) {
            Object value = entry.getValue();
            Class<?> aClass = value.getClass();
            Field[] fields = aClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowrited.class)) {
                    Autowrited annotation = field.getAnnotation(Autowrited.class);
                    field.setAccessible(true);
                    field.set(value, instanceMap.get(annotation.value()));
                }
            }
        }
    }

    private void getInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (String packageName : packageNames) {
            Class<?> packageClass = Class.forName(packageName);
            if (packageClass.isAnnotationPresent(Controller.class)) {
                Controller annotation = packageClass.getAnnotation(Controller.class);
                String value = annotation.value();
                instanceMap.put(value, packageClass.newInstance());
            } else if (packageClass.isAnnotationPresent(Service.class)) {
                Service annotation = packageClass.getAnnotation(Service.class);
                String value = annotation.value();
                instanceMap.put(value, packageClass.newInstance());
            } else if (packageClass.isAnnotationPresent(Repository.class)) {
                Repository annotation = packageClass.getAnnotation(Repository.class);
                String value = annotation.value();
                instanceMap.put(value, packageClass.newInstance());
            }

        }
    }

    private void scanPackage(String basePackage) {
        URL resource = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));
        File file = new File(resource.getPath());
        File[] files = file.listFiles();
        for (File childFile : files) {
            if (childFile.isDirectory()) {
                scanPackage(basePackage + "." + childFile.getName());
            } else if(childFile.isFile()){
                packageNames.add(basePackage + "." + childFile.getName().split("\\.")[0]);
            }
        }
    }

    private void doDispatcherServlet(HttpServletRequest req, HttpServletResponse resp) {
        String requestURI = req.getRequestURI();
        String url = requestURI.replace(req.getContextPath(), "");
        Method method = urlMethodMap.get(url);
        if (method != null) {
            String className = methodClassNameMap.get(method);
            Object clazz = instanceMap.get(className);
            Parameter[] parameters = method.getParameters();
            Object[] params = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                params[i] = req.getParameter(parameters[i].getName());
            }
            try {
                method.invoke(clazz, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcherServlet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcherServlet(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcherServlet(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatcherServlet(req, resp);
    }
}
