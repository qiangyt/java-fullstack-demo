package io.github.qiangyt.common.rest;

import java.util.Date;

// Consistent with springframework mvc error response schema
@lombok.Getter
@lombok.Setter
@lombok.Builder
public class ErrorResponse {

    Date timestamp;

    int status;

    String error;

    Object[] params;

}
