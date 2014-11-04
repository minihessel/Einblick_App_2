package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eskil Hesselroth This class defines what a column is
 */
public class Kolonne {

    //En kolonne må ha et navn
    //Den må også tilhøre en tabell
    public final String NAVN;
    public final Table tbl;
    public final int kolonneNummer;

    //En kolonne inneholder felter(altså dataen i en kolonne
    private List<String> fields;
    public List<Kolonne> listOfColumns;

    boolean amICombined;

    public Kolonne(String kolonneNavn, Integer kolonneIndex, Table tbl1) {
        //konstruktøren
        NAVN = kolonneNavn;
        tbl = tbl1;
        fields = new ArrayList<>();
        amICombined = false;
        listOfColumns = null;
        kolonneNummer = kolonneIndex;
    }

    public Kolonne(String navn, Table tbl1, Boolean combined, Integer kolonneIndex) {
        //Konstruktøren for en kombinert kolonne
        fields = new ArrayList<>();
        listOfColumns = new ArrayList<Kolonne>();

        tbl = tbl1;
        NAVN = navn;
        amICombined = combined;
        kolonneNummer = kolonneIndex;

    }

    void addColumn(Kolonne kol) {
        listOfColumns.add(kol);
    }

    void addField(String item) {
        fields.add(item);
    }

    void combineColumns() {
        Collections.sort(listOfColumns, new ColumnTableComperator());

        for (Kolonne kol : listOfColumns) {
            for (int i = 0; i < kol.fields.size(); i++) {
                fields.add(kol.fields.get(i));
            }
        }

    }

    List<String> allFields() {
        return new ArrayList<>(fields);
    }

    //Overkjører toString for å isteden returnere kolonnenavn og felter
    @Override
    public String toString() {
        return NAVN ;
    }

}
