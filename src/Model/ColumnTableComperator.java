/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *///

package Model;

import Model.Kolonne;
import java.util.Comparator;

/**
 * 
 * @author Eskil Hesselroth
 */
class ColumnTableComperator implements Comparator<Kolonne> {

        @Override
        public int compare(Kolonne e1, Kolonne e2) {
            int kolonne1TableNumber = e1.tbl.tableNumber;
            int kolonne2TableNumber = e2.tbl.tableNumber;
            if (kolonne1TableNumber < kolonne2TableNumber) {

                return -1;
            } else if (kolonne1TableNumber == kolonne2TableNumber) {

                return 0;

            } else {
                return 1;
            }
        }
    }
