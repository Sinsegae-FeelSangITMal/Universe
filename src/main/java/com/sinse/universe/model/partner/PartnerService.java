package com.sinse.universe.model.partner;

import com.sinse.universe.domain.Artist;
import com.sinse.universe.domain.Partner;

import java.util.List;

public interface PartnerService {
    public List<Partner> selectAll();
    public Partner select(int partnerId);;
    public void regist(Partner partner);
    public void update(Partner partner);
    public void delete(int partnerId);
}
