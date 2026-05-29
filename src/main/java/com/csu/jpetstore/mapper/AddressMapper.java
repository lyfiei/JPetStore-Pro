package com.csu.jpetstore.mapper;

import com.csu.jpetstore.domain.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    void insertAddress(Address address);
    void updateAddress(Address address);
    void deleteAddress(int addressId);
    List<Address> getAddressListByUsername(String username);
    Address getAddressById(int addressId);
}
