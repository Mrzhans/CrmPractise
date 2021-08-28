package com.bjpowernode.workbench.dao;

import com.bjpowernode.workbench.domain.Contacts;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> selectContactByName(String name);
}
