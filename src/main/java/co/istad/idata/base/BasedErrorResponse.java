package co.istad.idata.base;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasedErrorResponse {

    private BasedError error;

}
