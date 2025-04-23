package br.com.potential.inventory.dto.response;

import br.com.potential.inventory.entity.SupplierEntity;
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
