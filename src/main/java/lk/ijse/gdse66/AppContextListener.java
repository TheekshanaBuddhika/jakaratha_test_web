package lk.ijse.gdse66;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;

@WebListener( value = "servletContextListener")
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("context is initialized");
        BasicDataSource dataSource = new BasicDataSource(); // create a connection pool
        dataSource.setUrl("jdbc:mysql://localhost:3306/aad");
        dataSource.setUsername("root");
        dataSource.setPassword("dragon25");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setInitialSize(2);
        dataSource.setMaxTotal(5);
        System.out.println("DB configuration complete");

        sce.getServletContext().setAttribute("dbcp", dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("context is destroyed");
        BasicDataSource dbcp = (BasicDataSource) sce.getServletContext().getAttribute("dbcp");
        try {
            dbcp.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
