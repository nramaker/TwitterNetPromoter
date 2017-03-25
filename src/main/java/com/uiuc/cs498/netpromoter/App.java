package com.uiuc.cs498.netpromoter;

import spark.Request;
import spark.Response;
import spark.Route;
 
import static spark.Spark.*;

public class App 
{
    
    public static void main(String[] args) {
        get("/", (request, response) -> "Hello World");
    }
}
