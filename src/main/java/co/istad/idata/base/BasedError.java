package co.istad.idata.base;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasedError<T> {

    private String code;

    private T description;

}
