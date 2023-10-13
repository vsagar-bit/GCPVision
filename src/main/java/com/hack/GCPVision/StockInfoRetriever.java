package com.hack.GCPVision;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class StockInfoRetriever {

    private static final String API_KEY = "EY72HD9AVSQZ2YRH";
    private static final String BASE_URL = "https://www.alphavantage.co/query";
    private static final String SYMBOL_FUNCTION = "SYMBOL_SEARCH";
    private static final String OPTIONS_FUNCTION = "GLOBAL_QUOTE";

    private String getStockSymbolByCompanyName(String companyName) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Construct the API request
        String apiUrl = String.format("%s?function=%s&keywords=%s&apikey=%s", BASE_URL, SYMBOL_FUNCTION, companyName, API_KEY);
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            // Send the HTTP request
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String responseBody = EntityUtils.toString(response.getEntity());

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject bestMatch = jsonResponse.getJSONArray("bestMatches").getJSONObject(0);
            String symbol = bestMatch.getString("1. symbol");

            return symbol;
        } finally {
            // Close the HttpClient
            httpClient.close();
        }
    }

    private String getOptionDataForStock(String symbol) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String responseBody = "";
        // Construct the API request
        String apiUrl = String.format("%s?function=%s&symbol=%s&apikey=%s", BASE_URL, OPTIONS_FUNCTION, symbol, API_KEY);
        HttpGet httpGet = new HttpGet(apiUrl);

        try {
            // Send the HTTP request
            CloseableHttpResponse response = httpClient.execute(httpGet);
            responseBody = EntityUtils.toString(response.getEntity());

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONObject optionQuotes = jsonResponse.getJSONObject("Global Quote");

            // Print option data
            System.out.println("01. symbol: " + optionQuotes.getString("01. symbol"));
            System.out.println("02. open: " + optionQuotes.getDouble("02. open"));
            System.out.println("03. high: " + optionQuotes.getString("03. high"));
            System.out.println("04. low: " + optionQuotes.getDouble("04. low"));
            System.out.println("05. price: " + optionQuotes.getDouble("05. price"));
            System.out.println("06. volume: " + optionQuotes.getDouble("06. volume"));
            System.out.println("07. latest trading day: " + optionQuotes.getString("07. latest trading day"));
            System.out.println("08. previous close: " + optionQuotes.getDouble("08. previous close"));
            System.out.println("09. change: " + optionQuotes.getDouble("09. change"));
            System.out.println("10. change percent: " + optionQuotes.getString("10. change percent"));
            System.out.println("--------------------------");

        } finally {
            // Close the HttpClient
            httpClient.close();
        }
        return responseBody;
    }

    public String getCompanyStockOptions(String companyName) {
        try {
            return getOptionDataForStock(getStockSymbolByCompanyName(companyName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
