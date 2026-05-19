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

    public List<Address> getAddressListByUsername(String username) {
        return addressMapper.getAddressListByUsername(username);
    }

    public Address getAddressById(int addressId) { // ✅ 新增
        return addressMapper.getAddressById(addressId);
    }
}
