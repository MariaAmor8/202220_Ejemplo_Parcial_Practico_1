package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcialejemplo.entities.EspecialidadEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.MedicoEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.EspecialidadRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.MedicoRepository;

@Service
public class MedicoEspecialidadService {

    @Autowired
    MedicoRepository medicoRepository;

    @Autowired
    EspecialidadRepository especialidadRepository;

    @Transactional
    public MedicoEntity addEspecialidad(Long idMedico, Long idEspecialidad)throws IllegalOperationException, EntityNotFoundException{
        Optional<MedicoEntity> medicoEntity = medicoRepository.findById(idMedico);
        Optional<EspecialidadEntity> especialidadEntity = especialidadRepository.findById(idEspecialidad);
        if(medicoEntity.isEmpty()){
            throw new EntityNotFoundException("Medico no encontrado");
        }
        if(especialidadEntity.isEmpty()){
            throw new EntityNotFoundException("Especialidad no encontrada");
        }

        medicoEntity.get().getEspecialidades().add(especialidadEntity.get());
        return medicoEntity.get();
        
    }
}
