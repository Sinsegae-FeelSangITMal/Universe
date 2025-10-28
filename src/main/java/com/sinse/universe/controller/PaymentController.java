package com.sinse.universe.controller;

import com.sinse.universe.domain.Order;
import com.sinse.universe.model.order.OrderRepository;
import com.sinse.universe.model.payment.ApproveResponse;
import com.sinse.universe.model.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    @Value("${app.front-server.user.url}")
    private String userFrontServer;
    private final PaymentService paymentService;
    private final OrderRepository orderRepository;


    @GetMapping("/api/payment/success")
    public RedirectView approve(
            @RequestParam("pg_token") String pgToken,
            @RequestParam("order_id") int orderId
    ) {
        // 사용자가 결제 qr을 통해 확인 버튼을 클릭하면 결제승인 api를 호출해 최종적으로 결제 승인을 해야함

        Order order = orderRepository.findById(orderId).get();
        ApproveResponse response = paymentService.approve(pgToken, order);

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(userFrontServer + "/order/paid/" + orderId);
        return redirectView;
    }

    @GetMapping("/api/payment/cancel")
    public String cancelPayment() {
        return "취소 -_-";
    }

    @GetMapping("/api/payment/fail")
    public String failPayment() {
        return "실..패!";
    }
}
