package com.laptrinhjavaweb.service.impl;

import java.util.List;

import com.laptrinhjavaweb.dao.ICategoryDAO;
import com.laptrinhjavaweb.model.CategoryModel;
import com.laptrinhjavaweb.service.ICategoryService;
import javax.inject.Inject; // servlet weld

public class CategoryService implements ICategoryService {

//    private ICategoryDAO categoryDao;
//
//    public CategoryService() {
//        categoryDao = new CategoryDAO();
//    }
    @Inject
    private ICategoryDAO categoryDao;

    @Override
    public List<CategoryModel> findAll() {
        return categoryDao.findAll();
    }
}
