package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Address;
import com.csu.jpetstore.persistence.AddressDao;
import com.csu.jpetstore.persistence.impl.AddressDaoImpl;

import java.util.List;

public class AddressService {
    private AddressDao addressDao = new AddressDaoImpl();

    public void addAddress(Address address) {
        addressDao.insertAddress(address);
    }

    public List<Address> getAddressListByUsername(String username) {
        return addressDao.getAddressListByUsername(username);
    }

    public Address getAddressById(int addressId) { // ✅ 新增
        return addressDao.getAddressById(addressId);
    }
}
