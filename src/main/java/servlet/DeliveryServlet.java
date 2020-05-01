package servlet;

import org.json.simple.parser.ParseException;
import servlet.utils.Center;
import servlet.utils.CreateCentersResponse;
import servlet.utils.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@WebServlet(
        name = "DeliveryServlet",
        urlPatterns = {"/deliver"}
)
public class DeliveryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request , HttpServletResponse response) throws IOException , ServletException {
        Map<String, Product> stocksMap = (Map<String , Product>)getServletContext().getAttribute("stocksMap");
        Map<String, Center> centersMap = (Map<String, Center>) getServletContext().getAttribute("centersMap");
        String dropLocationName = (String) getServletContext().getAttribute("dropLocationName");
        String order = request.getParameter("order");
        if (order == null || order.length()== 0) {
            response.getOutputStream().print("please put an order as parameter");
            return;
        }
        order = order.replaceAll("\\s", "");
        MinCostComputer minCostComputer = new MinCostComputer();
        int minCost = minCostComputer.getMinCost(centersMap,stocksMap, order,dropLocationName);

        response.getOutputStream().print("min cost = " + minCost);
    }
    @Override
    protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException {
        String stockProductsPath = request.getParameter("stocksProductsPath");
        String centersPath = request.getParameter("centersPath");
        if (stockProductsPath == null || stockProductsPath.length() == 0 || centersPath == null || centersPath.length() ==0){
            response.getOutputStream().print("please put the stock and centers files paths");
            return;
        }
        SystemCreator systemCreator = new SystemCreator();
        Map<String, Product> stocksMap= null;
        try {
            stocksMap = systemCreator.createStocks(stockProductsPath);
            getServletContext().setAttribute("stocksMap" , stocksMap);
            CreateCentersResponse createCentersResponse = systemCreator.createCenters(centersPath);
            getServletContext().setAttribute("centersMap" , createCentersResponse.centersMap);
            getServletContext().setAttribute("dropLocationName" , createCentersResponse.dropLocationName);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        response.getOutputStream().print("System Updated Successfully");
    }
}
