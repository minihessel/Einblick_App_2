/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import javafx.scene.control.TableColumn;

/**
 *
 * @author Eskil Hesselroth
 */
public class Column extends TableColumn {

    String NAVN;

    public Column(String navn) {
        NAVN = navn;
    }

    @Override
    public String toString() {
        return NAVN;
    }

}
