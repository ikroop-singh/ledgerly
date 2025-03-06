package com.ledgerly.app.expense_tracker.service.aiScanner;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service

public class AIScannerServiceImpl implements AIScannerService {

    WebClient webClient;

    @Value("${gemini.api.url}")
    String geminiUrl;
    @Value("${gemini.api.key}")
    String apiKey;

    AIScannerServiceImpl(WebClient.Builder webclient){
        this.webClient=webclient.build();
    }

    @Override
    public String scanReceipt(MultipartFile file)  {

        String encodedData="";
        try{
            byte[]byteFile=file.getBytes();
            encodedData= Base64.getEncoder().encodeToString(byteFile);
        }
        catch(IOException exception){
            System.out.println(exception.getMessage());
        }

        String prompt="Analyze this receipt image and extract the following information in JSON format:\n" +
                "      - Total amount (just the number)\n" +
                "      - Date (in ISO format)\n" +
                "      - Description or items purchased (brief summary)\n" +
                "      - Merchant/store name\n" +
                "      - Suggested category (one of: housing,transportation,groceries,utilities,entertainment,food,shopping,healthcare,education,personal,travel,insurance,gifts,bills,other-expense )\n" +
                "      \n" +
                "      Only respond with valid JSON in this exact format:\n" +
                "      {\n" +
                "        \"amount\": number,\n" +
                "        \"date\": \"ISO date string\",\n" +
                "        \"description\": \"string\",\n" +
                "        \"merchantName\": \"string\",\n" +
                "        \"category\": \"string\"\n" +
                "      }\n" +
                "\n" +
                "      If its not a receipt, return an empty object";

        Map<String,Object>requestBody=Map.of("contents", List.of(
            Map.of("parts",List.of(
                    Map.of("text",prompt),

                    Map.of("inline_data",Map.of(
                            "mime_type",file.getContentType(),
                            "data",encodedData
                    ))
            )
            )


        ));

         String response=webClient.post()
                 .uri(geminiUrl+apiKey)
                 .header("Content-type","application/json")
                 .bodyValue(requestBody)
                 .retrieve()
                 .bodyToMono(String.class)
                 .block();
         return extractResponse(response);


    }
    String extractResponse(String response){
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode rootNode=objectMapper.readTree(response);
            String extracted= rootNode.path("candidates").get(0).path("content").path("parts")
                    .get(0).path("text").asText();
            return extracted.replaceAll("```json\\s*","").replaceAll("```\\s*","");

        } catch (Exception e) {
            return "Exception occurred "+e.getMessage();
        }
    }
}
