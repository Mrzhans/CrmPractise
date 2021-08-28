package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Customer;

public interface CustomerDao {

    Customer getByName(String company);

    int save(Customer customer);
}
