package br.com.potential.inventory.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UuidUtils {

    public static boolean isUUIDValid(UUID id){
        String idVerified = String.valueOf(id);
        try {
            UUID.fromString(idVerified);
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }
}
