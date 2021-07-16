package com.test.fourhealth.util;

import com.fasterxml.jackson.core.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class FoodApiUtil {

    @Value("${foodapi.datatype}")
    private String dataType;

    @Value("${foodapi.service.ingredient.key}")
    private String foodIngredientApi;

    @Value("${foodapi.service.healthfood.key")
    private String healthFoodApi;

    @Value("${foodapi.service.recipe.key")
    private String recipeApi;

    @Value("${foodapi.validation.key}")
    private String foodApiValidationKey;

    @Value("${foodapi.basicurl.url}")
    private String foodBasicUrl;


    @RequestMapping("/foodIngredientApi")
    private void foodApiData(){

        List<Map<String, String>> foodList = new ArrayList<Map<String, String>>();

//        String foodApiUrl = foodBasicUrl+"/"+foodIngredientApi+"/"+dataType;

        for(int i=0; i<40000; i++){

            int startIdx = 0+i;
            int endIdx = 1000+i;



            String foodApiUrl = foodBasicUrl+"/"+foodIngredientApi+"/"+dataType+"/"+startIdx+"/"+endIdx;

            log.info("[Checking foodApiCall Url] : {}",foodApiUrl);

            try {
                URL url = new URL(foodApiUrl);

                String line = "";
                String result = "";

                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(url.openStream()));

                while((line = br.readLine()) != null){
                    result = result.concat(line);
                    log.info("[Checking food url api test] : {}", result);
                }

                JSONParser parser = new JSONParser();
                parser.parse(result);

                LinkedHashMap<String, Object> map ;





            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

        }




    }
}
