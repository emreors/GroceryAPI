package com.grocery.groceryapi.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Emre ÖRS
 * Date 13.08.2021
 *
 * Updated by Emre ÖRS
 * Date 10.01.2022
 */

class GroceryItem {
    private GroceryItem() {
    }

    public static final String APPLE = "apple";
    public static final String GRAPES = "grapes";
}

class GroceryDTO {
    private Integer id;
    private String name;
    private Integer price;
    private Integer stock;

    public GroceryDTO() {
    }

    public GroceryDTO(Integer id, String name, Integer price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}

class AppleGroceryDTO extends GroceryDTO {
    public AppleGroceryDTO() {
        super(1, GroceryItem.APPLE, 3, 100);
    }
}

class GrapesGroceryDTO extends GroceryDTO {
    public GrapesGroceryDTO() {
        super(2, GroceryItem.GRAPES, 5, 50);
    }
}

class ResponseDTO {
    private List<GroceryDTO> data;

    public ResponseDTO() {
    }

    public ResponseDTO(List<GroceryDTO> data) {
        this.data = data;
    }

    public List<GroceryDTO> getData() {
        return data;
    }
}

class ErrorResponseDTO extends ResponseDTO {
    private String error;

    public ErrorResponseDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

@RestController
public class GroceryController {

    @GetMapping(value = "/allGrocery")
    public ResponseEntity<ResponseDTO> allGrocery() {
        List<String> wishList = Arrays.asList(GroceryItem.APPLE, GroceryItem.GRAPES);
        return new ResponseEntity<>(createResponseDTO(wishList), getHttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/allGrocery/{name}")
    public ResponseEntity<ResponseDTO> allGroceryByName(@PathVariable String name) {
        if (name.equals(GroceryItem.APPLE)) {
            List<String> wishList = Collections.singletonList(GroceryItem.APPLE);
            return new ResponseEntity<>(createResponseDTO(wishList), getHttpHeaders(), HttpStatus.OK);
        } else if (name.equals(GroceryItem.GRAPES)) {
            List<String> wishList = Collections.singletonList(GroceryItem.GRAPES);
            return new ResponseEntity<>(createResponseDTO(wishList), getHttpHeaders(), HttpStatus.OK);
        } else
            return new ResponseEntity<>(new ErrorResponseDTO("Parameter name should be given as valid grocery name"), getHttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseDTO> addGrocery(@RequestBody GroceryDTO grocery) {
        if (grocery == null)
            return new ResponseEntity<>(new ErrorResponseDTO("Request body can not be null"), getHttpHeaders(), HttpStatus.BAD_REQUEST);
        else if (grocery.getId() == null || grocery.getName() == null || grocery.getPrice() == null || grocery.getStock() == null)
            return new ResponseEntity<>(new ErrorResponseDTO("Request body fields can not be null"), getHttpHeaders(), HttpStatus.BAD_REQUEST);
        else if (grocery.getName().equals(GroceryItem.APPLE))
            return new ResponseEntity<>(new ErrorResponseDTO("Apple is already be added"), getHttpHeaders(), HttpStatus.BAD_REQUEST);
        else if (grocery.getName().equals(GroceryItem.GRAPES))
            return new ResponseEntity<>(new ErrorResponseDTO("Grapes is already be added"), getHttpHeaders(), HttpStatus.BAD_REQUEST);
        else {
            return new ResponseEntity<>(new ResponseDTO(Collections.singletonList(grocery)), getHttpHeaders(), HttpStatus.OK);
        }

    }

    private HttpHeaders getHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }

    private ResponseDTO createResponseDTO(List<String> wishList) {
        List<GroceryDTO> data = new ArrayList<>();
        for (String s : wishList) {
            GroceryDTO grocery = null;
            if (s.equals(GroceryItem.APPLE))
                grocery = new AppleGroceryDTO();
            else if (s.equals(GroceryItem.GRAPES))
                grocery = new GrapesGroceryDTO();
            if (grocery != null)
                data.add(grocery);
        }
        return new ResponseDTO(data);
    }
}
