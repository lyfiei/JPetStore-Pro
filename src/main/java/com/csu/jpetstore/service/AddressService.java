package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Address;
import com.csu.jpetstore.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public void addAddress(Address address) {
        addressMapper.insertAddress(address);
    }

    public void updateAddress(Address address) {
        addressMapper.updateAddress(address);
    }

    public void deleteAddress(int addressId) {
        addressMapper.deleteAddress(addressId);
    }

    public List<Address> getAddressListByUsername(String username) {
        return addressMapper.getAddressListByUsername(username);
    }

    public Address getAddressById(int addressId) {
        return addressMapper.getAddressById(addressId);
    }

    public void setDefaultAddress(int addressId) {
        Address address = addressMapper.getAddressById(addressId);
        if (address != null) {
            addressMapper.updateDefaultFlag(address.getUsername(), false);
            address.setIsDefault(true);
            addressMapper.updateAddress(address);
        }
    }
}
