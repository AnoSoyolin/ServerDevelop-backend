package com.seecoder.BlueWhale.controller;

import com.seecoder.BlueWhale.service.ProductService;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.ProductVO;
import com.seecoder.BlueWhale.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ResultVO<Boolean> create(@RequestBody ProductVO productVO){
        return ResultVO.buildSuccess(productService.create(productVO));
    }

    @PostMapping("/{id}/stock")
    public ResultVO<Boolean> addStock(@PathVariable(value = "id") Integer id,@RequestParam("number") Integer number){
        return ResultVO.buildSuccess(productService.addStock(id,number));
    }

    @GetMapping
    public ResultVO<List<ProductVO>> getAllProducts(@RequestParam("storeId") Integer storeId){
        return ResultVO.buildSuccess(productService.getAllProducts(storeId));
    }

    @GetMapping("/{id}")
    public ResultVO<ProductVO> getProduct(@PathVariable(value = "id") Integer id){
        return ResultVO.buildSuccess(productService.getProduct(id));
    }

    @GetMapping("/comment")
    public ResultVO<List<CommentVO>> getComments(@RequestParam("productId")Integer productId){
        return ResultVO.buildSuccess(productService.getComments(productId));
    }
}
