package com.laptrinhjavaweb.controller.admin;

import com.laptrinhjavaweb.constant.SystemConstant;
import com.laptrinhjavaweb.model.NewModel;
import com.laptrinhjavaweb.paging.PageRequest;
import com.laptrinhjavaweb.paging.Pageble;
import com.laptrinhjavaweb.service.ICategoryService;
import com.laptrinhjavaweb.service.INewService;
import com.laptrinhjavaweb.sort.Sorter;
import com.laptrinhjavaweb.utils.FormUtil;
import com.laptrinhjavaweb.utils.MessageUtil;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin-new"})
public class NewController extends HttpServlet {

  private static final long serialVersionUID = 2686801510274002166L;
  @Inject private INewService newService;
  @Inject private ICategoryService categoryService;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    //        get parameter from url
    NewModel model = FormUtil.toModel(NewModel.class, request);
    String view = "";
    if (model.getType().equals(SystemConstant.LIST)) {
      Pageble pageble =
          new PageRequest(
              model.getPage(),
              model.getMaxPageItem(),
              new Sorter(model.getSortName(), model.getSortBy()));
      model.setListResult(newService.findAll(pageble));

      //          set totalPage to return for view
      model.setTotalItem(newService.getTotalItem());
      model.setTotalPage((int) Math.ceil((double) model.getTotalItem() / model.getMaxPageItem()));
      //          move data from 'controller' to 'view'
      view = "/views/admin/new/list.jsp";
    } else if (model.getType().equals(SystemConstant.EDIT)) {
      if (model.getId() != null) {
        model = newService.findOne(model.getId());
      }
      request.setAttribute("categories", categoryService.findAll());
      view = "/views/admin/new/edit.jsp";
    }
    // notification
    MessageUtil.showMessage(request);
    request.setAttribute(SystemConstant.MODEL, model);
    RequestDispatcher rd = request.getRequestDispatcher(view);
    rd.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {}
}
