package controller.action.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ApplicationAction {
    String userAttrName = "userDetails";

    void save(HttpServletRequest request, HttpServletResponse response);
    void get(HttpServletRequest request, HttpServletResponse response);
    void update(HttpServletRequest request, HttpServletResponse response);
    void delete(HttpServletRequest request, HttpServletResponse response);
}
