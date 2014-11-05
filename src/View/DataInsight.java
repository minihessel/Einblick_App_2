/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Kolonne;
import Model.Table;
import apriori.AlgoFPGrowth;
import apriori.Itemset;
import apriori.Itemsets;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
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

    public Table getInsight(Integer nameColumn, Integer valueColumn, TabPane tabPane, Map mapOverTabsAndTables) throws FileNotFoundException, UnsupportedEncodingException, IOException, SQLException, ClassNotFoundException, InterruptedException, ExecutionException {

        Table selectedTable = (Table) mapOverTabsAndTables.get(tabPane.getSelectionModel().getSelectedItem());
        for (List<String> a : selectedTable.sortedData) {
            addNewDataPoint(a.get(1), a.get(0));
        }

        // Note : we here set the output file path to null
        // because we want that the algorithm save the 
        // result in memory for this example.
        double minsup = 0.004; // means a minsup of 2 transaction (we used a relative support)

        // Applying the Apriori algorithm
        //   AlgoApriori apriori = new AlgoApriori();
        // Itemsets result = apriori.runAlgorithm(minsup, transdata, null);
        AlgoFPGrowth apriori = new AlgoFPGrowth();
        Itemsets result = apriori.runAlgorithm(transdata, null, minsup);

        apriori.printStats();

        Table table = new Table("Data insight");
        Kolonne kolonne = new Kolonne("itemset", 0, table);
        Kolonne kolonne2 = new Kolonne("Support", 1, table);
        Kolonne kolonne3 = new Kolonne("Level", 2, table);
        int levelCount = 0;
        for (List<Itemset> level : result.getLevels()) {
            if (levelCount > 1) {
                for (Itemset itemset : level) {
                    Arrays.sort(itemset.getItems());
                    // print the itemset

                    int[] s = itemset.getItems();
                    String itemSet = "";
                    for (int i : s) {
                        itemSet += invertedItemMap.get(i) + " & ";

                    }

                    kolonne.addField(itemSet);
                    // print the support of this itemset
                    kolonne2.addField(itemset.getRelativeSupportAsString(apriori.getDatabaseSize()));
                    kolonne3.addField("" + levelCount);
                }

            }
            levelCount++;
        }
        table.listofColumns.add(kolonne);
        table.listofColumns.add(kolonne2);
        return table;

    }

    static <V, K> Map<V, K> invert(Map<K, V> map) {

        Map<V, K> inv = new HashMap<V, K>();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            inv.put(entry.getValue(), entry.getKey());
        }

        return inv;
    }

}
