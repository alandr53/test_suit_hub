package com.mb.testsuithub.servlets;

import com.google.gson.Gson;
import com.mb.node.Node;
import com.mb.node.NodeManager;
import com.mb.testsuithub.baseservices.BaseHttpServlet;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet({"/hub/register","/hub/register/status"})
public class RegisterNodeServlet extends BaseHttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegisterNodeServlet.class.getSimpleName());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        String body = initDefaultIncomingRequest(request, response);
/*
        String username = request.getHeader("X-Username");
        String password = request.getHeader("X-Password");
        String ip = request.getHeader("X-Node-IP");
        String status = request.getHeader("X-Node-Status");

        String resp = "";
        String str = request.getRequestURI().split("/")[3];//status
        if("status".equals(str)) {
            if(NodeManager.setNodeStatus(ip, status)){
                LOGGER.info("The status of the node has been successfully updated to: "
                        + (NodeManager.getNodeByIP(ip)).getStatus());
                resp = "Staus of Node with ip "+ ip +" was successfully updated to: " + (NodeManager.getNodeByIP(ip)).getStatus();
            }else {
                LOGGER.info("Node with "+ ip +" is not registered at TestSuitHub. Node has status: " + status);
                resp = "Please register your Node first. The Node with ip "+ ip + " is not available in TestSuitHub";
                }
            }*/
            response.getWriter().println("resp");
        }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        String body = initDefaultIncomingRequest(request, response);

   /*     String username = request.getHeader("X-Username");
        String password = request.getHeader("X-Password");
        String ip = request.getHeader("X-Node-IP");
        String status = request.getHeader("X-Node-Status");*/
        String resp = "";

        Gson gson = new Gson();
        Node requestBody = gson.fromJson(body, Node.class);

        //register Node
        boolean reg = NodeManager.registerNode(requestBody.getIp(), requestBody.getPort(), requestBody.getUnicID(), requestBody.getStatus(), requestBody.getDescription());
        if(reg){
            resp = "Node successfully registered";
            LOGGER.info("Node with unic ID: " + requestBody.getUnicID() + " successfully registered");
        }else {
            resp = "Node with the same ip: " + requestBody.getUnicID() + " already exist";
            LOGGER.info("Node with the same ip: " + requestBody.getUnicID() + " already exist");
        }
        response.getWriter().println(resp);
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        initDefaultIncomingRequest(request, response);
        response.getWriter().println("PUT request received");
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        initDefaultIncomingRequest(request, response);

        String username = request.getHeader("X-Username");
        String password = request.getHeader("X-Password");
        String ip = request.getHeader("X-Node-IP");
        String resp = "GET request received: X-Username: " + username + "| X-Password: "+ password + "| X-Node-IP: " + ip;

        NodeManager.removeNode(ip);
        response.getWriter().println("DELETE request received");
    }

}
