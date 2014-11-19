package com.rainerschuster.sudoku.vaadin;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

public class NumberField extends TextField {

    // TODO assert that minNumber <= maxNumber

    private static final long serialVersionUID = 1L;

    private int minNumber = -1;
    private int maxNumber = -1;

    /**
     * Constructs an empty <code>NumberField</code> with no caption.
     */
    public NumberField() {
        setValue(null);
    }

    /**
     * Constructs an empty <code>NumberField</code> with given caption.
     * 
     * @param caption
     *            the caption <code>String</code> for the editor.
     */
    public NumberField(String caption) {
        this();
        setCaption(caption);
    }

//    /**
//     * Constructs a new <code>NumberField</code> that's bound to the specified
//     * <code>Property</code> and has no caption.
//     * 
//     * @param dataSource
//     *            the Property to be edited with this editor.
//     */
//    public NumberField(Property<Integer> dataSource) {
//        setPropertyDataSource(dataSource);
//    }
//
//    /**
//     * Constructs a new <code>NumberField</code> that's bound to the specified
//     * <code>Property</code> and has the given caption <code>String</code>.
//     * 
//     * @param caption
//     *            the caption <code>String</code> for the editor.
//     * @param dataSource
//     *            the Property to be edited with this editor.
//     */
//    public NumberField(String caption, Property<Integer> dataSource) {
//        this(dataSource);
//        setCaption(caption);
//    }

    /**
     * Constructs a new <code>NumberField</code> with the given caption and
     * initial text contents. The editor constructed this way will not be bound
     * to a Property unless
     * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
     * is called to bind it.
     * 
     * @param caption
     *            the caption <code>String</code> for the editor.
     * @param value
     *            the initial content of the editor.
     */
    public NumberField(String caption, Integer value) {
        setValue(Integer.toString(value));
        setCaption(caption);
    }

    @Override
    public void setValue(String t) {
        setNumber((t == null || t.isEmpty()) ? null : Integer.valueOf(t));
    }

    public Integer getNumber() {
        if (super.getValue().isEmpty()) {
            return 0;
        }
        return Integer.valueOf(super.getValue());
    }

    public void setNumber(Integer val) {
        super.setValue(val == null ? "" : val.toString());
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

}
