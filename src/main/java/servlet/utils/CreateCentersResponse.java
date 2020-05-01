package servlet.utils;

import java.util.Map;

public class CreateCentersResponse{
    public String dropLocationName;
    public Map<String, Center> centersMap;
    public CreateCentersResponse(String dropLocationName, Map<String, Center> centersMap) {
        this.dropLocationName = dropLocationName;
        this.centersMap = centersMap;
    }
}
