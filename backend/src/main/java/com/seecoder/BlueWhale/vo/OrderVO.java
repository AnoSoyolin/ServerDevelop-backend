package com.seecoder.BlueWhale.vo;

import com.seecoder.BlueWhale.enums.OrderStatusEnum;
import com.seecoder.BlueWhale.enums.OrderTypeEnum;
import com.seecoder.BlueWhale.po.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrderVO {
    
    private Integer id;

    private Integer userId;

    private Integer productId;

    private Integer amount;

    private Double paid;

    private OrderTypeEnum type;

    private String content;

    private Integer rating;

    private OrderStatusEnum status;

    private Date createTime;

    private Date finishTime;

    private String productName;

    private Integer storeId;

    private Double price;

    public Order toPO(){
        Order order=new Order();
        order.setAmount(this.amount);
        order.setContent(this.content);
        order.setCreateTime(this.createTime);
        order.setFinishTime(this.finishTime);
        order.setId(this.id);
        order.setPaid(this.paid);
        order.setProductId(this.productId);
        order.setRating(this.rating);
        order.setStatus(this.status);
        order.setType(this.type);
        order.setUserId(this.userId);
        return order;
    }

}
