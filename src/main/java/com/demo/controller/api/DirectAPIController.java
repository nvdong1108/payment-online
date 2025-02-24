package com.demo.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/directapi")
public class DirectAPIController {
	
	private static String path_webhook = "http://localhost:8090/api/payment/webhook";
	private String path_return = "http://localhost:8090/payment/return";
	private String path_checkout = "http://localhost:8090/checkout";
	
	
	
// http://localhost:8090/directapi?public_key=MTE3MDBfOTE3XzIwMjUwMjIzMTUzMjUw&terNO=917&bill_currency=USD&product_name=dev testing product&fullname=Dev Full Name&bill_email=test.9900@test.com&bill_address=36A Alpha&bill_city=Jurong&bill_state=SG&bill_country=SG&bill_zip=KEN&bill_phone=6562294466&reference=23120228&mop=dc&ccno=5555555555554444&ccvv=123&month=01&year=30&bill_amt=110.00
	
	
	// http://localhost:8090/directapi?public_key=MTEzMTFfODE2XzIwMjQxMjExMTMwNTQ4&terNO=816&bill_currency=USD&product_name=dev testing product&fullname=Dev Full Name&bill_email=test.9900@test.com&bill_address=36A Alpha&bill_city=Jurong&bill_state=SG&bill_country=SG&bill_zip=KEN&bill_phone=6562294466&reference=23120228&mop=dc&ccno=5555555555554444&ccvv=123&month=01&year=30&bill_amt=110.00
	// localhost:9091/directapi?public_key=MTEzMTFfODE2XzIwMjQxMjExMTMwNTQ4&terNO=816&bill_currency=USD&product_name=dev testing product&fullname=Dev Full Name&bill_email=test.9900@test.com&bill_address=36A Alpha&bill_city=Jurong&bill_state=SG&bill_country=SG&bill_zip=KEN&bill_phone=6562294466&reference=23120228&mop=dc&ccno=4242424242424242&ccvv=123&month=01&year=30&bill_amt=110.00

	@GetMapping
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> dataPost = new HashMap<>();
        
        // Extract request parameters
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String paramValue = request.getParameter(paramName);
            dataPost.put(paramName, paramValue);
            
            if(request.getParameter("qp") != null && request.getParameter("qp") != "") 
            {
            	PrintWriter out = response.getWriter();
            	// Print the parameter name and value
	            out.println("<p>" + paramName + " = " + paramValue + "</p>");
            	
            }
        }
        
        String gatewayUrl = getOrDefault(request, "gatewayUrl", "https://gw.paywb.co/directapi");
        String webhook_url = getOrDefault(request, "webhook_url",path_webhook);
        String return_url = getOrDefault(request, "return_url", path_return);
        String checkout_url = getOrDefault(request, "checkout_url", path_checkout);
        
        try {
            

            // Add additional parameters
            dataPost.put("gatewayUrl", gatewayUrl);
            dataPost.put("webhook_url", webhook_url);
            dataPost.put("return_url", return_url);
            dataPost.put("checkout_url", checkout_url);
            	
            
            //actual IP retrieval logic
    			String clientIP = request.getHeader("X-Forwarded-For");
            if (clientIP != null && !clientIP.isEmpty()) {
                // Split the header value by commas
                String[] ipAddresses = clientIP.split(",");

                // Return the first IP after trimming any leading/trailing whitespace
                clientIP = ipAddresses[0].trim();
            } else {
                // Fallback to request's remote address if no X-Forwarded-For header
            		clientIP = request.getRemoteAddr();
            }
            
            if (clientIP.isEmpty()) { clientIP = "127.0.0.1" ; }
            
            // Default (fixed) value * default
            dataPost.put("integration-type", "s2s");
            // dataPost.put("unique_reference", "Y");
            dataPost.put("bill_ip", clientIP); 
            dataPost.put("source", "Java-Spring-Curl-Direct-Payment");
            //dataPost.put("source_url", source_url);

            // Send data to the gateway
            String jsonResponse = sendPostRequest(gatewayUrl, dataPost);
            
            
         // Parse and handle the JSON response
            handleResponse(jsonResponse, response);
               

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write("Error processing the request: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
	
	
	private String sendPostRequest(String urlStr, Map<String, String> dataPost) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        // Build POST data string
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> entry : dataPost.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(entry.getKey()).append('=').append(entry.getValue());
        }

        // Send data
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.toString().getBytes());
        }

        // Read response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String responseLine;
            StringBuilder responseBuilder = new StringBuilder();
            while ((responseLine = in.readLine()) != null) {
                responseBuilder.append(responseLine);
            }
            return responseBuilder.toString();
        }
    }

    private void handleResponse(String jsonResponse, HttpServletResponse response) throws IOException {
        try {
            // Parse the JSON response using Jackson
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(jsonResponse);

            if (jsonNode.has("authurl")) {
                String authUrl = jsonNode.get("authurl").asText();
                // Redirect to the auth URL
                response.sendRedirect(authUrl);
            } else if (jsonNode.has("error")) {
                String error = jsonNode.get("error").asText();
                response.getWriter().write("Error in Gateway Response: " + error);
            } else if (jsonNode.has("order_status")) {
                int orderStatus = jsonNode.get("order_status").asInt();
                response.getWriter().write("Order Status: " + orderStatus);
            } else {
                response.getWriter().write("Unknown Response: " + jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error parsing response: " + e.getMessage());
        }
    }
    
    
    public static String getOrDefault(HttpServletRequest request, String paramName, String defaultValue) {
        String value = request.getParameter(paramName);
        return (value != null) ? value : defaultValue;
    }
	
	
	

}
