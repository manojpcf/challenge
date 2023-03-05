


package com.dws.challenge.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Description of Account.
 */
@Data
public class Account {

  private String id ;


  private BigDecimal balance ;

  public Account id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier
   * @return id
  **/

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Account balance(BigDecimal balance) {
    this.balance = balance;
    return this;
  }

   /**
   * DataCatalog Identifier, can be renamed
   * @return balance
  **/

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.id, account.id) &&
        Objects.equals(this.balance, account.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, balance);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
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

