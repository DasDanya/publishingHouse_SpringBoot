package kafpinpin120.publishingHouse.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenerateReportRequest {

    private long userId;

    @NotBlank
    private String userRole;


    @NotNull
    private LocalDate startPeriod;

    @NotNull
    private LocalDate endPeriod;
}
