package com.laptrinhjavaweb.controller.web;

import com.laptrinhjavaweb.model.UserModel;
import com.laptrinhjavaweb.service.ICategoryService;
import com.laptrinhjavaweb.service.INewService;
import com.laptrinhjavaweb.service.IUserService;
import com.laptrinhjavaweb.utils.FormUtil;
import com.laptrinhjavaweb.utils.SessionUtil;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.inject.Inject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/trang-chu", "/dang-nhap", "/thoat"})
public class HomeController extends HttpServlet {

    /**
     *
     */
    @Inject
//    need object manager for Dependency = beans.xml
    private INewService newService;
    @Inject
    private ICategoryService catagoryService;
    @Inject
    private IUserService userService;
    private static final long serialVersionUID = 1L;

    ResourceBundle resourceBundle = ResourceBundle.getBundle("message");

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        get parameter form url
        String action = request.getParameter("action");
        if (action != null && action.equals("login")) {
//            get message
            String message = request.getParameter("message");
            String alert = request.getParameter("alert");
            if (message != null & alert != null) {
                request.setAttribute("message", resourceBundle.getString(message));
                request.setAttribute("alert", alert);
            }

            RequestDispatcher rd = request.getRequestDispatcher("/views/login.jsp");
            rd.forward(request, response);
        } else if (action != null && action.equals("logout")) {
            SessionUtil.getInstance().removeValue(request, "USERMODEL");
//            redirect to controller, not to view
//            from controller 'thoat' to controller 'trang chu'
            response.sendRedirect(request.getContextPath() + "/trang-chu");
        } else {
//            request.setAttribute("categories", catagoryService.findAll());

            // display on view
            RequestDispatcher rd = request.getRequestDispatcher("/views/web/home.jsp");
            rd.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null && action.equals("login")) {
//            mapper from view to UserModel
            UserModel model = FormUtil.toModel(UserModel.class, request);
            // authentication
            model = userService.findByUserNameAndPasswordAndStatus(model.getUserName(), model.getPassword(), 1);
            if (model != null) {
                SessionUtil.getInstance().putValue(request, "USERMODEL", model);
                // authorization
                if (model.getRole().getCode().equals("USER")) {
                    response.sendRedirect(request.getContextPath() + "/trang-chu");
                } else if (model.getRole().getCode().equals("ADMIN")) {
                    response.sendRedirect(request.getContextPath() + "/admin-home");
                }
            } else {
//            redirect back
                response.sendRedirect(request.getContextPath() + "/dang-nhap?action=login&message=username_password_invalid&alert=danger");
            }
        }
    }
}
