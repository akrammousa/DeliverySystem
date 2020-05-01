package servlet.utils;

import java.util.HashMap;
import java.util.Map;

public class Center {
     public String name;
     public Map<String , Edge> neighbours;
     public Center(){
        neighbours = new HashMap<>();
    }
}
