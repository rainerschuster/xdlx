package com.rainerschuster.dlx;

/**
 * The default strategy for choosing the next column to be covered. Chooses the column with least amount of nodes.
 */
public class MinLengthColumnChooser<C, V extends Value<C>> implements ColumnChooser<C, V> {

    public MinLengthColumnChooser() {
        // default constructor
    }

    @Override
    public Column<C, V> chooseColumn(final DancingLinksData<C, V> dlData) {
        Column<C, V> bestColumn = null;
        for (Column<C, V> curCol : dlData) {
//            if (verbosity > 2) {
//                System.out.print(" " + curCol.getValue() + "(" + curCol.getLength() + ")");
//            }
            if (bestColumn == null || curCol.getLength() < bestColumn.getLength()) {
                bestColumn = curCol;
            }
        }
        return bestColumn;
    }

}
