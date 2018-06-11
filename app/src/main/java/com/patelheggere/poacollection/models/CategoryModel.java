package com.patelheggere.poacollection.models;

import java.util.ArrayList;
import java.util.List;

public class CategoryModel {
    private String Category;
    private List<String> subCat;

    public CategoryModel()
    {
        subCat = new ArrayList<>();
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public List<String> getSubCat() {
        return subCat;
    }

    public void setSubCat(List<String> subCat) {
        this.subCat = subCat;
    }
}
