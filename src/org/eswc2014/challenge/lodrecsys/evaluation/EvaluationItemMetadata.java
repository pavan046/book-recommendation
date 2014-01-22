package org.eswc2014.challenge.lodrecsys.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class EvaluationItemMetadata {

    private List<String> attributes;
    private List<String> categories;
    private Map<String, Item> items;

    public EvaluationItemMetadata(String itemAttributeFile, String itemCategoryFile) throws Exception {
        BufferedReader reader;
        String line;

        if (itemAttributeFile == null) {
            throw new IllegalArgumentException("null item attribute file");
        }
        if (itemCategoryFile == null) {
            throw new IllegalArgumentException("null item category file");
        }

        this.attributes = new ArrayList<String>();
        this.categories = new ArrayList<String>();
        this.items = new HashMap<String, Item>();

        reader = new BufferedReader(new FileReader(itemAttributeFile));
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, " \t,");

            String itemId = tokenizer.nextToken();
            String attributeName = tokenizer.nextToken();
            double attributeWeight = Double.valueOf(tokenizer.nextToken());

            if (!this.items.containsKey(itemId)) {
                this.items.put(itemId, new Item(itemId));
            }
            this.items.get(itemId).addAttribute(attributeName, attributeWeight);

            if (!this.attributes.contains(attributeName)) {
                this.attributes.add(attributeName);
            }
        }
        reader.close();

        reader = new BufferedReader(new FileReader(itemCategoryFile));
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, " \t,");

            String itemId = tokenizer.nextToken();
            String categoryName = tokenizer.nextToken();
            double categoryWeight = Double.valueOf(tokenizer.nextToken());

            if (!this.items.containsKey(itemId)) {
                this.items.put(itemId, new Item(itemId));
            }
            this.items.get(itemId).addCategory(categoryName, categoryWeight);

            if (!this.categories.contains(categoryName)) {
                this.categories.add(categoryName);
            }
        }

        Collections.sort(this.attributes);
        Collections.sort(this.categories);

        reader.close();
    }

    public Set<String> getItemIds() {
        return this.items.keySet();
    }

    public Item getItem(String itemId) {
        if (itemId != null && this.items.containsKey(itemId)) {
            return this.items.get(itemId);
        }
        return null;
    }

    public int getNumItems() {
        return this.items.size();
    }

    public List<String> getAttributes() {
        return this.attributes;
    }

    public int getNumAttributes() {
        return this.attributes.size();
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public int getNumCategories() {
        return this.categories.size();
    }
}
