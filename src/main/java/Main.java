import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import servlet.DeliveryServlet;
import servlet.DeliverySystemInitializer;

import java.io.File;


public class Main {
    public static void main(String[] args) throws Exception {

        String absolutePath = new File(".").getAbsolutePath();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.valueOf("8080"));

        Context ctx = tomcat.addContext("/", absolutePath);

        //adding servlets to tomcat server
        DeliveryServlet deliveryServlet = new DeliveryServlet();
        DeliverySystemInitializer deliverySystemInitializer = new DeliverySystemInitializer();
        ctx.addServletContainerInitializer(deliverySystemInitializer,null);
        tomcat.addServlet(ctx.getPath() , "DeliveryServlet" , deliveryServlet);
        ctx.addServletMapping("/deliver" , "DeliveryServlet");
        tomcat.start();
        tomcat.getServer().await();


    }
}
