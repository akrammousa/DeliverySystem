package servlet;

import org.json.simple.parser.ParseException;
import servlet.utils.Center;
import servlet.utils.CreateCentersResponse;
import servlet.utils.Product;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DeliverySystemInitializer implements ServletContainerInitializer {


    @Override
    public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
        SystemCreator systemCreator = new SystemCreator();
        Map<String, Product> stocksMap= null;
        try {
            stocksMap = systemCreator.createStocks(System.getProperty("user.dir") +  "/src/main/resources/defaultStocksProducts");
            servletContext.setAttribute("stocksMap" , stocksMap);
            CreateCentersResponse createCentersResponse = systemCreator.createCenters(System.getProperty("user.dir") +  "/src/main/resources/defaultCenters");
            servletContext.setAttribute("centersMap" , createCentersResponse.centersMap);
            servletContext.setAttribute("dropLocationName" , createCentersResponse.dropLocationName);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
