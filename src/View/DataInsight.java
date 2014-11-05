/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Table;
import apriori.AlgoApriori;
import apriori.Itemsets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TabPane;

/**
 *
 * @author Eskil Hesselroth
 */
public class DataInsight {

    private Map<String, List<Item>> transdata = new HashMap<>();

    Map<String, Item> itemMap = new HashMap<>();
    Map<Integer, String> invertedItemMap = new HashMap();
    Integer productNUmber = 1;

    void addNewDataPoint(String key, String name) {
        Item item;
        if (itemMap.containsKey(name)) {
            item = itemMap.get(name);

        } else {

            item = new Item(name, productNUmber);
            itemMap.put(name, item);

            productNUmber++;

            invertedItemMap.put(item.createdInt, item.ID);
        }

        if (transdata.containsKey(key)) {
            transdata.get(key).add(item);

        } else {
            List<Item> transaction = new ArrayList();
            transdata.put(key, transaction);
            transaction.add(item);

        }
    }

    public void getInsight(Integer nameColumn, Integer valueColumn, TabPane tabPane, Map mapOverTabsAndTables) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        Table selectedTable = (Table) mapOverTabsAndTables.get(tabPane.getSelectionModel().getSelectedItem());
        for (List<String> a : selectedTable.sortedData) {
            addNewDataPoint(a.get(0), a.get(4));
        }

        // Note : we here set the output file path to null
        // because we want that the algorithm save the 
        // result in memory for this example.
        double minsup = 0.001; // means a minsup of 2 transaction (we used a relative support)

        // Applying the Apriori algorithm
        AlgoApriori apriori = new AlgoApriori();
        Itemsets result = apriori.runAlgorithm(minsup, transdata, null);

        apriori.printStats();
        result.printItemsets(apriori.getDatabaseSize(), invertedItemMap);

    }

    static <V, K> Map<V, K> invert(Map<K, V> map) {

        Map<V, K> inv = new HashMap<V, K>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            inv.put(entry.getValue(), entry.getKey());
        }

        return inv;
    }

}
