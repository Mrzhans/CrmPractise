package com.bjpowernode.workbench.service;

import com.bjpowernode.workbench.domain.Contacts;

import java.util.List;

public interface ContactService {

    List<Contacts> selectContactByName(String name);
}
