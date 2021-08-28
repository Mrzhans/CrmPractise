package com.bjpowernode.workbench.service.impl;

import com.bjpowernode.settings.dao.DicTypeDao;
import com.bjpowernode.settings.dao.DicValueDao;
import com.bjpowernode.settings.domain.DicType;
import com.bjpowernode.settings.domain.DicValue;
import com.bjpowernode.util.DateTimeUtil;
import com.bjpowernode.util.SqlSessionUtil;
import com.bjpowernode.util.UUIDUtil;
import com.bjpowernode.workbench.dao.*;
import com.bjpowernode.workbench.domain.*;
import com.bjpowernode.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public List<DicType> getAll() {
        DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
        List<DicType> typeList = dicTypeDao.getAll();
        return typeList;
    }

    @Override
    public List<DicValue> getValueList(String code) {
        DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
        List<DicValue> valueList = dicValueDao.getValueList(code);
        return valueList;
    }

    @Override
    public int save(Clue c) {
        int count = clueDao.save(c);
        return count;
    }

    @Override
    public List<Clue> selectAllClue() {
        List<Clue> clueList = clueDao.selectAllClue();
        return clueList;
    }

    @Override
    public Clue selectByClueId(String id) {
        Clue clue = clueDao.selectByClueId(id);
        return clue;
    }

    @Override
    public List<Activity> getActivityByClueId(String clueid) {
        List<Activity> activityList = clueDao.getActivityByClueId(clueid);
        return activityList;
    }

    @Override
    public boolean unbund(String id) {
        int count = clueDao.unbund(id);
        boolean flag = false;
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivtiys(String clueid, String name) {
        List<Activity> activityList = clueDao.getActivitys(clueid, name);
        return activityList;
    }

    @Override
    public boolean createActivtiyRelation(Map<String, Object> map) {
        int count = clueDao.createActivtiyRelation(map);
        boolean flag = false;
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean convert(String clueid, Tran t, String createBy) {
        boolean flag = true;
        //(1)获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.selectByClueId(clueid);
        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        Customer customer = customerDao.getByName(c.getCompany());
        if (customer == null) {
            customer = new Customer();
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();
            customer.setId(id);
            customer.setName(c.getCompany());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setPhone(c.getPhone());
            customer.setWebsite(c.getWebsite());
            customer.setContactSummary(c.getContactSummary());
            customer.setNextContactTime(c.getNextContactTime());
            customer.setAddress(c.getAddress());
            customer.setDescription(c.getDescription());

            int count = customerDao.save(customer);
            if (count != 1) {
                flag = false;
            }
        }

        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setFullname(c.getFullname());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setJob(c.getJob());
        contacts.setMphone(c.getMphone());
        contacts.setAppellation(c.getAppellation());
        contacts.setEmail(c.getEmail());
        contacts.setSource(c.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setContactSummary(c.getContactSummary());
        contacts.setNextContactTime(c.getNextContactTime());
        contacts.setAddress(c.getAddress());
        contacts.setDescription(c.getDescription());
        int count1 = contactsDao.save(contacts);
        if (count1 != 1) {
            flag = false;
        }

        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> crlist = clueRemarkDao.selectById(clueid);
        ContactsRemark contactsRemark = new ContactsRemark();
        CustomerRemark customerRemark = new CustomerRemark();

        for (ClueRemark clueRemark : crlist) {
            //客户备注
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setEditFlag(clueRemark.getEditFlag());
            customerRemark.setCustomerId(customer.getId());
            int count2 = customerRemarkDao.save(customerRemark);
            if (count2 != 1) {
                flag = false;
            }

            //联系人备注
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setEditFlag(clueRemark.getEditFlag());
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setContactsId(contacts.getId());
            int count3 = contactsRemarkDao.save(contactsRemark);
            if (count3 != 1) {
                flag = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> carlist = clueActivityRelationDao.getActRelationById(clueid);
        ContactsActivityRelation contar = new ContactsActivityRelation();
        for (ClueActivityRelation car : carlist) {
            contar.setId(UUIDUtil.getUUID());
            contar.setContactsId(contacts.getId());
            contar.setActivityId(car.getActivityId());
            int count4 = contactsActivityRelationDao.save(contar);
            if (count4 != 1) {
                flag = false;
            }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if (t != null) {
            int count5 = tranDao.save(t);
            if (count5 != 1) {
                flag = false;
            }

            TranHistory history = new TranHistory();
            history.setId(UUIDUtil.getUUID());
            history.setMoney(t.getMoney());
            history.setExpectedDate(t.getExpectedDate());
            history.setCreateBy(t.getCreateBy());
            history.setCreateTime(t.getCreateTime());
            history.setStage(t.getStage());
            history.setTranId(t.getId());
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            int count6 = tranHistoryDao.save(history);
            if (count6 != 1) {
                flag = false;
            }
        }
        //(8) 删除线索备注
        clueRemarkDao.deleteById(clueid);

        //(9) 删除线索和市场活动的关系
        clueActivityRelationDao.deleteById(clueid);

        //(10) 删除线索
        clueDao.deleteById(clueid);
        return flag;
    }
}
