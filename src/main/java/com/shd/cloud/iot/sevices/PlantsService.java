package com.shd.cloud.iot.sevices;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shd.cloud.iot.exception.BadRequestException;
import com.shd.cloud.iot.exception.NotFoundException;
import com.shd.cloud.iot.models.Plants;
import com.shd.cloud.iot.payload.response.PlantsIdAndTitle;
import com.shd.cloud.iot.repositorys.PlantsRepository;

@Service
public class PlantsService {

    @Autowired
    private PlantsRepository plantsRepository;

    final static private String regex = "[a-zA-Z]";

    public Page<PlantsIdAndTitle> search(String key, int page) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(key);
        if (matcher.find())
            throw new BadRequestException("invalid search key");
        Pageable pageable = PageRequest.of(page, 5);
        return plantsRepository.search(key, pageable);
    }

    public Plants get(Integer id){
        return plantsRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("plant Not Found with id " + id));
    }
}
