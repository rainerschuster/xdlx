package com.rainerschuster.sudoku.vaadin;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractField;

public class JNumberFieldField extends AbstractField<Integer> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an empty <code>JNumberField</code> with no caption.
     */
    public JNumberFieldField() {
        setValue(null);
    }

    /**
     * Constructs an empty <code>JNumberField</code> with given caption.
     * 
     * @param caption
     *            the caption <code>String</code> for the editor.
     */
    public JNumberFieldField(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Constructs a new <code>JNumberField</code> that's bound to the specified
     * <code>Property</code> and has no caption.
     * 
     * @param dataSource
     *            the Property to be edited with this editor.
     */
    public JNumberFieldField(Property<Integer> dataSource) {
        setPropertyDataSource(dataSource);
    }

    /**
     * Constructs a new <code>JNumberField</code> that's bound to the specified
     * <code>Property</code> and has the given caption <code>String</code>.
     * 
     * @param caption
     *            the caption <code>String</code> for the editor.
     * @param dataSource
     *            the Property to be edited with this editor.
     */
    public JNumberFieldField(String caption, Property<Integer> dataSource) {
        this(dataSource);
        setCaption(caption);
    }

    /**
     * Constructs a new <code>JNumberField</code> with the given caption and
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
    public JNumberFieldField(String caption, Integer value) {
        setValue(value);
        setCaption(caption);
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

}
