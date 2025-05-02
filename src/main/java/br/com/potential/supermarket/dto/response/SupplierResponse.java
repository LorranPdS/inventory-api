package br.com.potential.supermarket.dto.response;

import br.com.potential.supermarket.entity.SupplierEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
public class SupplierResponse {

    private UUID id;
    private String name;

    public static SupplierResponse of(SupplierEntity supplierEntity){
        var supplierResponse = new SupplierResponse();
        BeanUtils.copyProperties(supplierEntity, supplierResponse);
        return supplierResponse;
    }
}
