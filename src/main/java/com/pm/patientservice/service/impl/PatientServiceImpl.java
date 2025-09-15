package com.pm.patientservice.service.impl;

import com.pm.patientservice.dtos.PatientRequestDto;
import com.pm.patientservice.dtos.PatientResponseDto;
import com.pm.patientservice.dtos.PatientUpdateRequestDto;
import com.pm.patientservice.enums.ResponseCode;
import com.pm.patientservice.exception.*;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repo.PatientRepo;
import com.pm.patientservice.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepo patientRepo;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    @Override
    public List<PatientResponseDto> getPatients() throws RetrivedFailedException {
        log.info("getPatients service...");
        try {
            List<Patient> patients = patientRepo.findAll();
            List<PatientResponseDto> patientResponseDtos = patients.stream()
                    .map(patient -> PatientMapper.toDto(patient)).toList();

//        List<PatientResponseDto> patientResponseDtos = patients.stream()
//                .map(PatientMapper::toDto).toList();

            return patientResponseDtos;
        } catch (Exception e) {
            log.error("error occured while getting patients. error: ", e.getMessage());
            ResponseCode.PATIENT_LIST_FAIL.setReason(e.getMessage());
            throw new RetrivedFailedException(ResponseCode.PATIENT_LIST_FAIL);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto) throws PatientSaveFailedException {
        log.info("createPatient service...");
        try {
            Patient patient = PatientMapper.toPatient(patientRequestDto);
            Patient newPatient = patientRepo.save(patient);

            billingServiceGrpcClient.createBillingAccount(
                    newPatient.getId().toString(),
                    newPatient.getName(),
                    newPatient.getEmail());

            return PatientMapper.toDto(newPatient);
        } catch (Exception e) {
            log.error("error occured while creating patient error: ", e.getMessage());
            ResponseCode.PATIENT_SAVE_FAIL.setReason(e.getMessage());
            throw new PatientSaveFailedException(ResponseCode.PATIENT_SAVE_FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = { Exception.class })
    public PatientResponseDto updatePatient(UUID patientId, PatientUpdateRequestDto requestDto)
            throws PatientNotFoundException, PatientUpdateFailedException {
        log.info("updatePatient service...");
        try {
            Patient patient = patientRepo.findById(patientId).orElseThrow(
                    () -> {
                        log.error("Patient not found with ID: " + patientId);
                        ResponseCode.PATIENT_NOT_FOUND.setReason("Patient not found with ID: " + patientId);
                        return new PatientNotFoundException(ResponseCode.PATIENT_NOT_FOUND);
                    }
            );
            patient.setName(requestDto.getName());
            patient.setEmail(requestDto.getEmail());
            patient.setAddress(requestDto.getAddress());
            patient.setDateOfBirth(LocalDate.parse(requestDto.getDateOfBirth()));

            Patient updatedPatient = patientRepo.save(patient);
            return PatientMapper.toDto(updatedPatient);
        } catch (PatientNotFoundException e) {
            throw new PatientNotFoundException(ResponseCode.PATIENT_NOT_FOUND);
        } catch (Exception e) {
            log.error("error occured while updating patient error: ", e.getMessage());
            ResponseCode.PATIENT_UPDATE_FAIL.setReason(e.getMessage());
            throw new PatientUpdateFailedException(ResponseCode.PATIENT_UPDATE_FAIL);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deletePatient(UUID id) throws PatientNotFoundException, PatientDeleteFailedException {
        log.info("deletePatient service...");
        try {
            if (!patientRepo.existsById(id)) {
                log.error("Patient not found with ID: " + id);
                ResponseCode.PATIENT_NOT_FOUND.setReason("Patient not found with ID: " + id);
                throw new PatientNotFoundException(ResponseCode.PATIENT_NOT_FOUND);
            }
            patientRepo.deleteById(id);
        } catch (PatientNotFoundException e) {
            throw new PatientNotFoundException(ResponseCode.PATIENT_NOT_FOUND);
        } catch (Exception e) {
            log.error("error occured while deleting patient error: ", e.getMessage());
            ResponseCode.PATIENT_DELETE_FAIL.setReason(e.getMessage());
            throw new PatientDeleteFailedException(ResponseCode.PATIENT_DELETE_FAIL);
        }
    }
}
