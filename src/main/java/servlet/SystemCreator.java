package servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import servlet.utils.Center;
import servlet.utils.CreateCentersResponse;
import servlet.utils.Edge;
import servlet.utils.Product;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SystemCreator {

    public Map<String , Product> createStocks(String path) throws IOException, ParseException {
        Map<String , Product> stocksMap = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

        FileReader reader = new FileReader(path);
        JSONObject object =  (JSONObject) jsonParser.parse(reader);

        for (Object center: object.keySet()) {
            JSONArray products = (JSONArray) object.get(center);
            for (Object product: products) {
                JSONObject productJSON = (JSONObject) product;
                Product newProduct = new Product();
                newProduct.name = (String) productJSON.get("name");
                newProduct.weight = Float.valueOf(productJSON.get("weight").toString());
                newProduct.center = center.toString();
                stocksMap.put(newProduct.name , newProduct);
            }
        }


        return stocksMap;
    }


    public CreateCentersResponse createCenters(String path) throws IOException, ParseException {
        Map<String , Center> centersMap = new HashMap<>();

        JSONParser jsonParser = new JSONParser();

        FileReader reader = new FileReader(path);
        JSONObject object =  (JSONObject) jsonParser.parse(reader);
        JSONArray centersArray = (JSONArray) object.get("centers");
        for (Object center: centersArray) {
            final Center newCenter = new Center();
            JSONObject centerJson = (JSONObject) center;
            if (((JSONObject) center).keySet().toArray()[0] instanceof String){
                centersMap.putIfAbsent((String) ((JSONObject) center).keySet().toArray()[0], newCenter);
            }
            String centerName = (String) ((JSONObject) center).keySet().toArray()[0];
            newCenter.name = centerName;
            JSONArray edges = (JSONArray) centerJson.get(centerName);
            for (Object edge: edges) {
                JSONObject edgeJSON = (JSONObject) edge;
                Edge newEdge = new Edge();
                newEdge.units = Float.valueOf((String) edgeJSON.get("units"));
                centersMap.computeIfAbsent((String) edgeJSON.get("neighbour") , c->{
                    Center centerObject = new Center();
                    centerObject.name = (String) edgeJSON.get("neighbour");
                    return centerObject;
                });
                newEdge.neighbour = centersMap.get((String) edgeJSON.get("neighbour"));
                centersMap.get(centerName).neighbours.put((String) edgeJSON.get("neighbour"),newEdge);
            }
        }
        String dropLocationName = (String) object.get("dropLocationName");
        return new CreateCentersResponse(dropLocationName , centersMap);
    }
}
