package com.sinse.universe.model.partner;

import com.sinse.universe.domain.Partner;
import com.sinse.universe.enums.ErrorCode;
import com.sinse.universe.exception.CustomException;
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
        return partnerRepository.findById(partnerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));
    }

    @Override
    public void regist(Partner partner) {
        partnerRepository.save(partner);
    }

    @Override
    public void update(Partner partner) {
        Partner existing = partnerRepository.findById(partner.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));

        // 테이블 컬럼에 맞게 필드 업데이트
        existing.setName(partner.getName());  // PT_NAME
        existing.setAddress(partner.getAddress());    // PT_ADR
        existing.setFile(partner.getFile());    // PT_FIL

        partnerRepository.save(existing);
    }

    @Override
    public void delete(int partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new CustomException(ErrorCode.PARTNER_NOT_FOUND));

        partnerRepository.delete(partner);
    }
}
