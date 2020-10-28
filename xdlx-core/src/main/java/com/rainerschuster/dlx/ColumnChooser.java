package com.rainerschuster.dlx;

/**
 * Strategy for choosing the next column to be covered.
 */
public interface ColumnChooser<C, V extends Value<C>> {
    /**
     * Choose the next column to be covered
     * @param dlData The Dancing Links data structure
     * @return The next column to be covered
     */
    Column<C, V> chooseColumn(DancingLinksData<C, V> dlData);
}
