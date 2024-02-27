package site.hclub.hyndai.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegisterProductsRequest {
    private String  productName;   // 상품명
    private Long    productPrice;   // 상품 가격
    private String  productImage;   // 상품 사진 URL
}
