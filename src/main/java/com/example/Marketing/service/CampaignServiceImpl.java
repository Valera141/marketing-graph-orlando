package com.example.Marketing.service;

import com.example.Marketing.dto.CampaignRequest;
import com.example.Marketing.dto.CampaignResponse;
import com.example.Marketing.exception.ResourceNotFoundException;
import com.example.Marketing.mapper.CampaignMapper;
import com.example.Marketing.model.Campaign;
import com.example.Marketing.model.User;
import com.example.Marketing.repository.CampaignRepository;
import com.example.Marketing.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository, UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<CampaignResponse> getAllCampaigns(Pageable pageable) {
        @SuppressWarnings("null")
        Page<Campaign> campaignPage = campaignRepository.findAll(pageable);
        return campaignPage.map(CampaignMapper::toResponse);
    }

    @Override
    public CampaignResponse getCampaignById(Integer id) {
        @SuppressWarnings("null")
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada con id: " + id));
        return CampaignMapper.toResponse(campaign);
    }

    @Override
    public CampaignResponse createCampaign(CampaignRequest request) {
        @SuppressWarnings("null")
        User creator = userRepository.findById(request.getCreatorUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado con id: " + request.getCreatorUserId()));
        Campaign newCampaign = CampaignMapper.toEntity(request);
        newCampaign.setCreatorUser(creator);
        newCampaign.setCreationDate(OffsetDateTime.now());

        Campaign savedCampaign = campaignRepository.save(newCampaign);

        return CampaignMapper.toResponse(savedCampaign);
    }

    @Override
    public CampaignResponse updateCampaign(Integer id, CampaignRequest request) {
        @SuppressWarnings("null")
        Campaign existingCampaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaña no encontrada con id: " + id));

        @SuppressWarnings("null")
        User creator = userRepository.findById(request.getCreatorUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario creador no encontrado con id: " + request.getCreatorUserId()));
        
        CampaignMapper.copyToEntity(request, existingCampaign);
        
        existingCampaign.setCreatorUser(creator);

        Campaign updatedCampaign = campaignRepository.save(existingCampaign);

        return CampaignMapper.toResponse(updatedCampaign);
    }

    @SuppressWarnings("null")
    @Override
    public Integer deleteCampaign(Integer id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campaña no encontrada con id: " + id);
        }
        
        campaignRepository.deleteById(id);
        
        return id;
    }
}