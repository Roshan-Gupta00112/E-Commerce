package com.example.ecommerce.dtos.request;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateInfoUsingEmail {

    String emailId;

    String newName;

    String newDob;

    String newMobNo;

    String newAddress;
}
