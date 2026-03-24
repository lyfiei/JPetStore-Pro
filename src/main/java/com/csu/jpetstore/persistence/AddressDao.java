package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Address;

import java.util.List;

public interface AddressDao {
    void insertAddress(Address address);
    List<Address> getAddressListByUsername(String username);
    Address getAddressById(int addressId);

}
