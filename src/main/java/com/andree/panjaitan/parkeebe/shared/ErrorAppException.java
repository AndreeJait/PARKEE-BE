package com.andree.panjaitan.parkeebe.shared;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorAppException extends RuntimeException{
    @JsonProperty("code_error")
    private String codeError;

    private String message;
}
