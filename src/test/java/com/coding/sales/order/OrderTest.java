package com.coding.sales.order;

import org.junit.Test;

public class OrderTest {


    @Test
    public void calculate_order_sum_test(){
        Order order = new Order();

        order.sum();
    }
}
