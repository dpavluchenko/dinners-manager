package controller.action.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionAction {
    String userAttrName = "userDetails";

    void process(HttpServletRequest request, HttpServletResponse response);
}
