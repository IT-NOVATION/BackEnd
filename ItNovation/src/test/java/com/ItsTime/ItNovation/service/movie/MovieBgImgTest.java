package com.ItsTime.ItNovation.service.movie;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MovieBgImgTest {

//    @Value("${ttkey}")
//    private String API_KEY;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//
//
//    @Test
//    void bgImgReqTest(){
//        String url = "https://api.themoviedb.org/3/movie/109445/images?api_key=" + API_KEY;
//        Map<String, List<Map<String,String>>> forObject = restTemplate.getForObject(url, Map.class);
//        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//
//        String fileUrl = forObject.get("backdrops").get(0).get("file_path");
//        System.out.println("map " + forObject.get("backdrops").get(3).get("file_path"));
//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        Assertions.assertThat(fileUrl.length()).isGreaterThan(10);
//    }
//

}
