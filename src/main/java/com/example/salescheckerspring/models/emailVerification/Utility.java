package com.example.salescheckerspring.models.emailVerification;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");
    }

    public Utility() {
    }

}
