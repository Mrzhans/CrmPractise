package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.util.SqlSessionUtil;
import com.bjpowernode.workbench.dao.ContactsDao;
import com.bjpowernode.workbench.domain.Contacts;
import com.bjpowernode.workbench.service.ContactService;

import java.util.List;

public class ContactServiceImpl implements ContactService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    @Override
    public List<Contacts> selectContactByName(String name) {
        List<Contacts> conlist = contactsDao.selectContactByName(name);
        return conlist;
    }
}
