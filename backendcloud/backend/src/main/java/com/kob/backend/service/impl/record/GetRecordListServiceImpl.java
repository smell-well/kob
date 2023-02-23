package com.kob.backend.service.impl.record;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Record;
import com.kob.backend.pojo.User;
import com.kob.backend.service.record.GetRecordListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetRecordListServiceImpl implements GetRecordListService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public JSONObject getList(Integer page) {
        IPage<Record> recordIPage = new Page<>(page, 10);
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Record> records = recordMapper.selectPage(recordIPage, queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        List<JSONObject> items = new ArrayList<>();
        System.out.println(page);
        for (Record record : records) {
            User a = userMapper.selectById(record.getAId());
            User b = userMapper.selectById(record.getBId());

            JSONObject item = new JSONObject();
            item.put("a_photo", a.getPhoto());
            item.put("a_username", a.getUsername());
            item.put("b_photo", b.getPhoto());
            item.put("b_username", b.getUsername());
            item.put("record", record);
            items.add(item);
        }
        resp.put("records", items);
        resp.put("record_count", recordMapper.selectCount(null));

        return resp;
    }
}
