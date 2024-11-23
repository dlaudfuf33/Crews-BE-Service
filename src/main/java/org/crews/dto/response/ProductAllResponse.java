package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.dto.core.ProductResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAllResponse {

    private List<ProductResponse> products = new ArrayList<>();
}
