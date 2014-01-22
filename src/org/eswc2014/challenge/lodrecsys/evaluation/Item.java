/**
 * Item.java
 *
 * Stores the information associated to an item: its identifier, and vectors
 * with its weighted attributes and categories.
 *
 * @version 1.0 (November 11, 2013)
 *
 * @author Ivan Cantador, Universidad Autonoma de Madrid, ivan.cantador@uam.es
 */
package org.eswc2014.challenge.lodrecsys.evaluation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Item {

    private String id;
    private Map<String, Double> attributes;
    private Map<String, Double> categories;

    /**
     * Builds an Item instance storing the input identifier, and creating empty
     * vectors of weighted attributes and categories.
     *
     * @param id the item's identifier
     *
     * @throws IllegalArgumentException in case of a null item identifier
     */
    public Item(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("null item id");
        }

        this.id = id;
        this.attributes = new HashMap<String, Double>();
        this.categories = new HashMap<String, Double>();
    }

    /**
     * Builds an Item instance storing the input identifier, and vectors of
     * weighted attributes and categories.
     *
     * @param id the item's identifier
     * @param attributes the item's weighted attributes
     * @param categories the item's weighted categories
     *
     * @throws IllegalArgumentException in case of a null inout argument
     */
    public Item(String id, Map<String, Double> attributes, Map<String, Double> categories) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("null item id");
        }
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException("null or empty item attribute set");
        }
        if (categories == null || categories.isEmpty()) {
            throw new IllegalArgumentException("null or empty item category set");
        }

        this.id = id;
        this.attributes = attributes;
        this.categories = categories;
    }

    /**
     * Returns the item's identifier.
     *
     * @return the item's identifiers
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the vector of the item's weighted attributes.
     *
     * @return the item's attributes
     */
    public Map<String, Double> getAttributes() {
        return this.attributes;
    }

    /**
     * Returns the item's number of attributes.
     *
     * @return the item's number of attributes
     */
    public int getNumAttributes() {
        return this.attributes.size();
    }

    /**
     * Returns the vector of the item's weighted categories.
     *
     * @return the item's categories
     */
    public Map<String, Double> getCategories() {
        return this.categories;
    }

    /**
     * Returns the item's number of categories.
     *
     * @return the item's number of categories
     */
    public int getNumCategories() {
        return this.categories.size();
    }

    /**
     * Stores a new weighted attribute of the item.
     *
     * @param name the name of the item's attribute
     *
     * @param weight the weight of the item's attrinute
     */
    public void addAttribute(String name, double weight) {
        if (name != null) {
            this.attributes.put(name, weight);
        }
    }

    /**
     * Stores a new weighted category of the item.
     *
     * @param name the name of the item's category
     *
     * @param weight the weight of the item's category
     */
    public void addCategory(String name, double weight) {
        if (name != null) {
            this.categories.put(name, weight);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Item{" + "id=" + id + ", attributes=" + attributes + ", categories=" + categories + '}';
    }
}
