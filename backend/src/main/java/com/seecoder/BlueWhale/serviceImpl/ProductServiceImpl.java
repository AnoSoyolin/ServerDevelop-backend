package com.seecoder.BlueWhale.serviceImpl;

import com.seecoder.BlueWhale.exception.BlueWhaleException;
import com.seecoder.BlueWhale.po.Order;
import com.seecoder.BlueWhale.po.Product;
import com.seecoder.BlueWhale.po.Store;
import com.seecoder.BlueWhale.repository.OrderRepository;
import com.seecoder.BlueWhale.repository.ProductRepository;
import com.seecoder.BlueWhale.repository.StoreRepository;
import com.seecoder.BlueWhale.repository.UserRepository;
import com.seecoder.BlueWhale.service.ProductService;
import com.seecoder.BlueWhale.vo.CommentVO;
import com.seecoder.BlueWhale.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Boolean create(ProductVO productVO) {
        if (productRepository.findByStoreIdAndName(productVO.getStoreId(),productVO.getName())!=null){
            throw BlueWhaleException.nameAlreadyExists();
        }
        Product product=productVO.toPO();
        product.setSalesAmount(0);
        product.setStock(0);
        product.setNumber(0);
        product.setRating(0.0);
        productRepository.save(product);
        return true;
    }

    @Override
    public Boolean addStock(Integer id, Integer number) {
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw BlueWhaleException.productNotExists();
        }
        product.setStock(product.getStock()+number);
        productRepository.save(product);
        return true;
    }

    @Override
    public List<ProductVO> getAllProducts(Integer storeId) {
        Store store = storeRepository.findById(storeId).orElse(null);
        if (store == null) {
            throw BlueWhaleException.storeNotExists();
        }
        return productRepository.findAllByStoreId(storeId).stream().map(Product::toVO).collect(Collectors.toList());
    }

    @Override
    public ProductVO getProduct(Integer id) {
        Product product=productRepository.findById(id).orElse(null);
        if(product==null){
            throw BlueWhaleException.productNotExists();
        }
        return product.toVO();
    }

    @Override
    public List<CommentVO> getComments(Integer productId) {
        List<Order> orders=orderRepository.findByProductIdAndFinishTimeNotNull(productId);
        return orders.stream().map(x->{
            CommentVO commentVO=new CommentVO();
            commentVO.setOrderId(x.getId());
            commentVO.setRating(x.getRating());
            commentVO.setTime(x.getFinishTime());
            commentVO.setComment(x.getContent());
            commentVO.setUserId(x.getUserId());
            commentVO.setUserName(userRepository.findById(x.getUserId()).get().getName());
            return commentVO;
        }).collect(Collectors.toList());
    }
}
