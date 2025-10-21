package com.hyf.trade.application.db.entity;

import com.hyf.trade.application.Constants;
import com.hyf.trade.application.util.FileUtil;
import lombok.Data;

import java.io.File;
import java.util.UUID;

@Data
public class Strategy {

    private static final String STRATEGY_DIR = Constants.HOME + File.separator + "strategy";

    private String id;
    private String name;

    public void init() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
    }

    public void store(String question) {
        FileUtil.write(file(), question);
    }

    public String load() {
        String question = FileUtil.read(file());
        return question;
    }

    public void destroy() {
        FileUtil.delete(file());
    }

    public File file() {
        return new File(STRATEGY_DIR, id);
    }
}
