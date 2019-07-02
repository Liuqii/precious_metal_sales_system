package com.coding.sales;

import com.coding.sales.input.OrderCommand;
import com.coding.sales.input.OrderItemCommand;
import com.coding.sales.output.DiscountItemRepresentation;
import com.coding.sales.output.OrderRepresentation;

import java.util.List;

/**
 * 销售系统的主入口
 * 用于打印销售凭证
 */
public class OrderApp {

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("参数不正确。参数1为销售订单的JSON文件名，参数2为待打印销售凭证的文本文件名.");
        }

        String jsonFileName = args[0];
        String txtFileName = args[1];

        String orderCommand = FileUtils.readFromFile(jsonFileName);
        OrderApp app = new OrderApp();
        String result = app.checkout(orderCommand);
        FileUtils.writeToFile(result, txtFileName);
    }

    public String checkout(String orderCommand) {
        OrderCommand command = OrderCommand.from(orderCommand);
        OrderRepresentation result = checkout(command);
        
        return result.toString();
    }

    OrderRepresentation checkout(OrderCommand command) {



        OrderRepresentation result = null;

        //TODO: 请完成需求指定的功能

        //根据客户购买的贵金属计算订单金额
        //客户可以使用账户余额购买贵金属，按客户等级计算积分，如果达到升级积分，则提升客户等级。
        //客户支付时，可以使用打折券，对参与打折的商品享受折扣。

        //获取订单条目
        List<OrderItemCommand> items =command.getItems();


        //获取客户使用的优惠

        List<String> discounts = command.getDiscounts();

        //根据促销规则，计算优惠金额。
        //打印销售凭证。

        return result;
    }
}
