package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.common.Result;
import com.csu.jpetstore.domain.Address;
import com.csu.jpetstore.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressApiController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public Result<List<Address>> getAddressList(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return Result.success(addressService.getAddressListByUsername(username));
    }

    @PostMapping
    public Result<Address> addAddress(@RequestBody Address address,
                                      HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        address.setUsername(username);
        addressService.addAddress(address);
        return Result.success(address);
    }

    @PutMapping("/{addressId}")
    public Result<Void> updateAddress(@PathVariable int addressId,
                                      @RequestBody Address address) {
        address.setAddressId(addressId);
        addressService.updateAddress(address);
        return Result.success();
    }

    @DeleteMapping("/{addressId}")
    public Result<Void> deleteAddress(@PathVariable int addressId) {
        addressService.deleteAddress(addressId);
        return Result.success();
    }

    @PutMapping("/{addressId}/default")
    public Result<Void> setDefaultAddress(@PathVariable int addressId) {
        addressService.setDefaultAddress(addressId);
        return Result.success();
    }
}
