package lk.ijse.gdse66.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "cusFilter" , urlPatterns = "/*")
public class customerFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String origin = req.getHeader("origin");
        if(origin == null){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST,"CORS Policy vioklation");
        }

        res.addHeader("Access-Control-Allow-Origin" , origin);
        res.addHeader("Access-Control-Allow-Headers" , "Content-type");
        res.setHeader("Access-Control-Allow-Methods" , "GET,POST,DELETE,PUT");
        res.setContentType("application/json");
        chain.doFilter(req,res);
    }
}
