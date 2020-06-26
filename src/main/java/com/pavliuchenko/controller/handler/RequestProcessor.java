package com.pavliuchenko.controller.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestProcessor {
    void process(HttpServletRequest request, HttpServletResponse response);
}
