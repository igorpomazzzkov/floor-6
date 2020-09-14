package by.dormitory.service;

import by.dormitory.entity.Duty;
import by.dormitory.repository.DutyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DutyService {
    private final DutyRepository dutyRepository;

    public DutyService(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    public List<Duty> findAll() {
        return dutyRepository.findAll();
    }

    public Duty save(Duty duty){
        return dutyRepository.save(duty);
    }

    public List<Duty> saveList(List<Duty> duties){
        return dutyRepository.saveAll(duties);
    }

    public void delete(Duty duty){
        this.dutyRepository.deleteById(duty.getId());
    }
}