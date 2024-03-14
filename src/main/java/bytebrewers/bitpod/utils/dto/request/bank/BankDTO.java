package bytebrewers.bitpod.utils.dto.request.bank;

import bytebrewers.bitpod.entity.Bank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BankDTO {
    @Pattern(regexp = "^\\S+$", message = "Please provide a valid name")
    @NotNull(message = "please provide a valid name")
    private String name;
    @NotNull
    private String address;

    public Bank toEntity() {
        return Bank.builder()
                .name(name)
                .address(address)
                .build();
    }
}