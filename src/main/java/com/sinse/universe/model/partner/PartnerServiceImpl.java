package com.sinse.universe.model.partner;

import com.sinse.universe.domain.Partner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerServiceImpl implements PartnerService{

    public final PartnerRepository partnerRepository;

    public PartnerServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public List<Partner> selectAll() {
        return partnerRepository.findAll();
    }

    @Override
    public Partner select(int partnerId) {
        return partnerRepository.findById(partnerId).orElse(null);
    }

    @Override
    public void regist(Partner partner) {
        partnerRepository.save(partner);
    }

    @Override
    public void update(Partner partner) {
        partnerRepository.save(partner);
    }

    @Override
    public void delete(int partnerId) {
        partnerRepository.deleteById(partnerId);
    }
}
