

package com.dws.challenge.model;



import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * The information from money transfer
 */

@Data
public class TransferMoneyDetails {
  @NotNull
  @NotEmpty
  private String from;

  @NotNull
  @NotEmpty
  private String to;

  @NotNull
  @Min(value = 0, message = "Initial balance must be positive.")
  private BigDecimal amount;



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TransferMoneyDetails transferMoneyDetails = (TransferMoneyDetails) o;
    return Objects.equals(this.from, transferMoneyDetails.from) &&
        Objects.equals(this.to, transferMoneyDetails.to) &&
        Objects.equals(this.amount, transferMoneyDetails.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, amount);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransferMoneyDetails {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

