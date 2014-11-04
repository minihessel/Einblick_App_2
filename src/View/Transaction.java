/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Eskil Hesselroth
 */
class Transaction {
    String id;
    List<Item> listOfItems ;
    
    public Transaction()
    {
     listOfItems = new ArrayList();
    
    }
    
    void add(Item item)
    {
        listOfItems.add(item);
    }

}
