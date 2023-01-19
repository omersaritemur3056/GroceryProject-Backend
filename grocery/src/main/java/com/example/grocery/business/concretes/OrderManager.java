package com.example.grocery.business.concretes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.grocery.business.abstracts.CustomerService;
import com.example.grocery.business.abstracts.OrderService;
import com.example.grocery.business.abstracts.PaymentService;
import com.example.grocery.business.abstracts.ProductService;
import com.example.grocery.business.constants.Messages.CreateMessages;
import com.example.grocery.business.constants.Messages.DeleteMessages;
import com.example.grocery.business.constants.Messages.ErrorMessages;
import com.example.grocery.business.constants.Messages.GetByIdMessages;
import com.example.grocery.business.constants.Messages.GetListMessages;
import com.example.grocery.business.constants.Messages.UpdateMessages;
import com.example.grocery.core.utilities.business.BusinessRules;
import com.example.grocery.core.utilities.exceptions.BusinessException;
import com.example.grocery.core.utilities.mapper.MapperService;
import com.example.grocery.core.utilities.results.DataResult;
import com.example.grocery.core.utilities.results.Result;
import com.example.grocery.core.utilities.results.SuccessDataResult;
import com.example.grocery.core.utilities.results.SuccessResult;
import com.example.grocery.dataAccess.abstracts.OrderRepository;
import com.example.grocery.entity.concretes.Order;
import com.example.grocery.entity.concretes.Product;
import com.example.grocery.webApi.requests.order.CreateOrderRequest;
import com.example.grocery.webApi.requests.order.DeleteOrderRequest;
import com.example.grocery.webApi.requests.order.UpdateOrderRequest;
import com.example.grocery.webApi.responses.order.GetAllOrderResponse;
import com.example.grocery.webApi.responses.order.GetByIdOrderResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderManager implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MapperService mapperService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ProductService productService;

    @Override
    public Result add(CreateOrderRequest createOrderRequest) {

        Result rules = BusinessRules.run(isExistCustomerId(createOrderRequest.getCustomerId()),
                isExistPaymentId(createOrderRequest.getPaymentId()));

        Order order = mapperService.getModelMapper().map(createOrderRequest, Order.class);
        order.setCustomer(customerService.getCustomerById(createOrderRequest.getCustomerId()));
        order.setPayment(paymentService.getPaymentById(createOrderRequest.getPaymentId()));
        order.setProducts(productService.getProductsByIds(createOrderRequest.getProductIds()));
        orderRepository.save(order);
        log.info("order saved to DB");
        return new SuccessResult(CreateMessages.ORDER_CREATED);
    }

    @Override
    public Result delete(DeleteOrderRequest deleteOrderRequest) {

        Result rules = BusinessRules.run(isExistId(deleteOrderRequest.getId()));

        Order orderForLogging = orderRepository.findById(deleteOrderRequest.getId())
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Order order = mapperService.getModelMapper().map(deleteOrderRequest, Order.class);
        log.info("order id: {} removed from DB", orderForLogging.getId());
        orderRepository.delete(order);
        return new SuccessResult(DeleteMessages.ORDER_DELETED);
    }

    @Override
    public Result update(UpdateOrderRequest updateOrderRequest, Long id) {
        Order inDbOrder = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));

        Result rules = BusinessRules.run(isExistId(id), isExistCustomerId(updateOrderRequest.getCustomerId()),
                isExistPaymentId(updateOrderRequest.getPaymentId()));

        Order order = mapperService.getModelMapper().map(updateOrderRequest, Order.class);
        order.setCustomer(customerService.getCustomerById(updateOrderRequest.getCustomerId()));
        order.setPayment(paymentService.getPaymentById(updateOrderRequest.getPaymentId()));
        order.setProducts(productService.getProductsByIds(updateOrderRequest.getProductIds()));
        order.setId(inDbOrder.getId());
        orderRepository.save(order);
        log.info("order id: {} updated", id);
        return new SuccessResult(UpdateMessages.ORDER_UPDATED);
    }

    @Override
    public DataResult<List<GetAllOrderResponse>> getAll() {
        List<GetAllOrderResponse> returnList = new ArrayList<>();
        List<Order> orderList = orderRepository.findAll();
        for (Order order : orderList) {
            Order order1 = orderRepository.findById(order.getId())
                    .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
            GetAllOrderResponse addFields = mapperService.getModelMapper().map(order,
                    GetAllOrderResponse.class);
            addFields.setCustomerId(order1.getCustomer().getId());
            addFields.setPaymentId(order1.getPayment().getId());
            List<Long> ids = new ArrayList<>();
            for (Product x : order.getProducts()) {
                ids.add(x.getId());
            }
            addFields.setProductIds(ids);
            returnList.add(addFields);
        }
        return new SuccessDataResult<>(returnList, GetListMessages.ORDERS_LISTED);
    }

    @Override
    public DataResult<GetByIdOrderResponse> getById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorMessages.ID_NOT_FOUND));
        GetByIdOrderResponse getByIdOrderResponse = mapperService.getModelMapper().map(order,
                GetByIdOrderResponse.class);
        getByIdOrderResponse.setCustomerId(order.getCustomer().getId());
        getByIdOrderResponse.setPaymentId(order.getPayment().getId());
        List<Long> ids = new ArrayList<>();
        for (Product x : order.getProducts()) {
            ids.add(x.getId());
        }
        getByIdOrderResponse.setProductIds(ids);
        return new SuccessDataResult<>(getByIdOrderResponse, GetByIdMessages.ORDER_LISTED);
    }

    private Result isExistId(Long id) {
        if (!orderRepository.existsById(id)) {
            log.warn("Order id could not found!");
            throw new BusinessException(ErrorMessages.ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistCustomerId(Long customerId) {
        if (customerService.getCustomerById(customerId) == null) {
            throw new BusinessException(ErrorMessages.CUSTOMER_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    private Result isExistPaymentId(Long paymentId) {
        if (paymentService.getPaymentById(paymentId) == null) {
            throw new BusinessException(ErrorMessages.PAYMENT_ID_NOT_FOUND);
        }
        return new SuccessResult();
    }

    // private Result isExistProductsId(int... productId) {
    // if (productService.getProductsById(productId) == null) {
    // throw new BusinessException(ErrorMessages.PRODUCT_ID_NOT_FOUND);
    // }
    // return new SuccessResult();
    // }
}
