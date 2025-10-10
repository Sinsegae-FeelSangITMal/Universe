package com.sinse.universe.model.payment;

import com.sinse.universe.domain.Membership;
import com.sinse.universe.domain.Order;
import com.sinse.universe.domain.OrderProduct;
import com.sinse.universe.domain.Product;
import com.sinse.universe.enums.OrderStatus;
import com.sinse.universe.model.cart.CartRepository;
import com.sinse.universe.model.membership.MembershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 카카오페이 결제 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CartRepository cartRepository;
    private final MembershipRepository membershipRepository;
    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.dev.secretKey}")
    private String secretKey;

    private RestTemplate restTemplate;  //외부 서버로 api 요청 보내기 위해 사용
    private final PaymentRepository paymentRepository;

    // 카카오가 요구하는 형식의 HTTP header 생성
    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        String auth = "SECRET_KEY " + secretKey;

        headers.set("Authorization", auth);
        headers.set("Content-Type", "application/json");
        return headers;
    }

    // 1. 결제 요청
    @Transactional
    public KakaoReadyResponse requestKakaoPayReady(Order order) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();

        params.put("cid", cid);                                             // 가맹점 코드 10자 (client id 아님), test 결제는 코드가 정해져 있음
        params.put("partner_order_id", order.getNo());                      // 가맹점 주문번호, 최대 100자
        params.put("partner_user_id", order.getUser().getId());             // 가맹점 회원 id, 최대 100자 (개인정보 X - 그냥 유저를 식별할수만 있으면 됨)
        params.put("item_name", buildItemName(order));                      // 상품명, 최대 100자
        params.put("quantity", order.getOrderProducts().size());            // 상품 종류별 수량을 다 합친 값
        params.put("total_amount", order.getTotalPrice());                  // 결제 총액

        params.put("tax_free_amount", "0");     // 상품 비과세 금액
        params.put("approval_url", "http://localhost:7777/api/payment/success?order_id=" + order.getId());        // 결제 성공 시 redirect url, 최대 255자
        params.put("cancel_url", "http://localhost:7777/api/payment/cancel");          // 결제 취소 시 redirect url
        params.put("fail_url", "http://localhost:7777/api/payment/fail");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, this.getHeaders());
        log.debug("결제 http 요청 메시지 {}", requestEntity);

        KakaoReadyResponse kakaoReadyResponse = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                requestEntity,
                KakaoReadyResponse.class
        );

        log.debug("카카오에서 보낸 응답 {}", kakaoReadyResponse);

        // tid (결제 번호) 저장
        paymentRepository.save(Integer.toString(order.getId()), kakaoReadyResponse.getTid(), Duration.ofMinutes(15));

        return kakaoReadyResponse;
    }

    // 카카오 결제창에서 보여줄 상품명 ex) "블랙핑크 멤버십 외 N건"
    private String buildItemName(Order order) {
        List<OrderProduct> products = order.getOrderProducts();
        String firstItemName = products.get(0).getProduct().getName();

        return (products.size() > 1)
                ? firstItemName + " 외 " + (products.size() - 1) + "건"
                : firstItemName;
    }

    @Transactional
    public ApproveResponse approve(String pgToken, Order order) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("cid", cid);
        params.put("tid", paymentRepository.get(Integer.toString(order.getId())));
        params.put("partner_order_id", order.getNo());
        params.put("partner_user_id", String.valueOf(order.getUser().getId()));
        params.put("pg_token", pgToken);


        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, getHeaders());

        ApproveResponse response = restTemplate.postForObject(
                "https://open-api.kakaopay.com/online/v1/payment/approve",
                entity,
                ApproveResponse.class
        );

        log.debug("kakao에게 받은 approve resopnse={}", response);

        // Todo: 장바구니 삭제
        List<Integer> productIds = order.getOrderProducts().stream()
                .map(OrderProduct::getProduct)
                .map(Product::getId)
                .toList();

        cartRepository.deleteByUser_IdAndProduct_IdIn(order.getUser().getId(), productIds);


        // Todo: 멤버십 상품 체크 후 db insert
        List<Product> membershipProductList = order.getOrderProducts().stream()
                .map(OrderProduct::getProduct)
                .filter(p -> "멤버십".equals(p.getCategory().getName()))
                .toList();

        membershipProductList.forEach(p -> {
            Membership membership = new Membership();
            membership.setUser(order.getUser());
            membership.setArtist(p.getArtist());
            LocalDate orderDate = order.getDate().toLocalDate();
            membership.setStartDate(orderDate.atStartOfDay());
            membership.setEndDate(orderDate.plusYears(1).atTime(23, 59, 59));

            membershipRepository.save(membership);
        });

        // Todo: order status PAID로 변경
        order.setStatus(OrderStatus.PAID);

        return response;
    }

}
