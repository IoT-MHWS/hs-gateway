package artgallery.geteway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
  @NotNull
  public Long id;
  @NotNull
  public String login;
  @NotNull
  public List<String> authorities;
}
