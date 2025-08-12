package com.acmeair.acmeairapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home() {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Flight Booking API</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        text-align: center;
                        padding-top: 100px;
                    }
                    h1 {
                        color: #333;
                    }
                    button {
                        background-color: #007bff;
                        color: white;
                        border: none;
                        padding: 12px 24px;
                        font-size: 16px;
                        border-radius: 5px;
                        cursor: pointer;
                        transition: background-color 0.3s ease;
                    }
                    button:hover {
                        background-color: #0056b3;
                    }
                </style>
            </head>
            <body>
                <h1>✈️ Acme Air API is Running</h1>
                <p>Go to the Open Swagger UI for a more interactive experience!</p>
                <a href="/swagger-ui.html">
                    <button>Go to Open Swagger UI</button>
                </a>
            </body>
            </html>
        """;
    }
}