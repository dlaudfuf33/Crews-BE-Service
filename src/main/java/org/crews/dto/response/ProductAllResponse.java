package org.crews.dto.response;

import lombok.*;
import org.crews.dto.core.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAllResponse {

    private List<ProductResponse> products = new ArrayList<>();
}
