package servlet;

import servlet.utils.Center;
import servlet.utils.Edge;
import servlet.utils.Product;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class MinCostComputer {
    private static final int FIRST_FIVE_KGS_COST_PER_UNIT = 10;
    private static final int ADDITIONAL_FIVE_KGS_COST_PER_UNIT = 8;

    public int getMinCost(Map<String , Center> centersMap , Map<String , Product> productsMap , String order , String dropLocationName){
        String[] orderSplitted = order.split(",");
        if (orderSplitted.length == 0)
            return -1;
        OrderInfo orderInfo = new OrderInfo();
        for (int orderSplittedIndex = 0; orderSplittedIndex < orderSplitted.length ; orderSplittedIndex++) {
            handleOrder(orderSplitted, orderSplittedIndex , orderInfo, centersMap , productsMap , dropLocationName);
        }
        String lastProductName = productsMap.get(orderSplitted[orderSplitted.length-1].split("-")[0]).center;
        Center orderLastProductCenter = centersMap.get(lastProductName);;
        orderInfo.currentCost+= getPathCost(orderInfo.currentWeight , orderLastProductCenter.neighbours.get(dropLocationName));

        return orderInfo.currentCost;
    }

    private void handleOrder(String[] orderSplitted, int orderSplittedIndex, OrderInfo orderInfo, Map<String, Center> centersGraph, Map<String, Product> stocksMap, String dropLocationName) {
        orderInfo.currentWeight += computeTotalProductWeight(orderSplitted[orderSplittedIndex] , stocksMap);
        if (orderSplittedIndex == orderSplitted.length-1){
            return;
        }
        String currentCenterName = stocksMap.get(orderSplitted[orderSplittedIndex].split("-")[0]).center;
        Center currentCenter = centersGraph.get(currentCenterName);
        String destinationCenterName  = stocksMap.get(orderSplitted[orderSplittedIndex+1].split("-")[0]).center;
        Center destinationCenter = centersGraph.get(destinationCenterName);
        if (currentCenter.name.equalsIgnoreCase(destinationCenter.name)){
            return;
        }
        int pathCostThroughDropCenter = getPathCostThroughDropCenter(dropLocationName , currentCenter , centersGraph, destinationCenter, orderInfo.currentWeight);
        int pathCostThroughStockCenters = getPathCostThroughStockCenters(dropLocationName,currentCenter , centersGraph , destinationCenter , orderInfo.currentWeight , new HashSet<String>(Arrays.asList(orderSplitted[orderSplittedIndex])), 0);

        if (pathCostThroughStockCenters >= pathCostThroughDropCenter){
            orderInfo.currentWeight = 0;
            orderInfo.currentCost+= pathCostThroughDropCenter;
        }
        else
            orderInfo.currentCost+= pathCostThroughStockCenters;

    }
    private int getPathCostThroughDropCenter(String dropLocationName, Center startCenter, Map<String, Center> centersGraph, Center destinationCenter, float currentWeight) {
        int cost = 0;
        cost += getPathCost(currentWeight , startCenter.neighbours.get(dropLocationName));
        cost += getPathCost(0 , centersGraph.get(dropLocationName).neighbours.get(destinationCenter.name));
        return cost;
    }

    private int getPathCostThroughStockCenters(String dropLocationName , Center currentCenter, Map<String, Center> centersGraph, Center destination, float currentWeight, HashSet<String> visited, int currentCost) {
        if (currentCenter.name.equalsIgnoreCase(destination.name)){
            return currentCost;
        }
        Map<String , Edge> neighbours = currentCenter.neighbours;
        int minCost = Integer.MAX_VALUE;

        for (Edge edge: neighbours.values()) {
            if (!visited.contains(edge.neighbour.name) && !edge.neighbour.name.equalsIgnoreCase(dropLocationName)){
                visited.add(edge.neighbour.name);
                minCost = Math.min(minCost , getPathCostThroughStockCenters(dropLocationName ,edge.neighbour , centersGraph , destination , currentWeight , visited , currentCost + getPathCost(currentWeight , edge)));
                visited.remove(edge.neighbour.name);
            }
        }
        return minCost;
    }
    private int getPathCost(float currentWeight, Edge edge) {
        if (currentWeight <=5){
            return (int) (FIRST_FIVE_KGS_COST_PER_UNIT * edge.units);
        }
        int parts = (int) (currentWeight / 5);
        int cost = 0;
        if (currentWeight % 5 > 0){
            cost = (int) ((FIRST_FIVE_KGS_COST_PER_UNIT * edge.units) + (ADDITIONAL_FIVE_KGS_COST_PER_UNIT * edge.units) * parts);
        }
        else
            cost = FIRST_FIVE_KGS_COST_PER_UNIT + ADDITIONAL_FIVE_KGS_COST_PER_UNIT * parts - 1;

        return cost;
    }
    private float computeTotalProductWeight(String productQuantity, Map<String, Product> stocksMap) {
        String[] productQuantitySplitted = productQuantity.split("-");
        if (productQuantitySplitted.length < 2)
            return 0;
        return stocksMap.get(productQuantitySplitted[0]).weight * Integer.parseInt(productQuantitySplitted[1]);
    }

    private class OrderInfo{
        int currentCost;
        float currentWeight;

        OrderInfo() {
            currentCost = 0;
            currentWeight= 0;
        }
    }
}
