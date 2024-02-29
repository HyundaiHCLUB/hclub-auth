package site.hclub.hyndai.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MypageProductsResponse {
    /* FROM PRODUCTS TABLE*/
    private String  productName;
    private Long    productPrice;
    private String  productImage;

}
