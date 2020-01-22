package com.fbi.entity.extra;

import com.fbi.entity.BaseRepository;
import com.fbi.util.exception.CodeObjectConst;
import com.fbi.fbdata.misc.SeedFpo;
import org.springframework.stereotype.Repository;

@Repository
public class SeedRepository extends BaseRepository<SeedFpo> {

    @Override
    protected Class<SeedFpo> getDataClass() {
        return SeedFpo.class;
    }

    @Override
    protected CodeObjectConst getCodeObjectConst() {
        return CodeObjectConst.BLANK;
    }

    //public List<SeedFpo> find
}
