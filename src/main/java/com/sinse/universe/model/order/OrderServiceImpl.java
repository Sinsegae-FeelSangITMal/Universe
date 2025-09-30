package com.sinse.universe.model.order;

import com.sinse.universe.domain.*;
import com.sinse.universe.dto.request.OrderSubmitRequest;
import com.sinse.universe.dto.response.OrderForEntResponse;
import com.sinse.universe.dto.response.OrderForUserResponse;
import com.sinse.universe.dto.response.OrderProductForUserResponse;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.enums.MembershipStatus;
import com.sinse.universe.enums.OrderStatus;
import com.sinse.universe.exception.CustomException;
import com.sinse.universe.model.membership.MembershipRepository;
import com.sinse.universe.model.product.ProductRepository;
import com.sinse.universe.model.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MembershipRepository membershipRepository;
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, MembershipRepository membershipRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.membershipRepository = membershipRepository;
    }

    // 판매자 페이지) 소속사의 상품이 포함된 주문 목록 요청
    @Override
    public List<OrderForEntResponse> getListByPartnerId(int partnerId) {
        return orderRepository.findByPartnerId(partnerId).stream()
                .map(OrderForEntResponse::from)
                .toList();
    }
    
    // 판매자 페이지) 아티스트의 상품이 포함된 주문 목록 요청
    @Override
    public List<OrderForEntResponse> getListByArtistId(int artistId) {
        return orderRepository.findByArtistId(artistId).stream()
                .map(OrderForEntResponse::from)
                .toList();
    }

    // 유저 페이지) 유저의 주문 목록 요청
    @Override
    public List<OrderForUserResponse> getListByUserId(int userId) {
        return orderRepository.findByUser_Id(userId).stream()
                .map(OrderForUserResponse::from)
                .toList();
    }

    // 유저 페이지) 주문 상세 요청
    @Override
    public OrderForUserResponse getDetail(int orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    // orderProducts -> DTO 변환 (멤버십 상품 체크)
                    List<OrderProductForUserResponse> productResponses = order.getOrderProducts().stream()
                            .map(op -> {
                                // 기본: 멤버십 없음
                                Membership membership = null;

                                // 카테고리가 "멤버십"이면 membership 조회
                                if ("멤버십".equals(op.getProduct().getCategory().getName())) {
                                    membership = membershipRepository
                                            .findTopByUserIdAndArtistIdOrderByStartDateDesc(
                                                    order.getUser().getId(),
                                                    op.getProduct().getArtist().getId()
                                            )
                                            .orElse(null);
                                }

                                return OrderProductForUserResponse.from(op, membership);
                            })
                            .toList();

                    return OrderForUserResponse.from(order, productResponses);
                })
                .orElse(null);
    }


    // 유저 페이지) 주문 등록
    @Override
    @Transactional
    public int submitOrder(OrderSubmitRequest request) throws CustomException {
        log.debug("Order submit request: {}", request);

        // 새로운 주문내역 생성
        Order order = new Order();

        // 주문번호 (UUID 대체 → yyMMdd + 6자리 숫자)
        order.setNo(generateOrderNo());

        // 주문자 정보
        order.setOrdererName(request.orderer().name());
        order.setOrdererEmail(request.orderer().email());

        // 수령인 정보
        order.setReceiverName(request.receiver().name());
        order.setReceiverPhone(request.receiver().phone());
        order.setReceiverAddr(request.receiver().address());
        order.setReceiverAddrDetail(request.receiver().addressDetail());
        order.setReceiverCountry(request.receiver().country());
        order.setReceiverCity(request.receiver().city());
        order.setReceiverState(request.receiver().state());
        order.setReceiverPostal(request.receiver().postal());

        // 결제/주문 관련
        order.setPayment(request.paymentMethod());
        order.setTotalPrice(request.totalPrice());
        order.setAgree(request.agree());
        order.setStatus(OrderStatus.PAID); // 기본 상태: 주문(결제) 완료
        order.setDate(LocalDateTime.now());

        // 유저 매핑 (User 엔티티 조회)
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        order.setUser(user);

        // 주문상품 저장 (cascade = ALL)
        for (OrderSubmitRequest.OrderProductRequest productRequest : request.items()) {
            Product product = productRepository.findById(productRequest.productId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            OrderProduct op = new OrderProduct();
            op.setOrder(order);
            op.setProduct(product);
            op.setQty(productRequest.qty());
            op.setPrice(productRequest.price());

            order.getOrderProducts().add(op);

            // 멤버십 상품 체크
            if ("멤버십".equals(product.getCategory().getName())) {
                Membership membership = new Membership();
                membership.setUser(user);
                membership.setArtist(product.getArtist());

                // 시작일: 현재
                LocalDate start = LocalDate.now();
                // 종료일: 1년 + 1일 뒤 자정
                LocalDate end = start.plusYears(1);

                membership.setStartDate(start.atStartOfDay());
                membership.setEndDate(end.atStartOfDay());

                membershipRepository.save(membership);
            }
        }

        // 한 번만 save 호출 → Order + OrderProducts 함께 저장
        orderRepository.save(order);

        return order.getId();
    }


    // 주문 번호 생성 메서드 (랜덤값 이용)
    private String generateOrderNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        int randomNum = (int)(Math.random() * 1_000_000); // 0 ~ 999999
        String randPart = String.format("%06d", randomNum);
        return "OR" + datePart + randPart;
    }

}
