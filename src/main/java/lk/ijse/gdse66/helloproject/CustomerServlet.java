package lk.ijse.gdse66.helloproject;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.model.customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "Customer", urlPatterns = "/customers", loadOnStartup = 1, initParams = {
        @WebInitParam(name = "username" , value = "root"),
        @WebInitParam(name = "password" , value = "dragon25"),
        @WebInitParam(name = "url" , value = "jdbc:mysql://localhost:3306/aad")
})
public class CustomerServlet extends HttpServlet {
    private String username;
    private String password;
    private String url;

    @Override
    public void init() throws ServletException {
        /*ServletConfig is used to get configuration information such as database url, mysql username and password*/
        ServletConfig sc = getServletConfig();
        username = sc.getInitParameter("username");
        password = sc.getInitParameter("password");
        url = sc.getInitParameter("url");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;

       /* JsonObject object = Json.createReader(req.getReader()).readObject();
        String id = object.getString("id");
        String name = object.getString("name");
        String address = object.getString("address");
*/

        customer customer = JsonbBuilder.create().fromJson(req.getReader(), customer.class);
        String id = customer.getId();
        String name = customer.getName();
        String address = customer.getAddress();

        System.out.printf("id=%s, name=%s, address=%s\n", id,name,address);

        /*create a database connection and save data in database*/
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
            PreparedStatement stm = connection.prepareStatement("INSERT INTO customer(id, name, address) VALUES (?,?,?)");

            stm.setString(1,id);
            stm.setString(2,name);
            stm.setString(3,address);

            if(stm.executeUpdate()>0){
                resp.setStatus(HttpServletResponse.SC_CREATED);

            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(connection !=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("select * from customer");

     //       JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            ArrayList<customer> customers = new ArrayList<>();
            Jsonb jsonb = JsonbBuilder.create();
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                System.out.println(id + " " + name + " " + address);
         //       arrayBuilder.add(Json.createObjectBuilder().add("id",id).add("name",name).add("address",address).build());
                customers.add(new customer(id,name,address));
            }
            jsonb.toJson(customers,writer);
           // resp.getWriter().write(arrayBuilder.build().toString());

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(connection !=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;
/*
        JsonObject object = Json.createReader(req.getReader()).readObject();
        String id = object.getString("id");
        String name = object.getString("name");
        String address = object.getString("address");
*/
        customer customer = JsonbBuilder.create().fromJson(req.getReader(), customer.class);
        String id = customer.getId();
        String name = customer.getName();
        String address = customer.getAddress();
        System.out.printf("id=%s, name=%s, address=%s\n", id,name,address);

        /*create a database connection and save data in database*/
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
            PreparedStatement stm = connection.prepareStatement("UPDATE customer SET name = ? , address =? WHERE id = ?");

            stm.setString(1,name);
            stm.setString(2,address);
            stm.setString(3,id);

            if(stm.executeUpdate()>0){
                resp.setStatus(HttpServletResponse.SC_CREATED);

            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(connection !=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Connection connection = null;

    //    String id = Json.createReader(req.getReader()).readObject().getString("id");
        String id = JsonbBuilder.create().fromJson(req.getReader(), customer.class).getId();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,username,password);
            PreparedStatement stm = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
            stm.setString(1,id);
            if(stm.executeUpdate()>0){
                resp.setStatus(HttpServletResponse.SC_CREATED);

            }else{
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if(connection !=null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}