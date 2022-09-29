package com.laptrinhjavaweb.service.impl;

import com.laptrinhjavaweb.dao.ICategoryDAO;
import com.laptrinhjavaweb.dao.INewDAO;
import com.laptrinhjavaweb.model.CategoryModel;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.INewService;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

public class NewService implements INewService {

  @Inject private INewDAO newDao;

  @Inject private ICategoryDAO categoryDao;

  @Override
  public List<NewModel> findByCategoryId(Long categoryId) {
    return newDao.findByCategoryId(categoryId);
  }

  @Override
  public NewModel save(NewModel newModel) {
    // not SQL Exception -> not null
    newModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    CategoryModel categoryModel = categoryDao.findOneByCode(newModel.getCategoryCode());
    newModel.setCategoryId(categoryModel.getId());
    Long newId = newDao.save(newModel);
    return newDao.findOne(newId);
  }

  @Override
  public NewModel update(NewModel updateNew) {
    NewModel oldNew = newDao.findOne(updateNew.getId());
    updateNew.setCreatedDate(oldNew.getCreatedDate());
    updateNew.setCreatedBy(oldNew.getCreatedBy());
    // not SQL Exception <-> not null
    updateNew.setModifiedDate(new Timestamp(System.currentTimeMillis()));
    CategoryModel categoryModel = categoryDao.findOneByCode(updateNew.getCategoryCode());
    updateNew.setCategoryId(categoryModel.getId());

    newDao.update(updateNew);
    return newDao.findOne(updateNew.getId());
  }

  @Override
  public void delete(long[] ids) {
    for (long id : ids) {
      /*
      news-1    n-comment-n   1-user
      before delete  news(id) -> delete comment (FK: new_id)
       */
      newDao.delete(id);
    }
  }

  @Override
  public List<NewModel> findAll(Pageble pageble) {
    return newDao.findAll(pageble);
  }

  @Override
  public int getTotalItem() {
    return newDao.getTotalItem();
  }

  @Override
  public NewModel findOne(long id) {
    NewModel newModel = newDao.findOne(id);
    CategoryModel categoryModel = categoryDao.findOne(newModel.getCategoryId());
    newModel.setCategoryCode(categoryModel.getCode());
    return newModel;
  }
}
